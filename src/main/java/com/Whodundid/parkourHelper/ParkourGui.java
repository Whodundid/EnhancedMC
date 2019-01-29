package com.Whodundid.parkourHelper;

import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiTextField;
import java.io.IOException;

//Last edited: 10-16-18
//First Added: 11-16-17
//Author: Hunter Bragg

public class ParkourGui extends EnhancedGui {
	
	ParkourAI parkour;
	double curVal;
	EGuiTextField valInput;
	
	public ParkourGui() { super(); }
	public ParkourGui(EnhancedGui oldGui) { super(oldGui); }
	public ParkourGui(int posX, int posY) { super(posX, posY); }
	public ParkourGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	
	@Override
	public void initGui() {
		super.initGui();
		parkour = (ParkourAI) RegisteredSubMods.getMod(SubModType.PARKOUR);
		curVal = parkour.jumpOffset;
	}
	
	@Override
	public void initObjects() {
		valInput = new EGuiTextField(this, wPos - 68, hPos - 46, 137, 20);
		addObject(valInput);
	}
	
	@Override
	public void drawObject(int x, int y, float ticks) {
		drawDefaultBackground();
		drawCenteredStringWithShadow("" + curVal, wPos, hPos - 76, 0xFFFFFF);
		drawCenteredStringWithShadow("0.535 & 0.629", wPos, hPos + 76, 0x00FF00);
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 28) { parseInput(valInput.getText()); }
		super.keyTyped(typedChar, keyCode);
	}
	
	private boolean parseInput(String input) {
		if (!input.isEmpty()) {
			try {
				double parsedVal = Double.parseDouble(input);
				parkour.jumpOffset = parsedVal;
				curVal = parkour.jumpOffset;
				return true;
			} catch (Exception e) { e.printStackTrace(); }
		}
		return false;
	}
}
