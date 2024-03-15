//Created by Chris Wille at 14.03.2024
package eu.lotusgc.mc.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import eu.lotusgc.mc.command.BuildCMD;
import eu.lotusgc.mc.main.Main;
import eu.lotusgc.mc.misc.LotusController;
import eu.lotusgc.mc.misc.MySQL;
import eu.lotusgc.mc.misc.Playerdata;
import eu.lotusgc.mc.misc.Prefix;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;

public class ScoreboardHandler implements Listener{
	
	private static HashMap<String, String> tabHM = new HashMap<>(); //HashMap for Tab
	private static HashMap<String, String> chatHM = new HashMap<>(); //HashMap for Chat
	private static HashMap<String, String> roleHM = new HashMap<>(); //HashMap for Team Priority (Sorted)
	private static HashMap<String, String> sbHM = new HashMap<>(); //HashMap for Sideboard (Like Chat, just with no additional chars)
	public static HashMap<Player, Long> buildTime = new HashMap<>();
	
	public void setSB(Player player) {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective o = sb.registerNewObjective("aaa", Criteria.DUMMY, "LGCINFOBOARD");
		LotusController lc = new LotusController();
		String sbPrefix = lc.getPrefix(Prefix.SCOREBOARD);
		
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		if(BuildCMD.hasPlayer(player)) {
			ItemStack mainHandItem = player.getInventory().getItemInMainHand();
			ItemStack offHandItem = player.getInventory().getItemInOffHand();
			o.setDisplayName("§bBuild Statistics");
			o.getScore(lc.sendMessageToFormat(player, "event.scoreboard.build.usedTime")).setScore(6);
			o.getScore("§7» §a" + getBuildTime(player)).setScore(5);
			o.getScore("§0").setScore(4);
			if(!mainHandItem.getType().toString().equalsIgnoreCase("air")) {
				o.getScore(lc.sendMessageToFormat(player, "event.scoreboard.build.block") + "§6").setScore(3);
				o.getScore("§7» §a" + mainHandItem.getType().toString()).setScore(2);
			}
			if(!offHandItem.getType().toString().equalsIgnoreCase("air")) {
				o.getScore(lc.sendMessageToFormat(player, "event.scoreboard.build.block") + "§7").setScore(1);
				o.getScore("§7» §a" + offHandItem.getType().toString()).setScore(0);
			}
		}else {
			o.setDisplayName(sbPrefix);
			//playerinfo
			o.getScore(lc.sendMessageToFormat(player, "event.scoreboard.userid")).setScore(8);
			o.getScore("§7» §a" + lc.getPlayerData(player, Playerdata.LotusChangeID)).setScore(7);
			o.getScore("§7Clan:").setScore(6);
			o.getScore("§7» §a" + lc.getPlayerData(player, Playerdata.Clan)).setScore(5);
			//role
			o.getScore(lc.sendMessageToFormat(player, "event.scoreboard.role")).setScore(4);
			o.getScore(retGroup(player)).setScore(3);
			//money
			o.getScore(lc.sendMessageToFormat(player, "event.scoreboard.money")).setScore(2);
			o.getScore("§7» Pocket: §a" + lc.getPlayerData(player, Playerdata.MoneyPocket) + " §6Loti").setScore(1);
			o.getScore("§7» Bank: §e" + lc.getPlayerData(player, Playerdata.MoneyBank) + " §6Loti").setScore(0);
		
		}
		player.setScoreboard(sb);
		
		Team projlead = getTeam(sb, "projectlead", ChatColor.DARK_GRAY);
		Team viceProjLead = getTeam(sb, "viceprojlead", ChatColor.DARK_GRAY);
		Team staffmanager = getTeam(sb, "staffmanager", ChatColor.DARK_GRAY);
		Team staffsupervisor = getTeam(sb, "staffsupervisor", ChatColor.DARK_GRAY);
		Team developer = getTeam(sb, "developer", ChatColor.DARK_GRAY);
		Team headofcommunity = getTeam(sb, "headofcommunity", ChatColor.DARK_GRAY);
		Team humanresources = getTeam(sb, "humanresources", ChatColor.DARK_GRAY);
		Team qualityassman = getTeam(sb, "qualityassman", ChatColor.DARK_GRAY);
		Team admin = getTeam(sb, "admin", ChatColor.GRAY);
		Team builder = getTeam(sb, "builder", ChatColor.GRAY);
		Team designer = getTeam(sb, "designer", ChatColor.GRAY);
		Team moderator = getTeam(sb, "moderator", ChatColor.GRAY);
		Team support = getTeam(sb, "support", ChatColor.GRAY);
		Team translator = getTeam(sb, "translator", ChatColor.GRAY);
		Team addon = getTeam(sb, "addon", ChatColor.GRAY);
		Team retired = getTeam(sb, "retired", ChatColor.WHITE);
		Team beta = getTeam(sb, "beta", ChatColor.WHITE);
		Team userg = getTeam(sb, "default", ChatColor.WHITE);
		
		for(Player all : Bukkit.getOnlinePlayers()) {
			//Lotus Internal
			String nick = lc.getPlayerData(all, Playerdata.Nick);
			String clan = lc.getPlayerData(all, Playerdata.Clan);
			String id = lc.getPlayerData(all, Playerdata.LotusChangeID);
			if(nick.equalsIgnoreCase("none")) {
				all.setCustomName(all.getName());
			}else {
				all.setCustomName(nick);
			}
			if(clan.equalsIgnoreCase("none")) {
				clan = "";
			}
			
			//LuckPerms
			UserManager um = Main.luckPerms.getUserManager();
			User user = um.getUser(all.getName());
			
			if(user.getPrimaryGroup().equalsIgnoreCase("projectlead")) {
				projlead.addEntry(all.getName());
				all.setDisplayName(returnPrefix(user.getPrimaryGroup(), RankType.CHAT) + all.getCustomName());
				all.setPlayerListName(returnPrefix(user.getPrimaryGroup(), RankType.TAB) + all.getCustomName() + " §7(§a" + id + "§7) §f" + clan);
			}else if(user.getPrimaryGroup().equalsIgnoreCase("viceprojectleader")) {
				viceProjLead.addEntry(all.getName());
				all.setDisplayName(returnPrefix(user.getPrimaryGroup(), RankType.CHAT) + all.getCustomName());
				all.setPlayerListName(returnPrefix(user.getPrimaryGroup(), RankType.TAB) + all.getCustomName() + " §7(§a" + id + "§7) §f" + clan);
			}else if(user.getPrimaryGroup().equalsIgnoreCase("staffmanager")) {
				staffmanager.addEntry(all.getName());
				all.setDisplayName(returnPrefix(user.getPrimaryGroup(), RankType.CHAT) + all.getCustomName());
				all.setPlayerListName(returnPrefix(user.getPrimaryGroup(), RankType.TAB) + all.getCustomName() + " §7(§a" + id + "§7) §f" + clan);
			}else if(user.getPrimaryGroup().equalsIgnoreCase("staffsupervisor")) {
				staffsupervisor.addEntry(all.getName());
				all.setDisplayName(returnPrefix(user.getPrimaryGroup(), RankType.CHAT) + all.getCustomName());
				all.setPlayerListName(returnPrefix(user.getPrimaryGroup(), RankType.TAB) + all.getCustomName() + " §7(§a" + id + "§7) §f" + clan);
			}else if(user.getPrimaryGroup().equalsIgnoreCase("developer")) {
				developer.addEntry(all.getName());
				all.setDisplayName(returnPrefix(user.getPrimaryGroup(), RankType.CHAT) + all.getCustomName());
				all.setPlayerListName(returnPrefix(user.getPrimaryGroup(), RankType.TAB) + all.getCustomName() + " §7(§a" + id + "§7) §f" + clan);
			}else if(user.getPrimaryGroup().equalsIgnoreCase("headofcommunity")) {
				headofcommunity.addEntry(all.getName());
				all.setDisplayName(returnPrefix(user.getPrimaryGroup(), RankType.CHAT) + all.getCustomName());
				all.setPlayerListName(returnPrefix(user.getPrimaryGroup(), RankType.TAB) + all.getCustomName() + " §7(§a" + id + "§7) §f" + clan);
			}else if(user.getPrimaryGroup().equalsIgnoreCase("humanresources")) {
				humanresources.addEntry(all.getName());
				all.setDisplayName(returnPrefix(user.getPrimaryGroup(), RankType.CHAT) + all.getCustomName());
				all.setPlayerListName(returnPrefix(user.getPrimaryGroup(), RankType.TAB) + all.getCustomName() + " §7(§a" + id + "§7) §f" + clan);
			}else if(user.getPrimaryGroup().equalsIgnoreCase("qualityassman")) {
				qualityassman.addEntry(all.getName());
				all.setDisplayName(returnPrefix(user.getPrimaryGroup(), RankType.CHAT) + all.getCustomName());
				all.setPlayerListName(returnPrefix(user.getPrimaryGroup(), RankType.TAB) + all.getCustomName() + " §7(§a" + id + "§7) §f" + clan);
			}else if(user.getPrimaryGroup().equalsIgnoreCase("admin")) {
				admin.addEntry(all.getName());
				all.setDisplayName(returnPrefix(user.getPrimaryGroup(), RankType.CHAT) + all.getCustomName());
				all.setPlayerListName(returnPrefix(user.getPrimaryGroup(), RankType.TAB) + all.getCustomName() + " §7(§a" + id + "§7) §f" + clan);
			}else if(user.getPrimaryGroup().equalsIgnoreCase("builder")) {
				builder.addEntry(all.getName());
				all.setDisplayName(returnPrefix(user.getPrimaryGroup(), RankType.CHAT) + all.getCustomName());
				all.setPlayerListName(returnPrefix(user.getPrimaryGroup(), RankType.TAB) + all.getCustomName() + " §7(§a" + id + "§7) §f" + clan);
			}else if(user.getPrimaryGroup().equalsIgnoreCase("designer")) {
				designer.addEntry(all.getName());
				all.setDisplayName(returnPrefix(user.getPrimaryGroup(), RankType.CHAT) + all.getCustomName());
				all.setPlayerListName(returnPrefix(user.getPrimaryGroup(), RankType.TAB) + all.getCustomName() + " §7(§a" + id + "§7) §f" + clan);
			}else if(user.getPrimaryGroup().equalsIgnoreCase("moderator")) {
				moderator.addEntry(all.getName());
				all.setDisplayName(returnPrefix(user.getPrimaryGroup(), RankType.CHAT) + all.getCustomName());
				all.setPlayerListName(returnPrefix(user.getPrimaryGroup(), RankType.TAB) + all.getCustomName() + " §7(§a" + id + "§7) §f" + clan);
			}else if(user.getPrimaryGroup().equalsIgnoreCase("support")) {
				support.addEntry(all.getName());
				all.setDisplayName(returnPrefix(user.getPrimaryGroup(), RankType.CHAT) + all.getCustomName());
				all.setPlayerListName(returnPrefix(user.getPrimaryGroup(), RankType.TAB) + all.getCustomName() + " §7(§a" + id + "§7) §f" + clan);
			}else if(user.getPrimaryGroup().equalsIgnoreCase("translator")) {
				translator.addEntry(all.getName());
				all.setDisplayName(returnPrefix(user.getPrimaryGroup(), RankType.CHAT) + all.getCustomName());
				all.setPlayerListName(returnPrefix(user.getPrimaryGroup(), RankType.TAB) + all.getCustomName() + " §7(§a" + id + "§7) §f" + clan);
			}else if(user.getPrimaryGroup().equalsIgnoreCase("addon")) {
				addon.addEntry(all.getName());
				all.setDisplayName(returnPrefix(user.getPrimaryGroup(), RankType.CHAT) + all.getCustomName());
				all.setPlayerListName(returnPrefix(user.getPrimaryGroup(), RankType.TAB) + all.getCustomName() + " §7(§a" + id + "§7) §f" + clan);
			}else if(user.getPrimaryGroup().equalsIgnoreCase("retired")) {
				retired.addEntry(all.getName());
				all.setDisplayName(returnPrefix(user.getPrimaryGroup(), RankType.CHAT) + all.getCustomName());
				all.setPlayerListName(returnPrefix(user.getPrimaryGroup(), RankType.TAB) + all.getCustomName() + " §7(§a" + id + "§7) §f" + clan);
			}else if(user.getPrimaryGroup().equalsIgnoreCase("beta")) {
				beta.addEntry(all.getName());
				all.setDisplayName(returnPrefix(user.getPrimaryGroup(), RankType.CHAT) + all.getCustomName());
				all.setPlayerListName(returnPrefix(user.getPrimaryGroup(), RankType.TAB) + all.getCustomName() + " §7(§a" + id + "§7) §f" + clan);
			}else {
				userg.addEntry(all.getName());
				all.setDisplayName(returnPrefix("default", RankType.CHAT) + all.getCustomName());
				all.setPlayerListName(returnPrefix("default", RankType.TAB) + all.getCustomName() + " §7(§a" + id + "§7) §f" + clan);
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		setSB(event.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent event) {
		LotusController lc = new LotusController();
		String message = ChatColor.translateAlternateColorCodes('&', event.getMessage().replace("%", "%%"));
		event.setFormat(event.getPlayer().getDisplayName() + " §7(" + lc.getPlayerData(event.getPlayer(), Playerdata.LotusChangeID) + "): " + message);
	}
	
	private static String getBuildTime(Player player) {
		if(buildTime.containsKey(player)) {
			long seconds = (System.currentTimeMillis() / 1000) - (buildTime.get(player));
			long hours = (seconds % (24* 3600)) / 3600; 
			long minutes = (seconds % 3600) / 60;
			long remainingSeconds = seconds % 60;
			return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
		}else {
			return "§bError!";
		}
	}
	
	public Team getTeam(Scoreboard scoreboard, String role, ChatColor chatcolor) {
		Team team = scoreboard.registerNewTeam(returnPrefix(role, RankType.TEAM));
		team.setPrefix(returnPrefix(role, RankType.TAB));
		team.setColor(chatcolor);
		team.setOption(Option.COLLISION_RULE, OptionStatus.NEVER); //TBD for removal if issues arise.
		return team;
	}
	
	private String retGroup(Player player) {
		String group = "";
		UserManager um = Main.luckPerms.getUserManager();
		User user = um.getUser(player.getName());
		group = "§a" + returnPrefix(user.getPrimaryGroup(), RankType.SIDEBOARD);
		return group;
	}
	
	public static void initRoles() {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM core_ranks");
			ResultSet rs = ps.executeQuery();
			tabHM.clear();
			chatHM.clear();
			roleHM.clear();
			sbHM.clear();
			int count = 0;
			while(rs.next()) {
				count++;
				tabHM.put(rs.getString("ingame_id"), rs.getString("colour") + rs.getString("short"));
				chatHM.put(rs.getString("ingame_id"), rs.getString("colour") + rs.getString("name"));
				roleHM.put(rs.getString("ingame_id"), rs.getString("priority"));
				sbHM.put(rs.getString("ingame_id"), rs.getString("name"));
			}
			Main.logger.info("Downloaded " + count + " roles for the Prefix System. | Source: ScoreboardHandler#initRoles();");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String returnPrefix(String role, RankType type) {
		String toReturn = "";
		if(type == RankType.TAB) {
			if(tabHM.containsKey(role)) {
				toReturn = tabHM.get(role) + " §7» ";
			}else {
				toReturn = "&cDEF";
			}
		}else if(type == RankType.CHAT) {
			if(chatHM.containsKey(role)) {
				toReturn = chatHM.get(role) + " §7» ";
			}else {
				toReturn = "&cDEF";
			}
		}else if(type == RankType.SIDEBOARD) {
			if(sbHM.containsKey(role)) {
				toReturn = sbHM.get(role);
			}else {
				toReturn = "DEF";
			}
		}else if(type == RankType.TEAM) {
			if(roleHM.containsKey(role)) {
				toReturn = roleHM.get(role);
			}else {
				Random r = new Random();
				toReturn = "0" + r.nextInt(0, 250) + "0";
			}
		}
		toReturn = net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', toReturn); //transforms & -> §
		toReturn = LotusController.translateHEX(toReturn); //translates HEX Color Codes into Minecraft (Custom Color Codes ability)
		return toReturn;
	}
	
	public enum RankType {
		TAB,
		SIDEBOARD,
		CHAT,
		TEAM
	}
	
	public void startScheduler(int delay, int sideboardRefresh, int tabRefresh) {
		//SYNC TASK - ONLY FOR THE SIDEBOARD
		new BukkitRunnable() {
			@Override
			public void run() {
				for(Player all : Bukkit.getOnlinePlayers()) {
					setSB(all);
				}
			}
		}.runTaskTimer(Main.main, delay, sideboardRefresh);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				LotusController lc = new LotusController();
				for(Player all : Bukkit.getOnlinePlayers()) {
					all.setPlayerListHeaderFooter("§cLotus §aGaming §fCommunity", "§7Server: §a" + lc.getServerName() + "\n§7Time: §a00:00\n§7Ping: §a" + all.getPing());
				}
			}
		}.runTaskTimerAsynchronously(Main.main, delay, tabRefresh);
	}

}