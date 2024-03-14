package eu.lotusgc.mc.misc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;

public class MySQL {
	
	static Connection connection;
	static String sqlprefix = "§6MySQL §7- ";
	
	public static void connect(String host, String port, String database, String username, String password) {
		if(!isConnected()) {
			try {
				connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
				Bukkit.getConsoleSender().sendMessage(sqlprefix + "§aConnected successfully.");
			}catch (SQLException e) {
				Bukkit.getConsoleSender().sendMessage(sqlprefix + "§cCouldn't connect to DB-Server. Error: " + e.getMessage());
			}
		}
	}
	
	public static void disconnect() {
		if(isConnected()) {
			try {
				connection.close();
				Bukkit.getConsoleSender().sendMessage(sqlprefix + "§aDisconnected successfully.");
			}catch (SQLException e) {
				Bukkit.getConsoleSender().sendMessage(sqlprefix + "§cCouldn't disconnect from DB-Server. Error: " + e.getMessage());
			}
		}
	}
	
	public static Connection getConnection() throws SQLException {
		return connection;
	}
	
	public static boolean isConnected() {
		return (connection == null ? false : true);
	}

}