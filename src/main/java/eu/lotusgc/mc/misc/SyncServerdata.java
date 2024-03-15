package eu.lotusgc.mc.misc;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import eu.lotusgc.mc.main.Main;

public class SyncServerdata {
	
	private static Field recentTps;
	private static Object minecraftServer;
	
	public static void startScheduler() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				LotusController lc = new LotusController();
				try {
					PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE mc_serverstats SET currentPlayers = ?, currentStaffs = ?, maxPlayers = ?, ram_usage = ?, ram_alloc = ?, tps = ?, version = ?, lastUpdated = ? WHERE servername = ?");
					int allPlayers = 0;
					int staffPlayers = 0;
					for(Player all : Bukkit.getOnlinePlayers()) {
						allPlayers++;
						if(all.hasPermission("lgc.isStaff")) {
							staffPlayers++;
						}
					}
					ps.setInt(1, allPlayers);
					ps.setInt(2, staffPlayers);
					ps.setInt(3, Bukkit.getMaxPlayers());
					ps.setString(4, lc.getRAMInfo(RAMInfo.USING));
					ps.setString(5, lc.getRAMInfo(RAMInfo.ALLOCATED));
					ps.setString(6, formatDouble());
					ps.setString(7, Bukkit.getBukkitVersion().split("-")[0]);
					ps.setLong(8, System.currentTimeMillis());
					ps.setString(9, "Gameslobby");
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskTimerAsynchronously(Main.main, 0, 10*20);
	}
	
	public static void setOnlineStatus(boolean status) {
		PreparedStatement ps;
		if(status) {
			try {
				ps = MySQL.getConnection().prepareStatement("UPDATE mc_serverstats SET isOnline = ?, onlineSince = ? WHERE servername = ?");
				ps.setBoolean(1, status);
				ps.setLong(2, System.currentTimeMillis());
				ps.setString(3, new LotusController().getServerName());
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			try {
				ps = MySQL.getConnection().prepareStatement("UPDATE mc_serverstats SET isOnline = ? WHERE servername = ?");
				ps.setBoolean(1, status);
				ps.setString(2, new LotusController().getServerName());
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static double[] getRecentTPS() throws Throwable {
		if(minecraftServer == null) {
			Server server = Bukkit.getServer();
			Field consoleField = server.getClass().getDeclaredField("console");
			consoleField.setAccessible(true);
			minecraftServer = consoleField.get(server);
		}
		if(recentTps == null) {
			recentTps = minecraftServer.getClass().getSuperclass().getDeclaredField("recentTps");
			recentTps.setAccessible(true);
		}
		return (double[]) recentTps.get(minecraftServer);
	}
	
	static String formatDouble() {
		double tps = 0;
		try {
			tps = getRecentTPS()[0];
		} catch (Throwable e) {
			e.printStackTrace();
		}
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(tps);
	}

}
