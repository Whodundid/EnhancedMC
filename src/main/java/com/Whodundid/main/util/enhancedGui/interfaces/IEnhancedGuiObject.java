package com.Whodundid.main.util.enhancedGui.interfaces;

import com.Whodundid.main.util.enhancedGui.guiObjectUtil.EObjectGroup;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiHeader;
import com.Whodundid.main.util.enhancedGui.guiUtil.FocusEvent;
import com.Whodundid.main.util.playerUtil.PlayerFacing.Direction;
import com.Whodundid.main.util.storageUtil.EArrayList;
import com.Whodundid.main.util.storageUtil.EDimension;

//Jan 2, 2019
//Last edited: Jan 11, 2019
//First Added: Oct 2, 2018
//Author: Hunter Bragg

public interface IEnhancedGuiObject {
	//init
	public boolean hasBeenInitialized();
	public void initObjects();
	public void reInitObjects();
	public void onObjectAddedToParent();
	//main draw
	public void drawObject(int mX, int mY, float ticks);
	public void updateCursorImage();
	//obj ids
	public int getObjectID();
	public IEnhancedGuiObject setObjectID(int idIn);
	//drawing checks
	public boolean checkDraw();
	public boolean isEnabled();
	public boolean isVisible();
	public boolean isPersistent();
	public IEnhancedGuiObject setEnabled(boolean val);
	public IEnhancedGuiObject setVisible(boolean val);
	public IEnhancedGuiObject setPersistent(boolean val);
	//size
	public boolean hasHeader();
	public boolean isResizeable();
	public EGuiHeader getHeader();
	public int getMinimumWidth();
	public int getMinimumHeight();
	public int getMaximumWidth();
	public int getMaximumHeight();
	public IEnhancedGuiObject setMinimumWidth(int widthIn);
	public IEnhancedGuiObject setMinimumHeight(int heightIn);
	public IEnhancedGuiObject setMaximumWidth(int widthIn);
	public IEnhancedGuiObject setMaximumHeight(int heightIn);
	public IEnhancedGuiObject setResizeable(boolean val);
	public IEnhancedGuiObject resize(int xIn, int yIn, Direction areaIn);
	//position
	public void move(int newX, int newY);
	public boolean isPositionLocked();
	public IEnhancedGuiObject resetPosition();
	public IEnhancedGuiObject setPosition(int newX, int newY);
	public IEnhancedGuiObject setPositionLocked(boolean val);
	public IEnhancedGuiObject setDimensions(EDimension dimIn);
	public IEnhancedGuiObject setDimensions(int startXIn, int startYIn, int endXIn, int endYIn);
	public EDimension getDimensions();
	//objects
	public boolean isChildOfObject(IEnhancedGuiObject objIn);
	public IEnhancedGuiObject addObject(IEnhancedGuiObject... objsIn);
	public IEnhancedGuiObject removeObject(IEnhancedGuiObject... objsIn);
	public EObjectGroup getObjectGroup();
	public IEnhancedGuiObject setObjectGroup(EObjectGroup groupIn);
	public EArrayList<IEnhancedGuiObject> getObjects();
	public EArrayList<IEnhancedGuiObject> getAllChildren();
	public EArrayList<IEnhancedGuiObject> getAllObjectsUnderMouse();
	//parents
	public IEnhancedGuiObject getParent();
	public IEnhancedGuiObject setParent(IEnhancedGuiObject parentIn);
	public IEnhancedTopParent getTopParent();
	//zLevel
	public int getZLevel();
	public int getHighestZLevel();
	public int getLowestZLevel();
	public void updateZLevel();
	public IEnhancedGuiObject setZLevel(int zLevelIn);
	public IEnhancedGuiObject bringForward();
	public IEnhancedGuiObject bringToFront();
	public IEnhancedGuiObject sendBackwards();
	public IEnhancedGuiObject sendToBack();
	//focus
	public boolean hasFocus();
	public boolean relinquishFocus();
	public void onFocusGained(FocusEvent eventIn);
	public void onFocusLost(FocusEvent eventIn);
	public void transferFocus(IEnhancedGuiObject objIn);
	public void drawFocusLockBorder();
	public IEnhancedGuiObject requestFocus();
	//mouse checks
	public boolean isMouseOnObjEdge(int mX, int mY);
	public Direction getEdgeAreaMouseIsOn();
	public void mouseEntered(int mX, int mY);
	public void mouseExited(int mX, int mY);
	public boolean isMouseInside(int mX, int mY);
	public boolean isMouseInsideObject(int mX, int mY);
	public IEnhancedGuiObject getHighestZObjectUnderMouse();
	//basic inputs
	public void parseMousePosition(int mX, int mY);
	public void mousePressed(int mX, int mY, int button);
	public void mouseReleased(int mX, int mY, int button);
	public void mouseDragged(int mX, int mY, int button, long timeSinceLastClick);
	public void mouseScrolled(int change);
	public void keyPressed(char typedChar, int keyCode);
	public void keyReleased(char typedChar, int keyCode);
	//updateScreen
	public void updateScreen();
	//action
	public void actionPerformed(IEnhancedActionObject object);
	//close object
	public void close();
}
