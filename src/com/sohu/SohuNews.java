package com.sohu;

import com.sohu.bean.NewsBean;
import com.sohu.bean.searchBean;
import com.sohu.db.ConnectionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.HeadingTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TextareaTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.htmlparser.tags.TableTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.filters.NodeClassFilter; 

/**
 * ����ָ��Url��ץȡ����
 * 
 */
public class SohuNews 
{

    private Parser parser = null;                //���������ҳ�Ķ���
    private List newsList = new ArrayList();     //���ڴ���ֶ���Ϣ������
    searchBean searchBean=new searchBean();
    private NewsBean bean = new NewsBean();      //�����ݿ��ֶζ�Ӧ��JavaBean
    private ConnectionManager manager = null;    //���ݿ�������
    private PreparedStatement pstmt = null;      //ִ��sql�����Ҫʹ�õĶ���

    /**
     * ���캯��
     */
    public SohuNews() 
    {
    	//��ԭ���ݿ��е��������
    	//initDB();
    }

    /**
     * ��JavaBean�е��ֶζ�Ӧ��ӵ�������
     * @param newsBean
     * @return
     */
    public List getNewsList(final NewsBean newsBean) 
    {
        List list = new ArrayList();
        
        String newstitle   = newsBean.getNewsTitle();
        String newsauthor  = newsBean.getNewsAuthor();
        String newscontent = newsBean.getNewsContent(); 
        String newsdate    = newsBean.getNewsDate();
        
        list.add(newstitle);
        list.add(newsauthor);
        list.add(newscontent);
        list.add(newsdate);
        
        return list;
    }

    /**
     *  �趨JavaBean�и��ֶε�ֵ
     * @param newsTitle   ���ű������Ƣ�
     * @param newsauthor  ���α༭
     * @param newsContent ��������
     * @param newsDate    ��������
     * @param url         ��ҳ���ӵ�ַ
     */
    public void setNews( String newsTitle, 
    		             String newsauthor, 
    		             String newsContent,
    		             String newsDate, 
    		             String url) 
    {
        bean.setNewsTitle(newsTitle);
        bean.setNewsAuthor(newsauthor);
        bean.setNewsContent(newsContent);
        bean.setNewsDate(newsDate);
        bean.setNewsURL(url);
    }

    public String getDate(String Url)
    {
    	int index=Url.indexOf("20");
    	String date=Url.substring(index,index+8);
    	return date;
    }
    /**
     * ��JavaBean�е��ֶ�д�뵽���ݿ���
     */
    /*protected void newsToDataBase() 
    {

        //�����߳�ִ��д�����ݿ�Ĳ�������Ϊ��Ҫ֧�ֶ��û���
        Thread thread = new Thread( new Runnable()
        {

            public void run() 
            {
                boolean sucess = saveToDB(bean);
                if (sucess != false) 
                {
                    System.out.println("д���ݿ����ʧ�ܣ�");
                }
            }
        } );
        //�����߳�
        thread.start();
    }*/

