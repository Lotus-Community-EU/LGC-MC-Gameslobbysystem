//Created by Chris Wille at 14.03.2024
package eu.lotusgc.mc.misc;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class HotbarItem {
	
	public static String hb_extras = "§9Extras";
	public static String hb_language = "§6Language";
	public static String hb_navigator = "§aNavigator";
	public static String hb_friends = "§3Friends";
	public static String hb_psettings = "§3Settings";
	
	public void setHotbarItems(Player player) {
		LotusController lc = new LotusController();
		player.getInventory().clear();
		player.getInventory().setItem(0, lc.defItem(Material.ECHO_SHARD, hb_extras, 1)); //extras
		player.getInventory().setItem(1, lc.defItem(Material.BOOK, hb_language, 1)); //language
		player.getInventory().setItem(5, lc.defItem(Material.COMPASS, hb_navigator, 1)); //navigator
		player.getInventory().setItem(7, lc.skullItem(1, hb_psettings, player)); //profilesettings
		player.getInventory().setItem(8, lc.skullItem(1, hb_friends, player)); //friends
	}

}

