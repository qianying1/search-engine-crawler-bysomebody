package com.sohu.crawler;

import com.sohu.SohuNews;
import java.util.HashSet;
import java.util.Set;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 *  ������ҳ�д��ڵ����� 
 *  @author Bob Hu
 *  @note �����������ָ����urlҳ���д��ڵķ��Ϲ�������������
 */
public class LinkParser 
{   
	/**
	 * ��ָ����url�н��������д��ڵ����ӣ�����������һ��set������
	 * @param url
	 * @param filter
	 * @return
	 */
    public static Set<String> extracLinks(String url, LinkFilter filter) 
    {
        Set<String> links = new HashSet<String>();
        try 
        {   
            //����һ�����������󣬴���url��ַ
            Parser parser = new Parser(url);
            //�趨���뷽ʽ
            parser.setEncoding("gb2312");
            
            //���� <frame> ��ǩ�� filter��������ȡ frame ��ǩ��� src ��������ʾ������
            NodeFilter frameFilter = new NodeFilter() 
            {   
            	//�趨ƥ��ķ��򣬸ýڵ��Ƿ���"frame src="��ͷ
                public boolean accept(Node node) 
                {
                    if (node.getText().startsWith("frame src=")) 
                    {
                        return true;
                    }
                    else 
                    {
                        return false;
                    }
                }
            };
           //OrFilter �����ù��� <a> ��ǩ�� <frame> ��ǩ��������ǩ�� or �Ĺ�ϵ
            OrFilter linkFilter = new OrFilter( new NodeClassFilter(LinkTag.class),
            		                            frameFilter );
            // �õ����о������˵ı�ǩ
            NodeList list = parser.extractAllNodesThatMatch(linkFilter);
            for (int i = 0; i < list.size(); i++) 
            {
                Node tag = list.elementAt(i);
                if (tag instanceof LinkTag)   //��<a>��ǩ
                {
                    LinkTag link = (LinkTag) tag;
                    String linkUrl = link.getLink();  //url
                    //System.out.println(linkUrl);    //������
                    
                    //�����Ϲ���������������ӵ�set�����У�������Ľ���ʹ��
                    if (filter.accept(linkUrl)) 
                    {
                        links.add(linkUrl);
                    }
                } 
                else    // <frame>��ǩ��
                {
                	//��ȡ frame �� src ���Ե������� <frame src="xxx.html"/>
                    String frame = tag.getText();
                    int start = frame.indexOf("src=");
                    frame = frame.substring(start);
                    int end = frame.indexOf(" ");
                    if (end == -1) 
                    {
                        end = frame.indexOf(">");
                    }
                    String frameUrl = frame.substring(5, end - 1);
                    
                    //�����Ϲ���������������ӵ�set�����У�������Ľ���ʹ��
                    if (filter.accept(frameUrl)) 
                    {
                        links.add(frameUrl);
                    }
                }
            }
        } 
        catch (ParserException e) 
        {
            e.printStackTrace();
        }
        return links;
    }
    /**
     * ����ָ��url������ʵ��
     * �ú����е����˱�����extracLinks������ȡ��ҳ�е����ӣ�Ȼ�������SohuNews���е�parse������
     * ��ȡָ�������е��ֶ���Ϣ���Ӷ����ץȡ������
     * @param url
     */
    public void doParser(String url) 
    {
        SohuNews news = new SohuNews();
        Set<String> links = LinkParser.extracLinks(url, new LinkFilter() 
        {
        	//���ù������ӵ�����
            public boolean accept(String url) 
            {
            	//http://www.byhh.net/cgi-bin/bbstcon?board=EI&file=M.1265631155.A&start=1798
                if (url.matches("http://news.sohu.com/[\\d]+/n[\\d]+.shtml")) 
            	//if (url.matches("http://www.byhh.net/cgi-bin/bbstdoc\\?board=EI&start=[0-9]+")) 
            	{
                    return true;
                }
            	else 
            	{
                    return false;
                }
            }
        } );
        
        //ִ����extracLinks������set�������Ѿ������������Ҫ���н�����url
        //���������set���ϣ����������е�url���н���
        for (String link : links) 
        {
            System.out.println(link);  //����ʹ��
            news.parser(link);         //ȥ���Ժ󼴲���д���ݿ⣬ֱ�Ӵ�ӡ����̨��
        }
    }

    //����������������ʱʹ��
    public static void main(String[] args) 
    {
        String url = "http://news.sohu.com/";
        LinkParser parser = new LinkParser();
        parser.doParser(url);

    }
}

