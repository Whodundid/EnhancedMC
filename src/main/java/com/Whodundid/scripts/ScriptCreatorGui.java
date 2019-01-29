package com.Whodundid.scripts;

import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiHeader;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiTextArea;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiTextField;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.storageUtil.EDimension;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

//Last edited: Oct 18, 2018
//First Added: Oct 18, 2018
//Author: Hunter Bragg

public class ScriptCreatorGui extends EnhancedGui {

	EGuiTextArea explorer, writingArea, outputLog;
	EGuiButton compile, save, load, fileMenu, run, help, settings;
	
	public ScriptCreatorGui() { super(); }
	public ScriptCreatorGui(EnhancedGui oldGui) { super(oldGui); }
	public ScriptCreatorGui(int posX, int posY) { super(posX, posY); }
	public ScriptCreatorGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	
	@Override
	public void initGui() {
		centerGuiWithDimensions(700, 365);
		super.initGui();
		
		Keyboard.enableRepeatEvents(true);
	}
	
	@Override
	public void initObjects() {
		explorer = new EGuiTextArea(this, startX + 5, startY + 33, 140, 312);
		writingArea = new EGuiTextArea(this, startX + 149, startY + 33, 545, 212).setDrawLineNumbers(true);
		outputLog = new EGuiTextArea(this, startX + 149, startY + 252, 545, 93).setDrawLineNumbers(true);
		
		EGuiHeader writingHeader = new EGuiHeader(writingArea, false, 13).drawDisplayString(false);
		EDimension dim = writingHeader.getDimensions();
		writingHeader.addObject(new EGuiTextField(writingHeader, dim.startX + 3, dim.startY, 150, 13).setEnableBackgroundDrawing(true).setTextColor(0x55ff55));
		
		writingArea.addObject(writingHeader);
		explorer.addObject(new EGuiHeader(explorer, false, 13).setDisplayString("Script Explorer").setDisplayStringColor(0xffbb00));
		
		run = new EGuiButton(this, startX + 624, startY + 2, 70, 17, "Run Script");
		save = new EGuiButton(this, startX + 5, startY + 2, 40, 17, "Save");
		load = new EGuiButton(this, startX + 47, startY + 2, 40, 17, "Load");
		
		addObject(explorer, writingArea, outputLog);
		addObject(run, save, load);
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		drawDefaultBackground();
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		
	}
}
