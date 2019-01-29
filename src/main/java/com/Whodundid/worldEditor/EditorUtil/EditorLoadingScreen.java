package com.Whodundid.worldEditor.EditorUtil;

import com.Whodundid.main.util.enhancedGui.EnhancedGui;

//Last edited: Sep 28, 2018
//First Added: Sep 28, 2018
//Author: Hunter Bragg

public class EditorLoadingScreen extends EnhancedGui {
	
	@Override public void initGui() {
		super.initGui();
		enableHeader(false);
		finishInit();
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "Loading...", wPos, hPos, 0x00ff00);
	}
}
