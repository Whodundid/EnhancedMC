package com.Whodundid.hotkeys.hotKeyGuis;

import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;

//Last edited: Dec 21, 2018
//First Added: Dec 21, 2018
//Author: Hunter Bragg

public class HotKeyGuiMain extends EnhancedGui {
	
	EGuiButton creator, keyList, keyVisual;
	
	public HotKeyGuiMain() { super(); }
	public HotKeyGuiMain(EnhancedGui oldGui) { super(oldGui); }
	public HotKeyGuiMain(int posX, int posY) { super(posX, posY); }
	public HotKeyGuiMain(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		creator = new EGuiButton(this, wPos - 75, hPos + 5, 150, 20, "Hotkey Creator");
		keyList = new EGuiButton(this, wPos - 75, hPos + 30, 150, 20, "Hotkey List");
		keyVisual = new EGuiButton(this, wPos - 75, hPos + 55, 150, 20, "Registered Hotkey Visual");
		
		addObject(creator, keyList, keyVisual);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawDefaultBackground();
		
		drawCenteredStringWithShadow("This is the main HotKey Gui.", wPos, hPos - 105, 0xffd800);
		
		drawCenteredStringWithShadow("Hotkeys can be created", wPos, hPos - 80, 0xffd800);
		drawCenteredStringWithShadow("and modified here.", wPos, hPos - 70, 0xffd800);
		
		drawCenteredStringWithShadow("Select an option below to", wPos, hPos - 45, 0xffd800);
		drawCenteredStringWithShadow("access a specific hotkey menu.", wPos, hPos - 35, 0xffd800);
		
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(creator)) { mc.displayGuiScreen(new HotKeyCreatorGui(this)); }
		if (object.equals(keyList)) { mc.displayGuiScreen(new HotKeyListGui(this)); }
		if (object.equals(keyVisual)) { }
	}
}
