package com.Whodundid.hotkeys.keySaveLoad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import com.Whodundid.hotkeys.HotKeyManager;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.CommandHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.DebugHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.GuiOpenerHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.HandHeldCommandHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.KeyBindModifierHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ModActivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ModDeactivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ScriptHotKey;
import com.Whodundid.main.MainMod;

import net.minecraft.client.Minecraft;

//Last edited: Sep 30, 2018
//First Added: Sep 25, 2018
//Author: Hunter Bragg

public class KeySaver {
	
	static Minecraft mc = MainMod.getMC();
	HotKeyManager man;
	private PrintWriter saver = null;
	
	public KeySaver(HotKeyManager manIn) {
		man = manIn;
	}
	
	public void saveKeysToFile() {
		try {
			File hotKeyFolder = new File("EnhancedMC/HotKeys/");
			
			if (!hotKeyFolder.exists()) { hotKeyFolder.mkdirs(); }
			
			saveBuiltInKeys();
			saveUserMadeKeys();
			
		} catch (IOException e) { e.printStackTrace(); }
		finally {
			if (saver != null) { saver.close(); }
		}
	}
	
	public void saveBuiltInKeys() throws FileNotFoundException, UnsupportedEncodingException {
		saver = new PrintWriter("EnhancedMC/HotKeys/BuiltIn.cfg", "UTF-8");
		
		saver.println("** Built-In Hot Keys");
		saver.println("** Key, keyCodes, isEnabled");
		saver.println();
		
		String previousType = "";
		
		if (man.getBuiltInHotKeys() != null && !man.getBuiltInHotKeys().isEmpty() && man.getBuiltInHotKeys().get(0) != null) {
			previousType = man.getBuiltInHotKeys().get(0).getBuiltInSubModType();
			saver.println("** " + previousType + " **");
			for (HotKey k : man.getBuiltInHotKeys()) {
				if (!k.getBuiltInSubModType().equals(previousType)) {
					previousType = k.getBuiltInSubModType();
					saver.println();
					saver.println("** " + previousType + " **");
				}
				
				saver.print(k.getKeyName() + " ");
				for (int i = 0, size = k.getKeyCombo().getKeys().size(); i < size; i++) {
					int code = k.getKeyCombo().getKeys().get(i);
					saver.print((i == size - 1) ? code : code + ",");
				}
				saver.print(" " + k.isEnabled());
				saver.println();
			}
		}
		
		saver.print("END");
		if (saver != null) { saver.close(); }
	}
	
	public void saveUserMadeKeys() throws FileNotFoundException, UnsupportedEncodingException {
		saver = new PrintWriter("EnhancedMC/HotKeys/UserMade" + ".cfg", "UTF-8");
		
		saver.println("** User-Made Hot Keys");
		saver.println();
		
		if (man.getUserMadeHotKeys() != null && !man.getUserMadeHotKeys().isEmpty() && man.getUserMadeHotKeys().get(0) != null) {
			for (HotKey k : man.getUserMadeHotKeys()) {
				saver.println("KEYDEF " + k.getKeyName());
				saver.println("KEYTYPE " + k.getHotKeyType());
				
				saver.print("KEYS ");
				for (int i = 0; i < k.getKeyCombo().getKeys().size(); i++) {
					int key = k.getKeyCombo().getKeys().get(i);
					saver.print((i == k.getKeyCombo().getKeys().size() - 1) ? key : key + ",");
				}
				saver.println();
				
				saver.println("KEYDESC " + k.getKeyDescription());
				saver.println("KEYENABLED " + k.isEnabled());
				
				switch (k.getHotKeyType()) {
				case COMMAND:
					saver.println("COMMAND " + ((CommandHotKey)k).getCommand());
					break;
				case COMMAND_PLAYER_HANDHELD_CONDITION:
					saver.println("COMMAND " + ((HandHeldCommandHotKey)k).getCommand());
					saver.println("ITEM " + ((HandHeldCommandHotKey)k).getItemID());
					break;
				case DEBUG:
					saver.println("DEBUGFUNC " + ((DebugHotKey)k).getDebugFunction());
					break;
				case GUI_OPENER:
					saver.println("GUI " + ((GuiOpenerHotKey)k).getGui());
					break;
				case MC_KEYBIND_MODIFIER:
					saver.println("KEYBIND " + ((KeyBindModifierHotKey)k).getKeyBinding().getKeyDescription() + " " + ((KeyBindModifierHotKey)k).getNewVal());
					break;
				case MOD_ACTIVATOR:
					saver.println("MOD " + ((ModActivatorHotKey)k).getSubMod());
					break;
				case MOD_DEACTIVATOR:
					saver.println("MOD " + ((ModDeactivatorHotKey)k).getSubMod());
					break;
				case SCRIPT:
					saver.println("SCRIPT " + ((ScriptHotKey)k).getScript().getScriptName());
					saver.print("SCRIPTARGS ");
					for (int i = 0; i < ((ScriptHotKey)k).getScriptArgs().length; i++) {
						String arg = ((ScriptHotKey)k).getScriptArgs()[i];
						saver.print((i == ((ScriptHotKey)k).getScriptArgs().length - 1) ? arg : arg + ",");
					}
					saver.println();
					break;
				default: break;
				}
				saver.println("KEYDEFEND");
				saver.println();
			}
		}
		
		saver.print("END");
		if (saver != null) { saver.close(); }
	}
}
