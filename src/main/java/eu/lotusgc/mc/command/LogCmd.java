package eu.lotusgc.mc.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.lotusgc.mc.main.Main;
import eu.lotusgc.mc.misc.LotusController;
import eu.lotusgc.mc.misc.MySQL;

public class LogCmd implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Main.consoleSend);
        }else {
            Player player = (Player) sender;
            LotusController lc = new LotusController();
            if(command.getName().equalsIgnoreCase("login")){
                if(player.hasPermission("lgc.uselogcmd")){
                    if(getLogState(player)){
                        //player.sendMessage(Main.prefix + "§cYou are already logged in!");
                        lc.sendMessageReady(player, "cmd.login.already");
                    }else {
                        setLogState(player, true);
                        //player.sendMessage(Main.prefix + "§aYou are now logged out!");
                        lc.sendMessageReady(player, "cmd.login.now");
                    }
                }else {
                    lc.noPerm(player, "lgc.uselogcmd");
                }
            }else if(command.getName().equalsIgnoreCase("logout")){
                if(player.hasPermission("lgc.uselogcmd")){
                    if(getLogState(player)){
                        setLogState(player, false);
                        //player.sendMessage(Main.prefix + "§aYou are now logged out!");
                        lc.sendMessageReady(player, "cmd.logout.now");
                    }else {
                        //player.sendMessage(Main.prefix + "§cYou are already logged out!");
                        lc.sendMessageReady(player, "cmd.logout.already");
                    }
                }else {
                    lc.noPerm(player, "lgc.uselogcmd");
                }
            }
        }
        return true;
    }

    private void setLogState(Player player, boolean state){
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE mc_users SET isLoggedIn = ? WHERE mcuuid = ?");
            ps.setBoolean(1, state);
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean getLogState(Player player){
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT isLoggedIn FROM mc_users WHERE mcuuid = ?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if(!rs.next()) return false;
            return rs.getBoolean("isLoggedIn");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}