package com.Whodundid.main.util.enhancedGui.guiObjects;

import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.main.util.miscUtil.Resources;

//Last edited: Oct 26, 2018
//First Added: Oct 26, 2018
//Author: Hunter Bragg

public class ERightClickMenu extends InnerEnhancedGui {
	
	public String title = "";
	public boolean useTitle = false;
	boolean globalAction = false;
	
	public ERightClickMenu(IEnhancedGuiObject parentIn, int x, int y, int width, int height) {
		init(parentIn, x, y, width, height);
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		mc.renderEngine.bindTexture(Resources.guiRCMBase);
		drawModalRectWithCustomSizedTexture(startX, startY, 0, 0, width, height, width, height);
		super.drawObject(mX, mY, ticks);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 1) { getTopParent().removeObject(this); }
	}
	
	public void runGlobalAction() {}
}
