package com.sohu.servlet;

import com.sohu.CrawlerUI;
import com.sohu.SohuNews;
import com.sohu.crawler.Crawler;
import com.sohu.crawler.LinkParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
/**
 *   һ��servlet�࣬����jsp��ҳ��web�������Ľ���
 *   @author Bob Hu
 * 
 */
public class GetNewsServlet extends HttpServlet 
{
    SohuNews news = new SohuNews();
    Thread thread = null;
    List newsList = new ArrayList();
        
    /*    ���ַ���ֻ����ɽ���һ��urlҳ���е��������ӣ����ܵݹ�ִ��*/
    protected void processRequest1( HttpServletRequest request, 
    		                       HttpServletResponse response )
                                   throws ServletException, IOException 
    {
    	//�趨���뷽ʽ
        response.setContentType("text/html;charset=UTF-8");
        //���ɽ���������
        final LinkParser parser = new LinkParser();
        // ��ȡ��ҳ�������url��ڵ�ַ
        final String url = request.getParameter("newsfield");  
        System.out.println(url+"���");    //����ʹ��
        
        //�����̣߳�������ҳ�Ľ���
        thread = new Thread (new Runnable() 
        {
            public void run() 
            {
                parser.doParser(url);
            }
        } );
        
        thread.start();             
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest( HttpServletRequest request, 
            HttpServletResponse response )
            throws ServletException, IOException 
    {
    	//�趨���뷽ʽ
        response.setContentType("text/html;charset=UTF-8");
        //���ɽ���������
        CrawlerUI crawlerUI=new CrawlerUI();
        final String url = request.getParameter("newsfield"); 
        final  Crawler crawler = new Crawler(crawlerUI,new String[]{url});
        // ��ȡ��ҳ�������url��ڵ�ַ
        
        System.out.println(url+"���");    //����ʹ��
        
        //�����̣߳�������ҳ�Ľ���
        thread = new Thread (new Runnable() 
        {
            public void run() 
            {
                crawler.crawling();
            }
        } );
        
        thread.start();
        
        request.getSession().setAttribute("newsList", newsList);
        //��ҳ�ض�λ
        //response.sendRedirect("detail.jsp");
    }
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                         throws ServletException, IOException 
    {
        processRequest(request, response); 
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
                          throws ServletException, IOException 
    {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() 
    {
        return "Short description";
    }
}
