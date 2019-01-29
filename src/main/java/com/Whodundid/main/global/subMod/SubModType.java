package com.Whodundid.main.global.subMod;

//Last edited: Oct 15, 2018
//First Added: Oct 15, 2018
//Author: Hunter Bragg

public enum SubModType {
	AUTOCORRECT("CMD Autocorrect"),
	AUTOGM3("Auto /GM 3"),
	BLINK("Blinks"),
	ENHANCEDCHAT("Enhanced Chat"),
	CLEARVISUALS("Clear Visuals"),
	ALL("Global"),
	HOTKEYS("Hotkeys"),
	MINIMAP("Minimap"),
	MULTIHOTBAR("Multi Hotbar"),
	NAMEHISTORY("Name History"),
	PARKOUR("Parkour AI"),
	PING("Ping Display"),
	SCRIPTS("Scripts"),
	SLS("SLS"),
	WORLDEDITOR("World Editor");
	
	String modName = "";
	
	private SubModType(String modNameIn) {
		modName = modNameIn;
	}
	
	public static String getModName(SubModType modIn) { return modIn.modName; }
	
	public static SubModType getSubModFromString(String modIn) {
		try {
			return valueOf(modIn);
		} catch (IllegalArgumentException e) {}
		
		switch (modIn.toLowerCase()) {
		case "cmd autocorrect": return AUTOCORRECT;
		case "auto /gm 3": return AUTOGM3;
		case "blinks": return BLINK;
		case "enhanced chat": return ENHANCEDCHAT;
		case "clear visuals": return CLEARVISUALS;
		case "global": return ALL;
		case "hotkeys": return HOTKEYS;
		case "minimap": return MINIMAP;
		case "multi hotbar": return MULTIHOTBAR;
		case "name history": return NAMEHISTORY;
		case "parkour ai": return PARKOUR;
		case "ping display": return PING;
		case "scripts": return SCRIPTS;
		case "sls": return SLS;
		case "world editor": return WORLDEDITOR;
		default: return null;
		}
	}
}
