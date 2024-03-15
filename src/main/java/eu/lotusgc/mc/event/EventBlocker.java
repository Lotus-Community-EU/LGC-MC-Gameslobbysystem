//Created by Chris Wille at 15.03.2024
package eu.lotusgc.mc.event;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import eu.lotusgc.mc.misc.LotusController;

public class EventBlocker implements Listener{
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		List<DamageCause> causes = new ArrayList<>();
		causes.add(DamageCause.ENTITY_ATTACK);
		causes.add(DamageCause.FALL);
		causes.add(DamageCause.CONTACT);
		causes.add(DamageCause.LAVA);
		causes.add(DamageCause.DROWNING);
		causes.add(DamageCause.FIRE);
		causes.add(DamageCause.WITHER);
		causes.add(DamageCause.HOT_FLOOR);
		causes.add(DamageCause.FREEZE);
		
		Entity entity = event.getEntity();
		if(entity instanceof Player) {
			if(causes.contains(event.getCause())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onHunger(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
    	Player p = e.getPlayer();
    	String message = e.getMessage();
    	LotusController lc = new LotusController();
    	if(message.equalsIgnoreCase("/pl") || message.equalsIgnoreCase("/plugins") || message.equalsIgnoreCase("/bukkit:pl") || message.equalsIgnoreCase("/bukkit:plugins")) {
    		if(p.hasPermission("*")) {
    			e.setCancelled(false);
    		}else {
    			e.setCancelled(true);
    			lc.noPerm(p, "bukkit.command.plugins");
    		}
    	}else if(message.equalsIgnoreCase("/help") || message.equalsIgnoreCase("/?") || message.equalsIgnoreCase("/bukkit:?") || message.equalsIgnoreCase("/bukkit:help") || message.equalsIgnoreCase("/minecraft:help")) {
    		if(p.hasPermission("*")) {
    			e.setCancelled(false);
    		}else {
    			e.setCancelled(true);
    			lc.noPerm(p, "bukkit.command.help");
    		}
    	}else if(message.equalsIgnoreCase("/icanhasbukkit") || message.equalsIgnoreCase("/bukkit:about") || message.equalsIgnoreCase("/about") || message.equalsIgnoreCase("/bukkit:ver") || message.equalsIgnoreCase("/bukkit:version")) {
    		if(p.hasPermission("*")) {
    			e.setCancelled(false);
    		}else {
    			e.setCancelled(true);
    			lc.noPerm(p, "bukkit.command.version");
    		}
    	}
    }

}

