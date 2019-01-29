package com.Whodundid.autoCorrect;

import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiTextField;

//Last edited: Dec 2, 2018
//First Added: Dec 2, 2018
//Author: Hunter Bragg

public class AutoCorrectCreatorGui extends EnhancedGui {

	AutoCorrectManager man = (AutoCorrectManager) RegisteredSubMods.getMod(SubModType.AUTOCORRECT);
	EGuiTextField commandName;
	EGuiButton create, modify, cancel;
	
	public AutoCorrectCreatorGui() { super(); }
	public AutoCorrectCreatorGui(EnhancedGui oldGui) { super(oldGui); }
	public AutoCorrectCreatorGui(int posX, int posY) { super(posX, posY); }
	public AutoCorrectCreatorGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		commandName = new EGuiTextField(this, wPos + 10, hPos - 105, 180, 20);
		create = new EGuiButton(this, wPos + 10, hPos + 98, 70, 22, "Create");
		cancel = new EGuiButton(this, wPos - 190, hPos + 98, 70, 22, "Delete");
		
		addObject(commandName, create, cancel);
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		drawDefaultBackground();
	}
	
	public void createCommand() {
		
	}
	
}
