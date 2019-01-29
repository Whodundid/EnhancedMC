package com.Whodundid.main.util.enhancedGui.guiObjects;

import com.Whodundid.main.util.enhancedGui.EnhancedGuiObject;
import com.Whodundid.main.util.playerUtil.PlayerFacing.Direction;
import java.util.ArrayList;

//Last edited: Oct 22, 2018
//First Added: Oct 12, 2018
//Author: Hunter Bragg

public class EGuiScrollList extends EnhancedGuiObject {
	
	public ArrayList<EnhancedGuiObject> listContents = new ArrayList();
	public boolean isVertical = true;
	EGuiScrollBar scrollbar = new EGuiScrollBar(this, 0, 0, true, Direction.E);
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		super.drawObject(mX, mY, ticks);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
	}
	
	@Override
	public void mouseScrolled(int change) {
		super.mouseScrolled(change);
	}
}
