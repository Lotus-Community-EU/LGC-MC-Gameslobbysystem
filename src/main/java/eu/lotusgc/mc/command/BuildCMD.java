package eu.lotusgc.mc.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import eu.lotusgc.mc.event.ScoreboardHandler;
import eu.lotusgc.mc.main.Main;
import eu.lotusgc.mc.misc.HotbarItem;
import eu.lotusgc.mc.misc.LotusController;

public class BuildCMD implements CommandExecutor, Listener{
	
	private static List<Player> allowedPlayers = new ArrayList<>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player player) {
			LotusController lc = new LotusController();
			if(args.length == 1) {
				Player player2 = Bukkit.getPlayerExact(args[0]);
				if(player2.isOnline()) {
					if(player.hasPermission("lgc.build.others")) {
						if(allowedPlayers.contains(player2)) {
							lc.sendMessageReady(player, "cmd.build.others.self.remove");
							lc.sendMessageReady(player2, "cmd.build.others.recipient.remove");
							allowedPlayers.remove(player2);
							if(ScoreboardHandler.buildTime.containsKey(player2)) {
								ScoreboardHandler.buildTime.remove(player2);
							}
							player2.setGameMode(GameMode.SURVIVAL);
							new HotbarItem().setHotbarItems(player2);
						}else {
							lc.sendMessageReady(player, "cmd.build.others.self.add");
							lc.sendMessageReady(player2, "cmd.build.others.recipient.add");
							allowedPlayers.add(player2);
							ScoreboardHandler.buildTime.put(player2, System.currentTimeMillis() / 1000);
							player2.setGameMode(GameMode.CREATIVE);
							player2.getInventory().clear();
						}
					}else {
						lc.noPerm(player, "lgc.build.others");
					}
				}else {
					lc.sendMessageReady(player, "global.playerOffline");
				}
			}else {
				if(player.hasPermission("lgc.build.self")) {
					if(allowedPlayers.contains(player)) {
						allowedPlayers.remove(player);
						lc.sendMessageReady(player, "cmd.build.self.remove");
						if(ScoreboardHandler.buildTime.containsKey(player)) {
							ScoreboardHandler.buildTime.remove(player);
						}
						player.setGameMode(GameMode.SURVIVAL);
						new HotbarItem().setHotbarItems(player);
					}else {
						allowedPlayers.add(player);
						lc.sendMessageReady(player, "cmd.build.self.add");
						ScoreboardHandler.buildTime.put(player, System.currentTimeMillis() / 1000);
						player.setGameMode(GameMode.CREATIVE);
						player.getInventory().clear();
					}
				}else {
					lc.noPerm(player, "lgc.build.self");
				}
			}
		}else {
			sender.sendMessage(Main.consoleSend);
		}
		return false;
	}
	
	public static boolean hasPlayer(Player player) {
		return allowedPlayers.contains(player);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if(allowedPlayers.contains(player)) {
			event.setCancelled(false);
		}else {
			event.setCancelled(true);
			new LotusController().sendMessageReady(player, "event.build.blockpb.cantDoThat");
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if(allowedPlayers.contains(player)) {
			event.setCancelled(false);
		}else {
			event.setCancelled(true);
			new LotusController().sendMessageReady(player, "event.build.blockpb.cantDoThat");
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(event.getAction() == Action.PHYSICAL) {
			if(player.getLocation().getBlock().getType() != Material.BIG_DRIPLEAF) {
				event.setCancelled(true);
				new LotusController().sendMessageReady(player, "event.build.wheat.cantDoThat");
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerQuitEvent event) {
		if(allowedPlayers.contains(event.getPlayer())) {
			allowedPlayers.remove(event.getPlayer());
		}
	}
}