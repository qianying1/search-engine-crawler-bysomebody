package com.sohu.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  ���ݿ������࣬��java�����ܹ��������ݿ�
 *  @author Bob Hu
 * 
 */
public class ConnectionManager 
{
    private Connection con = null;  
    private boolean autoCommit = true;
    /**
     * ���캯��
     */
    public ConnectionManager() 
    {
       
    }

    /**
     *  �������ݿ�����Ӳ���������ȡ���ݿ����ӵĶ���
     */
    public Connection getConnection()
    {
         try 
         {
        	 String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        	 String usr = "QZQuser";
             String pwd = "2514qzq";
             String url = "jdbc:sqlserver://localhost:1433; dataBase=news";

        	//ʹ�����ϵĲ������г�ʼ��
            Class.forName(driverName).newInstance();
            con = DriverManager.getConnection(url, usr, pwd);
            con.setAutoCommit(autoCommit);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex) 
        {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IllegalAccessException ex) 
        {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex) 
        {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return  con;
    }
    
    /**
     * �ر����ݿ������
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
    
    //����ʹ��
    public static void main(String[] args) 
    {
       ConnectionManager conn = new ConnectionManager();
       Connection c = conn.getConnection();
       System.out.println(c.toString());
    }
}
