//Created by Maurice H. at 02.09.2024
package eu.lotusgc.mc.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.lotusgc.mc.main.Main;
import eu.lotusgc.mc.misc.LotusController;
import eu.lotusgc.mc.misc.Money;
import eu.lotusgc.mc.misc.MySQL;
import eu.lotusgc.mc.misc.Playerdata;
import eu.lotusgc.mc.misc.Prefix;

public class BankCommands implements CommandExecutor{
	
	/*
	 * Consists of following commands:
	 *  /pay <Player> <Amount>
	 *  /bankdeposit <Amount>
	 *  /bankwithdraw <Amount>
	 *  /topbal(ance) <Bank|Pocket|Mixed> | Top 5 of each rank (if not specified, Top 3 of each)
	 *  
	 *  Admin:
	 *  /setbankmoney <Player> <Amount>
	 *  /setpocketmoney <Player> <Amount>
	 *  /recovereconomy <Id> | if /reseteconomy is forced, the ID of it will be taken to recover it.
	 *  /addbankmoney <Player> <Amount>
	 *  /addpocketmoney <Player> <Amount>
	 *  /removebankmoney <Player> <Amount> | Cannot go into negative
	 *  /removepocketmoney <Player> <Amount> | Cannot go into negative
	 *  /reseteconomy <Player> | Resets the economy of specified player (Only * Permission)
	 */
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Main.consoleSend);
		}else {
			Player player = (Player)sender;
			LotusController lc = new LotusController();
			if(command.getName().equals("bankdeposit")) {
				if(args.length == 1) {
					if(args[0].matches("^[0-9]+$")) {
						int amount = Integer.parseInt(args[0]);
						if(lc.hasEnoughFunds(player, amount, Money.POCKET)) {
                            lc.removeMoney(player, amount, Money.POCKET);
                            lc.addMoney(player, amount, Money.BANK);
                            player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "command.bankdeposit.success").replace("%amount%", String.valueOf(amount)));
						}else {
							lc.sendMessageReady(player, "command.bankdeposit.not_enough_funds");
						}
					}else {
						lc.sendMessageReady(player, "command.bankdeposit.invalid_characters");
					}
				}else {
					player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "global.args") + "§7/bankdeposit <Amount>");
				}
			}else if(command.getName().equals("bankwithdraw")) {
                if(args.length == 1) {
                    if(args[0].matches("^[0-9]+$")) {
                        int amount = Integer.parseInt(args[0]);
                        if(lc.hasEnoughFunds(player, amount, Money.BANK)) {
                            lc.removeMoney(player, amount, Money.BANK);
                            lc.addMoney(player, amount, Money.POCKET);
                            player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "command.bankwithdraw.success").replace("%amount%", String.valueOf(amount)));
                        }else {
                            lc.sendMessageReady(player, "command.bankwithdraw.not_enough_funds");
                        }
                    }else {
                        lc.sendMessageReady(player, "command.bankwithdraw.invalid_characters");
                    }
                }else {
                    player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "global.args") + "§7/bankwithdraw <Amount>");
                }
			}else if(command.getName().equals("pay")) {
                if(args.length == 2) {
                    if(args[1].matches("^[0-9]+$")) {
                        int amount = Integer.parseInt(args[1]);
                        if(lc.hasEnoughFunds(player, amount, Money.POCKET)) {
                            Player target = Bukkit.getPlayerExact(args[0]);
                            if(target != null) {
                                lc.removeMoney(player, amount, Money.POCKET);
                                lc.addMoney(target, amount, Money.POCKET);
                                player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "command.pay.success").replace("%amount%", String.valueOf(amount)).replace("%displayer%", target.getName()));
                                target.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(target, "command.pay.received").replace("%amount%", String.valueOf(amount)).replace("%displayer%", player.getName()));
                            }else {
                                lc.sendMessageReady(player, "global.playerOffline");
                            }
                        }else {
                            lc.sendMessageReady(player, "command.pay.not_enough_funds");
                        }
                    }else {
                        lc.sendMessageReady(player, "command.pay.invalid_characters");
                    }
                }else {
                    player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "global.args") + "§7/pay <Player> <Amount>");
                }
			}else if(command.getName().equals("topbal")) {
				if(args.length == 1) {
					String arg = args[0];
					if(arg.equalsIgnoreCase("bank")) {
						try {
							PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT money_bank,name FROM mc_users ORDER BY money_bank DESC LIMIT 10");
							ResultSet rs = ps.executeQuery();
							int i = 0;
							player.sendMessage("§7----------[§6Top Balance §aBank§7]----------");
							while(rs.next()) {
								i++;
								switch(i) {
								case 1: player.sendMessage("§7" + i + ". §a" + rs.getString("name") + " §7- §c" + rs.getInt("money_bank") + " §7Loti"); break;
								case 2: player.sendMessage("§7" + i + ". §a" + rs.getString("name") + " §7- §e" + rs.getInt("money_bank") + " §7Loti"); break;
								case 3: player.sendMessage("§7" + i + ". §a" + rs.getString("name") + " §7- §a" + rs.getInt("money_bank") + " §7Loti"); break;
								default: player.sendMessage("§7" + i + ". §a" + rs.getString("name") + " §7- §2" + rs.getInt("money_bank") + " §7Loti"); break;
								}
							}
							rs.close();
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}else if(arg.equalsIgnoreCase("pocket")) {
						try {
							PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT money_pocket,name FROM mc_users ORDER BY money_pocket DESC LIMIT 10");
							ResultSet rs = ps.executeQuery();
							int i = 0;
							player.sendMessage("§7----------[§6Top Balance §9Pocket§7]----------");
							while(rs.next()) {
								i++;
								switch(i) {
								case 1: player.sendMessage("§7" + i + ". §a" + rs.getString("name") + " §7- §c" + rs.getInt("money_pocket") + " §7Loti"); break;
								case 2: player.sendMessage("§7" + i + ". §a" + rs.getString("name") + " §7- §e" + rs.getInt("money_pocket") + " §7Loti"); break;
								case 3: player.sendMessage("§7" + i + ". §a" + rs.getString("name") + " §7- §a" + rs.getInt("money_pocket") + " §7Loti"); break;
								default: player.sendMessage("§7" + i + ". §a" + rs.getString("name") + " §7- §2" + rs.getInt("money_pocket") + " §7Loti"); break;
								}
							}
							rs.close();
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}else if(arg.equalsIgnoreCase("mixed")) {
						try {
							PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT money_pocket,money_bank,name FROM mc_users ORDER BY (money_pocket+money_bank) DESC LIMIT 10");
							ResultSet rs = ps.executeQuery();
							int i = 0;
							player.sendMessage("§7----------[§6Top Balance §eCombined§7]----------");
							while(rs.next()) {
								i++;
								switch(i) {
								case 1: player.sendMessage("§7" + i + ". §a" + rs.getString("name") + " §7- §c" + (rs.getInt("money_pocket") + rs.getInt("money_bank")) + " §7Loti"); break;
								case 2: player.sendMessage("§7" + i + ". §a" + rs.getString("name") + " §7- §e" + (rs.getInt("money_pocket") + rs.getInt("money_bank")) + " §7Loti"); break;
								case 3: player.sendMessage("§7" + i + ". §a" + rs.getString("name") + " §7- §a" + (rs.getInt("money_pocket") + rs.getInt("money_bank")) + " §7Loti"); break;
								default: player.sendMessage("§7" + i + ". §a" + rs.getString("name") + " §7- §2" + (rs.getInt("money_pocket") + rs.getInt("money_bank")) + " §7Loti"); break;
								}
							}
							rs.close();
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}else {
						player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "global.args") + "§7/topbal <Bank|Pocket|Mixed>");
					}
				}else {
					player.sendMessage(lc.getPrefix(Prefix.MAIN) + lc.sendMessageToFormat(player, "global.args") + "§7/topbal <Bank|Pocket|Mixed>");
				}
			}else if(command.getName().equals("money")) {
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + "§7Pocket: §a" + lc.getPlayerData(player, Playerdata.MoneyPocket) + " Loti");
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + "§7Bank: §a" + lc.getPlayerData(player, Playerdata.MoneyBank) + " Loti");
				player.sendMessage(lc.getPrefix(Prefix.MAIN) + "§7Interest Level: §a" + lc.getPlayerData(player, Playerdata.MoneyInterestLevel));
			}
		}
		return true;
	}
}