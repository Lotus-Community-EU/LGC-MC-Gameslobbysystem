package eu.lotusgc.mc.event;

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
import eu.lotusgc.mc.misc.ChatbridgeEnums;
import eu.lotusgc.mc.misc.ChatbridgeUtils;
import eu.lotusgc.mc.misc.HotbarItem;
import eu.lotusgc.mc.misc.LotusController;
import eu.lotusgc.mc.misc.Money;
import eu.lotusgc.mc.misc.MySQL;
import eu.lotusgc.mc.misc.Playerdata;
import eu.lotusgc.mc.misc.Prefix;

public class InventorySetterHandling implements Listener{
	
	static String navi_title = "§a§nNavigator";
	static String navi_spawn = "§6Spawn";
	static String navi_back = "§cback";
	
	static String navi_bedwars = "§a§fBed§cWars";
	static String navi_ffa = "§cFFA";
	static String navi_monsterhunter = "§3MonsterHunter";
	static String navi_1vs1 = "§41 vs. 1";
	static String navi_spleef = "§a§fSpleef";
	static String navi_masterbuilders = "§bMaster§9Builders";
	static String navi_gungames = "§8GunGames";
	static String navi_endergames = "§dEndergames";
	static String navi_comingsoon = "§ccoming soon"; //If a new game is planned being added, we will use this itemname.
	
	public static String profileSettings = "§bProfile Settings";
	public static String profile_DCBBukkit = "§bDCB Options §6Events";
	public static String profile_DCBProxy = "§bDCB Options §6General";
	
	static String language_title = "§6Languages";
	
	static String extras_title = "§9Extras";
	static String extras_boots = "§dEffects";
	static String extras_jboost = "§9Jumpboost";
	static String extras_sboost = "§bSpeedboost";
	
	static String sboost_title = "§bSpeedboost";
	static String sboost_stage1 = "§2Default";
	static String sboost_stage2 = "§7Stage §a1";
	static String sboost_stage3 = "§7Stage §e2";
	static String sboost_stage4 = "§7Stage §63";
	static String sboost_stage5 = "§7Stage §c4";
	
	static String jboost_title = "§9Jumpboost";
	static String jboost_stage1 = "§2Default";
	static String jboost_stage2 = "§7Stage §a1";
	static String jboost_stage3 = "§7Stage §e2";
	static String jboost_stage4 = "§7Stage §63";
	static String jboost_stage5 = "§7Stage §c4";
	
	static String effect_hearts = "§7» §cHearts";
	static String effect_clouds = "§7» §fClouds";
	static String effect_music = "§7» §6Musicnotes";
	static String effect_slime = "§7» §aSlime";
	static String effect_water = "§7» §1Waterdrops";
	static String effect_ender = "§7» §9Enderffects";
	static String effect_emerald = "§7» §aEmerald";
	static String effect_lava = "§7» §cLavadrops";
	static String effect_honey = "§7» §6Honeydrops";
	static String effect_color = "§7» §cC§2o§6l§co§ar§9s";
	static String effect_snow = "§7» §fSnowball";
	static String effect_soul = "§7» §bSoul Fire";
	static String effect_ash = "§7» Ash";
	static String effect_souls = "§7» §bSouls";
	static String effect_glow = "§7» §eGlow";
	static String effect_endrod = "§7» §fEnd-Rod Particles";
	static String effect_cryobsidian = "§7» §5Obsidian Tears";
	static String effect_cherry = "§7» §dCherry Leaves";
	
	public static String dcbs_b_advancements = "§7Show Advancements: ";
	public static String dcbs_b_death = "§7Show Deaths: ";
	public static String dcbs_b_killEntity = "§7Show Entitykills: ";
	public static String dcbs_b_levelChange = "§7Show Levelup: ";
	public static String dcbs_b_worldChange = "§7Show World Change: ";
	
	public static String dcbs_p_joins = "§7Show Joins: ";
	public static String dcbs_p_serverChange = "§7Show Serverchanges: ";
	public static String dcbs_p_quits = "§7Show Quits: ";
	public static String dcbs_p_clans = "§7Show Clans: ";
	public static String dcbs_p_id = "§7Show ID: ";
	public static String dcbs_p_nick = "§7Show Nick: ";
	public static String dcbs_p_role = "§7Show Role: ";
	
	public static String off = "§cno";
	public static String on = "§ayes";
	
