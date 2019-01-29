package com.Whodundid.main.util.enhancedGui.guiObjects;

import com.Whodundid.main.util.enhancedGui.EnhancedGuiObject;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedGuiObject;

//Last edited: Jan 9, 2019
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public class InnerEnhancedGui extends EnhancedGuiObject {
	
	protected EGuiHeader header;
	protected boolean moveWithParent = false;
	
	protected InnerEnhancedGui() {}
	public InnerEnhancedGui(IEnhancedGuiObject parentIn, int xIn, int yIn, int widthIn, int heightIn) {
		init(parentIn, xIn, yIn, widthIn, heightIn);
	}
	
	public void initGui() {}
	public void onInnerGuiClose() {}
	
	public void drawDefaultBackground() {
		drawRect(startX, startY, endX, endY, 0xff000000);
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, 0xff383838);
		drawRect(startX + 2, startY + 2, endX - 2, endY - 2, 0xff3f3f3f);
		drawRect(startX + 3, startY + 3, endX - 3, endY - 3, 0xff424242);
	}
	
	public boolean movesWithParent() { return moveWithParent; }
	public InnerEnhancedGui setMoveWithParent(boolean val) { moveWithParent = val; return this; }
	public EGuiHeader getHeader() { return header; }
}
