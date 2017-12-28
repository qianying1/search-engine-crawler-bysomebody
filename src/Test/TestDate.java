package Test;

import java.sql.ResultSet;

import com.sohu.bean.searchBean;

public class TestDate {

	public static void main(String[] args) {
		searchBean data = new searchBean();
		ResultSet RS = data.executeQuery("Select * FROM dbo.news");
		try {
			while( RS.next() )
		    {
		        //从数据库获取相应字段
		      String newstitle = data.strtochn(RS.getString("newstitle"));			        
	          String newsauthor = data.strtochn(RS.getString("newsauthor"));
	          String newscontent = data.strtochn(RS.getString("newscontent"));
           String newsurl = data.strtochn(RS.getString("newsurl"));
           String newsdate = data.strtochn(RS.getString("newsdate"));
           
           String transtitle = new String(newstitle.getBytes("ISO-8859-1"));
           String transAuthor = new String(newsauthor.getBytes("ISO-8859-1"));
           String transContent = new String(newscontent.getBytes("ISO-8859-1"));
           String transUrl = new String(newsurl.getBytes("ISO-8859-1"));
           String transDate = new String(newsdate.getBytes("ISO-8859-1")).trim();
                 //transDate=transDate.substring(0,transDate.length()-11);
           //String transDate_int = transDate.replaceAll("[^0-9]", "");
           
           System.out.println(transtitle+"--------"+transDate);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
	}

}
