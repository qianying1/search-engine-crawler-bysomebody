package com.sohu.servlet;

import java.util.*;
import java.sql.*;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import com.sohu.bean.searchBean;

import com.sohu.crawler.Crawler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SortNewsServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// System.out.println("SortNewsServlet");

		searchBean data = new searchBean();
		String searchName = null;
		String first = null;

		// ����ʱ����������
		int sorted_type = 0;
		// ��ȡ����ļ����ؼ���
		searchName = request.getParameter("searchName");
		first = request.getParameter("first");
		String str_sorted_type = request.getParameter("sorted_type");
		// searchName = new String(searchName.getBytes("ISO-8859-1"),"UTF-8");
		// System.out.println("-----------!"+searchName);
		if (str_sorted_type != null) {
			sorted_type = Integer.valueOf(str_sorted_type).intValue();
		}

		// ��������
		data.executeUpdate("truncate table dbo.sortedNews");
		
		if (first != null && first.equals("true")) {
			//����Ǵ���ҳindex.html�״η��ʣ������ԭ���������ܱ����������ܱ�����ȡ����
			
			data.executeUpdate("truncate table dbo.news");
			Crawler crawler = new Crawler(
					new String[] { "http://news.sohu.com" });
			crawler.crawling();
		}

		ResultSet RS = data.executeQuery("Select * FROM dbo.news");

		int counter = 0;
		try {
			while (RS.next()) {
				// �����ݿ��ȡ��Ӧ�ֶ�
				String newstitle = data.strtochn(RS.getString("newstitle"));
				String newsauthor = data.strtochn(RS.getString("newsauthor"));
				String newscontent = data.strtochn(RS.getString("newscontent"));
				String newsurl = data.strtochn(RS.getString("newsurl"));
				String newsdate = data.strtochn(RS.getString("newsdate"));

				// ȥ���ַ����ж���Ŀո�
				searchName = searchName.trim();

				// �����ַ�����ƥ��

				// �ַ������ת��
				String transtitle = new String(newstitle.getBytes("ISO-8859-1"));
				String transAuthor = new String(
						newsauthor.getBytes("ISO-8859-1"));
				String transContent = new String(
						newscontent.getBytes("ISO-8859-1"));
				String transUrl = new String(newsurl.getBytes("ISO-8859-1"));
				String transDate = new String(newsdate.getBytes("ISO-8859-1"));
				String transDate_int = transDate.replaceAll("[^0-9]", "");

				boolean isTitleFind = transtitle.contains(searchName);
				boolean isContentFind = transContent.contains(searchName);
				// System.out.println(newstitle+"--------"+isTitleFind+":"+isTitleFind);
				if ((isTitleFind || isContentFind)
						&& !searchName.trim().equals("")) {
					// ��ƥ��ļ�¼д��sortedNews����
					String sql = "insert into dbo.sortedNews(newstitle,newsauthor,"
							+ "newscontent,newsurl,newsdate,newsdate_int)";
					sql += " values('";
					sql += transtitle;
					sql += "','";
					sql += transAuthor;
					sql += "','";
					sql += transContent;
					sql += "','";
					sql += transUrl;
					sql += "','";
					sql += transDate;
					sql += "','";
					sql += transDate_int;
					sql += "')";
					data.executeUpdate(sql);
					counter++;
				}
			}

			// ���ݿ��¼����
			if (sorted_type == 1) {
				ResultSet tmpRS = data
						.executeQuery("select * from dbo.sortedNews order by newsdate_int desc");
				data.executeUpdate("truncate table dbo.sortedNews");

				while (tmpRS.next()) {
					// �����ݿ��ȡ��Ӧ�ֶ�
					String newstitle = data.strtochn(tmpRS
							.getString("newstitle"));
					String newsauthor = data.strtochn(tmpRS
							.getString("newsauthor"));
					String newscontent = data.strtochn(tmpRS
							.getString("newscontent"));
					String newsurl = data.strtochn(tmpRS.getString("newsurl"));
					String newsdate = data
							.strtochn(tmpRS.getString("newsdate"));

					// ȥ���ַ����ж���Ŀո�
					searchName = searchName.trim();

					// �����ַ�����ƥ��
					Integer isTitleFind = newstitle.indexOf(searchName);
					Integer isContentFind = newscontent.indexOf(searchName);

					// �ַ������ת��
					String transtitle = new String(
							newstitle.getBytes("ISO-8859-1"));
					String transAuthor = new String(
							newsauthor.getBytes("ISO-8859-1"));
					String transContent = new String(
							newscontent.getBytes("ISO-8859-1"));
					String transUrl = new String(newsurl.getBytes("ISO-8859-1"));
					String transDate = new String(
							newsdate.getBytes("ISO-8859-1"));
					String transDate_int = transDate.replaceAll("[^0-9]", "");

					String sql = "insert into dbo.sortedNews(newstitle,newsauthor,newscontent,newsurl,newsdate,newsdate_int)";
					sql += " values('";
					sql += transtitle;
					sql += "','";
					sql += transAuthor;
					sql += "','";
					sql += transContent;
					sql += "','";
					sql += transUrl;
					sql += "','";
					sql += transDate;
					sql += "','";
					sql += transDate_int;
					sql += "')";
					data.executeUpdate(sql);
				}
			}
			// ���ݲ�����dividePage.jspthis.getServletContext()
			this.getServletContext().setAttribute("searchName", searchName);
			this.getServletContext().setAttribute("totalSortedNum", counter);

			//��仰��ʡ�ԣ���search.js�н�����ת
			//request.getRequestDispatcher("/DividePageServlet").forward(request,response);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
