package com.Whodundid.hotkeys.control.hotKeyTypes;

import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import com.Whodundid.scripts.scriptBase.Script;

//Last edited: 9-30-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class ScriptHotKey extends HotKey {
	
	public Script script;
	public String[] scriptArgs;
	
	public ScriptHotKey(String keyNameIn, KeyComboAction keysIn, Script scriptIn, String[] scriptArgsIn) { this(keyNameIn, keysIn, scriptIn, scriptArgsIn, false, "", null); }
	public ScriptHotKey(String keyNameIn, KeyComboAction keysIn, Script scriptIn, String[] scriptArgsIn, boolean builtInVal) { this(keyNameIn, keysIn, scriptIn, scriptArgsIn, builtInVal, "", null); }
	public ScriptHotKey(String keyNameIn, KeyComboAction keysIn, Script scriptIn, String[] scriptArgsIn, String descriptionIn) { this(keyNameIn, keysIn, scriptIn, scriptArgsIn, false, descriptionIn, null); }
	public ScriptHotKey(String keyNameIn, KeyComboAction keysIn, Script scriptIn, String[] scriptArgsIn, boolean builtInVal, String descriptionIn, String builtInSubModTypeIn) {
		super(keyNameIn, keysIn, builtInVal, KeyActionType.SCRIPT, builtInSubModTypeIn);
		if (descriptionIn != null && !descriptionIn.isEmpty()) { description = descriptionIn; }
		script = scriptIn;
		scriptArgs = scriptArgsIn;
	}
	
	public Script getScript() { return script; }
	public String[] getScriptArgs() { return scriptArgs; }
	
	@Override public void executeHotKeyAction() { 
		//Script.tryStartScript(script, scriptArgs, true); 
	}
	
	@Override public String getHotKeyStatistics() {
		String base = super.getHotKeyStatistics();
		base += ("; " + script.getScriptName());
		for (String arg : scriptArgs) { base += (", " + arg); }
		return base;
	}
}
