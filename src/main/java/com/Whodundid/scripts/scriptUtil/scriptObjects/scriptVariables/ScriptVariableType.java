package com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables;

//Last edited: Oct 11, 2018
//First Added: Oct 11, 2018
//Author: Hunter Bragg

public enum ScriptVariableType {
	INT,
	DOUBLE,
	BOOL,
	UNDEFINED;
	
	public static ScriptVariableType getType(Object objIn) {
		if (objIn instanceof Integer) { return INT; }
		else if (objIn instanceof Double) { return DOUBLE; }
		else if (objIn instanceof Boolean) { return BOOL; }
		else { return UNDEFINED; }
	}
}
