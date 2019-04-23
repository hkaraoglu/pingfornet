package data;
/**
 *
 * @author hkaraoglu
 */
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
 
/**
 *
 * @author sqlitetutorial.net
 */
public class SQLite {

    private static Connection conn = null;
    
    public static Connection getConnection() {
       
        try
        {
            if(conn == null || conn.isClosed())
            {
                    String url = "jdbc:sqlite:data.db";
                    conn = DriverManager.getConnection(url);
                    System.out.println("Connection to SQLite has been established.");
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLite.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    public static void closeConnection()
    {
        try
        {
            conn.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
