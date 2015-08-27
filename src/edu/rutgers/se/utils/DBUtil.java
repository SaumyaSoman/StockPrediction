package edu.rutgers.se.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Connection to database
 * @author Saumya
 *
 */
public class DBUtil {
	
	static Connection conn = null;
	static Statement stmt = null;

	public static Statement connect() {
		try{
			//Make JDBC connection to database ; username - root ; passwors - (blank)
			DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
			conn = DriverManager.getConnection("jdbc:mysql://localhost/stock", "root", "");
			stmt = conn.createStatement();		
			return stmt;
		}catch(SQLException se){
			se.printStackTrace();
		}
		return null;		
	}
	
	public static void close() {
		
		try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
