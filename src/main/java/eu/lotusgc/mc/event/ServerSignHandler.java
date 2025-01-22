//Created by Maurice H. at 20.01.2025
package eu.lotusgc.mc.event;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import eu.lotusgc.mc.main.Main;
import eu.lotusgc.mc.misc.LotusController;
import eu.lotusgc.mc.misc.MySQL;
import eu.lotusgc.mc.misc.Prefix;
import eu.lotusgc.mc.misc.util.GameServer;
import eu.lotusgc.mc.misc.util.GameServer.Server;

public class ServerSignHandler implements Listener, CommandExecutor{
	
	static int loadAnim = 0;
	static List<UUID> delSign = new ArrayList<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consoleSend);
		}else {
			Player player = (Player)sender;
			if(player.hasPermission("lgc.command.deleteServersign")) {
				delSign.add(player.getUniqueId());
				new LotusController().sendMessageReady(player, "command.deleteServerSign.now");
			}else {
				new LotusController().noPerm(player, "lgc.command.deleteServersign");
			}
		}
		return true;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		Player player = event.getPlayer();
		LotusController lc = new LotusController();
		if(event.getClickedBlock().getState() instanceof Sign) {
			if(action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
				if(delSign.contains(player.getUniqueId())) {
					//Do nothing, as breaking still counts as "interaction" in the first click.
				}else {
					Sign sign = (Sign) event.getClickedBlock().getState();
					if(isServerSign(event.getClickedBlock().getLocation())) {
						if(sign.getSide(Side.FRONT).getLine(1).equals("§cloading") || sign.getSide(Side.FRONT).getLine(1).equals("§4Server does")){
							event.setCancelled(true);
							sign.getSide(Side.FRONT).setLine(1, "§cServer is");
							sign.getSide(Side.FRONT).setLine(2, "§cOffline!");
							sign.update(true);
						}else {
							event.setCancelled(true);
							String fancyName = sign.getSide(Side.FRONT).getLine(0);
							GameServer gs = new GameServer(fancyName, Server.Fancyname);
							if(gs.isLocked()) {
								player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.navi.targetServer.locked").replace("%targetServer%", fancyName));
							}else {
								sendPlayerToServer(player, fancyName, gs.getBungeeKey(), lc);
							}
						}
					}else {
						event.setCancelled(false);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();
		if(block.getState() instanceof Sign) {
			Location location = block.getLocation();
			if(isServerSign(location)) {
				if(delSign.contains(player.getUniqueId())) {
					event.setCancelled(false);
					delSign.remove(player.getUniqueId());
					removeServerSign(location);
					new LotusController().sendMessageReady(player, "event.serversign.remove.success");
				}else {
					event.setCancelled(true);
					new LotusController().sendMessageReady(player, "event.serversign.remove.error");
				}
			}
		}
	}

	@EventHandler
	public void onServerSign(SignChangeEvent event) {
		Player player = event.getPlayer();
		String[] lines = event.getLines();
		LotusController lc = new LotusController();
		if(lines[0].equals("[serversign]") || lines[0].equals("[ss]")) {
			if(player.hasPermission("lgc.serversigns.create")) {
				String targetServer = lines[1];
				addServerSign(targetServer, event.getBlock().getLocation(), player);
				event.setLine(0, "");
				event.setLine(1, "Set ServerSign");
				event.setLine(2, "successfully!");
				event.setLine(3, "");
			}else {
				lc.noPerm(player, "lgc.serversigns.create");
			}
		}
	}
	
	public static void startSignScheduler(int delay, int interval) {
		LotusController lc = new LotusController();
		new BukkitRunnable() {
			@Override
			public void run() {
				loadAnim++;
				if(loadAnim >= 6) {
					loadAnim = 0;
				}
				if(Bukkit.getOnlinePlayers().size() != 0) {
					try {
						PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM mc_serversigns WHERE sourceServer = ?");
						ps.setString(1, lc.getServerName());
						ResultSet rs = ps.executeQuery();
						while(rs.next()) {
							String targetServer = rs.getString("targetServer");
                            Location location = translateLocationData(rs.getString("location"));
                            Block matSign = Bukkit.getWorld(location.getWorld().getName()).getBlockAt(location);
                            if(matSign != null) {
                            	if(matSign.getState() instanceof Sign) {
									Sign sign = (Sign) matSign.getState();
									GameServer gs = new GameServer(targetServer, GameServer.Server.Key);
									if(gs.existServer()) {
										if(gs.isOnline()) {
											if(gs.isMinigameServer()) {
												GameState gamestate = getGameState(targetServer);
												if(gamestate == GameState.LOOKING) {
													if(gs.isLocked()) {
														sign.getSide(Side.FRONT).setLine(0, gs.getDisplayname());
														sign.getSide(Side.FRONT).setLine(1, "§a" + gs.getCurrentPlayers() + " §7/ §c" + gs.getMaxSlots());
														sign.getSide(Side.FRONT).setLine(2, "§d" + gs.getMinigame_Mapname());
														sign.getSide(Side.FRONT).setLine(3, "§4Locked. \uD83D\uDD12");
													}else {
														if(gs.isMonitored()) {
															sign.getSide(Side.FRONT).setLine(0, gs.getDisplayname());
															sign.getSide(Side.FRONT).setLine(1, "§e" + gs.getCurrentPlayers() + " / " + gs.getMaxSlots());
															sign.getSide(Side.FRONT).setLine(2, "§e" + gs.getMinigame_Mapname());
															sign.getSide(Side.FRONT).setLine(3, "§ewtg. for players");
														}else {
															sign.getSide(Side.FRONT).setLine(0, gs.getDisplayname());
															sign.getSide(Side.FRONT).setLine(1, "§a" + gs.getCurrentPlayers() + " §7/ §c" + gs.getMaxSlots());
															sign.getSide(Side.FRONT).setLine(2, "§d" + gs.getMinigame_Mapname());
															sign.getSide(Side.FRONT).setLine(3, "§awtg. for players");
														}
													}
												}else if(gamestate == GameState.INGAME) {
													sign.getSide(Side.FRONT).setLine(0, gs.getDisplayname());
													sign.getSide(Side.FRONT).setLine(1, "§a" + gs.getCurrentPlayers() + " §7/ §c" + gs.getMaxSlots());
													sign.getSide(Side.FRONT).setLine(2, "§d" + gs.getMinigame_Mapname());
													sign.getSide(Side.FRONT).setLine(3, "§2Ingame!");
												}else if(gamestate == GameState.END) {
													sign.getSide(Side.FRONT).setLine(0, gs.getDisplayname());
													sign.getSide(Side.FRONT).setLine(1, "§a" + gs.getCurrentPlayers() + " §7/ §c" + gs.getMaxSlots());
													sign.getSide(Side.FRONT).setLine(2, "§d" + gs.getMinigame_Mapname());
													sign.getSide(Side.FRONT).setLine(3, "§cGame Ended");
												}
											}else {
												if(gs.isLocked()) {
													sign.getSide(Side.FRONT).setLine(0, gs.getDisplayname());
													sign.getSide(Side.FRONT).setLine(1, "§a" + gs.getCurrentPlayers() + " §7/ §c" + gs.getMaxSlots());
													sign.getSide(Side.FRONT).setLine(2, "");
													sign.getSide(Side.FRONT).setLine(3, "§4Locked. \uD83D\uDD12");
												}else {
													if(gs.isMonitored()) {
														sign.getSide(Side.FRONT).setLine(0, gs.getDisplayname());
														sign.getSide(Side.FRONT).setLine(1, "§e" + gs.getCurrentPlayers() + " / " + gs.getMaxSlots());
														sign.getSide(Side.FRONT).setLine(2, "");
														sign.getSide(Side.FRONT).setLine(3, "§eOnline");
													}else {
														sign.getSide(Side.FRONT).setLine(0, gs.getDisplayname());
														sign.getSide(Side.FRONT).setLine(1, "§a" + gs.getCurrentPlayers() + " §7/ §c" + gs.getMaxSlots());
														sign.getSide(Side.FRONT).setLine(2, "");
														sign.getSide(Side.FRONT).setLine(3, "§aOnline");
													}
												}
											}
										}else {
											sign.getSide(Side.FRONT).setLine(0, "");
											sign.getSide(Side.FRONT).setLine(1, "§cloading");
											sign.getSide(Side.FRONT).setLine(2, loadAnim(loadAnim));
											sign.getSide(Side.FRONT).setLine(3, "");
										}
									}else {
										sign.getSide(Side.FRONT).setLine(0, "");
										sign.getSide(Side.FRONT).setLine(1, "§4Server does");
										sign.getSide(Side.FRONT).setLine(2, "§4not exist!");
										sign.getSide(Side.FRONT).setLine(3, "§");
									}
									sign.setWaxed(true);
									sign.update(true);
                            	}
                            }else {
                            	Main.logger.severe("NULLBLOCK at " + rs.getString("location"));
                            }
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}.runTaskTimer(Main.main, delay, interval);
	}
	
	private static String loadAnim(int time) {
		String anim = "";
		switch(time) {
		case 0: anim = "O o o o"; break;
		case 1: anim = "o O o o"; break;
		case 2: anim = "o o O o"; break;
		case 3: anim = "o o o O"; break;
		case 4: anim = "o o O o"; break;
		case 5: anim = "o O o o"; break;
		}
		return anim;
	}
	
	void addServerSign(String targetServer, Location location, Player player) {
		LotusController lc = new LotusController();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO mc_serversigns(sourceServer, targetServer, location) VALUES(?, ?, ?)");
			ps.setString(1, lc.getServerName());
			ps.setString(2, targetServer);
			ps.setString(3, translateLocationData(location));
			ps.executeUpdate();
			ps.close();
			player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.serversigns.create").replace("%targetServer%", targetServer));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	boolean isServerSign(Location location) {
		boolean isServerSign = false;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM mc_serversigns WHERE location = ? AND sourceServer = ?");
			ps.setString(1, translateLocationData(location));
			ps.setString(2, new LotusController().getServerName());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				isServerSign = true;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isServerSign;
	}
	
	void removeServerSign(Location Location) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM mc_serversigns WHERE location = ? AND sourceServer = ?");
			ps.setString(1, translateLocationData(Location));
			ps.setString(2, new LotusController().getServerName());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	String translateLocationData(Location location) {
		String world = location.getWorld().getName();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		return world + ";" + x + ";" + y + ";" + z;
	}
	
	static Location translateLocationData(String locationData) {
		String[] data = locationData.split(";");
		String world = data[0];
		int x = Integer.parseInt(data[1]);
		int y = Integer.parseInt(data[2]);
		int z = Integer.parseInt(data[3]);
		return new Location(Bukkit.getWorld(world), x, y, z);
	}
	
	static GameState getGameState(String server) {
		GameState gs = null;
		String state = new GameServer(server, GameServer.Server.Key).getMinigame_State();
		switch(state) {
		case "LOOKING": gs = GameState.LOOKING; break;
		case "INGAME": gs = GameState.INGAME; break;
		case "END": gs = GameState.END; break;
		}
		return gs;
	}
	
	enum GameState {
		LOOKING,
		INGAME,
		END;
	}
	
	private void sendPlayerToServer(Player player, String fancyName, String destinationServer, LotusController lc) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeUTF("Connect");
			dos.writeUTF(destinationServer);
			player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.navi.targetServer.success").replace("%target%", fancyName));
			Main.logger.info(player.getName() + " has been sent to " + destinationServer + " successfully.");
		} catch (IOException e) {
			Main.logger.severe(player.getName() + " attempted to be sent to " + destinationServer + " but failed!");
			e.printStackTrace();
		}
		player.sendPluginMessage(Main.main, "BungeeCord", baos.toByteArray());
	}
}