package com.Whodundid.worldEditor.EditorGuiObjects;

import com.Whodundid.main.util.enhancedGui.guiObjects.InnerEnhancedGui;
import com.Whodundid.worldEditor.EditorUtil.EditorGuiBase;

//Last edited: Nov 25, 2018
//First Added: Nov 25, 2018
//Author: Hunter Bragg

public abstract class EditorGuiObject extends InnerEnhancedGui {
	
	public EditorGuiBase guiInstance;
	
	public EditorGuiBase getGuiInstance() { return guiInstance; }
}
