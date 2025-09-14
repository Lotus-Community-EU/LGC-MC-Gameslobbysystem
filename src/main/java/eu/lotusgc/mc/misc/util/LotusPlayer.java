//Created by Maurice H. at 26.01.2025
package eu.lotusgc.mc.misc.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import eu.lotusgc.mc.misc.MySQL;
import eu.lotusgc.mc.misc.ScoreboardState;

public class LotusPlayer {

	private String uuid, clan, name, nick, currentLastServer, language, playerGroup, customDateFormat, customTimeFormat,
			timeZone, countryCode;
	private int id, lgcid, playTime, bankMoney, pocketMoney, moneyInterestLevel, killedPlayers, killedEntities,
			gotKilledByPlayers, gotKilledByEntities;
	private long discordId, firstJoin, lastJoin;
	@SuppressWarnings("unused")
	private boolean isOnline, isStaff, isBanned, isMuted, allowTPA, allowMSG, existLGAccount, isLoggedIn;
	private ScoreboardState sbState;

	/**
	 * Constructor class
	 * 
	 * @param player - needed to get the correct data for according player.
	 */
	public LotusPlayer(Player player) {
		try (PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM mc_users WHERE mcuuid = ?")) {
			ps.setString(1, player.getUniqueId().toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				this.id = rs.getInt("id");
				this.uuid = rs.getString("mcuuid");
				this.clan = rs.getString("clan");
				this.name = rs.getString("name");
				this.nick = rs.getString("nick");
				this.currentLastServer = rs.getString("currentLastServer");
				this.language = rs.getString("language");
				this.playerGroup = rs.getString("playerGroup");
				this.customDateFormat = rs.getString("customDateFormat");
				this.customTimeFormat = rs.getString("customTimeFormat");
				this.timeZone = rs.getString("timeZone");
				this.countryCode = rs.getString("countryCode");
				this.lgcid = rs.getInt("lgcid");
				this.playTime = rs.getInt("playTime");
				this.bankMoney = rs.getInt("money_bank");
				this.pocketMoney = rs.getInt("money_pocket");
				this.moneyInterestLevel = rs.getInt("money_interestLevel");
				this.killedPlayers = rs.getInt("playerKillsPlayer");
				this.killedEntities = rs.getInt("playerKillsEntity");
				this.gotKilledByPlayers = rs.getInt("playerKilledByPlayer");
				this.gotKilledByEntities = rs.getInt("playerKilledByEntity");
				this.discordId = rs.getLong("discordId");
				this.firstJoin = rs.getLong("firstJoin");
				this.lastJoin = rs.getLong("lastJoin");
				this.isOnline = rs.getBoolean("isOnline");
				this.isStaff = rs.getBoolean("isStaff");
				this.allowTPA = rs.getBoolean("allowTPA");
				this.allowMSG = rs.getBoolean("allowMSG");
				this.isLoggedIn = rs.getBoolean("isLoggedIn");
				this.existLGAccount = true;
				int sbState = rs.getInt("scoreboardState");
				switch (sbState) {
					case 0:
						this.sbState = ScoreboardState.OFF;
						break;
					case 1:
						this.sbState = ScoreboardState.DEFAULT;
						break;
					case 2:
						this.sbState = ScoreboardState.JOB;
						break;
					case 3:
						this.sbState = ScoreboardState.REPORTS;
						break;
					case 4:
						this.sbState = ScoreboardState.SERVERSTATUS;
						break;
					case 5:
						this.sbState = ScoreboardState.RADIO;
						break;
					case 6:
						this.sbState = ScoreboardState.SERVER;
						break;
					case 7:
						this.sbState = ScoreboardState.WORLD;
						break;
					case 8:
						this.sbState = ScoreboardState.PLAYERS;
						break;
					case 9:
						this.sbState = ScoreboardState.ENTITIES;
						break;
					case 10:
						this.sbState = ScoreboardState.VOIP;
						break;
					case 11:
						this.sbState = ScoreboardState.SPOTIFY;
						break;
				}
			} else {
				this.existLGAccount = false;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the saved UUID for the player.
	 * 
	 * @return the UUID
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Gets the clan the player is in.
	 * 
	 * @return the Clan Name (or none if not set)
	 */
	public String getClan() {
		return clan;
	}

	/**
	 * Gets the player name.
	 * 
	 * @return the player name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the player nick.
	 * 
	 * @return the nickname (or none if not set)
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Gets the current / last Server the player is / was in.
	 * 
	 * @return the server name
	 */
	public String getCurrentLastServer() {
		return currentLastServer;
	}

	/**
	 * Gets the set language from the player.
	 * 
	 * @return the Language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Gets the primary role of the player.
	 * 
	 * @return the name of the role
	 */
	public String getPlayerGroup() {
		return playerGroup;
	}

	/**
	 * Gets the custom date Format.
	 * 
	 * @return the date format (Default: dd.MM.yyyy)
	 */
	public String getCustomDateFormat() {
		return customDateFormat;
	}

	/**
	 * Gets the custom time Format.
	 * 
	 * @return the time format (Default: HH:mm:ss)
	 */
	public String getCustomTimeFormat() {
		return customTimeFormat;
	}

	/**
	 * Gets the timezone of the player.
	 * 
	 * @return the timezone
	 */
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * Gets the 2 Digit Country Shortcode (e.g. AT, DE, CZ).
	 * 
	 * @return the country code
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * Gets the internal ID of the player.
	 * 
	 * @return the ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the random ID of a user (can be chosen).
	 * 
	 * @return the ID
	 */
	public int getLGCId() {
		return lgcid;
	}

	/**
	 * Gets the playtime in seconds.
	 * 
	 * @return the time played in seconds
	 */
	public int getPlayTime() {
		return playTime;
	}

	/**
	 * Gets the available Money from the Bank Account.
	 * 
	 * @return the money from bank account
	 */
	public int getBankMoney() {
		return bankMoney;
	}

	/**
	 * Gets the available Money from the Pocket "Account".
	 * 
	 * @return the money from pocket
	 */
	public int getPocketMoney() {
		return pocketMoney;
	}

	/**
	 * Gets the available Money total from the player.
	 * 
	 * @return the money (Bank & Pocket)
	 */
	public int getCombinedMoney() {
		return (bankMoney + pocketMoney);
	}

	/**
	 * Gets the Interest Level of the player.
	 * 
	 * @return the interest level
	 */
	public int getMoneyInterestLevel() {
		return moneyInterestLevel;
	}

	/**
	 * Returns how many players the player has killed.
	 * 
	 * @return the amount of killed players
	 */
	public int getKilledPlayers() {
		return killedPlayers;
	}

	/**
	 * Returns how many entities the player has killed.
	 * 
	 * @return the amount of killed entities
	 */
	public int getKilledEntities() {
		return killedEntities;
	}

	/**
	 * Returns how often the player got killed by players.
	 * 
	 * @return the amount of being killed by players
	 */
	public int getGotKilledByPlayers() {
		return gotKilledByPlayers;
	}

	/**
	 * Returns how many entities the player has killed.
	 * 
	 * @return the amount of being killed by entities
	 */
	public int getGotKilledByEntities() {
		return gotKilledByEntities;
	}

	/**
	 * Returns the Discord User Snowflake.
	 * 
	 * @return 0 or the discord user snowflake
	 */
	public long getDiscordId() {
		return discordId;
	}

	/**
	 * Returns the timestamp when the player joined first.
	 * 
	 * @return the timestamp of the first join
	 */
	public long getFirstJoin() {
		return firstJoin;
	}

	/**
	 * Returns the timestamp when the player joined last time.
	 * 
	 * @return the timestamp of the last join
	 */
	public long getLastJoin() {
		return lastJoin;
	}

	/**
	 * Returns whether the player is online or not.
	 * 
	 * @return whether the player is online or not
	 */
	public boolean isOnline() {
		return isOnline;
	}

	/**
	 * Returns whether the player is staff or not.
	 * 
	 * @return whether the player is staff or not
	 */
	public boolean isStaff() {
		return isStaff;
	}

	/**
	 * This method is currently always returning false as the internal Punishment
	 * System is not yet implemented.
	 * 
	 * @return whether the user is banned or not
	 */
	public boolean isBanned() {
		return false;
	}

	/**
	 * This method is currently always returning false as the internal Punishment
	 * System is not yet implemented.
	 * 
	 * @return whether the user is muted or not
	 */
	public boolean isMuted() {
		return false;
	}

	/**
	 * Returns whether the player allows TPA Requests or not.
	 * 
	 * @return if the player allows TPA requests or not
	 */
	public boolean isAllowTPA() {
		return allowTPA;
	}

	/**
	 * Returns whether the player allows private messages or not.
	 * 
	 * @return if the player allows private messages or not
	 */
	public boolean isAllowMSG() {
		return allowMSG;
	}

	/**
	 * Returns whether the user has played on the server already or not.
	 * 
	 * @return true if the player has already joined Lotus, false if not
	 */
	public boolean hasExistingLotusGamingAccount() {
		return existLGAccount;
	}

	/**
	 * Returns whether the player is logged in or not.
	 * 
	 * @return true if the player is logged in, false if not
	 */
	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	/**
	 * Returns the ScoreboardState of the player.
	 * 
	 * @return the ScoreboardState of the player
	 */
	public ScoreboardState getScoreboardState() {
		return sbState;
	}
}