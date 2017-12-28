package com.sohu.crawler;

import com.sohu.SohuNews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.Timer;

import com.sohu.*;
import com.sohu.bean.searchBean;

/**
 *   ͨ������url��ַ����ɵݹ�������ӵĹ��� 
 *   @note ����ͨ��ʹ��LinkDB�࣬����url���еĹ���
 *         ͨ��ʹ��LinkParser�࣬ʵ��ָ��url��ַ��ҳ�����
 *         �Ӷ����������url�ĵݹ����
 *   @author Bob Hu
 */
public class Crawler 
{
    SohuNews news = new SohuNews();
    CrawlerUI crawlerUI = null;
    String[] seeds=null;

    public Crawler(CrawlerUI crawlerUI)
    {
    	this.crawlerUI = crawlerUI;
    }
    
    public Crawler(String[] seeds)
    {
    	this.seeds=seeds;
    	initCrawlerWithSeeds(seeds);
    }
    
    public Crawler(CrawlerUI crawlerUI,String[] seeds)
    {
    	 //����ڵ�url���뵽�����ʶ����У�Ϊ���濪ʼ������׼��
    	this.seeds=seeds;
    	this.crawlerUI = crawlerUI;
    	initCrawlerWithSeeds(seeds);
    }
    
    /**
     * ������ڵ�url��ַ�����������δ���ʶ�����
     * @param seeds
     */
    private void initCrawlerWithSeeds(String[] seeds) 
    {
    	//�ѷ��ʶ��к�δ���ʶ��еĳ�ʼ������ղ�����
    	LinkDB.clearUnVisitedUrl();
    	LinkDB.clearVisitedUrl();
    	
        for (int i = 0; i < seeds.length; i++) 
        {
            LinkDB.addUnvisitedUrl(seeds[i]);
        }
    }

    /**
     * ��ָ��url����ץȡ�ľ���ʵ�֣����������������֣�
     * 1����ȡ��ҳ�з��Ϲ������������ӵ�ַ���ݹ���ȡ��
     * 2���������˳�����url��ַ����ȡ��Ҫ���ֶ�
     * @param seeds   ��ʼ����url��ַ
     */
    public void crawling() 
    {
        LinkFilter filter = new LinkFilter() 
        {
        	//���÷��Ϲ���url�Ĺ���
            public boolean accept(String url) 
            {
                if (url.matches("http://news.sohu.com/[\\d]+/n[\\d]+.shtml")) 
                {
                    return true;
                }
                else 
                {
                    return false;
                }
            }
        };
      
        // whileѭ��ʵ�ֵݹ������ҳ���ӵĹ��ܣ�ͨ��ѭ�����������趨�ݹ�����
        while (!LinkDB.unVisitedUrlsEmpty() && LinkDB.getVisitedUrlNum() < 1) 
        {       
            //�Ӵ����ʶ���ȡ��url��ַ��׼������
            String visitUrl = LinkDB.unVisitedUrlDeQueue();
            
            //�����ȥ��url��ַΪ�գ���������������������һ��url��ַ
            if (visitUrl == null) 
            {
                continue;
            }
            
           //��¼���ѷ��ʵ�url��ַ
            LinkDB.addVisitedUrl(visitUrl);
            
            // ��ȡ��urlҳ���д��ڵ�����
            Set<String> links = LinkParser.extracLinks(visitUrl, filter);
            //System.out.println(links.size());
            //������url�����е�����
            for (String link : links) 
            {
            	//��url�е�ÿһ�����ӷ��뵽�����ʶ����У��Ӷ�ʵ�ֵݹ����
                LinkDB.addUnvisitedUrl(link.trim());
                //��ȡ��url��������ֶ���Ϣ
                //System.out.println(link);     //������
                news.parser(link);
                
                //crawlerUI.crawlingUrl.setText(link);
                //crawlerUI.crawlingNum++;                
            }
            
        }
    }

    // ����������������ʱʹ��
    public static void main(String[] args) 
    {
    	Crawler crawler = new Crawler(new String[]{"http://news.sohu.com/20161111/n472959687.shtml"});
        crawler.crawling();
    }
    
}

