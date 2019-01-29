package com.Whodundid.scripts.scriptUtil;

import com.Whodundid.scripts.scriptBase.Script;
import com.Whodundid.scripts.scriptUtil.scriptObjects.ScriptArray;
import com.Whodundid.scripts.scriptUtil.scriptObjects.ScriptObject;
import com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables.ScriptVariable;
import java.util.ArrayList;

//Last edited: Oct 11, 2018
//First Added: Oct 11, 2018
//Author: Hunter Bragg

public class ScriptLine {
	
	public Script parentScript;
	protected int lineNumber = 0;
	ArrayList<String> lineContents = new ArrayList();
	
	public ScriptLine(Script parentIn, int lineNumberIn) {
		parentScript = parentIn;
		lineNumber = lineNumberIn;
	}
	
	public ScriptLine setLineNumber(int lineNumberIn) { lineNumber = lineNumberIn; return this; }
	
	public ScriptLine addToLine(String objIn) { lineContents.add(objIn); return this; }
	public ScriptLine clearLine() { lineContents.clear(); return this; }
}