	static String close = "§cclose";
	static String back = "§cback";
	
	public static void setNavigatorInventory(Player player) {
		Inventory mainInventory = Bukkit.createInventory(null, 3*9, navi_title);
		LotusController lc = new LotusController();
		for(int i = 0; i < 27; i++) {
			mainInventory.setItem(i, lc.defItem(Material.BLUE_STAINED_GLASS_PANE, "§c", 1));
		}
		mainInventory.setItem(0, lc.loreItem(Material.RED_BED, 1, navi_bedwars, "§7There are §a0 Players", "§7on §60 Servers §7playing", "§7this minigame."));
		mainInventory.setItem(2, lc.loreItem(Material.IRON_HELMET, 1, navi_ffa, "§7There are §a0 Players", "§7on §60 Servers §7playing", "§7this minigame."));
		mainInventory.setItem(4, lc.defItem(Material.DIAMOND, navi_spawn, 1));
		mainInventory.setItem(6, lc.loreItem(Material.TOTEM_OF_UNDYING, 1, navi_monsterhunter, "§7There are §a0 Players", "§7on §60 Servers §7playing", "§7this minigame."));
		mainInventory.setItem(8, lc.loreItem(Material.GOLDEN_AXE, 1, navi_1vs1, "§7There are §a0 Players", "§7on §60 Servers §7playing", "§7this minigame."));
		mainInventory.setItem(18, lc.loreItem(Material.DIAMOND_SHOVEL, 1, navi_spleef, "§7There are §a0 Players", "§7on §60 Servers §7playing", "§7this minigame."));
		mainInventory.setItem(20, lc.loreItem(Material.RED_GLAZED_TERRACOTTA, 1, navi_masterbuilders, "§7There are §a0 Players", "§7on §60 Servers §7playing", "§7this minigame."));
		mainInventory.setItem(22, lc.defItem(Material.BEDROCK, navi_comingsoon, 1));
		mainInventory.setItem(24, lc.loreItem(Material.IRON_SWORD, 1, navi_gungames, "§7There are §a0 Players", "§7on §60 Servers §7playing", "§7this minigame."));
		mainInventory.setItem(26, lc.loreItem(Material.ENDER_PEARL, 1, navi_endergames, "§7There are §a0 Players", "§7on §60 Servers §7playing", "§7this minigame."));
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
		jbInv.setItem(9, lc.potionItem(1, jboost_stage1, new PotionEffect(PotionEffectType.JUMP_BOOST, 1200, 1))); //default
		jbInv.setItem(10, lc.defItem(Material.LIME_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(11, lc.potionItem(1, jboost_stage2, new PotionEffect(PotionEffectType.JUMP_BOOST, 1200, 2))); //stage 1
		jbInv.setItem(12, lc.defItem(Material.YELLOW_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(13, lc.potionItem(1, jboost_stage3, new PotionEffect(PotionEffectType.JUMP_BOOST, 1200, 3))); //stage 2
		jbInv.setItem(14, lc.defItem(Material.ORANGE_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(15, lc.potionItem(1, jboost_stage4, new PotionEffect(PotionEffectType.JUMP_BOOST, 1200, 4))); //stage 3
		jbInv.setItem(16, lc.defItem(Material.RED_STAINED_GLASS_PANE, "§c", 1));
		jbInv.setItem(17, lc.potionItem(1, jboost_stage5, new PotionEffect(PotionEffectType.JUMP_BOOST, 1200, 5))); //stage 4
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
			effectsInv.setItem(10, lc.enchantedItem(Material.RED_DYE, effect_hearts, 1, Enchantment.EFFICIENCY, 1));
		}else {
			effectsInv.setItem(10, lc.defItem(Material.RED_DYE, effect_hearts, 1));
		}
		if(map.get("clouds")) {
			effectsInv.setItem(11, lc.enchantedItem(Material.BONE_MEAL, effect_clouds, 1, Enchantment.EFFICIENCY, 1));
		}else {
			effectsInv.setItem(11, lc.defItem(Material.BONE_MEAL, effect_clouds, 1));
		}
		if(map.get("music")) {
			effectsInv.setItem(12, lc.enchantedItem(Material.NOTE_BLOCK, effect_music, 1, Enchantment.EFFICIENCY, 1));
		}else {
			effectsInv.setItem(12, lc.defItem(Material.NOTE_BLOCK, effect_music, 1));
		}
		if(map.get("slime")) {
			effectsInv.setItem(13, lc.enchantedItem(Material.SLIME_BALL, effect_slime, 1, Enchantment.EFFICIENCY, 1));
		}else {
			effectsInv.setItem(13, lc.defItem(Material.SLIME_BALL, effect_slime, 1));
		}
		if(map.get("water")) {
			effectsInv.setItem(14, lc.enchantedItem(Material.WATER_BUCKET, effect_water, 1, Enchantment.EFFICIENCY, 1));
		}else {
			effectsInv.setItem(14, lc.defItem(Material.WATER_BUCKET, effect_water, 1));
		}
		if(map.get("ender")) {
			effectsInv.setItem(15, lc.enchantedItem(Material.ENDER_EYE, effect_ender, 1, Enchantment.EFFICIENCY, 1));
		}else {
			effectsInv.setItem(15, lc.defItem(Material.ENDER_EYE, effect_ender, 1));
		}
		if(map.get("emerald")) {
			effectsInv.setItem(16, lc.enchantedItem(Material.EMERALD, effect_emerald, 1, Enchantment.EFFICIENCY, 1));
		}else {
			effectsInv.setItem(16, lc.defItem(Material.EMERALD, effect_emerald, 1));
		}
		if(map.get("lava")) {
			effectsInv.setItem(19, lc.enchantedItem(Material.LAVA_BUCKET, effect_lava, 1, Enchantment.EFFICIENCY, 1));
		}else {
			effectsInv.setItem(19, lc.defItem(Material.LAVA_BUCKET, effect_lava, 1));
		}
		if(map.get("honey")) {
			effectsInv.setItem(20, lc.enchantedItem(Material.HONEY_BOTTLE, effect_honey, 1, Enchantment.EFFICIENCY, 1));
		}else {
			effectsInv.setItem(20, lc.defItem(Material.HONEY_BOTTLE, effect_honey, 1));
		}
		if(map.get("redstone")) {
			effectsInv.setItem(21, lc.enchantedItem(Material.REDSTONE, effect_color, 1, Enchantment.EFFICIENCY, 1));
		}else {
			effectsInv.setItem(21, lc.defItem(Material.REDSTONE, effect_color, 1));
		}
		if(map.get("snow")) {
			effectsInv.setItem(22, lc.enchantedItem(Material.SNOWBALL, effect_snow, 1, Enchantment.EFFICIENCY, 1));
		}else {
			effectsInv.setItem(22, lc.defItem(Material.SNOWBALL, effect_snow, 1));
		}
		if(map.get("soulfire")) {
			effectsInv.setItem(23, lc.enchantedItem(Material.SOUL_TORCH, effect_soul, 1, Enchantment.EFFICIENCY, 1));
		}else {
			effectsInv.setItem(23, lc.defItem(Material.SOUL_TORCH, effect_soul, 1));
		}
		if(map.get("ash")) {
			effectsInv.setItem(24, lc.enchantedItem(Material.BASALT, effect_ash, 1, Enchantment.EFFICIENCY, 1));
		}else {
			effectsInv.setItem(24, lc.defItem(Material.BASALT, effect_ash, 1));
		}
		if(map.get("souls")) {
			effectsInv.setItem(25, lc.enchantedItem(Material.SOUL_SAND, effect_souls, 1, Enchantment.EFFICIENCY, 1));
		}else {
			effectsInv.setItem(25, lc.defItem(Material.SOUL_SAND, effect_souls, 1));
		}
		if(map.get("glow")) {
			effectsInv.setItem(28, lc.enchantedItem(Material.GLOWSTONE, effect_glow, 1, Enchantment.EFFICIENCY, 1));
		}else {
			effectsInv.setItem(28, lc.defItem(Material.GLOWSTONE, effect_glow, 1));
		}
		if(map.get("endrod")) {
			effectsInv.setItem(32, lc.enchantedItem(Material.END_ROD, effect_endrod, 1, Enchantment.EFFICIENCY, 1));
		}else {
			effectsInv.setItem(32, lc.defItem(Material.END_ROD, effect_endrod, 1));
		}
		if(map.get("cryobsidian")) {
			effectsInv.setItem(34, lc.enchantedItem(Material.CRYING_OBSIDIAN, effect_cryobsidian, 1, Enchantment.EFFICIENCY, 1));
		}else {
			effectsInv.setItem(34, lc.defItem(Material.CRYING_OBSIDIAN, effect_cryobsidian, 1));
		}
		if(map.get("cherry")) {
			effectsInv.setItem(30, lc.enchantedItem(Material.CHERRY_LEAVES, effect_cherry, 1, Enchantment.EFFICIENCY, 1));
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
		mainInventory.setItem(11, lc.defItem(Material.LEATHER_BOOTS, extras_boots, 1));
		mainInventory.setItem(13, lc.defItem(Material.POTION, extras_sboost, 1));
		mainInventory.setItem(15, lc.defItem(Material.POTION, extras_jboost, 1));
		mainInventory.setItem(22, lc.defItem(Material.BARRIER, close, 1));
		player.openInventory(mainInventory);
	}
	
	public static void setProfileSettingsInventory(Player player) {
		Inventory inventory = Bukkit.createInventory(null, 3*9, profileSettings);
		LotusController lc = new LotusController();
		inventory.setItem(8, lc.loreItem(Material.GOLD_NUGGET, 1, "§aMoney", "§7Pocketmoney: §a" + lc.getMoney(player, Money.POCKET), "§7Bankmoney: §a" + lc.getMoney(player, Money.BANK), "§7Interest Level: §a" + lc.getInterestLevel(player)));
		inventory.setItem(26, lc.defItem(Material.BOOK, language_title, 1));
		inventory.setItem(18, lc.defItem(Material.FEATHER, profile_DCBProxy, 1));
		inventory.setItem(19, lc.defItem(Material.FEATHER, profile_DCBBukkit, 1));
		player.openInventory(inventory);
	}
	
	public static void setDCB_BukkitSettingsInventory(Player player) {
		Inventory inventory = Bukkit.createInventory(null, 1*9, profile_DCBBukkit);
		HashMap<String, Boolean> map = new ChatbridgeUtils().getChatbridgeSettings(player.getUniqueId());
		LotusController lc = new LotusController();
		if(map.get(ChatbridgeEnums.SHOW_BUKKITADVANCEMENTS.getNodename())) {
			inventory.setItem(0, lc.defItem(Material.GREEN_WOOL, dcbs_b_advancements + "§ayes", 1));
		}else {
			inventory.setItem(0, lc.defItem(Material.RED_WOOL, dcbs_b_advancements + "§cno", 1));
		}
		if(map.get(ChatbridgeEnums.SHOW_BUKKITDEATH.getNodename())) {
			inventory.setItem(1, lc.defItem(Material.GREEN_WOOL, dcbs_b_death + "§ayes", 1));
		}else {
			inventory.setItem(1, lc.defItem(Material.RED_WOOL, dcbs_b_death + "§cno", 1));
		}
		if(map.get(ChatbridgeEnums.SHOW_BUKKITKILLENTITY.getNodename())) {
			inventory.setItem(2, lc.defItem(Material.GREEN_WOOL, dcbs_b_killEntity + "§ayes", 1));
		}else {
			inventory.setItem(2, lc.defItem(Material.RED_WOOL, dcbs_b_killEntity + "§cno", 1));
		}
		if(map.get(ChatbridgeEnums.SHOW_BUKKITWORLDCHANGE.getNodename())) {
			inventory.setItem(3, lc.defItem(Material.GREEN_WOOL, dcbs_b_worldChange + "§ayes", 1));
		}else {
			inventory.setItem(3, lc.defItem(Material.RED_WOOL, dcbs_b_worldChange + "§cno", 1));
		}
		if(map.get(ChatbridgeEnums.SHOW_BUKKITLEVELCHANGE.getNodename())) {
			inventory.setItem(4, lc.defItem(Material.GREEN_WOOL, dcbs_b_levelChange + "§ayes", 1));
		}else {
			inventory.setItem(4, lc.defItem(Material.RED_WOOL, dcbs_b_levelChange + "§cno", 1));
		}
		player.openInventory(inventory);
	}
	
	public static void setDCB_GeneralSettingsInventory(Player player) {
		Inventory inventory = Bukkit.createInventory(null, 1*9, profile_DCBProxy);
		HashMap<String, Boolean> map = new ChatbridgeUtils().getChatbridgeSettings(player.getUniqueId());
		LotusController lc = new LotusController();
		if(map.get(ChatbridgeEnums.SHOW_JOIN.getNodename())) {
			inventory.setItem(0, lc.defItem(Material.GREEN_WOOL, dcbs_p_joins + "§ayes", 1));
		}else {
			inventory.setItem(0, lc.defItem(Material.RED_WOOL, dcbs_p_joins + "§cno", 1));
		}
		if(map.get(ChatbridgeEnums.SHOW_SERVERCHANGE.getNodename())) {
			inventory.setItem(1, lc.defItem(Material.GREEN_WOOL, dcbs_p_serverChange + "§ayes", 1));
		}else {
			inventory.setItem(1, lc.defItem(Material.RED_WOOL, dcbs_p_serverChange + "§cno", 1));
		}
		if(map.get(ChatbridgeEnums.SHOW_QUIT.getNodename())) {
			inventory.setItem(2, lc.defItem(Material.GREEN_WOOL, dcbs_p_quits + "§ayes", 1));
		}else {
			inventory.setItem(2, lc.defItem(Material.RED_WOOL, dcbs_p_quits + "§cno", 1));
		}
		if(map.get(ChatbridgeEnums.SHOW_CLAN.getNodename())) {
			inventory.setItem(3, lc.defItem(Material.GREEN_WOOL, dcbs_p_clans + "§ayes", 1));
		}else {
			inventory.setItem(3, lc.defItem(Material.RED_WOOL, dcbs_p_clans + "§cno", 1));
		}
		if(map.get(ChatbridgeEnums.SHOW_ID.getNodename())) {
			inventory.setItem(4, lc.defItem(Material.GREEN_WOOL, dcbs_p_id + "§ayes", 1));
		}else {
			inventory.setItem(4, lc.defItem(Material.RED_WOOL, dcbs_p_id + "§cno", 1));
		}
		if(map.get(ChatbridgeEnums.SHOW_NICK.getNodename())) {
			inventory.setItem(5, lc.defItem(Material.GREEN_WOOL, dcbs_p_nick + "§ayes", 1));
		}else {
			inventory.setItem(5, lc.defItem(Material.RED_WOOL, dcbs_p_nick + "§cno", 1));
		}
		if(map.get(ChatbridgeEnums.SHOW_ROLE.getNodename())) {
			inventory.setItem(6, lc.defItem(Material.GREEN_WOOL, dcbs_p_role + "§ayes", 1));
		}else {
			inventory.setItem(6, lc.defItem(Material.RED_WOOL, dcbs_p_role + "§cno", 1));
		}
		player.openInventory(inventory);
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
			if(itemName.equalsIgnoreCase(navi_spawn)) {
				Location spawn = SpawnSystem.getSpawn("mainSpawn");
				player.closeInventory();
				player.teleport(spawn);
			}else if(itemName.equalsIgnoreCase(navi_1vs1)) {
				Location spawn = SpawnSystem.getSpawn("1vs1");
				player.closeInventory();
				player.teleport(spawn);
			}else if(itemName.equalsIgnoreCase(navi_bedwars)) {
				Location spawn = SpawnSystem.getSpawn("bedwars");
				player.closeInventory();
				player.teleport(spawn);
			}else if(itemName.equalsIgnoreCase(navi_comingsoon)) {
				//send out "This minigame is not released yet."
				lc.sendMessageReady(player, "event.navi.comingsoon");
			}else if(itemName.equalsIgnoreCase(navi_endergames)) {
				Location spawn = SpawnSystem.getSpawn("endergames");
				player.closeInventory();
				player.teleport(spawn);
			}else if(itemName.equalsIgnoreCase(navi_ffa)) {
				Location spawn = SpawnSystem.getSpawn("ffa");
				player.closeInventory();
				player.teleport(spawn);
			}else if(itemName.equalsIgnoreCase(navi_gungames)) {
				Location spawn = SpawnSystem.getSpawn("gungames");
				player.closeInventory();
				player.teleport(spawn);
			}else if(itemName.equalsIgnoreCase(navi_masterbuilders)) {
				Location spawn = SpawnSystem.getSpawn("masterbuilders");
				player.closeInventory();
				player.teleport(spawn);
			}else if(itemName.equalsIgnoreCase(navi_monsterhunter)) {
				Location spawn = SpawnSystem.getSpawn("monsterhunter");
				player.closeInventory();
				player.teleport(spawn);
			}else if(itemName.equalsIgnoreCase(navi_spleef)) {
				Location spawn = SpawnSystem.getSpawn("spleef");
				player.closeInventory();
				player.teleport(spawn);
			}
			
		}else if(event.getView().getTitle().equalsIgnoreCase(extras_title)) {
			event.setCancelled(true);
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
			}else if(itemName.equalsIgnoreCase(close)) {
				player.closeInventory();
			}
		} else if(event.getView().getTitle().equalsIgnoreCase(jboost_title)) {
			event.setCancelled(true);
			LotusController lc = new LotusController();
			if(event.getCurrentItem() == null && event.getCurrentItem().getItemMeta() == null) return;
			String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
			if(itemName.equalsIgnoreCase(jboost_stage1)) {
				player.removePotionEffect(PotionEffectType.JUMP_BOOST);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.jumpboost.default"));
			}else if(itemName.equalsIgnoreCase(jboost_stage2)) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 99999, 2));
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.jumpboost.staged").replace("%stage%", "1"));
			}else if(itemName.equalsIgnoreCase(jboost_stage3)) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 99999, 4));
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.jumpboost.staged").replace("%stage%", "2"));
			}else if(itemName.equalsIgnoreCase(jboost_stage4)) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 99999, 6));
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.extras.jumpboost.staged").replace("%stage%", "3"));
			}else if(itemName.equalsIgnoreCase(jboost_stage5)) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 99999, 8));
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
		}else if(event.getView().getTitle().equalsIgnoreCase(profile_DCBBukkit)) {
			String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
			LotusController lc = new LotusController();
			event.setCancelled(true);
			HashMap<String, Boolean> map = new ChatbridgeUtils().getChatbridgeSettings(player.getUniqueId());
			if(itemName.equalsIgnoreCase(dcbs_b_advancements + on)) {
				map.put(ChatbridgeEnums.SHOW_BUKKITADVANCEMENTS.getNodename(), false);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.disabled").replace("%setting%", "Advancements"));
			}else if(itemName.equalsIgnoreCase(dcbs_b_advancements + off)) {
				map.put(ChatbridgeEnums.SHOW_BUKKITADVANCEMENTS.getNodename(), true);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.enabled").replace("%setting%", "Advancements"));
			}else if(itemName.equalsIgnoreCase(dcbs_b_death + on)) {
				map.put(ChatbridgeEnums.SHOW_BUKKITDEATH.getNodename(), false);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.disabled").replace("%setting%", "Death Messages"));
			}else if(itemName.equalsIgnoreCase(dcbs_b_death + off)) {
				map.put(ChatbridgeEnums.SHOW_BUKKITDEATH.getNodename(), true);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.enabled").replace("%setting%", "Death Messages"));
			}else if(itemName.equalsIgnoreCase(dcbs_b_killEntity + on)) {
				map.put(ChatbridgeEnums.SHOW_BUKKITKILLENTITY.getNodename(), false);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.disabled").replace("%setting%", "Killed Entity"));
			}else if(itemName.equalsIgnoreCase(dcbs_b_killEntity + off)) {
				map.put(ChatbridgeEnums.SHOW_BUKKITKILLENTITY.getNodename(), true);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.enabled").replace("%setting%", "Killed Entity"));
			}else if(itemName.equalsIgnoreCase(dcbs_b_levelChange + on)) {
				map.put(ChatbridgeEnums.SHOW_BUKKITLEVELCHANGE.getNodename(), false);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.disabled").replace("%setting%", "Level Up Message"));
			}else if(itemName.equalsIgnoreCase(dcbs_b_levelChange + off)) {
				map.put(ChatbridgeEnums.SHOW_BUKKITLEVELCHANGE.getNodename(), true);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.enabled").replace("%setting%", "Level Up Message"));
			}else if(itemName.equalsIgnoreCase(dcbs_b_worldChange + on)) {
				map.put(ChatbridgeEnums.SHOW_BUKKITWORLDCHANGE.getNodename(), false);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.disabled").replace("%setting%", "World Change"));
			}else if(itemName.equalsIgnoreCase(dcbs_b_worldChange + off)) {
				map.put(ChatbridgeEnums.SHOW_BUKKITWORLDCHANGE.getNodename(), true);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.enabled").replace("%setting%", "World Change"));
			}
			setDCB_BukkitSettingsInventory(player);
		}else if(event.getView().getTitle().equalsIgnoreCase(profile_DCBProxy)) {
			String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
			LotusController lc = new LotusController();
			HashMap<String, Boolean> map = new ChatbridgeUtils().getChatbridgeSettings(player.getUniqueId());
			event.setCancelled(true);
			if(itemName.equalsIgnoreCase(dcbs_p_clans + on)) {
				map.put(ChatbridgeEnums.SHOW_CLAN.getNodename(), false);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.disabled").replace("%setting%", "Show Clan"));
			}else if(itemName.equalsIgnoreCase(dcbs_p_clans + off)) {
				map.put(ChatbridgeEnums.SHOW_CLAN.getNodename(), true);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.enabled").replace("%setting%", "Show Clan"));
			}else if(itemName.equalsIgnoreCase(dcbs_p_id + on)) {
				map.put(ChatbridgeEnums.SHOW_ID.getNodename(), false);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.disabled").replace("%setting%", "Show ID"));
			}else if(itemName.equalsIgnoreCase(dcbs_p_id + off)) {
				map.put(ChatbridgeEnums.SHOW_ID.getNodename(), true);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.enabled").replace("%setting%", "Show ID"));
			}else if(itemName.equalsIgnoreCase(dcbs_p_joins + on)) {
				map.put(ChatbridgeEnums.SHOW_JOIN.getNodename(), false);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.disabled").replace("%setting%", "Announce Join"));
			}else if(itemName.equalsIgnoreCase(dcbs_p_joins + off)) {
				map.put(ChatbridgeEnums.SHOW_JOIN.getNodename(), true);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.enabled").replace("%setting%", "Announce Join"));
			}else if(itemName.equalsIgnoreCase(dcbs_p_nick + on)) {
				map.put(ChatbridgeEnums.SHOW_NICK.getNodename(), false);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.disabled").replace("%setting%", "Show Nick"));
			}else if(itemName.equalsIgnoreCase(dcbs_p_nick + off)) {
				map.put(ChatbridgeEnums.SHOW_NICK.getNodename(), true);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.enabled").replace("%setting%", "Show Nick"));
			}else if(itemName.equalsIgnoreCase(dcbs_p_quits + on)) {
				map.put(ChatbridgeEnums.SHOW_QUIT.getNodename(), false);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.disabled").replace("%setting%", "Announce Quit"));
			}else if(itemName.equalsIgnoreCase(dcbs_p_quits + off)) {
				map.put(ChatbridgeEnums.SHOW_QUIT.getNodename(), true);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.enabled").replace("%setting%", "Announce Quit"));
			}else if(itemName.equalsIgnoreCase(dcbs_p_role + on)) {
				map.put(ChatbridgeEnums.SHOW_ROLE.getNodename(), false);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.disabled").replace("%setting%", "Show Role"));
			}else if(itemName.equalsIgnoreCase(dcbs_p_role + off)) {
				map.put(ChatbridgeEnums.SHOW_ROLE.getNodename(), true);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.enabled").replace("%setting%", "Show Role"));
			}else if(itemName.equalsIgnoreCase(dcbs_p_serverChange + on)) {
				map.put(ChatbridgeEnums.SHOW_SERVERCHANGE.getNodename(), false);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.disabled").replace("%setting%", "Announce Server Change"));
			}else if(itemName.equalsIgnoreCase(dcbs_p_serverChange + off)) {
				map.put(ChatbridgeEnums.SHOW_SERVERCHANGE.getNodename(), true);
				new ChatbridgeUtils().setChatbridgeSettings(player.getUniqueId(), map);
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "event.settings.chatbridge.enabled").replace("%setting%", "Announce Server Change"));
			}
			setDCB_GeneralSettingsInventory(player);
		}else if(event.getView().getTitle().equalsIgnoreCase(profileSettings)) {
			event.setCancelled(true);
			LotusController lc = new LotusController();
			String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
			if(itemName.equalsIgnoreCase(language_title)) {
				lc.sendMessageReady(player, "event.hotbar.open.language");
				setLanguageInventory(player);
			}else if(itemName.equalsIgnoreCase(profile_DCBBukkit)) {
				setDCB_BukkitSettingsInventory(player);
			}else if(itemName.equalsIgnoreCase(profile_DCBProxy)) {
				setDCB_GeneralSettingsInventory(player);
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
						setProfileSettingsInventory(player);
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