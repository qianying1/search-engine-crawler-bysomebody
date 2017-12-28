package Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sohu.SohuNews;
import com.sohu.db.ConnectionManager;

public class ClearTable {

	    private static ConnectionManager manager = null;    //数据库连接类
	    private static PreparedStatement pstmt = null;      //执行sql语句需要使用的对象

	
	public static void main(String[] args) {
		initDB();
		System.out.println("数据表已清空!");
	}
	public static void initDB()
    {         //sortedNews
    	String sql = "truncate table dbo.sortedNews";
    	manager = new ConnectionManager();
    	
    	try 
        {
        	//和数据库建立连接
            pstmt = manager.getConnection().prepareStatement(sql);
            pstmt.execute();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally 
        {
            try 
            {
                pstmt.close();
                manager.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
