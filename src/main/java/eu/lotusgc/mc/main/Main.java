//Created by Chris Wille at 14.03.2024
package eu.lotusgc.mc.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import eu.lotusgc.mc.misc.MySQL;
import eu.lotusgc.mc.misc.SyncServerdata;
import net.luckperms.api.LuckPerms;

public class Main extends JavaPlugin{
	
	public static Main main;
	public static String consoleSend = "§cPlease execute this command in-game!";
	public static Logger logger;
	public static LuckPerms luckPerms;
	
	public void onEnable() {
		main = this;
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setLevel(Level.ALL);
		
		LotusManager mgr = new LotusManager();
		mgr.preInit();
		mgr.init();
		mgr.postInit();
		
		SyncServerdata.setOnlineStatus(true);
	}
	
	public void onDisable() {
		main = null;
		SyncServerdata.setOnlineStatus(false);
		MySQL.disconnect();
	}

}

