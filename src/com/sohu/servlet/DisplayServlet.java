package com.sohu.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sohu.bean.searchBean;

public class DisplayServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		searchBean searchBean=new searchBean();
		//��ȡsort.jsp�м���������title
				String newsid = request.getParameter("newsid");
				///��ȡsort.jsp�е�searchName�����û�����ļ�������
				String searchName = (String)this.getServletContext().getAttribute("searchName");
				//System.out.println("searchName-----------"+searchName);
				//String transSearchName = new String(searchName.getBytes("ISO-8859-1"),"UTF-8");
				//String transSearchName=searchName;
				//System.out.println("QZQ-----"+transSearchName);
				String sql = "Select * FROM dbo.sortedNews where newsid='";
				sql += newsid;
				sql += "'";

				//�����ַ�������ת���������ܴ����ݿ��м���������
				String tansSql = new String(sql.getBytes("ISO-8859-1"));

				//ͨ��title��������������
				ResultSet RS = searchBean.executeQuery(tansSql);
				String textContent=null;
				String textTitle=null;
				String newsAuthor=null;
				String newsUrl=null;
				String newsDate=null;
				//��ʾ��������,����"�����ؼ���"�ú�ɫ��ʾ
				try {
					if (RS.next()) {
						textContent = searchBean.strtochn(RS.getString("newscontent"));
						textTitle= searchBean.strtochn(RS.getString("newstitle"));			        
						newsAuthor = searchBean.strtochn(RS.getString("newsauthor"));
						newsUrl = searchBean.strtochn(RS.getString("newsurl"));
						newsDate = searchBean.strtochn(RS.getString("newsdate"));
						newsDate=newsDate.substring(0,4)+"-"+newsDate.substring(4,6)
								+"-"+newsDate.substring(6,8);
						
						
						//���ֱ���������ԭ�е����ĸ�ʽ���ᶪʧ�������Ҫ����
						textContent = new String(textContent.getBytes("ISO-8859-1"));
						textTitle = new String(textTitle.getBytes("ISO-8859-1"));
						newsAuthor = new String(newsAuthor.getBytes("ISO-8859-1"));
						newsUrl = new String(newsUrl.getBytes("ISO-8859-1"));
						newsDate = new String(newsDate.getBytes("ISO-8859-1"));
						//searchName = new String(searchName.getBytes("UTF-8"));

						
						textContent=textContent
						.replace(" ", "&nbsp;")
						.replace("\r\n", "<br/>")
						.replace(searchName,"<font color=\"green\">" + searchName + "</font>");
						
						//System.out.println("index-----------"+textContent.indexOf(searchName));
						
						/*System.out.println(textContent);
						System.out.println("========================================");
						System.out.println("index--------"+index);*/
						
					}
					else {
						textContent = new String("û�в鵽��������~".getBytes("ISO-8859-1"));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				this.getServletContext().setAttribute("displayContent", textContent);
				this.getServletContext().setAttribute("displayTitle", textTitle);
				this.getServletContext().setAttribute("displayAuthor", newsAuthor);
				this.getServletContext().setAttribute("displayUrl", newsUrl);
				this.getServletContext().setAttribute("displayDate", newsDate);
				this.getServletContext().setAttribute("clickCount",new Random().nextInt(100)+50+"");
				 request.getRequestDispatcher("/display.jsp").forward(request, response);	
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}

}
