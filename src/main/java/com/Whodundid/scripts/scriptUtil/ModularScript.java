package com.Whodundid.scripts.scriptUtil;

import com.Whodundid.scripts.scriptUtil.scriptObjects.ScriptAction;
import com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables.ScriptVariable;
import java.util.ArrayList;

public abstract class ModularScript {
	
	private ArrayList<ScriptAction> script = new ArrayList();
	protected ArrayList<ScriptVariable> declaredVars = new ArrayList();
	private ScriptAction currentAction;
	private int positionCounter = 0;
	public long delayInterval = 100;
	public boolean end = false;
	public boolean actionRunning = false;
	
	public synchronized void addAction(ScriptAction action) {
		script.add(action);
		resetPosition();
	}
	
	/**
	 * Removes ScriptAction at position in script. Script position is reset.
	 * 
	 * @param i
	 * 		position number
	 */
	public synchronized void removeActionAtPosition(int i) {
		resetPosition();
		script.remove(i);
	}
	
	public int getScriptSize() {
		return this.script.size();
	}
	
	public void setDelayInterval(long interval) {
		this.delayInterval = interval;
	}
	
	public long getDelayInterval() {
		return this.delayInterval;
	}
	
	public void runNextAction() {
		if (positionCounter < script.size()) {
			this.currentAction = script.get(positionCounter);
			try {
				actionRunning = true;
				this.currentAction.performAction();
			} catch (Exception e) {
				e.printStackTrace();
			}
			positionCounter++;
		}		
	}
	
	public boolean hasActionCompleted() {
		return this.currentAction.done;
	}
	
	public void resetPosition() {
		this.positionCounter = 0;
	}
	
	public void clearScript() {
		synchronized (script) {
			script.clear();
			resetPosition();
		}
	}
}
