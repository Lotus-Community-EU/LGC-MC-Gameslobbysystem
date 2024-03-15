package eu.lotusgc.mc.misc;

public enum Serverdata {
	CurrentPlayers("currentPlayers"),
	CurrentStaffs("currentStaffs"),
	MaxPlayers("maxPlayers"),
	OnlineStatus("isOnline"),
	LockedStatus("isLocked"),
	MonitoredStatus("isMonitored"),
	FancyName("displayname"),
	IsHybrid("isHybrid"),
	RequiredJoinlevel("req_joinlevel"),
	ServerPlayerCapacity("playerCapacity"),
	BungeeCordKey("bungeeKey"),
	IsStaff("isStaffOnly");
	
	public String databaseColName;
	
	Serverdata(String columnName){
		this.databaseColName = columnName;
	}
	
	public String getColumnName() {
		return databaseColName;
	}
}
