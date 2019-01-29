package com.Whodundid.multiHotBar;

import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import java.io.IOException;

//Last edited: Nov 2, 2018
//First Added: Nov 2, 2018
//Author: Hunter Bragg

public class HotBarGui extends EnhancedGui {

	EGuiButton layerVal, drawLayered;
	MultiHotbar mod = (MultiHotbar) RegisteredSubMods.getMod(SubModType.MULTIHOTBAR);
	
	public HotBarGui() { super(); }
	public HotBarGui(EnhancedGui oldGui) { super(oldGui); }
	public HotBarGui(int posX, int posY) { super(posX, posY); }
	public HotBarGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		layerVal = new EGuiButton(this, wPos + 15, hPos - 106, 75, 20, "" + mod.numberOfLayers);
		drawLayered = new EGuiButton(this, wPos + 15, hPos - 81, 75, 20, (mod.layered) ? "Rows" : "One Long Row");
		addObject(layerVal, drawLayered);
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		drawDefaultBackground();
		drawString("Number of hotbars:", wPos - 90, hPos - 100, 0xffffff);
		drawString("HotBar mode:", wPos - 90, hPos - 75, 0xffffff);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject b) {
		if (b.equals(layerVal)) {
			if (mod.numberOfLayers == 4) { mod.numberOfLayers = 1; }
			else { mod.numberOfLayers += 1; }
			mod.slotSet = false;
			layerVal.displayString = "" + mod.numberOfLayers;
		}
		if (b.equals(drawLayered)) {
			mod.layered = !mod.layered;
			mod.slotSet = false;
			drawLayered.displayString = (mod.layered) ? "Rows" : "One Long Row";
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 13) { mod.debugMode = !mod.debugMode; }
		super.keyTyped(typedChar, keyCode);
	}
}
