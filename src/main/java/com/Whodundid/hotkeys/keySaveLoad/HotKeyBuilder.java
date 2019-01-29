package com.Whodundid.hotkeys.keySaveLoad;

import com.Whodundid.debug.IDebugCommand;
import com.Whodundid.hotkeys.HotKeyManager;
import com.Whodundid.hotkeys.control.*;
import com.Whodundid.hotkeys.control.hotKeyTypes.*;
import com.Whodundid.hotkeys.control.hotKeyUtil.*;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.scripts.scriptBase.Script;
import net.minecraft.client.settings.KeyBinding;

//Last edited: Sep 30, 2018
//First Added: Sep 30, 2018
//Author: Hunter Bragg

public class HotKeyBuilder {
	
	HotKeyManager man;
	HotKey createdKey = null;
	KeyComboAction keys;
	String command;
	int testHeldItemId = -1;
	IDebugCommand debugCommand;
	Class gui;
	Script script;
	String[] scriptArgs;
	KeyBinding keyBind;
	boolean val;
	SubModType mod;
	String builderErrorMessage = "";
	
	public HotKeyBuilder(HotKeyManager manIn) {
		man = manIn;
	}
	
	public void setBuilderKeys(KeyComboAction keysIn) { keys = keysIn; }
	public void setBuilderCommand(String commandIn) { command = commandIn; }
	public void setBuilderCommandAndItemArgs(String commandIn, int id) { command = commandIn; testHeldItemId = id; }
	public void setBuilderDebugCommand(IDebugCommand commandIn) { debugCommand = commandIn; }
	public void setBuilderGuiToBeOpened(Class guiClassIn) { gui = guiClassIn; }
	public void setBuilderScriptToBeRun(Script scriptIn, String[] scriptArgsIn) { script = scriptIn; scriptArgs = scriptArgsIn; }
	public void setBuilderKeyBindingIn(KeyBinding keyBindingIn, boolean newVal) { keyBind = keyBindingIn; val = newVal;}
	public void setBuilderSubMod(SubModType modIn) { mod = modIn; }
	
	public void clearBuilderArgs() {
		keys = null;
		command = null;
		testHeldItemId = -1;
		debugCommand = null;
		gui = null;
		script = null;
		scriptArgs = null;
		keyBind = null;
		val = false;
		mod = null;
	}
	
	public boolean buildHotKey(String keyName, String keyDescription, boolean keyEnabled, KeyActionType keyType) {
		try {
			switch (keyType) {
			case COMMAND: createdKey = new CommandHotKey(keyName, keys, command, keyDescription); break;
			case COMMAND_PLAYER_HANDHELD_CONDITION: createdKey = new HandHeldCommandHotKey(keyName, keys, command, testHeldItemId, keyDescription); break;
			case DEBUG: createdKey = new DebugHotKey(keyName, keys, debugCommand, keyDescription); break;
			case GUI_OPENER: createdKey = new GuiOpenerHotKey(keyName, keys, gui, keyDescription); break;
			case MC_KEYBIND_MODIFIER: createdKey = new KeyBindModifierHotKey(keyName, keys, keyBind, val, keyDescription); break;
			case MOD_ACTIVATOR: createdKey = new ModActivatorHotKey(keyName, keys, mod, keyDescription); break;
			case MOD_DEACTIVATOR: createdKey = new ModDeactivatorHotKey(keyName, keys, mod, keyDescription); break;
			case SCRIPT: createdKey = new ScriptHotKey(keyName, keys, script, scriptArgs, keyDescription);break;
			default: break;
			}
			
			if (createdKey != null) { 
				if (man.registerHotKey(createdKey)) {
					man.getKeySaver().saveKeysToFile();
					return true;
				}
			}
			clearBuilderArgs();
		} catch (Exception e) { e.printStackTrace(); }
		return false;
	}
}
