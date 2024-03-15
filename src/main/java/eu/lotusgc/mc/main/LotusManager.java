//Created by Chris Wille at 14.03.2024
package eu.lotusgc.mc.main;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;

import eu.lotusgc.mc.command.BuildCMD;
import eu.lotusgc.mc.command.SpawnSystem;
import eu.lotusgc.mc.event.EffectMoveEvent;
import eu.lotusgc.mc.event.EventBlocker;
import eu.lotusgc.mc.event.InventorySetterHandling;
import eu.lotusgc.mc.event.JoinLeaveEvents;
import eu.lotusgc.mc.event.ScoreboardHandler;
import eu.lotusgc.mc.misc.LotusController;
import eu.lotusgc.mc.misc.MySQL;
import eu.lotusgc.mc.misc.SyncServerdata;
import net.luckperms.api.LuckPerms;

public class LotusManager {
	
	public static File mainFolder = new File("plugins/LotusGaming");
	public static File mainConfig = new File("plugins/LotusGaming/config.yml");
	public static File propsConfig = new File("plugins/LotusGaming/propertiesBackup.yml");
	
	public void preInit() {
		long timestamp = System.currentTimeMillis();
		
		if(!mainFolder.exists()) mainFolder.mkdirs();
		if(!mainConfig.exists()) try { mainConfig.createNewFile(); } catch (Exception ex) { };
		if(!propsConfig.exists()) try { propsConfig.createNewFile(); } catch (Exception ex) { };
		
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(mainConfig);
		cfg.addDefault("MySQL.Host", "127.0.0.1");
		cfg.addDefault("MySQL.Port", "3306");
		cfg.addDefault("MySQL.Database", "TheDataBaseTM");
		cfg.addDefault("MySQL.Username", "user");
		cfg.addDefault("MySQL.Password", "pass");
		cfg.options().copyDefaults(true);
		
		try { cfg.save(mainConfig); } catch (Exception ex) { }
		
		if(!cfg.getString("MySQL.Password").equalsIgnoreCase("pass")) {
			MySQL.connect(cfg.getString("MySQL.Host"), cfg.getString("MySQL.Port"), cfg.getString("MySQL.Database"), cfg.getString("MySQL.Username"), cfg.getString("MySQL.Password"));
		}
		
		Bukkit.getConsoleSender().sendMessage("§aInitialisation took §6" + (System.currentTimeMillis() - timestamp) + "§ams.");
	}
	
	public void init() {
		long timestamp = System.currentTimeMillis();
		
		Main.main.getCommand("build").setExecutor(new BuildCMD());
		Main.main.getCommand("spawn").setExecutor(new SpawnSystem());
		Main.main.getCommand("spawn-admin").setExecutor(new SpawnSystem());
		
		PluginManager pM = Bukkit.getPluginManager();
		pM.registerEvents(new BuildCMD(), Main.main);
		pM.registerEvents(new SpawnSystem(), Main.main);
		pM.registerEvents(new ScoreboardHandler(), Main.main);
		pM.registerEvents(new EffectMoveEvent(), Main.main);
		pM.registerEvents(new JoinLeaveEvents(), Main.main);
		pM.registerEvents(new EventBlocker(), Main.main);
		pM.registerEvents(new InventorySetterHandling(), Main.main);
		
		Bukkit.getConsoleSender().sendMessage("§aInitialisation took §6" + (System.currentTimeMillis() - timestamp) + "§ams.");
	}
	
	public void postInit() {
		long timestamp = System.currentTimeMillis();
		
		LotusController lc = new LotusController();
		lc.initLanguageSystem();
		lc.initPlayerLanguages();
		lc.initPrefixSystem();
		lc.loadServerIDName();
		
		SyncServerdata.startScheduler();
		new ScoreboardHandler().startScheduler(0, 50, 20);
		ScoreboardHandler.initRoles();
		
		Main.luckPerms = (LuckPerms) Bukkit.getServer().getServicesManager().load(LuckPerms.class);
		
		Bukkit.getConsoleSender().sendMessage("§aInitialisation took §6" + (System.currentTimeMillis() - timestamp) + "§ams.");
	}

}