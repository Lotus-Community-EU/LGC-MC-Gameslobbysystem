package eu.lotusgc.mc.misc;

public enum InputType {
	
	Servername("servername"),
	BungeeKey("bungeeKey"),
	ServerID("serverid");
	
public String databaseColName;
	
	InputType(String columnName){
		this.databaseColName = columnName;
	}
	
	public String getColumnName() {
		return databaseColName;
	}

}
