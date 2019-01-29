package com.Whodundid.scripts;

import java.util.concurrent.atomic.AtomicBoolean;
import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.storageUtil.EArrayList;
import com.Whodundid.main.util.storageUtil.StorageBox;
import com.Whodundid.scripts.scriptBase.Script;

public class ScriptManager extends SubMod {
	
	protected AtomicBoolean globalKill = new AtomicBoolean(false);
	protected EArrayList<Script> availableScripts, runningScripts;
	//protected ScriptReader reader;
	//protected ScriptRunner runner;
	
	public ScriptManager() {
		super(SubModType.SCRIPTS);
		dependencies.add(SubModType.HOTKEYS);
		setMainGui(new ScriptMainGui());
		addGui(new ScriptTaskManagerGui(), new ScriptCreatorGui());
	}
	
	@Override
	public EnhancedGui getMainGui(boolean setPosition, StorageBox<Integer, Integer> pos, EnhancedGui oldGui) {
		if (oldGui != null) { return setPosition ? new ScriptMainGui(pos.getObject(), pos.getValue(), oldGui) : new ScriptMainGui(oldGui); }
		return setPosition ? new ScriptMainGui(pos.getObject(), pos.getValue()) : new ScriptMainGui();
	}
	
	public ScriptManager toggleGlobalKill() { globalKill.set(!globalKill.get()); return this; }
	public ScriptManager setGlobalKill(boolean val) { globalKill.set(val); return this; }
	public boolean getGlobalKill() { return globalKill.get(); }
}