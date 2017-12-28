package Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sohu.SohuNews;
import com.sohu.db.ConnectionManager;

public class ClearTable {

	    private static ConnectionManager manager = null;    //���ݿ�������
	    private static PreparedStatement pstmt = null;      //ִ��sql�����Ҫʹ�õĶ���

	
	public static void main(String[] args) {
		initDB();
		System.out.println("���ݱ������!");
	}
	public static void initDB()
    {         //sortedNews
    	String sql = "truncate table dbo.sortedNews";
    	manager = new ConnectionManager();
    	
    	try 
        {
        	//�����ݿ⽨������
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
