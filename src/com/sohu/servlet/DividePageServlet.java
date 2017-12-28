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
		//��ȡ�����ؼ��ֺͼ��������ܼ�¼����	     
	     String searchName = (String)this.getServletContext().getAttribute("searchName");
	     Integer totalSortedNum  = (Integer)this.getServletContext().getAttribute("totalSortedNum");	
	     //System.out.println(searchName+"-"+totalSortedNum);
	     
	     //�ַ�����ת��
	     //String transSearchName = new String(searchName.getBytes("ISO-8859-1"));
	         
	     //ÿһҳ��10����¼  
	     int pages = 0;
	     int pageSize = 10;
	     //���ݼ�
	     ResultSet sortedRS;
	     String space = "&nbsp;&nbsp;&nbsp";    //����ո���������ַ���
	     String pages_str = request.getParameter("pages");
	     
	     searchBean data=new searchBean();
	     if (pages_str == null )
	     {pages_str = "0";}
	     
	     pages = new Integer(pages_str).intValue();
	     
	     //��ҳ�ĵ�һ������
	     String bottom;
	     //��ҳ�����һ������
	     String top;
	     //ִ�е�sql���
	     String sql = "";
	     int total_page=0;
	     //�������ǰҳ�����ʼ��¼��ź�ĩλ��¼���
	     bottom = String.valueOf( pages * pageSize + 1 );
	     top =    String.valueOf( pages * pageSize +pageSize );
	     
	     //������ʼ��¼��ź�ĩλ��¼��ţ������ݿ��м�������Ӧ�ļ�¼
	     sql = "select * from dbo.sortedNews where newsid>=" + bottom +" and newsid<=" + top;
	     //��ֹ�߽����⣬�磺totalSortedNum=103,�����һҳӦ��Ϊ��ʾ�ļ�¼���Ӧ��Ϊ101-103������101-110
	     String checkedTop = Integer.valueOf(top).intValue() < totalSortedNum.intValue() ? top : totalSortedNum.toString() ;
	     
	     //ִ��sql���
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
			     
			     //���ŵ�����ժҪ
			     int subint=0;
			     if(newscontent.length()>75)
			    	 subint=75;
			     else 
			    	 subint=newscontent.length();
			      newscontent=newscontent.substring(0,subint)+"...";
			     //�������"�����ؼ���"�ú�ɫ��ʾ
			     String redName= newstitle.replace(searchName,"<font color=\"red\">" + searchName+ "</font>");
			     String redContent= newscontent.replace(searchName,"<font color=\"red\">" + searchName+ "</font>");
			     
			     NewsBean tempBean=new NewsBean(redName,newsauthor,redContent,newsdate,newsurl);
			     currentNum++;
			     tempBean.setNewsId(currentNum+"");
			     newsBeans.add(tempBean);
			 }
			
			//��ȡҳ��
		     
		     //count(*)ȡ�ü�¼��
		     sql = "select ceiling(count(*)) as count1 from dbo.sortedNews"; 		     
		     sortedRS = data.executeQuery( sql );
		     
		     //��������ʹ�ã��������쳣����ʾ�Ҳ�����ǰ��
		     sortedRS.next();
		     int temp = sortedRS.getInt( 1 );
		     if(temp%10==0)//��ʮ����������pageSize
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
