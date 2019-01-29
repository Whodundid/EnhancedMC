package com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables;

import com.Whodundid.scripts.scriptUtil.scriptObjects.ScriptObject;

public abstract class ScriptVariable extends ScriptObject {
	
	protected String varName = "";
	protected ScriptVariableType variableType;
	
	protected ScriptVariable() {}
	public ScriptVariable(String name, Object objIn) {
		varName = name;
		variableType = ScriptVariableType.getType(objIn);
	}
	
	public ScriptVariableType getVariableType() { return variableType; }
}
