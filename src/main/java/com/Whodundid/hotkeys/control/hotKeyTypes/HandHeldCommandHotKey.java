package com.Whodundid.hotkeys.control.hotKeyTypes;

import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import com.Whodundid.main.util.playerUtil.PlayerTraits;

//Last edited: Sep 30, 2018
//First Added: Sep 25, 2018
//Author: Hunter Bragg

public class HandHeldCommandHotKey extends HotKey {
	
	String command = "";
	int itemID = -1;
	
	public HandHeldCommandHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn, int checkItemIDIn) { this(keyNameIn, keysIn, commandIn, checkItemIDIn, false, "", null); }
	public HandHeldCommandHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn, int checkItemIDIn, boolean builtInVal) { this(keyNameIn, keysIn, commandIn, checkItemIDIn, builtInVal, "", null); }
	public HandHeldCommandHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn, int checkItemIDIn, String descriptionIn) { this(keyNameIn, keysIn, commandIn, checkItemIDIn, false, descriptionIn, null); }
	public HandHeldCommandHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn, int checkItemIDIn, boolean builtInVal, String descriptionIn, String builtInSubModTypeIn) {
		super(keyNameIn, keysIn, builtInVal, KeyActionType.COMMAND_PLAYER_HANDHELD_CONDITION, builtInSubModTypeIn);
		if (descriptionIn != null && !descriptionIn.isEmpty()) { description = descriptionIn; }
		command = commandIn;
		itemID = checkItemIDIn;
	}
	
	public String getCommand() { return command; }
	public int getItemID() { return itemID; }
	
	@Override public void executeHotKeyAction() {
		if (mc.thePlayer != null && PlayerTraits.isHoldingItem() && PlayerTraits.getHeldItemId() == itemID) { mc.thePlayer.sendChatMessage(command); }
	}
	
	@Override public String getHotKeyStatistics() {
		String base = super.getHotKeyStatistics();
		base += ("; " + command);
		base += ("; " + itemID);
		return base;
	}
}
