package eu.lotusgc.mc.event;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.lotusgc.mc.command.BuildCMD;
import eu.lotusgc.mc.command.SpawnSystem;
import eu.lotusgc.mc.main.Main;
import eu.lotusgc.mc.misc.HotbarItem;
import eu.lotusgc.mc.misc.InputType;
import eu.lotusgc.mc.misc.LotusController;
import eu.lotusgc.mc.misc.MySQL;
import eu.lotusgc.mc.misc.Playerdata;
import eu.lotusgc.mc.misc.Prefix;
import eu.lotusgc.mc.misc.Serverdata;

public class InventorySetterHandling implements Listener{
	
	public static String navi_title = "§aNavigator";
	public static String navi_spawn = "§6Spawn";
	public static String navi_rewards = "§bRewards";
	public static String navi_back = "§cback";
	
	public static String language_title = "§6Languages";
	
	public static String extras_title = "§9Extras";
	public static String extras_pets = "§6Pets";
	public static String extras_boots = "§dEffects";
	public static String extras_jboost = "§9Jumpboost";
	public static String extras_sboost = "§bSpeedboost";
	
	public static String rewards_title = "§bRewards";
	public static String rewards_crates = "§eCrates §4(CLOSED)";
	public static String rewards_dailyRewards = "§aDaily Rewards";
	
	public static String sboost_title = "§bSpeedboost";
	public static String sboost_stage1 = "§2Default";
	public static String sboost_stage2 = "§7Stage §a1";
	public static String sboost_stage3 = "§7Stage §e2";
	public static String sboost_stage4 = "§7Stage §63";
	public static String sboost_stage5 = "§7Stage §c4";
	
	public static String jboost_title = "§9Jumpboost";
	public static String jboost_stage1 = "§2Default";
	public static String jboost_stage2 = "§7Stage §a1";
	public static String jboost_stage3 = "§7Stage §e2";
	public static String jboost_stage4 = "§7Stage §63";
	public static String jboost_stage5 = "§7Stage §c4";
	
	public static String effect_hearts = "§7» §cHearts";
	public static String effect_clouds = "§7» §fClouds";
	public static String effect_music = "§7» §6Musicnotes";
	public static String effect_slime = "§7» §aSlime";
	public static String effect_water = "§7» §1Waterdrops";
	public static String effect_ender = "§7» §9Enderffects";
	public static String effect_emerald = "§7» §aEmerald";
	public static String effect_lava = "§7» §cLavadrops";
	public static String effect_honey = "§7» §6Honeydrops";
	public static String effect_color = "§7» §cC§2o§6l§co§ar§9s";
	public static String effect_snow = "§7» §fSnowball";
	public static String effect_soul = "§7» §bSoul Fire";
	public static String effect_ash = "§7» Ash";
	public static String effect_souls = "§7» §bSouls";
	public static String effect_glow = "§7» §eGlow";
	public static String effect_endrod = "§7» §fEnd-Rod Particles";
	public static String effect_cryobsidian = "§7» §5Obsidian Tears";
	public static String effect_cherry = "§7» §dCherry Leaves";
	
	public static String close = "§cclose";
	public static String back = "§cback";
	
	public static void setNavigatorInventory(Player player) {
		boolean tempBool = true;
		if(tempBool) {
			//concurrent version
			Inventory mainInventory = Bukkit.createInventory(null, 3*9, navi_title);
			LotusController lc = new LotusController();
			for(int i = 0; i < 27; i++) {
				mainInventory.setItem(i, lc.defItem(Material.LIME_STAINED_GLASS_PANE, "§0", 1));
			}
			mainInventory.setItem(2, lc.naviServerItem(Material.RED_BED, "Gameslobby"));
			mainInventory.setItem(6, lc.naviServerItem(Material.NETHERITE_AXE, "Survival"));
			mainInventory.setItem(10, lc.naviServerItem(Material.GRASS_BLOCK, "SkyBlock"));
			mainInventory.setItem(13, lc.defItem(Material.EMERALD, navi_spawn, 1));
			mainInventory.setItem(16, lc.naviServerItem(Material.GOLDEN_HOE, "Farmserver"));
			mainInventory.setItem(20, lc.naviServerItem(Material.WOODEN_AXE, "Staffserver"));
			mainInventory.setItem(24, lc.naviServerItem(Material.DIAMOND_PICKAXE, "Creative"));
			player.openInventory(mainInventory);
		}else {
			//version 1.12.2 (HX Servers)
			Inventory mainInventory = Bukkit.createInventory(null, 9*1, navi_title);
			LotusController lc = new LotusController();
			for(int i = 0; i < 8; i++) {
				mainInventory.setItem(i, lc.defItem(Material.ORANGE_STAINED_GLASS_PANE, "§0", 1));
			}
			mainInventory.setItem(1, lc.naviServerItem(Material.DIAMOND_PICKAXE, "Creative HX"));
			mainInventory.setItem(3, lc.defItem(Material.EMERALD, navi_spawn, 1));
			mainInventory.setItem(5, lc.defItem(Material.ECHO_SHARD, navi_rewards, 1));
			mainInventory.setItem(7, lc.naviServerItem(Material.GOLDEN_SWORD, "Survival HX"));
			player.openInventory(mainInventory);
		}
	}
	
