package com.Whodundid.enhancedChat;

import com.Whodundid.enhancedChat.chatUtil.ChatType;
import com.Whodundid.enhancedChat.chatUtil.TimedChatLine;
import com.Whodundid.main.EnhancedInGameGui;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.util.storageUtil.EArrayList;
import net.minecraft.client.Minecraft;

//Dec 31, 2018
//Jan 13, 2019
//Last edited: Jan 20, 2019
//First Added: Apr 14, 2017
//Author: Hunter Bragg

public class ChatOrganizer {
	
	protected Minecraft mc = MainMod.getMC();
	protected EnhancedChat mod;
	EnhancedInGameGui hudInstance;
	int chatHistoryLength = 1000;
	public EArrayList<TimedChatLine> allHistory = new EArrayList();
	public EArrayList<TimedChatLine> gameHistory = new EArrayList();
    public EArrayList<TimedChatLine> guildHistory = new EArrayList();
    public EArrayList<TimedChatLine> partyHistory = new EArrayList();
    public EArrayList<TimedChatLine> privateHistory = new EArrayList();
    public EArrayList<TimedChatLine> staffHistory = new EArrayList();
    public EArrayList<TimedChatLine> lobbyHistory = new EArrayList();
    public EArrayList<TimedChatLine> partyInvHistory = new EArrayList();
    public EArrayList<TimedChatLine> upperList = new EArrayList();
    protected EArrayList<String> gameKeyWords = new EArrayList();
	protected EArrayList<String> trashKeyWords = new EArrayList();
    String oldMessage = "", lastChat = "";
	
	public ChatOrganizer(EnhancedChat modIn) {
		mod = modIn;
		
		gameKeyWords.add("Guild Name:", "Total Members:", "Online Members:", "https://store.hypixel.net", "/watchdogreport", "Total Earned Coins:", "Total Earned Experience:");
		gameKeyWords.add("Quakecraft Coins", "Hypixel Experience", "Rate this map by clicking:", "Automatically activated:", "Daily Quest:", "Hotkey Mode:");
		gameKeyWords.add("Click the link to visit our website and claim your reward:", "CHALLENCGE IN:", "Current Tool:", "Brush Type:", "Performer:", "Replace Material: ");
		gameKeyWords.add("Voxel:", "Water Mode:", "Brush Size:", "Currently selected biome type:", "Also join our discord server!", "Get deals and news sent to your email!");
		gameKeyWords.add("See all the posts shared by Hypixel on Facebook!", "That warp does not exist.", "Be the first to watch Hypixel YouTube videos!");
		gameKeyWords.add("If you happen to come across any bugs make sure you report them at", "Keep up with the latest from Hypixel on Twitter!", "The time was set to ");
		gameKeyWords.add("Now playing:", "Unknown direction:", "Usage:", "CHALLENGE IN:", "§r§6MVP§r§c++§r§6 §r§ahas access to the following emotes in chat:§r");
		gameKeyWords.add(":star:", ":yes:", ":no:", ":java:", ":arrow:", ":shrug:", ":tableflip:", ":123:", ":totem:", ":typing:", ":maths:", ":snail:", ":thinking:");
		gameKeyWords.add("The entity UUID provided is in an invalid format]", ": Teleported", ": Saved the world]", ": Set own game mode to ");
		gameKeyWords.add("For parameter 'to': Invalid block '", "Network Boosters are available at ", "http://store.hypixel.net/");
		gameKeyWords.add("Total Earned Guild Experience: ", "[@: Set the time to ", "[@: Toggled downfall]", "Command set: ", "§r§6Script: ");
		
		trashKeyWords.add("Ping:", "Miss Rate:", "Damage Rate:", "Selected Kit:", "HP:", "You received:", "Murderer Chance:", "Detective:", "Hero:");
		trashKeyWords.add("Achievement Unlocked:", "Winner:", "Murderer:", "You picked up the Bow!", "To leave, type", "Cages open in:");
	}
	
	public void readChat(TimedChatLine e) {
		addTo(ChatType.ALL, e);
		
		if (mod.getEnableChatOrganizer()) {
			String message = e.getChatComponent().getFormattedText();
			if (!checkListForMessage(trashKeyWords, e)) {
				if (message.contains("Guild >")) { addTo(ChatType.GUILD, e); }
				else if (message.contains("Party >")) { addTo(ChatType.PARTY, e); }
				else if (message.contains("From ") || message.contains("To ")) { addTo(ChatType.PRIVATE, e); }
				else if (checkListForMessage(gameKeyWords, e)) { addTo(ChatType.GAME, e); }
				else if (message.contains(":")) { addTo(ChatType.LOBBY, e); }
				else { addTo(ChatType.GAME, e); }
			}
		}
		
		/*if (oldMessage == null || !(message.equals(oldMessage))) {
    		oldMessage = message;
        	if (!Minecraft.getMinecraft().isSingleplayer()) {
        		String ip = Minecraft.getMinecraft().getCurrentServerData().serverIP;
        		if (ip.equals("mc.hypixel.net")) {
        			
        		}      		
        	}
		}
		*/
	}
	
	public EArrayList<TimedChatLine> getChat(ChatType type) {
		switch (type) {
		case ALL: return allHistory;
		case GAME: return gameHistory;
		case GUILD: return guildHistory;
		case PARTY: return partyHistory;
		case PRIVATE: return privateHistory;
		case STAFF: return staffHistory;
		case LOBBY: return lobbyHistory;
		case PARTYINV: return partyInvHistory;
		default: return null;
		}
	}
	
	public void addTo(ChatType type, TimedChatLine line) {
		EArrayList<TimedChatLine> list = getChat(type);
		if (list != null) {
			if (list.size() > chatHistoryLength) { list.remove(list.size() - 1); }
	    	list.add(0, line);
	    	hudInstance.sendChatToCorrectWindow(type, line);
		}
    }
	
	protected boolean checkListForMessage(EArrayList<String> checkList, TimedChatLine messageIn) {
		String message = messageIn.getChatComponent().getUnformattedText();
		for (String keyWord : checkList) {
			if (message.contains(keyWord)) { return true; }
		}
		return false;
	}
	
	public void printOutContents() {
		allHistory.clear();
		lobbyHistory.clear();
    	System.out.println("-------------- GUILD --------------");
    	for (int i = 0; i < getChat(ChatType.GUILD).size(); i++)
    		System.out.println(getChat(ChatType.GUILD).get(i));
    	System.out.println("-------------- PARTY --------------");
    	for (int i = 0; i < getChat(ChatType.PARTY).size(); i++)
    		System.out.println(getChat(ChatType.PARTY).get(i));
    	System.out.println("------------- PRIVATE ------------");
    	for (int i = 0; i < getChat(ChatType.PRIVATE).size(); i++)
    		System.out.println(getChat(ChatType.PRIVATE).get(i));
    	System.out.println("-------------- LOBBY -------------");
    	for (int i = 0; i < getChat(ChatType.LOBBY).size(); i++)
    		System.out.println(getChat(ChatType.LOBBY).get(i));
    	System.out.println("--------------- END --------------");
    }
	
	public void setDependencies() { hudInstance = MainMod.getInGameGui(); }
}
