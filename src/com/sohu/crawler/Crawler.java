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
 *   通过给定url地址，完成递归解析链接的功能 
 *   @note 该类通过使用LinkDB类，进行url队列的管理
 *         通过使用LinkParser类，实现指定url地址的页面解析
 *         从而完成完成入口url的递归解析
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
    	 //将入口的url放入到待访问队列中，为爬虫开始工作作准备
    	this.seeds=seeds;
    	this.crawlerUI = crawlerUI;
    	initCrawlerWithSeeds(seeds);
    }
    
    /**
     * 输入入口的url地址，并将其放入未访问队列中
     * @param seeds
     */
    private void initCrawlerWithSeeds(String[] seeds) 
    {
    	//已访问队列和未访问队列的初始化（清空操作）
    	LinkDB.clearUnVisitedUrl();
    	LinkDB.clearVisitedUrl();
    	
        for (int i = 0; i < seeds.length; i++) 
        {
            LinkDB.addUnvisitedUrl(seeds[i]);
        }
    }

    /**
     * 对指定url进行抓取的具体实现，包括以下两个部分：
     * 1、提取网页中符合过滤条件的链接地址（递归提取）
     * 2、解析过滤出来的url地址，获取需要的字段
     * @param seeds   初始化的url地址
     */
    public void crawling() 
    {
        LinkFilter filter = new LinkFilter() 
        {
        	//设置符合过滤url的规则
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
      
        // while循环实现递归解析网页链接的功能，通过循环条件可以设定递归的深度
        while (!LinkDB.unVisitedUrlsEmpty() && LinkDB.getVisitedUrlNum() < 1) 
        {       
            //从待访问队列取出url地址，准备访问
            String visitUrl = LinkDB.unVisitedUrlDeQueue();
            
            //如果出去的url地址为空，则舍弃，并继续访问下一个url地址
            if (visitUrl == null) 
            {
                continue;
            }
            
           //记录下已访问的url地址
            LinkDB.addVisitedUrl(visitUrl);
            
            // 提取该url页面中存在的链接
            Set<String> links = LinkParser.extracLinks(visitUrl, filter);
            //System.out.println(links.size());
            //遍历该url中所有的链接
            for (String link : links) 
            {
            	//将url中的每一个链接放入到待访问队列中，从而实现递归访问
                LinkDB.addUnvisitedUrl(link.trim());
                //提取该url中所需的字段信息
                //System.out.println(link);     //测试用
                news.parser(link);
                
                //crawlerUI.crawlingUrl.setText(link);
                //crawlerUI.crawlingNum++;                
            }
            
        }
    }

    // 主函数，单独测试时使用
    public static void main(String[] args) 
    {
    	Crawler crawler = new Crawler(new String[]{"http://news.sohu.com/20161111/n472959687.shtml"});
        crawler.crawling();
    }
    
}

