package com.Whodundid.scripts;

import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;

//Last edited: Dec 16, 2018
//First Added: Dec 16, 2018
//Author: Hunter Bragg

public class ScriptMainGui extends EnhancedGui {
	
	EGuiButton creator, scriptList, taskManager;
	
	public ScriptMainGui() { super(); }
	public ScriptMainGui(EnhancedGui oldGui) { super(oldGui); }
	public ScriptMainGui(int posX, int posY) { super(posX, posY); }
	public ScriptMainGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		creator = new EGuiButton(this, wPos - 75, hPos - 100, 150, 20, "Script Creator");
		scriptList = new EGuiButton(this, wPos - 75, hPos - 75, 150, 20, "Script List");
		taskManager = new EGuiButton(this, wPos - 75, hPos - 50, 150, 20, "Script Task Manager");
		
		addObject(creator, scriptList, taskManager);
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		drawDefaultBackground();
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(creator)) { mc.displayGuiScreen(new ScriptCreatorGui(guiInstance)); }
		if (object.equals(scriptList)) { }
		if (object.equals(taskManager)) { mc.displayGuiScreen(new ScriptTaskManagerGui(guiInstance)); }
	}
}
