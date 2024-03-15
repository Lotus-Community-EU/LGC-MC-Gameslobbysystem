//Created by Chris Wille at 15.03.2024
package eu.lotusgc.mc.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import eu.lotusgc.mc.misc.LotusController;
import eu.lotusgc.mc.misc.MySQL;

public class EffectMoveEvent implements Listener {
	
public static HashMap<UUID, HashMap<String, Boolean>> playerEffects = new HashMap<>();
	
	@EventHandler
	public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
		UUID uuid = event.getUniqueId();
		playerEffects.put(uuid, getEffectSettings(uuid));
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		playerEffects.remove(event.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		HashMap<String, Boolean> map = playerEffects.get(player.getUniqueId());
		if(map.get("hearts")) {
			player.getWorld().spawnParticle(Particle.HEART, player.getLocation(), 1);
		}
		if(map.get("clouds")) {
			player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 1);
		}
		if(map.get("music")) {
			player.getWorld().spawnParticle(Particle.NOTE, player.getLocation(), 1);
		}
		if(map.get("slime")) {
			player.getWorld().spawnParticle(Particle.SLIME, player.getLocation(), 1);
		}
		if(map.get("water")) {
			player.getWorld().spawnParticle(Particle.WATER_DROP, player.getLocation(), 1);
		}
		if(map.get("ender")) {
			player.getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 1);
		}
		if(map.get("emerald")) {
			player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation(), 1);
		}
		if(map.get("lava")) {
			player.getWorld().spawnParticle(Particle.DRIP_LAVA, player.getLocation(), 1);
		}
		if(map.get("honey")) {
			player.getWorld().spawnParticle(Particle.DRIPPING_HONEY, player.getLocation(), 1);
		}
		if(map.get("redstone")) {
			Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(getRGB(), getRGB(), getRGB()), 2);
			player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 1, dust);
		}
		if(map.get("snow")) {
			player.getWorld().spawnParticle(Particle.SNOWBALL, player.getLocation(), 1);
		}
		if(map.get("soulfire")) {
			player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, player.getLocation(), 1);
		}
		if(map.get("ash")) {
			player.getWorld().spawnParticle(Particle.WHITE_ASH, player.getLocation().add(0, 1, 0), 1);
			player.getWorld().spawnParticle(Particle.ASH, player.getLocation().add(0, 1, 0), 1);
		}
		if(map.get("souls")) {
			player.getWorld().spawnParticle(Particle.SOUL, player.getLocation().add(0, 1, 0), 1);
		}
		if(map.get("glow")) {
			player.getWorld().spawnParticle(Particle.GLOW, player.getLocation().add(0, 1, 0), 1);
		}
		if(map.get("endrod")) {
			player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation().add(0, 1, 0), 1);
		}
		if(map.get("cryobsidian")) {
			player.getWorld().spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, player.getLocation().add(0, 1, 0), 1);
		}
		if(map.get("cherry")) {
			player.getWorld().spawnParticle(Particle.CHERRY_LEAVES, player.getLocation().add(0, 1, 0), 1);
		}
	}
	
	HashMap<String, Boolean> getEffectSettings(UUID uuid){
		HashMap<String, Boolean> map = new HashMap<>();
		LotusController lc = new LotusController();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT effectSettings FROM mc_users WHERE mcuuid = ?");
			ps.setString(1, uuid.toString());
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
	
	int getRGB() {
		Random random = new Random();
		int number = random.nextInt(0, 255);
		while (number < 0)
			number = random.nextInt(255);
		return number;
	}

}

