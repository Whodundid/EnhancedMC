package com.Whodundid.hotkeys.control.hotKeyTypes;

import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;

//Last edited: 9-30-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class CommandHotKey extends HotKey {
	
	public String command = "/";
	
	public CommandHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn) { this(keyNameIn, keysIn, commandIn, false, "", null); }
	public CommandHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn, boolean builtInVal) { this(keyNameIn, keysIn, commandIn, builtInVal, "", null); }
	public CommandHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn, String descriptionIn) { this(keyNameIn, keysIn, commandIn, false, descriptionIn, null); }
	public CommandHotKey(String keyNameIn, KeyComboAction keysIn, String commandIn, boolean builtInVal, String descriptionIn, String builtInSubModTypeIn) {
		super(keyNameIn, keysIn, builtInVal, KeyActionType.COMMAND, builtInSubModTypeIn);
		if (descriptionIn != null && !descriptionIn.isEmpty()) { description = descriptionIn; }
		command = commandIn;
	}
	
	public String getCommand() { return command; }
	
	@Override public void executeHotKeyAction() {
		if (mc.thePlayer != null) { mc.thePlayer.sendChatMessage(command); }
	}
	
	@Override public String getHotKeyStatistics() {
		String base = super.getHotKeyStatistics();
		base += ("; " + command);
		return base;
	}
}
