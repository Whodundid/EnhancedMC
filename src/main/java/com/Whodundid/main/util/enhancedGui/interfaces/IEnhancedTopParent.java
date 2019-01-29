package com.Whodundid.main.util.enhancedGui.interfaces;

import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiUtil.ObjectModifyTypes;
import com.Whodundid.main.util.playerUtil.PlayerFacing.Direction;
import java.util.Stack;

//Jan 1, 2019
//Last edited: Jan 21, 2019
//First Added: Dec 27, 2018
//Author: Hunter Bragg

public interface IEnhancedTopParent extends IEnhancedGuiObject {
	//history
	public Stack<EnhancedGui> getGuiHistory();
	public IEnhancedGuiObject sendGuiHistory(Stack<EnhancedGui> historyIn);
	//focus
	public IEnhancedGuiObject getDefaultFocusObject();
	public IEnhancedTopParent setDefaultFocusObject(IEnhancedGuiObject objIn);
	public IEnhancedGuiObject getFocusedObject();
	public IEnhancedTopParent setObjectRequestingFocus(IEnhancedGuiObject objIn);
	public IEnhancedGuiObject getFocusLockObject();
	public IEnhancedTopParent setFocusLockObject(IEnhancedGuiObject objIn);
	public IEnhancedTopParent clearFocusLockObject();
	public boolean doesFocusLockExist();
	public void clearFocusedObject();
	public void updateFocus();
	//object modification
	public boolean isMoving();
	public boolean isResizing();
	public ObjectModifyTypes getModifyType();
	public IEnhancedTopParent setResizingDir(Direction areaIn);
	public IEnhancedTopParent setModifyingObject(IEnhancedGuiObject objIn, ObjectModifyTypes typeIn);
	public IEnhancedTopParent setModifyMousePos(int mX, int mY);
	public IEnhancedGuiObject getModifyingObject();
	public IEnhancedTopParent clearModifyingObject();
	//close
	public void closeGui();
	public IEnhancedTopParent setCloseAndRecenter(boolean val);
}
