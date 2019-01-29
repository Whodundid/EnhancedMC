package com.Whodundid.scripts.scriptUtil;

import com.Whodundid.scripts.scriptBase.Script;
import java.util.ArrayList;

public class AvailableScripts {
	
	static ArrayList<String> scriptList = new ArrayList<>();
	static ArrayList<Script> unregisteredScripts = new ArrayList<>();
	
	public static void addNewScript(Script scriptIn) {
		if (!unregisteredScripts.contains(scriptIn)) {
			
		}
	}
	
	protected static void registerScript(Script scriptIn) {
		if (!scriptList.contains(scriptIn.getScriptName())) {
			scriptList.add(scriptIn.getScriptName());
		}
	}
	
	public static void registerAllScripts() {
		synchronized (scriptList) {
			scriptList.clear();
			
		}
	}
	
	public static void unregisterAllScripts() {
		synchronized (scriptList) {
			scriptList.clear();
		}
	}
}