	public static void setRewardsInventory(Player player) {
		Inventory mainInventory = Bukkit.createInventory(null, 9*1, rewards_title);
		LotusController lc = new LotusController();
		for(int i = 0; i < 9; i++) {
			mainInventory.setItem(i, lc.defItem(Material.MAGENTA_STAINED_GLASS_PANE, "§0", 1));
		}
		mainInventory.setItem(3, lc.defItem(Material.AMETHYST_SHARD, rewards_crates, 1));
		mainInventory.setItem(5, lc.defItem(Material.DIAMOND, rewards_dailyRewards, 1));
		player.openInventory(mainInventory);
	}
	
	public static void setLanguageInventory(Player player) {
		Inventory mainInventory = Bukkit.createInventory(null, 9*3, language_title);
		LotusController lc = new LotusController();
		int slot = 0;
		String playerLang = lc.getPlayerData(player, Playerdata.Language);
		for(String string : lc.getAvailableLanguages()) {
			String fancyName = langs.getOrDefault(string, "Error!");
			if(string.equalsIgnoreCase(playerLang)) {
				mainInventory.setItem(slot, lc.loreItem(Material.NETHER_STAR, 1, "§6" + fancyName, "§7Language is:", "§7» §a" + string));
			}else {
				mainInventory.setItem(slot, lc.defItemRandom(matList(), "§a" + fancyName, 1, "§7Language is:", "§7» §a" + string));
			}
			slot++;
		}
		player.openInventory(mainInventory);
	}
	
