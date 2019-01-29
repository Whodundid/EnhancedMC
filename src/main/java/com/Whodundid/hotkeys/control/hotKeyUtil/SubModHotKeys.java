package com.Whodundid.hotkeys.control.hotKeyUtil;

import com.Whodundid.hotkeys.HotKeyManager;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.main.MainMod;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;

//Last edited: Sep 30, 2018
//First Added: Sep 25, 2018
//Author: Hunter Bragg

public abstract class SubModHotKeys {
	
	protected static Minecraft mc = MainMod.getMC();
	protected String subModName = "";
	protected ArrayList<HotKey> keys = new ArrayList();
	protected HotKeyManager man;
	
	public void registerKeys() {
		for (HotKey k : keys) { man.registerHotKey(k); }
	}
	
	public SubModHotKeys rebuildKeys() {
		keys.clear();
		addKeys();
		return this;
	}
	
	public ArrayList<HotKey> getKeys() { return keys; }
	
	protected abstract void addKeys();
}
