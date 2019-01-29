package com.Whodundid.main.util.enhancedGui.guiUtil;

import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedGuiObject;

//Last edited: Dec 31, 2018
//First Added: Dec 31, 2018
//Author: Hunter Bragg

public class FocusEvent {
	
	public IEnhancedGuiObject eventObject;
	public FocusType type;
	public int actionCode = -1;
	public int mX = -1, mY = -1;
	
	public FocusEvent(IEnhancedGuiObject objIn, FocusType typeIn) {
		eventObject = objIn;
		type = typeIn;
	}
	
	public FocusEvent(IEnhancedGuiObject objIn, FocusType typeIn, int actionCodeIn, int mXIn, int mYIn) {
		eventObject = objIn;
		type = typeIn;
		actionCode = actionCodeIn;
		mX = mXIn;
		mY = mYIn;
	}
}
