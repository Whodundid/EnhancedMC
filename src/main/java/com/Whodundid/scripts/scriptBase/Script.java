package com.Whodundid.scripts.scriptBase;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import com.Whodundid.main.MainMod;
import com.Whodundid.scripts.scriptUtil.ScriptException;
import com.Whodundid.scripts.scriptUtil.ScriptLine;
import com.Whodundid.scripts.scriptUtil.scriptObjects.ScriptArray;
import com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables.ScriptVariable;
import net.minecraft.client.Minecraft;

public abstract class Script {
	
	protected static Minecraft mc = MainMod.getMC();
	protected boolean builtIn;
	public int scriptID = 0;
	public AtomicBoolean kill = new AtomicBoolean(false);
	public String ScriptName = "no script name";
	public long timeAlive = 0l;
	public long refreshTime = 0l;
	public boolean showStageMessages = false;
	public int currentLine = 0;
	public ArrayList<ScriptLine> lines = new ArrayList();
	public ArrayList<ScriptVariable> variables = new ArrayList();
	public ArrayList<ScriptArray> arrays = new ArrayList();
	
	public int getScriptID() { return scriptID; }
	public String getScriptName() { return ScriptName; }
	public boolean isScriptKilled() { return kill.get(); }
	public long getTimeAlive() { return timeAlive; }
	
	public synchronized Script setScriptRefreshTime(long timeIn) { refreshTime = timeIn; return this; }
	public void interruptScript(String[] args) { kill.set(true); }
	
	public Script executeNextLine() throws ScriptException {
		if (currentLine > lines.size() - 1) {
			currentLine++;
		}
		if (currentLine == lines.size() - 1) { kill.set(true); }
		return this;
	}
	
	public void initScript() {}
}