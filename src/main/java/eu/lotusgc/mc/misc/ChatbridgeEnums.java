//Created by Christopher at 19.03.2024
package eu.lotusgc.mc.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public enum ChatbridgeEnums {
	SHOW_ROLE("showRole"),
	SHOW_ID("showID"),
	SHOW_NICK("showNick"),
	SHOW_SERVERCHANGE("showServerchange"),
	SHOW_JOIN("showJoin"),
	SHOW_QUIT("showLeave"),
	SHOW_CLAN("showClan"),
	
	SHOW_BUKKITADVANCEMENTS("showBAdvancements"),
	SHOW_BUKKITWORLDCHANGE("showBWorldChange"),
	SHOW_BUKKITLEVELCHANGE("showBLevelChange"),
	SHOW_BUKKITKILLENTITY("showBkillEntity"),
	SHOW_BUKKITDEATH("showBdie");
	
	public String node;
	
	ChatbridgeEnums(String node) {
		this.node = node;
	}
	
	public String getNodename() {
		return node;
	}
	
	public static ChatbridgeEnums getEnum(String input) {
		if(getEnumMap().containsKey(input)) {
			return getEnumMap().get(input);
		}else {
			return null;
		}
	}
	
	List<ChatbridgeEnums> getEnums() {
		List<ChatbridgeEnums> list = new ArrayList<>();
		list.add(SHOW_BUKKITADVANCEMENTS);
		list.add(SHOW_BUKKITDEATH);
		list.add(SHOW_BUKKITKILLENTITY);
		list.add(SHOW_BUKKITLEVELCHANGE);
		list.add(SHOW_BUKKITWORLDCHANGE);
		list.add(SHOW_CLAN);
		list.add(SHOW_ID);
		list.add(SHOW_JOIN);
		list.add(SHOW_NICK);
		list.add(SHOW_QUIT);
		list.add(SHOW_ROLE);
		list.add(SHOW_SERVERCHANGE);
		return list;
	}
	
	static HashMap<String, ChatbridgeEnums> getEnumMap() {
		HashMap<String, ChatbridgeEnums> map = new HashMap<>();
		map.put(SHOW_BUKKITADVANCEMENTS.getNodename(), SHOW_BUKKITADVANCEMENTS);
		map.put(SHOW_BUKKITDEATH.getNodename(), SHOW_BUKKITDEATH);
		map.put(SHOW_BUKKITKILLENTITY.getNodename(), SHOW_BUKKITKILLENTITY);
		map.put(SHOW_BUKKITLEVELCHANGE.getNodename(), SHOW_BUKKITLEVELCHANGE);
		map.put(SHOW_BUKKITWORLDCHANGE.getNodename(), SHOW_BUKKITWORLDCHANGE);
		map.put(SHOW_CLAN.getNodename(), SHOW_CLAN);
		map.put(SHOW_ID.getNodename(), SHOW_ID);
		map.put(SHOW_JOIN.getNodename(), SHOW_JOIN);
		map.put(SHOW_NICK.getNodename(), SHOW_NICK);
		map.put(SHOW_QUIT.getNodename(), SHOW_QUIT);
		map.put(SHOW_ROLE.getNodename(), SHOW_ROLE);
		map.put(SHOW_SERVERCHANGE.getNodename(), SHOW_SERVERCHANGE);
		return map;
	}
}