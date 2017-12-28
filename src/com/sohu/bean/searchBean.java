package com.sohu.bean;
import java.sql.*;

/**
 * ���ݿ������
 * @author Bob Hu
 *
 */
public class searchBean
{
	private Connection con = null; 
    ResultSet rs = null;
    Statement stmt_query;
    Statement stmt_update;
    
    /**
     * ���캯����������ݿ�����ӹ���
     */
    public searchBean()
    {
         try 
         {
        	 String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        	 String usr = "QZQuser";
             String pwd = "2514qzq";
             String url = "jdbc:sqlserver://localhost:1433; dataBase=news";

             Class.forName(driverName);
             con = DriverManager.getConnection(url, usr, pwd);
             stmt_query = con.createStatement();
             stmt_update = con.createStatement();            
         }
         catch(java.lang.ClassNotFoundException e)
         { 
         	   System.err.println("faq()L" + e.getMessage());
         }
         catch(java.sql.SQLException e1)
         {
         	  System.err.println("faq():" + e1.getMessage());
         }
     }

    /**
     * �Ͽ������ݿ������
     */
     public void close()
     {
     	if (con != null) 
	    {
	        try 
	        {
	            con.close();
	        }
	        catch (Exception e) 
	        {
	            e.printStackTrace();
	        } 
	        finally 
	        {
	            con = null;
	        }
	    }
     }
     
    /**
     * ����ָ����sql��䷵�ز�ѯ������ݼ� 
     * @param sql ��ָ����sql���
     * @return
     */
    public ResultSet executeQuery(String sql)
	{
	    rs = null;
	    try
	    {
	        rs=stmt_query.executeQuery(sql);
	    }
	    catch(SQLException ex)
	    {
	   	    System.err.println("aq.executeQuery:" + ex.getMessage());
	    }
	    return rs;
	}
	
    /**
     * �ַ�����ת������Ҫ�����������
     * @param str
     * @return
     */
	public String strtochn(String str)
	{
		   byte[] byte1=str.getBytes();
		   String tmp="";
		   try
		   {
		   	    tmp= new String(byte1,"8859_1");
		   }
		   catch(Exception e)
		   {
		   	   System.err.println("strToChn�쳣" + e.getMessage() );   
		   }
		   return tmp;
	}
	
	/**
	 * ִ��sql���
	 * @param sql
	 * @return
	 */
	public boolean executeUpdate( String sql )
	{
		   try
		   {
		   	   stmt_update.executeUpdate( sql );
		   	   return true;
		   }
		   catch( SQLException e )
		   {
		       System.err.println( " aq.excuteUpdate: " + e.getMessage() );	
		   }
		   return false;
	}
}



