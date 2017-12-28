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
 * 负责指定Url的抓取工作
 * 
 */
public class SohuNews 
{

    private Parser parser = null;                //负责解析网页的对象
    private List newsList = new ArrayList();     //用于存放字段信息的链表
    searchBean searchBean=new searchBean();
    private NewsBean bean = new NewsBean();      //和数据库字段对应的JavaBean
    private ConnectionManager manager = null;    //数据库连接类
    private PreparedStatement pstmt = null;      //执行sql语句需要使用的对象

    /**
     * 构造函数
     */
    public SohuNews() 
    {
    	//将原数据库中的内容清空
    	//initDB();
    }

    /**
     * 将JavaBean中的字段对应添加到链表中
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
     *  设定JavaBean中各字段的值
     * @param newsTitle   新闻标题名称
     * @param newsauthor  责任编辑
     * @param newsContent 新闻内容
     * @param newsDate    发表日期
     * @param url         网页连接地址
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
     * 将JavaBean中的字段写入到数据库中
     */
    /*protected void newsToDataBase() 
    {

        //创建线程执行写入数据库的操作（因为需要支持多用户）
        Thread thread = new Thread( new Runnable()
        {

            public void run() 
            {
                boolean sucess = saveToDB(bean);
                if (sucess != false) 
                {
                    System.out.println("写数据库操作失败！");
                }
            }
        } );
        //启动线程
        thread.start();
    }*/

    /**
     *  清空数据库原有内容，并将自动编号的起始值置为1
     */
    public void initDB()
    {
    	String sql = "truncate table dbo.news";
    	manager = new ConnectionManager();
    	
    	try 
        {
        	//和数据库建立连接
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
     * 写数据库操作的实现（sql语句的执行）
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
        //对新闻标题长度的限制
        if (titleLength.length() > 60)     
        { 
            return flag;
        }
        try 
        {
        	//和数据库建立连接
            pstmt = manager.getConnection().prepareStatement(sql);
            
            //从JavaBean中获取字段，写入数据库中
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
     * 从网页上获取新闻的标题
     * @param titleFilter
     * @param parser
     * @return
     */
    private String getTitle(NodeFilter titleFilter, Parser parser) 
    {
        String titleName = "";
        try 
        {
            //依据titleFilter的法则对网页进行过滤
            NodeList titleNodeList = (NodeList) parser.parse(titleFilter);
            //获取新闻标题字符串
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
     * 获取新闻的责任编辑
     * @param newsauthorFilter
     * @param parser
     * @return
     */
    private String getNewsAuthor(NodeFilter newsauthorFilter, Parser parser) 
    {
        String newsAuthor = "";
        try 
        {
        	//依据newsauthorFilter的法则对网页进行过滤
            NodeList authorList = (NodeList) parser.parse(newsauthorFilter);
            
            //获取责任编辑字符串
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
     * 获取新闻时间
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
     * 获取正文内容
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
        	//根据指定的过滤法则处理网页
            NodeList newsContentList = (NodeList) parser.parse(newsContentFilter);
            //  System.out.println(newsContentList.size());     测试使用
            
            //获取正文的内容
            for (int i = 0; i < newsContentList.size(); i++) 
            {
            	Div newsContenTag = (Div) newsContentList.elementAt(i);
                builder = builder.append(newsContenTag.getStringText());
            }
            content = builder.toString();  //类型转换
            
            if (content != null) 
            {  
                parser.reset();
                //选择编码方式
                parser = Parser.createParser(content, "gb2312");
                //遍历paser的所有节点，获取里面的内容
                StringBean sb = new StringBean();
                sb.setCollapse(true);
                parser.visitAllNodesWith(sb);
                content = sb.getStrings();
               
                //除去正文内容中的一些固定格式，将其替换为空
                if ( content != null )
                {
                    content = content.replaceAll("\\\".*[a-z].*\\}", "");           
                    content = content.replace("[我来说两句]", "");
                }


            } 
            else 
            {
               System.out.println("没有得到新闻内容！");
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
     * 对指定的url进行分析，获取其中的新闻标题、责任编辑、作者、发表日期、正文内容
     * @param url 指定的url
     */
    public void parser(String url) 
    {
        try {
            parser = new Parser(url);
            
            //标题过滤器
            NodeFilter titleFilter = new TagNameFilter("h1");
                   
            //正文过滤器
            NodeFilter contentFilter1 = new AndFilter(new TagNameFilter("div"), 
            		                    new HasAttributeFilter("id", "contentText"));
            NodeFilter contentFilter2 = new AndFilter(new TagNameFilter("div"),
            		                    new HasAttributeFilter("id", "sohu_content"));
            NodeFilter contentFilter = new OrFilter(contentFilter1,contentFilter2);
            
            
            // NodeFilter contentFilter = new TagNameFilter("textarea");   // 白云黄鹤 的方法
                    
            //新闻日期过滤器
            NodeFilter newsdateFilter1 = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "time"));
            //NodeFilter newsdateFilter2 = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "r"));
            /*NodeFilter newsdateFilter = new AndFilter(newsdateFilter2, new HasParentFilter(newsdateFilter1));*/
            //NodeFilter newsdateFilter = new OrFilter(newsdateFilter1,newsdateFilter2);
            
            //作者过滤器
            //NodeFilter newsauthorFilter1 = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "editUsr"));
            NodeFilter newsauthorFilter2 = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "source"));
            //NodeFilter newsauthorFilter = new OrFilter(newsauthorFilter1,newsauthorFilter2);
            
            String newsTitle = getTitle(titleFilter, parser);
            if(newsTitle==null || newsTitle.equals(""))
            	return ;
            System.out.println("qzq标题:"+newsTitle);
            //System.out.println(url);
            parser.reset();                    //重置解析器
            String newsContent = getNewsContent(contentFilter, parser);
            if(newsContent==null || newsContent.equals(""))
            	return ;
            //System.out.println("newsContent-----------"+newsContent);
            int index=newsContent.indexOf("http://");
            if(index>=0)
            	newsContent=newsContent.substring(0,index);
            //System.out.println("qzq正文:"+newsContent+"----"+newsContent.length());   //测试用
            parser.reset();
            String newsDate = getNewsDate(newsdateFilter1, parser);
            if(newsDate==null || newsDate.equals(""))
            	return ;
            //System.out.println("qzq日期:"+newsDate);      //测试用
            parser.reset();
            String newsauthor = getNewsAuthor(newsauthorFilter2, parser);
            if(newsauthor==null || newsauthor.equals(""))
            	return ;
            //System.out.println("newsauthor-----------"+newsauthor);
            int index1=newsauthor.indexOf("作者：");
            int index3=newsauthor.indexOf("name");
            if(index1>=0)//如果有作者
            {
            	int index2=newsauthor.indexOf("</span>", index1);
            	newsauthor=newsauthor.substring(index1+3, index2);
            }
            else if(index3>=0){
            	
            	int index4=newsauthor.indexOf("</span>", index3);
            	newsauthor=newsauthor.substring(index3+7, index4);
			}
            else {
            	newsauthor="人民日报";
			}
            //System.out.println("qzq作者:"+newsauthor);    //测试用

            //将从网络上获取的字段写入到newsBean中
            //System.out.println(newsTitle+":"+newsauthor+":"+newsContent+":"+getDate(url)+":"+url);
            setNews(newsTitle.trim(), newsauthor, newsContent.trim(), getDate(url).trim(), url.trim());
                               
            //将newsBean中的字段写入到数据库中
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
    
    //测试parser的功能
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
