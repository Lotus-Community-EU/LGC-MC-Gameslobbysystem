package eu.lotusgc.mc.command;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import eu.lotusgc.mc.main.LotusManager;
import eu.lotusgc.mc.main.Main;
import eu.lotusgc.mc.misc.LotusController;
import eu.lotusgc.mc.misc.Prefix;

public class SpawnSystem implements CommandExecutor, Listener{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player player) {
			if(command.getName().equalsIgnoreCase("spawn-admin")) {
				LotusController lc = new LotusController();
				// SYNTAX /spawn-admin <setSpawn|setdrew|setcrates|minHeight|maxHeight>
				if(args.length == 1) {
					String mode = args[0];
					if(mode.equalsIgnoreCase("setspawn")) {
						if(player.hasPermission("lgc.spawn.admin")) {
							player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "global.args") + " §7/spawn-admin <setSpawn|minHeight|maxHeight> [gameName(only if Setspawn is chosen)]");
						}else {
							lc.noPerm(player, "lgc.spawn.admin");
						}
					}else if(mode.equalsIgnoreCase("minHeight")) {
						if(player.hasPermission("lgc.spawn.admin")) {
							Location loc = player.getLocation();
							setHeight(loc.getY(), "min");
							DecimalFormat df = new DecimalFormat("#");
							player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "cmd.spawnadmin.height").replace("%mode%", "min").replace("%y%", df.format(loc.getY())));
						}else {
							lc.noPerm(player, "lgc.spawn.admin");
						}
					}else if(mode.equalsIgnoreCase("maxHeight")) {
						if(player.hasPermission("lgc.spawn.admin")) {
							Location loc = player.getLocation();
							setHeight(loc.getY(), "max");
							DecimalFormat df = new DecimalFormat("#");
							player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "cmd.spawnadmin.height").replace("%mode%", "max").replace("%y%", df.format(loc.getY())));
						}else {
							lc.noPerm(player, "lgc.spawn.admin");
						}
					}else {
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "global.args") + " §7/spawn-admin <setSpawn|minHeight|maxHeight> [gameName(only if Setspawn is chosen)]");
					}
				}else if(args.length == 2) {
					String mode = args[0];
					String name = args[1];
					
					if(mode.equalsIgnoreCase("setspawn")) {
						if(player.hasPermission("lgc.spawn.admin")) {
							setSpawn(player.getLocation(), player, name);
							//lc.sendMessageReady(player, "cmd.spawnadmin.update");
							player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "cmd.spawnadmin.update").replace("%spawnpoint%", name));
						}else {
							lc.noPerm(player, "lgc.spawn.admin");
						}
					}else {
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "global.args") + " §7/spawn-admin <setSpawn|setdrew|setcrates|minHeight|maxHeight>");
					}
				}else {
					player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "global.args") + " §7/spawn-admin <setSpawn|setdrew|setcrates|minHeight|maxHeight>");
				}
			}else if(command.getName().equalsIgnoreCase("spawn")) {
				// SYNTAX /spawn [no args]
				LotusController lc = new LotusController();
				player.teleport(getSpawn("mainSpawn"));
				lc.sendMessageReady(player, "cmd.spawn.success");
			}
		}else {
			Bukkit.getConsoleSender().sendMessage(Main.consoleSend);
		}
		return true;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if(event.getPlayer().hasPlayedBefore()) {
			event.getPlayer().teleport(getSpawn("mainSpawn"));
		}else {
			new BukkitRunnable() {
				
				@Override
				public void run() {
					event.getPlayer().teleport(getSpawn("mainSpawn"));
				}
			}.runTaskLater(Main.main, 10);
		}
		
	}
	
	void setHeight(double y, String pos) {
		File config = LotusManager.mainConfig;
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(config);
		cfg.set("Spawn.height." + pos, y);
		try {
			cfg.save(config);
		} catch (IOException e) {
			Main.logger.severe("Attempting to save spawn, but errored: " + e.getMessage());
		}
	}
	
	void setSpawn(Location location, Player player, String use) {
		File config = LotusManager.mainConfig;
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(config);
		
		if(location != null) {
			cfg.set("Spawn." + use + ".World", location.getWorld().getName());
			cfg.set("Spawn." + use + ".X", location.getX());
			cfg.set("Spawn." + use + ".Y", location.getY());
			cfg.set("Spawn." + use + ".Z", location.getZ());
			cfg.set("Spawn." + use + ".YAW", location.getYaw());
			cfg.set("Spawn." + use + ".PITCH", location.getPitch());
			cfg.set("Spawn." + use + ".Timestamp.Set", System.currentTimeMillis());
		}
		if(player != null) {
			cfg.set("Spawn." + use + ".Setter", player.getUniqueId().toString());
		}
		try {
			cfg.save(config);
			Main.logger.info("Spawn has been updated by " + player.getName() + " with the attribute: spawnType=" + use);
		} catch (IOException e) {
			Main.logger.severe("Attempting to save spawn, but errored: " + e.getMessage());
		}
	}
	
	public static Location getSpawn(String use) {
		File config = LotusManager.mainConfig;
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(config);
		
		Location location = new Location(Bukkit.getWorld(cfg.getString("Spawn." + use + ".World")), cfg.getDouble("Spawn." + use + ".X"), cfg.getDouble("Spawn." + use + ".Y"), cfg.getDouble("Spawn." + use + ".Z"), (float)cfg.getDouble("Spawn." + use + ".YAW"), (float)cfg.getDouble("Spawn." + use + ".PITCH"));
		return location;
	}

}
