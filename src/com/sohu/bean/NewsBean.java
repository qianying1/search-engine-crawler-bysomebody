package com.sohu.bean;

/**
 *
 * 一个 javaBean类，该类的成员变量和数据库中的各个字段对应
 * @author Bob Hu
 */
public class NewsBean 
{
    private String newsTitle;
    private String newsAuthor;
    private String newsContent;
    private String newsDate;
    private String newsURL;
    private String newsId="0";

    public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public NewsBean() {
		super();
	}

	public NewsBean(String newsTitle, String newsAuthor, String newsContent,
			String newsDate, String newsURL) {
		super();
		this.newsTitle = newsTitle;
		this.newsAuthor = newsAuthor;
		this.newsContent = newsContent;
		this.newsDate = newsDate;
		this.newsURL = newsURL;
	}

	/**
     * @return the newsTitle
     */
    public String getNewsTitle() 
    {
        return newsTitle;
    }

    /**
     * @param newsTitleS the newsTitleS to set
     */
    public void setNewsTitle(String newsTitle) 
    {
        this.newsTitle = newsTitle;
    }

    /**
     * @return the newsAuthor
     */
    public String getNewsAuthor() 
    {
        return newsAuthor;
    }

    /**
     * @param newsAuthor the newsAuthor to set
     */
    public void setNewsAuthor(String newsAuthor) 
    {
        this.newsAuthor = newsAuthor;
    }

    /**
     * @return the newsContent
     */
    public String getNewsContent() 
    {
        return newsContent;
    }

    /**
     * @param newsContent the newsContent to set
     */
    public void setNewsContent(String newsContent) 
    {
        this.newsContent = newsContent;
    }

    /**
     * @return the newsDate
     */
    public String getNewsDate() 
    {
        return newsDate;
    }

    /**
     * @param newsDate the newsDate to set
     */
    public void setNewsDate(String newsDate) 
    {
        this.newsDate = newsDate;
    }

    /**
     * @return the newsURL
     */
    public String getNewsURL() 
    {
        return newsURL;
    }

    /**
     * @param newsURL the newsURL to set
     */
    public void setNewsURL(String newsURL) 
    {
        this.newsURL = newsURL;
    }
}