	@SuppressWarnings("deprecation")
	public static void setSpeedboostInventory(Player player) {
		Inventory sbInv = Bukkit.createInventory(null, 9*3, sboost_title);
		LotusController lc = new LotusController();
		sbInv.setItem(0, lc.defItem(Material.GREEN_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(1, lc.defItem(Material.LIME_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(2, lc.defItem(Material.LIME_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(3, lc.defItem(Material.YELLOW_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(4, lc.defItem(Material.YELLOW_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(5, lc.defItem(Material.ORANGE_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(6, lc.defItem(Material.ORANGE_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(7, lc.defItem(Material.RED_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(8, lc.defItem(Material.RED_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(9, lc.potionItem(1, sboost_stage1, new PotionEffect(PotionEffectType.SPEED, 1200, 1))); //default
		sbInv.setItem(10, lc.defItem(Material.LIME_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(11, lc.potionItem(1, sboost_stage2, new PotionEffect(PotionEffectType.SPEED, 1200, 2))); //stage 1
		sbInv.setItem(12, lc.defItem(Material.YELLOW_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(13, lc.potionItem(1, sboost_stage3, new PotionEffect(PotionEffectType.SPEED, 1200, 3))); //stage 2
		sbInv.setItem(14, lc.defItem(Material.ORANGE_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(15, lc.potionItem(1, sboost_stage4, new PotionEffect(PotionEffectType.SPEED, 1200, 4))); //stage 3
		sbInv.setItem(16, lc.defItem(Material.RED_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(17, lc.potionItem(1, sboost_stage5, new PotionEffect(PotionEffectType.SPEED, 1200, 5))); //stage 4
		sbInv.setItem(18, lc.defItem(Material.GREEN_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(19, lc.defItem(Material.LIME_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(20, lc.defItem(Material.LIME_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(21, lc.defItem(Material.BARRIER, close, 1)); //close
		sbInv.setItem(22, lc.defItem(Material.YELLOW_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(23, lc.skullItem(1, back, "MHF_ArrowLeft")); //back
		sbInv.setItem(24, lc.defItem(Material.ORANGE_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(25, lc.defItem(Material.RED_STAINED_GLASS_PANE, "§c", 1));
		sbInv.setItem(26, lc.defItem(Material.RED_STAINED_GLASS_PANE, "§c", 1));
		player.openInventory(sbInv);
	}
	
	@SuppressWarnings("deprecation")
	public static void setJumpboostInventory(Player player) {
		Inventory jbInv = Bukkit.createInventory(null, 9*3, jboost_title);
		LotusController lc = new LotusController();
		jbInv.setItem(0, lc.defItem(Material.GREEN_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(1, lc.defItem(Material.LIME_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(2, lc.defItem(Material.LIME_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(3, lc.defItem(Material.YELLOW_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(4, lc.defItem(Material.YELLOW_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(5, lc.defItem(Material.ORANGE_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(6, lc.defItem(Material.ORANGE_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(7, lc.defItem(Material.RED_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(8, lc.defItem(Material.RED_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(9, lc.potionItem(1, jboost_stage1, new PotionEffect(PotionEffectType.JUMP, 1200, 1))); //default
		jbInv.setItem(10, lc.defItem(Material.LIME_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(11, lc.potionItem(1, jboost_stage2, new PotionEffect(PotionEffectType.JUMP, 1200, 2))); //stage 1
		jbInv.setItem(12, lc.defItem(Material.YELLOW_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(13, lc.potionItem(1, jboost_stage3, new PotionEffect(PotionEffectType.JUMP, 1200, 3))); //stage 2
		jbInv.setItem(14, lc.defItem(Material.ORANGE_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(15, lc.potionItem(1, jboost_stage4, new PotionEffect(PotionEffectType.JUMP, 1200, 4))); //stage 3
		jbInv.setItem(16, lc.defItem(Material.RED_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(17, lc.potionItem(1, jboost_stage5, new PotionEffect(PotionEffectType.JUMP, 1200, 5))); //stage 4
		jbInv.setItem(18, lc.defItem(Material.GREEN_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(19, lc.defItem(Material.LIME_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(20, lc.defItem(Material.LIME_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(21, lc.defItem(Material.BARRIER, close, 1)); //close
		jbInv.setItem(22, lc.defItem(Material.YELLOW_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(23, lc.skullItem(1, back, "MHF_ArrowLeft")); //back
		jbInv.setItem(24, lc.defItem(Material.ORANGE_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(25, lc.defItem(Material.RED_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(26, lc.defItem(Material.RED_STAINED_GLASS_PANE, "§c", 1));
		player.openInventory(jbInv);
	}
	
	@SuppressWarnings("deprecation")
	public static void setEffectsInventory(Player player) {
		Inventory effectsInv = Bukkit.createInventory(null, 9*6, extras_boots);
		LotusController lc = new LotusController();
		for(int i = 0; i < 54; i++) {
			effectsInv.setItem(i, lc.defItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "§a", 1));
		}
		HashMap<String, Boolean> map = EffectMoveEvent.playerEffects.get(player.getUniqueId());
		if(map.get("hearts")) {
			effectsInv.setItem(10, lc.enchantedItem(Material.RED_DYE, effect_hearts, 1, Enchantment.DURABILITY, 1));
		}else {
			effectsInv.setItem(10, lc.defItem(Material.RED_DYE, effect_hearts, 1));
		}
		if(map.get("clouds")) {
			effectsInv.setItem(11, lc.enchantedItem(Material.BONE_MEAL, effect_clouds, 1, Enchantment.DURABILITY, 1));
		}else {
			effectsInv.setItem(11, lc.defItem(Material.BONE_MEAL, effect_clouds, 1));
		}
		if(map.get("music")) {
			effectsInv.setItem(12, lc.enchantedItem(Material.NOTE_BLOCK, effect_music, 1, Enchantment.DURABILITY, 1));
		}else {
			effectsInv.setItem(12, lc.defItem(Material.NOTE_BLOCK, effect_music, 1));
		}
		if(map.get("slime")) {
			effectsInv.setItem(13, lc.enchantedItem(Material.SLIME_BALL, effect_slime, 1, Enchantment.DURABILITY, 1));
		}else {
			effectsInv.setItem(13, lc.defItem(Material.SLIME_BALL, effect_slime, 1));
		}
		if(map.get("water")) {
			effectsInv.setItem(14, lc.enchantedItem(Material.WATER_BUCKET, effect_water, 1, Enchantment.DURABILITY, 1));
		}else {
			effectsInv.setItem(14, lc.defItem(Material.WATER_BUCKET, effect_water, 1));
		}
		if(map.get("ender")) {
			effectsInv.setItem(15, lc.enchantedItem(Material.ENDER_EYE, effect_ender, 1, Enchantment.DURABILITY, 1));
		}else {
			effectsInv.setItem(15, lc.defItem(Material.ENDER_EYE, effect_ender, 1));
		}
		if(map.get("emerald")) {
			effectsInv.setItem(16, lc.enchantedItem(Material.EMERALD, effect_emerald, 1, Enchantment.DURABILITY, 1));
		}else {
			effectsInv.setItem(16, lc.defItem(Material.EMERALD, effect_emerald, 1));
		}
		if(map.get("lava")) {
			effectsInv.setItem(19, lc.enchantedItem(Material.LAVA_BUCKET, effect_lava, 1, Enchantment.DURABILITY, 1));
		}else {
			effectsInv.setItem(19, lc.defItem(Material.LAVA_BUCKET, effect_lava, 1));
		}
		if(map.get("honey")) {
			effectsInv.setItem(20, lc.enchantedItem(Material.HONEY_BOTTLE, effect_honey, 1, Enchantment.DURABILITY, 1));
		}else {
			effectsInv.setItem(20, lc.defItem(Material.HONEY_BOTTLE, effect_honey, 1));
		}
		if(map.get("redstone")) {
			effectsInv.setItem(21, lc.enchantedItem(Material.REDSTONE, effect_color, 1, Enchantment.DURABILITY, 1));
		}else {
			effectsInv.setItem(21, lc.defItem(Material.REDSTONE, effect_color, 1));
		}
		if(map.get("snow")) {
			effectsInv.setItem(22, lc.enchantedItem(Material.SNOWBALL, effect_snow, 1, Enchantment.DURABILITY, 1));
		}else {
			effectsInv.setItem(22, lc.defItem(Material.SNOWBALL, effect_snow, 1));
		}
		if(map.get("soulfire")) {
			effectsInv.setItem(23, lc.enchantedItem(Material.SOUL_TORCH, effect_soul, 1, Enchantment.DURABILITY, 1));
		}else {
			effectsInv.setItem(23, lc.defItem(Material.SOUL_TORCH, effect_soul, 1));
		}
		if(map.get("ash")) {
			effectsInv.setItem(24, lc.enchantedItem(Material.BASALT, effect_ash, 1, Enchantment.DURABILITY, 1));
		}else {
			effectsInv.setItem(24, lc.defItem(Material.BASALT, effect_ash, 1));
		}
		if(map.get("souls")) {
			effectsInv.setItem(25, lc.enchantedItem(Material.SOUL_SAND, effect_souls, 1, Enchantment.DURABILITY, 1));
		}else {
			effectsInv.setItem(25, lc.defItem(Material.SOUL_SAND, effect_souls, 1));
		}
		if(map.get("glow")) {
			effectsInv.setItem(28, lc.enchantedItem(Material.GLOWSTONE, effect_glow, 1, Enchantment.DURABILITY, 1));
		}else {
			effectsInv.setItem(28, lc.defItem(Material.GLOWSTONE, effect_glow, 1));
		}
		if(map.get("endrod")) {
			effectsInv.setItem(32, lc.enchantedItem(Material.END_ROD, effect_endrod, 1, Enchantment.DURABILITY, 1));
		}else {
			effectsInv.setItem(32, lc.defItem(Material.END_ROD, effect_endrod, 1));
		}
		if(map.get("cryobsidian")) {
			effectsInv.setItem(34, lc.enchantedItem(Material.CRYING_OBSIDIAN, effect_cryobsidian, 1, Enchantment.DURABILITY, 1));
		}else {
			effectsInv.setItem(34, lc.defItem(Material.CRYING_OBSIDIAN, effect_cryobsidian, 1));
		}
		if(map.get("cherry")) {
			effectsInv.setItem(30, lc.enchantedItem(Material.CHERRY_LEAVES, effect_cherry, 1, Enchantment.DURABILITY, 1));
		}else {
			effectsInv.setItem(30, lc.defItem(Material.CHERRY_LEAVES, effect_cherry, 1));
		}
		effectsInv.setItem(39, lc.defItem(Material.BARRIER, close, 1));
		effectsInv.setItem(41, lc.skullItem(1, back, "MHF_ArrowLeft"));
		player.openInventory(effectsInv);
	}
	
	public static void setExtrasInventory(Player player) {
		Inventory mainInventory = Bukkit.createInventory(null, 9*3, extras_title);
		LotusController lc = new LotusController();
		for(int i = 0; i < 27; i++) {
			mainInventory.setItem(i, lc.defItem(Material.BLUE_STAINED_GLASS_PANE, "§0", 1));
		}
		mainInventory.setItem(10, lc.loreItem(Material.PIG_SPAWN_EGG, 1, extras_pets, "§cThis Feature is", "§cnot enabled yet."));
		mainInventory.setItem(12, lc.defItem(Material.LEATHER_BOOTS, extras_boots, 1));
		mainInventory.setItem(14, lc.defItem(Material.POTION, extras_sboost, 1));
		mainInventory.setItem(16, lc.defItem(Material.POTION, extras_jboost, 1));
		mainInventory.setItem(22, lc.defItem(Material.BARRIER, close, 1));
		player.openInventory(mainInventory);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(event.getCurrentItem() == null) return;
		if(event.getView().getTitle().equalsIgnoreCase(navi_title)) {
			LotusController lc = new LotusController();
			event.setCancelled(true);
			if(event.getCurrentItem() == null && event.getCurrentItem().getItemMeta() == null) return;
			String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
			HashMap<String, String> fancyNames = getServerFancynames();
			if(fancyNames.containsKey(itemName)) {
				String bungeeName = fancyNames.get(itemName);
				if(lc.translateBoolean(lc.getServerData(bungeeName, Serverdata.OnlineStatus, InputType.BungeeKey))){
					if(lc.translateBoolean(lc.getServerData(bungeeName, Serverdata.LockedStatus, InputType.BungeeKey))) {
						if(player.hasPermission("lgc.bypassServerlock")) {
							if(lc.translateBoolean(lc.getServerData(bungeeName, Serverdata.IsStaff, InputType.BungeeKey))) {
								if(player.hasPermission("lgc.joinStaffserver")) {
									sendPlayerToServer(player, itemName, bungeeName, lc);
								}else {
									Main.logger.info(player.getName() + " tried to join a staff server.");
								}
							}else {
								sendPlayerToServer(player, itemName, bungeeName, lc);
							}
						}else {
							Main.logger.info(player.getName() + " tried to join a locked server.");
						}
					}else {
						if(lc.translateBoolean(lc.getServerData(bungeeName, Serverdata.IsStaff, InputType.BungeeKey))) {
							if(player.hasPermission("lgc.joinStaffserver")) {
								sendPlayerToServer(player, itemName, bungeeName, lc);
							}else {
								lc.noPerm(player, "lgc.joinStaffserver");
								Main.logger.info(player.getName() + " tried to join a staff server.");
							}
						}else {
							sendPlayerToServer(player, itemName, bungeeName, lc);
						}
					}
				}else {
					//server dead - how poor lol
				}
			}else {
				if(itemName.equalsIgnoreCase(navi_spawn)) {
					Location spawn = SpawnSystem.getSpawn("mainSpawn");
					player.closeInventory();
					player.teleport(spawn);
				}
			}
		}else if(event.getView().getTitle().equalsIgnoreCase(extras_title)) {
			event.setCancelled(true);
			LotusController lc = new LotusController();
			if(event.getCurrentItem() == null && event.getCurrentItem().getItemMeta() == null) return;
			String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
			if(itemName.equalsIgnoreCase(extras_boots)) {
				player.getOpenInventory().close();
				setEffectsInventory(player);
			}else if(itemName.equalsIgnoreCase(extras_jboost)) {
				player.getOpenInventory().close();
				setJumpboostInventory(player);
			}else if(itemName.equalsIgnoreCase(extras_sboost)) {
				player.getOpenInventory().close();
				setSpeedboostInventory(player);
			}else if(itemName.equalsIgnoreCase(extras_pets)) {
				lc.sendMessageReady(player, "event.pets.closed");
			}else if(itemName.equalsIgnoreCase(close)) {
				player.closeInventory();
			}
		} else {
			if(event.getView().getTitle().equalsIgnoreCase(rewards_title)) {
				event.setCancelled(true);
				//LotusController lc = new LotusController(); //for messages
				if(event.getCurrentItem() == null && event.getCurrentItem().getItemMeta() == null) return;
				String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
				if(itemName.equalsIgnoreCase(rewards_dailyRewards)) {
					Location loc = SpawnSystem.getSpawn("dailyRewards");
					player.teleport(loc);
				}else if(itemName.equalsIgnoreCase(rewards_crates)) {
					Location loc = SpawnSystem.getSpawn("crates");
					player.teleport(loc);
				}
			}else if(event.getView().getTitle().equalsIgnoreCase(jboost_title)) {
				event.setCancelled(true);
				LotusController lc = new LotusController();
				if(event.getCurrentItem() == null && event.getCurrentItem().getItemMeta() == null) return;
				String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
				if(itemName.equalsIgnoreCase(jboost_stage1)) {
					player.removePotionEffect(PotionEffectType.JUMP);
					player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.jumpboost.default"));
				}else if(itemName.equalsIgnoreCase(jboost_stage2)) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 99999, 2));
					player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.jumpboost.staged").replace("%stage%", "1"));
				}else if(itemName.equalsIgnoreCase(jboost_stage3)) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 99999, 4));
					player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.jumpboost.staged").replace("%stage%", "2"));
				}else if(itemName.equalsIgnoreCase(jboost_stage4)) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 99999, 6));
					player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.jumpboost.staged").replace("%stage%", "3"));
				}else if(itemName.equalsIgnoreCase(jboost_stage5)) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 99999, 8));
					player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.jumpboost.staged").replace("%stage%", "4"));
				}else if(itemName.equalsIgnoreCase(back)) {
					setExtrasInventory(player);
					lc.sendMessageReady(player, "event.extras.backToExtrasMenu");
				}else if(itemName.equalsIgnoreCase(close)) {
					lc.sendMessageReady(player, "event.extras.closedSubmenu");
					player.getOpenInventory().close();
				}
			}else if(event.getView().getTitle().equalsIgnoreCase(sboost_title)) {
				event.setCancelled(true);
				LotusController lc = new LotusController();
				if(event.getCurrentItem() == null && event.getCurrentItem().getItemMeta() == null) return;
				String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
				if(itemName.equalsIgnoreCase(sboost_stage1)) {
					player.setWalkSpeed(0.2f);
					player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.speedboost.default"));
				}else if(itemName.equalsIgnoreCase(sboost_stage2)) {
					player.setWalkSpeed(0.4f);
					player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.speedboost.staged").replace("%stage%", "1"));
				}else if(itemName.equalsIgnoreCase(sboost_stage3)) {
					player.setWalkSpeed(0.6f);
					player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.speedboost.staged").replace("%stage%", "2"));
				}else if(itemName.equalsIgnoreCase(sboost_stage4)) {
					player.setWalkSpeed(0.8f);
					player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.speedboost.staged").replace("%stage%", "3"));
				}else if(itemName.equalsIgnoreCase(sboost_stage5)) {
					player.setWalkSpeed(1.0f);
					player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.speedboost.staged").replace("%stage%", "4"));
				}else if(itemName.equalsIgnoreCase(back)) {
					setExtrasInventory(player);
					lc.sendMessageReady(player, "event.extras.backToExtrasMenu");
				}else if(itemName.equalsIgnoreCase(close)) {
					lc.sendMessageReady(player, "event.extras.closedSubmenu");
					player.getOpenInventory().close();
				}
			}else if(event.getView().getTitle().equalsIgnoreCase(language_title)) {
				event.setCancelled(true);
				LotusController lc = new LotusController();
				if(event.getCurrentItem() == null && event.getCurrentItem().getItemMeta() == null) return;
				String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
				itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getLore().get(1)).substring(2);
				if(findAndUpdatePlayerLanguage(player, itemName)) {
					//Updated language to %language% successfully!
					player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.languageInventory.success").replace("%language%", itemName));
				}else {
					//Error whilst updating to language %language%
					player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.languageInventory.error").replace("%language%", itemName));
				}
			}else if(event.getView().getTitle().equalsIgnoreCase(extras_boots)) {
				event.setCancelled(true);
				LotusController lc = new LotusController();
				String item = event.getCurrentItem().getItemMeta().getDisplayName();
				HashMap<String, Boolean> map = getEffectSettings(player);
				boolean closed = false;
				if(item.equalsIgnoreCase(effect_ash)) {
					if(map.get("ash")) {
						map.put("ash", false);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.removed").replace("%effect%", effect_ash));
					}else {
						map.put("ash", true);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.add").replace("%effect%", effect_ash));
					}
				}else if(item.equalsIgnoreCase(effect_cherry)) {
					if(map.get("cherry")) {
						map.put("cherry", false);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.removed").replace("%effect%", effect_cherry));
					}else {
						map.put("cherry", true);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.add").replace("%effect%", effect_cherry));
					}
				}else if(item.equalsIgnoreCase(effect_clouds)) {
					if(map.get("clouds")) {
						map.put("clouds", false);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.removed").replace("%effect%", effect_clouds));
					}else {
						map.put("clouds", true);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.add").replace("%effect%", effect_clouds));
					}
				}else if(item.equalsIgnoreCase(effect_color)) {
					if(map.get("redstone")) {
						map.put("redstone", false);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.removed").replace("%effect%", effect_color));
					}else {
						map.put("redstone", true);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.add").replace("%effect%", effect_color));
					}
				}else if(item.equalsIgnoreCase(effect_cryobsidian)) {
					if(map.get("cryobsidian")) {
						map.put("cryobsidian", false);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.removed").replace("%effect%", effect_cryobsidian));
					}else {
						map.put("cryobsidian", true);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.add").replace("%effect%", effect_cryobsidian));
					}
				}else if(item.equalsIgnoreCase(effect_emerald)) {
					if(map.get("emerald")) {
						map.put("emerald", false);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.removed").replace("%effect%", effect_emerald));
					}else {
						map.put("emerald", true);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.add").replace("%effect%", effect_emerald));
					}
				}else if(item.equalsIgnoreCase(effect_ender)) {
					if(map.get("ender")) {
						map.put("ender", false);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.removed").replace("%effect%", effect_ender));
					}else {
						map.put("ender", true);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.add").replace("%effect%", effect_ender));
					}
				}else if(item.equalsIgnoreCase(effect_endrod)) {
					if(map.get("endrod")) {
						map.put("endrod", false);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.removed").replace("%effect%", effect_endrod));
					}else {
						map.put("endrod", true);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.add").replace("%effect%", effect_endrod));
					}
				}else if(item.equalsIgnoreCase(effect_glow)) {
					if(map.get("glow")) {
						map.put("glow", false);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.removed").replace("%effect%", effect_glow));
					}else {
						map.put("glow", true);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.add").replace("%effect%", effect_glow));
					}
				}else if(item.equalsIgnoreCase(effect_hearts)) {
					if(map.get("hearts")) {
						map.put("hearts", false);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.removed").replace("%effect%", effect_hearts));
					}else {
						map.put("hearts", true);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.add").replace("%effect%", effect_hearts));
					}
				}else if(item.equalsIgnoreCase(effect_honey)) {
					if(map.get("honey")) {
						map.put("honey", false);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.removed").replace("%effect%", effect_honey));
					}else {
						map.put("honey", true);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.add").replace("%effect%", effect_honey));
					}
				}else if(item.equalsIgnoreCase(effect_lava)) {
					if(map.get("lava")) {
						map.put("lava", false);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.removed").replace("%effect%", effect_lava));
					}else {
						map.put("lava", true);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.add").replace("%effect%", effect_lava));
					}
				}else if(item.equalsIgnoreCase(effect_music)) {
					if(map.get("music")) {
						map.put("music", false);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.removed").replace("%effect%", effect_music));
					}else {
						map.put("music", true);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.add").replace("%effect%", effect_music));
					}
				}else if(item.equalsIgnoreCase(effect_slime)) {
					if(map.get("slime")) {
						map.put("slime", false);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.removed").replace("%effect%", effect_slime));
					}else {
						map.put("slime", true);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.add").replace("%effect%", effect_slime));
					}
				}else if(item.equalsIgnoreCase(effect_snow)) {
					if(map.get("snow")) {
						map.put("snow", false);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.removed").replace("%effect%", effect_snow));
					}else {
						map.put("snow", true);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.add").replace("%effect%", effect_snow));
					}
				}else if(item.equalsIgnoreCase(effect_soul)) {
					if(map.get("soulfire")) {
						map.put("soulfire", false);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.removed").replace("%effect%", effect_soul));
					}else {
						map.put("soulfire", true);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.add").replace("%effect%", effect_soul));
					}
				}else if(item.equalsIgnoreCase(effect_souls)) {
					if(map.get("souls")) {
						map.put("souls", false);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.removed").replace("%effect%", effect_souls));
					}else {
						map.put("souls", true);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.add").replace("%effect%", effect_souls));
					}
				}else if(item.equalsIgnoreCase(effect_water)) {
					if(map.get("water")) {
						map.put("water", false);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.removed").replace("%effect%", effect_water));
					}else {
						map.put("water", true);
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.effects.add").replace("%effect%", effect_water));
					}
				}else if(item.equalsIgnoreCase(back)) {
					closed = true;
					setExtrasInventory(player);
				}else if(item.equalsIgnoreCase(close)) {
					player.getOpenInventory().close();
					closed = true;
				}
				if(!closed) {
					setEffectSettings(player, map);
					EffectMoveEvent.playerEffects.put(player.getUniqueId(), map);
					setEffectsInventory(player);
				}
			}else {
				LotusController lc = new LotusController();
				String item = event.getCurrentItem().getItemMeta().getDisplayName();
				String noMoveMsg = lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.playerInventory.cancel").replace("%item%", item);
				if(item.equalsIgnoreCase(HotbarItem.hb_extras) || item.equalsIgnoreCase(HotbarItem.hb_friends) || item.equalsIgnoreCase(HotbarItem.hb_psettings) ||
						item.equalsIgnoreCase(HotbarItem.hb_language) || item.equalsIgnoreCase(HotbarItem.hb_navigator)) {
					player.sendMessage(noMoveMsg);
					event.setCancelled(true);
				}else {
					event.setCancelled(false);
				}
			}
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		String item = event.getItemDrop().getItemStack().getItemMeta().getDisplayName();
		LotusController lc = new LotusController();
		// You can't drop %item%!
		String noDropMsg = lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.drop.cancel").replace("%item%", item);
		HashMap<String, String> fancyNames = getServerFancynames();
		if(item.equalsIgnoreCase(HotbarItem.hb_extras) || item.equalsIgnoreCase(HotbarItem.hb_friends) || item.equalsIgnoreCase(HotbarItem.hb_psettings) ||
				item.equalsIgnoreCase(HotbarItem.hb_language) || item.equalsIgnoreCase(HotbarItem.hb_navigator) || fancyNames.containsKey(item)) {
			event.setCancelled(true);
			player.sendMessage(noDropMsg);
		}else {
			event.setCancelled(false);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		if(BuildCMD.hasPlayer(player)) {
			event.setCancelled(false);
		}else {
			if(event.getItem() != null && event.getItem().getItemMeta() != null) {
				String itemName = event.getItem().getItemMeta().getDisplayName();
				if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
					LotusController lc = new LotusController();
					if(itemName.equalsIgnoreCase(HotbarItem.hb_extras)) {
						event.setCancelled(true);
						lc.sendMessageReady(player, "event.hotbar.open.extras");
						setExtrasInventory(player);
					}else if(itemName.equalsIgnoreCase(HotbarItem.hb_friends)) {
						event.setCancelled(true);
						lc.sendMessageReady(player, "event.hotbar.open.friends");
					}else if(itemName.equalsIgnoreCase(HotbarItem.hb_psettings)) {
						event.setCancelled(true);
						lc.sendMessageReady(player, "event.hotbar.open.settings");
					}else if(itemName.equalsIgnoreCase(HotbarItem.hb_language)) {
						event.setCancelled(true);
						lc.sendMessageReady(player, "event.hotbar.open.language");
						setLanguageInventory(player);
					}else if(itemName.equalsIgnoreCase(HotbarItem.hb_navigator)) {
						event.setCancelled(true);
						setNavigatorInventory(player);
						lc.sendMessageReady(player, "event.hotbar.open.navigator");
					}
				}
			}
		}
	}
	
	private void sendPlayerToServer(Player player, String fancyName, String destinationServer, LotusController lc) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeUTF("Connect");
			dos.writeUTF(destinationServer);
			player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.navigator.sendPlayer.success"));
			Main.logger.info(player.getName() + " has been sent to " + destinationServer + " successfully.");
		} catch (IOException e) {
			Main.logger.severe(player.getName() + " attempted to be sent to " + destinationServer + " but failed!");
			e.printStackTrace();
		}
		player.sendPluginMessage(Main.main, "BungeeCord", baos.toByteArray());
	}
	
	private static HashMap<String, String> servers = new HashMap<>();
	private static HashMap<String, String> langs = new HashMap<>();
	
	public static void loadServer() {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT displayname,bungeeKey FROM mc_serverstats");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				servers.put(rs.getString("displayname"), rs.getString("bungeeKey"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			LotusController lc = new LotusController();
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM core_translations WHERE path = ?");
			ps.setString(1, "mcinternal.language");
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				for(String string : lc.getAvailableLanguages()) {
					langs.put(string, rs.getString(string));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private boolean findAndUpdatePlayerLanguage(Player player, String newLanguage) {
		boolean success = false;
		if(langs.containsKey(newLanguage)) {
			LotusController.playerLanguages.put(player.getUniqueId().toString(), newLanguage);
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE mc_users SET language = ? WHERE mcuuid = ?");
				ps.setString(1, newLanguage);
				ps.setString(2, player.getUniqueId().toString());
				ps.executeUpdate();
				success = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			success = false;
		}
		return success;
	}
	
	private HashMap<String, String> getServerFancynames(){
		return servers;
	}
	
	private static List<Material> matList(){
		List<Material> matList = new ArrayList<Material>();
		matList.add(Material.WHITE_CONCRETE_POWDER);
		matList.add(Material.LIGHT_GRAY_CONCRETE_POWDER);
		matList.add(Material.GRAY_CONCRETE_POWDER);
		matList.add(Material.BLACK_CONCRETE_POWDER);
		matList.add(Material.BROWN_CONCRETE_POWDER);
		matList.add(Material.RED_CONCRETE_POWDER);
		matList.add(Material.ORANGE_CONCRETE_POWDER);
		matList.add(Material.YELLOW_CONCRETE_POWDER);
		matList.add(Material.LIME_CONCRETE_POWDER);
		matList.add(Material.GREEN_CONCRETE_POWDER);
		matList.add(Material.CYAN_CONCRETE_POWDER);
		matList.add(Material.LIGHT_BLUE_CONCRETE_POWDER);
		matList.add(Material.BLUE_CONCRETE_POWDER);
		matList.add(Material.PURPLE_CONCRETE_POWDER);
		matList.add(Material.PINK_CONCRETE_POWDER);
		matList.add(Material.MAGENTA_CONCRETE_POWDER);
		return matList;
	}
	
	HashMap<String, Boolean> getEffectSettings(Player player){
		HashMap<String, Boolean> map = new HashMap<>();
		LotusController lc = new LotusController();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT effectSettings FROM mc_users WHERE mcuuid = ?");
			ps.setString(1, player.getUniqueId().toString());
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				String[] splitByEffect = rs.getString("effectSettings").split(";");
				for(String string : splitByEffect) {
					String effect = string.split("=")[0];
					boolean state = lc.translateBoolean(string.split("=")[1]);
					map.put(effect, state);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	void setEffectSettings(Player player, HashMap<String, Boolean> map) {
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, Boolean> entry : map.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue());
			sb.append(";");
		}
		String settings = sb.toString().substring(0, (sb.toString().length() - 1));
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE mc_users SET effectSettings = ? WHERE mcuuid = ?");
			ps.setString(1, settings);
			ps.setString(2, player.getUniqueId().toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}