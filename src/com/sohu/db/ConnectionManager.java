package com.sohu.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  数据库连接类，是java代码能够操作数据库
 *  @author Bob Hu
 * 
 */
public class ConnectionManager 
{
    private Connection con = null;  
    private boolean autoCommit = true;
    /**
     * 构造函数
     */
    public ConnectionManager() 
    {
       
    }

    /**
     *  进行数据库的连接操作，并获取数据库连接的对象
     */
    public Connection getConnection()
    {
         try 
         {
        	 String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        	 String usr = "QZQuser";
             String pwd = "2514qzq";
             String url = "jdbc:sqlserver://localhost:1433; dataBase=news";

        	//使用以上的参数进行初始化
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
     * 关闭数据库的连接
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
    
    //测试使用
    public static void main(String[] args) 
    {
       ConnectionManager conn = new ConnectionManager();
       Connection c = conn.getConnection();
       System.out.println(c.toString());
    }
}