    /**
     *  ������ݿ�ԭ�����ݣ������Զ���ŵ���ʼֵ��Ϊ1
     */
    public void initDB()
    {
    	String sql = "truncate table dbo.news";
    	manager = new ConnectionManager();
    	
    	try 
        {
        	//�����ݿ⽨������
            pstmt = manager.getConnection().prepareStatement(sql);
            pstmt.execute();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally 
        {
            try 
            {
                pstmt.close();
                manager.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
    /**
     * д���ݿ������ʵ�֣�sql����ִ�У�
     * @param bean
     * @return
     */
    public boolean saveToDB() 
    {
        boolean flag = true;
        String sql = "insert into dbo.news(newstitle,newsauthor,newscontent,"
        		+ "newsurl,newsdate) values(?,?,?,?,?)";
        manager = new ConnectionManager();
        String titleLength = bean.getNewsTitle();
        //�����ű��ⳤ�ȵ�����
        if (titleLength.length() > 60)     
        { 
            return flag;
        }
        try 
        {
        	//�����ݿ⽨������
            pstmt = manager.getConnection().prepareStatement(sql);
            
            //��JavaBean�л�ȡ�ֶΣ�д�����ݿ���
            pstmt.setString(1, bean.getNewsTitle());
            pstmt.setString(2, bean.getNewsAuthor());
            pstmt.setString(3, bean.getNewsContent());
            pstmt.setString(4, bean.getNewsURL());
            pstmt.setString(5, bean.getNewsDate());
            flag = pstmt.execute();

        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally 
        {
            try 
            {
                pstmt.close();
                manager.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return flag;
    }

    /**
     * ����ҳ�ϻ�ȡ���ŵı���
     * @param titleFilter
     * @param parser
     * @return
     */
    private String getTitle(NodeFilter titleFilter, Parser parser) 
    {
        String titleName = "";
        try 
        {
            //����titleFilter�ķ������ҳ���й���
            NodeList titleNodeList = (NodeList) parser.parse(titleFilter);
            //��ȡ���ű����ַ���
            for ( int i = 0; i < titleNodeList.size(); i++) 
            {   
                HeadingTag title = (HeadingTag) titleNodeList.elementAt(i);
                titleName = title.getStringText();
            }

        } 
        catch (ParserException ex) 
        {
            //Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
        	return null;
        }
        return titleName;
    }

    /**
     * ��ȡ���ŵ����α༭
     * @param newsauthorFilter
     * @param parser
     * @return
     */
    private String getNewsAuthor(NodeFilter newsauthorFilter, Parser parser) 
    {
        String newsAuthor = "";
        try 
        {
        	//����newsauthorFilter�ķ������ҳ���й���
            NodeList authorList = (NodeList) parser.parse(newsauthorFilter);
            
            //��ȡ���α༭�ַ���
            for (int i = 0; i < authorList.size(); i++) 
            {   
                Div authorSpan = (Div) authorList.elementAt(i);
                newsAuthor = authorSpan.getStringText();
            }

        } 
        catch (ParserException ex) 
        {
            //Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
        	return null;
        }
        return newsAuthor;

    }

    /*
     * ��ȡ����ʱ��
     */
    private String getNewsDate(NodeFilter dateFilter, Parser parser) 
    {
        String newsDate = "";
        try 
        {
            NodeList dateList = (NodeList) parser.parse(dateFilter);
            for (int i = 0; i < dateList.size(); i++) 
            {
            	Div dateTag =  (Div)dateList.elementAt(i);
                newsDate = dateTag.getStringText();
            }
        } 
        catch (ParserException ex) 
        {
           // Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
        	return null;
        }
        return newsDate;
    }

    /**
     * ��ȡ��������
     * @param newsContentFilter
     * @param parser
     * @return content 
     */
    private String getNewsContent(NodeFilter newsContentFilter, Parser parser) 
    {
        String content = null;
        StringBuilder builder = new StringBuilder();

        try 
        {
        	//����ָ���Ĺ��˷�������ҳ
            NodeList newsContentList = (NodeList) parser.parse(newsContentFilter);
            //  System.out.println(newsContentList.size());     ����ʹ��
            
            //��ȡ���ĵ�����
            for (int i = 0; i < newsContentList.size(); i++) 
            {
            	Div newsContenTag = (Div) newsContentList.elementAt(i);
                builder = builder.append(newsContenTag.getStringText());
            }
            content = builder.toString();  //����ת����
            
            if (content != null) 
            {  
                parser.reset();
                //ѡ����뷽ʽ
                parser = Parser.createParser(content, "gb2312");
                //����paser�����нڵ㣬��ȡ���������
                StringBean sb = new StringBean();
                sb.setCollapse(true);
                parser.visitAllNodesWith(sb);
                content = sb.getStrings();
               
                //��ȥ���������е�һЩ�̶���ʽ�������滻Ϊ��
                if ( content != null )
                {
                    content = content.replaceAll("\\\".*[a-z].*\\}", "");           
                    content = content.replace("[����˵����]", "");
                }


            } 
            else 
            {
               System.out.println("û�еõ��������ݣ�");
            }

        } 
        catch (ParserException ex) 
        {
            //Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
        	return null;
        }
        return content;
    }
    
    /**
     * ��ָ����url���з�������ȡ���е����ű��⡢���α༭�����ߡ��������ڡ���������
     * @param url ָ����url
     */
    public void parser(String url) 
    {
        try {
            parser = new Parser(url);
            
            //���������
            NodeFilter titleFilter = new TagNameFilter("h1");
                   
            //���Ĺ�����
            NodeFilter contentFilter1 = new AndFilter(new TagNameFilter("div"), 
            		                    new HasAttributeFilter("id", "contentText"));
            NodeFilter contentFilter2 = new AndFilter(new TagNameFilter("div"),
            		                    new HasAttributeFilter("id", "sohu_content"));
            NodeFilter contentFilter = new OrFilter(contentFilter1,contentFilter2);
            
            
            // NodeFilter contentFilter = new TagNameFilter("textarea");   // ���ƻƺ� �ķ���
                    
            //�������ڹ�����
            NodeFilter newsdateFilter1 = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "time"));
            //NodeFilter newsdateFilter2 = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "r"));
            /*NodeFilter newsdateFilter = new AndFilter(newsdateFilter2, new HasParentFilter(newsdateFilter1));*/
            //NodeFilter newsdateFilter = new OrFilter(newsdateFilter1,newsdateFilter2);
            
            //���߹�����
            //NodeFilter newsauthorFilter1 = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "editUsr"));
            NodeFilter newsauthorFilter2 = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "source"));
            //NodeFilter newsauthorFilter = new OrFilter(newsauthorFilter1,newsauthorFilter2);
            
            String newsTitle = getTitle(titleFilter, parser);
            if(newsTitle==null || newsTitle.equals(""))
            	return ;
            System.out.println("qzq����:"+newsTitle);
            //System.out.println(url);
            parser.reset();                    //���ý�����
            String newsContent = getNewsContent(contentFilter, parser);
            if(newsContent==null || newsContent.equals(""))
            	return ;
            //System.out.println("newsContent-----------"+newsContent);
            int index=newsContent.indexOf("http://");
            if(index>=0)
            	newsContent=newsContent.substring(0,index);
            //System.out.println("qzq����:"+newsContent+"----"+newsContent.length());   //������
            parser.reset();
            String newsDate = getNewsDate(newsdateFilter1, parser);
            if(newsDate==null || newsDate.equals(""))
            	return ;
            //System.out.println("qzq����:"+newsDate);      //������
            parser.reset();
            String newsauthor = getNewsAuthor(newsauthorFilter2, parser);
            if(newsauthor==null || newsauthor.equals(""))
            	return ;
            //System.out.println("newsauthor-----------"+newsauthor);
            int index1=newsauthor.indexOf("���ߣ�");
            int index3=newsauthor.indexOf("name");
            if(index1>=0)//���������
            {
            	int index2=newsauthor.indexOf("</span>", index1);
            	newsauthor=newsauthor.substring(index1+3, index2);
            }
            else if(index3>=0){
            	
            	int index4=newsauthor.indexOf("</span>", index3);
            	newsauthor=newsauthor.substring(index3+7, index4);
			}
            else {
            	newsauthor="�����ձ�";
			}
            //System.out.println("qzq����:"+newsauthor);    //������

            //���������ϻ�ȡ���ֶ�д�뵽newsBean��
            //System.out.println(newsTitle+":"+newsauthor+":"+newsContent+":"+getDate(url)+":"+url);
            setNews(newsTitle.trim(), newsauthor, newsContent.trim(), getDate(url).trim(), url.trim());
                               
            //��newsBean�е��ֶ�д�뵽���ݿ���
            if(!newsContent.trim().equals("")&&newsContent.trim().length()>10)
            {
            	//System.out.println("qzq");
               this.saveToDB();
            }
           /* try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
        }
        catch (ParserException ex) 
        {
            Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*public boolean DBContains(String url)
    {
    	ResultSet rSet=searchBean.executeQuery("Select * FROM dbo.news");
    	try {
			while( rSet.next() )
			{
				String newsurl = rSet.getString("newsurl").trim();
				if(url.equals(newsurl))
					return true;
				//System.out.println(newsurl);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return false;
    }*/
    
    //����parser�Ĺ���
    public static void main(String[] args) {
    	/*int index=Url.indexOf("20");
    	String date=Url.substring(index,index+8);*/
        SohuNews news = new SohuNews();
        news.parser("http://news.sohu.com/20161111/n472959687.shtml");   
        //String dateString=news.getDate("http://news.sohu.com/20090518/n264012864.shtml");
        //System.out.println("--------"+dateString);
    	
        //boolean is=news.DBContains("http://news.sohu.com/20161102/n472072170.shtml");
        //System.out.println(is);
    }
}
