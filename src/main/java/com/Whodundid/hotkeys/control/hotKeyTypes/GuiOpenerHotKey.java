package com.Whodundid.hotkeys.control.hotKeyTypes;

import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import net.minecraft.client.gui.GuiScreen;

//Last edited: 9-30-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class GuiOpenerHotKey extends HotKey {
	
	public Class gui;
	
	public GuiOpenerHotKey(String keyNameIn, KeyComboAction keysIn, Class guiIn) { this(keyNameIn, keysIn, guiIn, false, "", null); }
	public GuiOpenerHotKey(String keyNameIn, KeyComboAction keysIn, Class guiIn, boolean builtInVal) { this(keyNameIn, keysIn, guiIn, false, "", null); }
	public GuiOpenerHotKey(String keyNameIn, KeyComboAction keysIn, Class guiIn, String descriptionIn) { this(keyNameIn, keysIn, guiIn, false, descriptionIn, null); }
	public GuiOpenerHotKey(String keyNameIn, KeyComboAction keysIn, Class guiIn, boolean builtInVal, String descriptionIn, String builtInSubModTypeIn) {
		super(keyNameIn, keysIn, builtInVal, KeyActionType.GUI_OPENER, builtInSubModTypeIn);
		if (descriptionIn != null && !descriptionIn.isEmpty()) { description = descriptionIn; }
		gui = guiIn;
	}
	
	public String getGui() { return gui.getName(); }
	public String getGuiDisplayName() { return gui.getSimpleName(); }
	
	@Override public void executeHotKeyAction() {
		try { mc.displayGuiScreen((GuiScreen) Class.forName(gui.getName()).getConstructor().newInstance()); } catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override public String getHotKeyStatistics() {
		String base = super.getHotKeyStatistics();
		base += ("; " + gui.getSimpleName());
		return base;
	}
}
