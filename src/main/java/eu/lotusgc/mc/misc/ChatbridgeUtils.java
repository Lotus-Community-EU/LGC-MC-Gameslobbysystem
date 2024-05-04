//Created by Christopher at 07.04.2024
package eu.lotusgc.mc.misc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatbridgeUtils {
	
	public HashMap<String, Boolean> getChatbridgeSettings(UUID uuid){
		HashMap<String, Boolean> map = new HashMap<>();
		LotusController lc = new LotusController();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT chatbridgeOptions FROM mc_users WHERE mcuuid = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				String[] splitByOption = rs.getString("chatbridgeOptions").split(";");
				for(String string : splitByOption) {
					String option = string.split("=")[0];
					boolean state = lc.translateBoolean(string.split("=")[1]);
					map.put(ChatbridgeEnums.getEnum(option).getNodename(), state);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public void setChatbridgeSettings(UUID uuid, HashMap<String, Boolean> map) {
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, Boolean> mapLoop : map.entrySet()) {
			sb.append(mapLoop.getKey() + "=" + translateBoolean(mapLoop.getValue()));
			sb.append(";");
		}
		String output = sb.toString().substring(0, sb.toString().length()-1);
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE mc_users SET chatbridgeOptions = ? WHERE mcuuid = ?");
			ps.setString(1, output);
			ps.setString(2, uuid.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static int translateBoolean(boolean input) {
		if(input) {
			return 1;
		}else {
			return 0;
		}
	}
}