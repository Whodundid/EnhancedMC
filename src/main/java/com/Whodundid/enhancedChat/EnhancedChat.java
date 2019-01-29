package com.Whodundid.enhancedChat;

import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.storageUtil.StorageBox;

//Dec 31, 2018
//Jan 13, 2019
//Last edited: Jan 20, 2019
//First Added: Dec 31, 2018
//Author: Hunter Bragg

public class EnhancedChat extends SubMod {
	
	private final ChatOrganizer organizer;
	protected boolean showTimeStamps = true;
	protected boolean showMoreChatInfo = true;
	protected boolean enableChatWindows = true;
	protected boolean enableChatOrganizer = true;
	
	public EnhancedChat() {
		super(SubModType.ENHANCEDCHAT);
		dependencies.add(SubModType.HOTKEYS);
		organizer = new ChatOrganizer(this);
	}
	
	@Override
	public void postInit() {
		organizer.setDependencies();
	}
	
	@Override
	public EnhancedGui getMainGui(boolean setPosition, StorageBox<Integer, Integer> pos, EnhancedGui oldGui) {
		if (oldGui != null) { return setPosition ? new EnhancedChatGui(pos.getObject(), pos.getValue(), oldGui) : new EnhancedChatGui(oldGui); }
		return setPosition ? new EnhancedChatGui(pos.getObject(), pos.getValue()) : new EnhancedChatGui();
	}
	
	@Override
	public EnhancedChat setEnabled(boolean valueIn) {
		super.setEnabled(valueIn);
		if (!isEnabled()) { MainMod.getInGameGui().removeAllChatWindows(); }
		return this;
	}
	
	public ChatOrganizer getChatOrganizer() { return organizer; }
	
	public boolean getShowTimeStamps() { return showTimeStamps; }
	public boolean getShowMoreChatInfo() { return showMoreChatInfo; }
	public boolean getEnableChatWindows() { return enableChatWindows; }
	public boolean getEnableChatOrganizer() { return enableChatOrganizer; }
	public EnhancedChat setShowTimeStamps(boolean val) { showTimeStamps = val; return this; }
	public EnhancedChat setShowMoreChatInfo(boolean val) { showMoreChatInfo = val; return this; }
	public EnhancedChat setShowChatWindows(boolean val) { enableChatWindows = val; return this; }
	public EnhancedChat setEnableChatOrganizer(boolean val) { enableChatOrganizer = val; return this; }
}
