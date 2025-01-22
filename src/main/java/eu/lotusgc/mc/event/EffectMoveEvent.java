//Created by Chris Wille at 15.03.2024
package eu.lotusgc.mc.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiConsumer;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
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

		if (map == null){ return; } // Ensure the player has effects

		World world = player.getWorld();
		Location location = player.getLocation();

		// Define a method to spawn a particle if the effect is enabled
		BiConsumer<Particle, Location> spawnParticleIfEnabled = (particle, loc) -> {
			if (map.getOrDefault(particle.name().toLowerCase(), false)) {
				world.spawnParticle(particle, loc, 1);
			}
		};

		spawnParticleIfEnabled.accept(Particle.HEART, location);
		spawnParticleIfEnabled.accept(Particle.CLOUD, location);
		spawnParticleIfEnabled.accept(Particle.NOTE, location);
		spawnParticleIfEnabled.accept(Particle.ITEM_SLIME, location);
		spawnParticleIfEnabled.accept(Particle.DRIPPING_WATER, location);
		spawnParticleIfEnabled.accept(Particle.HAPPY_VILLAGER, location);
		spawnParticleIfEnabled.accept(Particle.DRIPPING_LAVA, location);
		spawnParticleIfEnabled.accept(Particle.DRIPPING_HONEY, location);
		spawnParticleIfEnabled.accept(Particle.DUST, location);
		spawnParticleIfEnabled.accept(Particle.ITEM_SNOWBALL, location);
		spawnParticleIfEnabled.accept(Particle.SOUL_FIRE_FLAME, location);
		spawnParticleIfEnabled.accept(Particle.WHITE_ASH, location.add(0, 1, 0));
		spawnParticleIfEnabled.accept(Particle.ASH, location.add(0, 1, 0));
		spawnParticleIfEnabled.accept(Particle.SOUL, location.add(0, 1, 0));
		spawnParticleIfEnabled.accept(Particle.GLOW, location.add(0, 1, 0));
		spawnParticleIfEnabled.accept(Particle.END_ROD, location.add(0, 1, 0));
		spawnParticleIfEnabled.accept(Particle.DRIPPING_OBSIDIAN_TEAR, location.add(0, 1, 0));
		spawnParticleIfEnabled.accept(Particle.CHERRY_LEAVES, location.add(0, 1, 0));
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