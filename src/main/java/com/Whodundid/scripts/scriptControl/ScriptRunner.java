package com.Whodundid.scripts.scriptControl;

import com.Whodundid.main.MainMod;
import com.Whodundid.main.util.miscUtil.ChatBuilder;
import com.Whodundid.scripts.ScriptManager;
import com.Whodundid.scripts.scriptBase.Script;
import com.Whodundid.scripts.scriptUtil.ScriptException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

//Last edited: Oct 11, 2018
//First Added: Oct 11, 2018
//Author: Hunter Bragg

public class ScriptRunner {
	
	static Minecraft mc = MainMod.getMC();
	protected ScriptManager man;
	private Thread updateThread;
	static int constantScriptIDNum = 0;
	private ArrayList<Script> runningScripts = new ArrayList();
	private volatile long shortestRefreshTime = 0l;
	private volatile long time = 0l;
	
	public ScriptRunner(ScriptManager manIn) {
		man = manIn;
		updateThread = new Thread() {
			@Override 
			public void run() {
				time = System.currentTimeMillis();
				while (true) {
					synchronized (runningScripts) {
						long timeChange = System.currentTimeMillis() - time;
						for (Script s : runningScripts) {
							if (timeChange >= s.refreshTime) {
								try {
									s.executeNextLine();
								} catch (ScriptException e) { mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.RED + e.getMessage()).build()); }
							}
						}
						if (timeChange >= 1000) {
							for (Script s : runningScripts) {
								s.timeAlive++;
							}
							time = System.currentTimeMillis();
						}
					}
				}
			}
		};
		updateThread.start();
	}
	
	public ScriptRunner addToRunningScripts(Script scriptIn) {
		runningScripts.add(scriptIn);
		return this;
	}
	
	private void checkForDeadScripts() {
		Iterator<Script> it = runningScripts.iterator();
		while (it.hasNext()) {
			Script s = it.next();
			if (s.isScriptKilled()) {
				it.remove();
				mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.GOLD + "Script: " + EnumChatFormatting.WHITE + s.getScriptName() + EnumChatFormatting.RED + " stopped.").build());
			}
		}
	}
	
	public List<Script> getRunningScripts() { return Collections.unmodifiableList(runningScripts); }
}
