package dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

	public static Connection getConnection() {
		Connection con = null;
		try {
			  String url="jdbc:mysql://localhost/javaBackend";
		      String user="root";
		      String pwd="";
		      
		      //load driver
		     Class.forName("com.mysql.jdbc.Driver").newInstance();
		     con = DriverManager.getConnection(url,user, pwd);
			 return con;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void close(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
