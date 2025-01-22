//Created by Maurice H. at 21.01.2025
package eu.lotusgc.mc.misc.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eu.lotusgc.mc.misc.MySQL;
import net.md_5.bungee.api.ChatColor;

public class GameServer {
	
	String servername, displayname, bungeeKey, minigameType, mg_mapName, mg_state;
	int mg_maxSlots, maxSlots, currentPlayers;
	boolean isOnline, isLocked, isMonitored, isMinigameServer, existServer;
	

	public GameServer(String server, Server srv) {
		String statement = "SELECT * FROM mc_serverstats WHERE";
		if(srv == Server.Name) {
			statement += " servername = ?";
		}else if(srv == Server.Key) {
			statement += " bungeeKey = ?";
		}else if(srv == Server.Fancyname) {
			statement += " displayname = ?";
		}
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement(statement);
			ps.setString(1, server);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				servername = rs.getString("servername");
				displayname = rs.getString("displayname");
				bungeeKey = rs.getString("bungeeKey");
				minigameType = rs.getString("minigameType");
				mg_mapName = rs.getString("mg_mapName");
				mg_maxSlots = rs.getInt("mg_maxSlots");
				mg_state = rs.getString("mg_state");
				isOnline = rs.getBoolean("isOnline");
				isLocked = rs.getBoolean("isLocked");
				isMonitored = rs.getBoolean("isMonitored");
				currentPlayers = rs.getInt("currentPlayers");
				isMinigameServer = rs.getBoolean("isMinigame");
				existServer = true;
			}else {
				existServer = false;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public enum Server {
		Name,
		Key,
		Fancyname;
	}

	public String getServername() {
		return servername;
	}

	public String getDisplayname() {
		return ChatColor.translateAlternateColorCodes('&', displayname);
	}

	public String getBungeeKey() {
		return bungeeKey;
	}

	public String getMinigameType() {
		return minigameType;
	}

	public String getMinigame_Mapname() {
		return mg_mapName;
	}
	
	public String getMinigame_State() {
		return mg_state;
	}

	public int getMaxSlots() {
		if(isMinigameServer) {
			return mg_maxSlots;
		}else {
			return maxSlots;
		}
		
	}
	
	public boolean isMinigameServer() {
		return isMinigameServer;
	}
	
	public int getCurrentPlayers() {
		return currentPlayers;
	}
	
	public boolean isOnline() {
		return isOnline;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public boolean isMonitored() {
		return isMonitored;
	}
	
	public boolean existServer() {
		return existServer;
	}
}