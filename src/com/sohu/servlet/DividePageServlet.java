package com.sohu.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sohu.bean.NewsBean;
import com.sohu.bean.searchBean;

public class DividePageServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取检索关键字和检索出的总记录条数	     
	     String searchName = (String)this.getServletContext().getAttribute("searchName");
	     Integer totalSortedNum  = (Integer)this.getServletContext().getAttribute("totalSortedNum");	
	     //System.out.println(searchName+"-"+totalSortedNum);
	     
	     //字符编码转换
	     //String transSearchName = new String(searchName.getBytes("ISO-8859-1"));
	         
	     //每一页有10条记录  
	     int pages = 0;
	     int pageSize = 10;
	     //数据集
	     ResultSet sortedRS;
	     String space = "&nbsp;&nbsp;&nbsp";    //定义空格所代表的字符串
	     String pages_str = request.getParameter("pages");
	     
	     searchBean data=new searchBean();
	     if (pages_str == null )
	     {pages_str = "0";}
	     
	     pages = new Integer(pages_str).intValue();
	     
	     //该页的第一条数据
	     String bottom;
	     //该页的最后一条数据
	     String top;
	     //执行的sql语句
	     String sql = "";
	     int total_page=0;
	     //计算出当前页面的起始记录编号和末位记录编号
	     bottom = String.valueOf( pages * pageSize + 1 );
	     top =    String.valueOf( pages * pageSize +pageSize );
	     
	     //根据起始记录编号和末位记录编号，从数据库中检索出对应的记录
	     sql = "select * from dbo.sortedNews where newsid>=" + bottom +" and newsid<=" + top;
	     //防止边界问题，如：totalSortedNum=103,则最后一页应该为显示的记录编号应该为101-103，而非101-110
	     String checkedTop = Integer.valueOf(top).intValue() < totalSortedNum.intValue() ? top : totalSortedNum.toString() ;
	     
	     //执行sql语句
	     sortedRS = data.executeQuery( sql );
	     String newstitle;
	     String newsauthor;
	     String newsdate;
	     String newsurl;
	     String newscontent;
	     int currentNum=0;
	     ArrayList<NewsBean> newsBeans=new ArrayList<NewsBean>();
	     try {
			while (sortedRS.next())
			 {
			     newstitle = sortedRS.getString("newstitle");
			     newsauthor = sortedRS.getString("newsauthor");
			     newsdate = sortedRS.getString("newsdate").trim();
			     newsurl = sortedRS.getString("newsurl");
			     newscontent = sortedRS.getString("newscontent");
			     newsdate=newsdate.substring(0,4)+"-"+newsdate.substring(4,6)+"-"+newsdate.substring(6,8);
			     
			     //新闻的内容摘要
			     int subint=0;
			     if(newscontent.length()>75)
			    	 subint=75;
			     else 
			    	 subint=newscontent.length();
			      newscontent=newscontent.substring(0,subint)+"...";
			     //将标题的"检索关键字"用红色显示
			     String redName= newstitle.replace(searchName,"<font color=\"red\">" + searchName+ "</font>");
			     String redContent= newscontent.replace(searchName,"<font color=\"red\">" + searchName+ "</font>");
			     
			     NewsBean tempBean=new NewsBean(redName,newsauthor,redContent,newsdate,newsurl);
			     currentNum++;
			     tempBean.setNewsId(currentNum+"");
			     newsBeans.add(tempBean);
			 }
			
			//获取页数
		     
		     //count(*)取得记录集
		     sql = "select ceiling(count(*)) as count1 from dbo.sortedNews"; 		     
		     sortedRS = data.executeQuery( sql );
		     
		     //该语句必须使用，否则将有异常，提示找不到当前行
		     sortedRS.next();
		     int temp = sortedRS.getInt( 1 );
		     if(temp%10==0)//整十倍的新闻数pageSize
		    	 total_page=temp/pageSize-1;
		     else
		    	 total_page=temp/pageSize;
		     sortedRS.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	     
	     this.getServletContext().setAttribute("bottom", bottom);
	     this.getServletContext().setAttribute("checkedTop", checkedTop);
	     this.getServletContext().setAttribute("space", space);
	     this.getServletContext().setAttribute("newsBeans", newsBeans);
	     this.getServletContext().setAttribute("total_page", total_page);
	     this.getServletContext().setAttribute("currentPage", pages+"");
	     request.getRequestDispatcher("/dividePage.jsp").forward(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
