package com.Whodundid.enhancedChat.chatUtil;

//Last edited: 11-18-17
//First Added: 4-14-17
//Author: Hunter Bragg

public enum ChatType {
	ALL("All", 0xFFAA88),
	GAME("Game", 0xFFAA00),
	GUILD("Guild", 0x00AA00),
	PARTY("Party", 0x5555FF),
	PRIVATE("Private", 0xFFAA00),	
	STAFF("Staff", 0xFF5555),	
	LOBBY("Lobby", 0x55FF55),
	NONE("None", 0xFFFFFF),
	PARTYINV("PartyInv", 0xFFFFFF);
	
	private final String type;
	private final int color;
	
	private ChatType(String typeIn, int displayColorIn) {
		type = typeIn;
		color = displayColorIn;
	}
	
	public String getChatType() { return type; }
	public int getChatDisplayColor() { return color; }
	public boolean isNone() { return type.equals("None");  }
	
	public static String getDisplayName(ChatType typeIn) { return typeIn.getChatType(); }
	public static int getDisplayColor(ChatType typeIn) { return typeIn.getChatDisplayColor(); }
}