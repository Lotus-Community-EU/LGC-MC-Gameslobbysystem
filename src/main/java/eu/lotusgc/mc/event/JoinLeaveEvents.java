//Created by Chris Wille at 14.03.2024
package eu.lotusgc.mc.event;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import eu.lotusgc.mc.misc.HotbarItem;
import eu.lotusgc.mc.misc.LotusController;
import eu.lotusgc.mc.misc.MySQL;

public class JoinLeaveEvents implements Listener{
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		event.setJoinMessage(null);
		player.setGameMode(GameMode.SURVIVAL);
		player.setHealth(20.0);
		player.setFoodLevel(20);
		player.setWalkSpeed((float) 0.2);
		new HotbarItem().setHotbarItems(player);
		
		updateOnlineStatus(player.getUniqueId(), true);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		updateOnlineStatus(event.getPlayer().getUniqueId(), false);
	}

	
	private void updateOnlineStatus(UUID uuid, boolean status) {
		try {
			PreparedStatement ps;
			if(status) {
				LotusController lc = new LotusController();
				ps = MySQL.getConnection().prepareStatement("UPDATE mc_users SET isOnline = ?, currentLastServer = ? WHERE mcuuid = ?");
				ps.setBoolean(1, status);
				ps.setString(2, lc.getServerName());
				ps.setString(3, uuid.toString());
			}else {
				ps = MySQL.getConnection().prepareStatement("UPDATE mc_users SET isOnline = ? WHERE mcuuid = ?");
				ps.setBoolean(1, status);
				ps.setString(2, uuid.toString());
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}