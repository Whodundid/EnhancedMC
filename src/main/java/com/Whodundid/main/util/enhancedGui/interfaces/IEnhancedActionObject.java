package com.Whodundid.main.util.enhancedGui.interfaces;

//Last edited: Dec 30, 2018
//First Added: Dec 30, 2018
//Author: Hunter Bragg

public interface IEnhancedActionObject {
	//actions
	public boolean runActionOnPress();
	public IEnhancedActionObject setRunActionOnPress(boolean val);
	public void performAction();
	//objects
	public IEnhancedActionObject setStorredObject(Object objIn);
	public Object getStorredObject();
}
