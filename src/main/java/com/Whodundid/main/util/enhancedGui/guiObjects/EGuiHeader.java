package com.Whodundid.main.util.enhancedGui.guiObjects;

import com.Whodundid.main.util.enhancedGui.EnhancedGuiObject;
import com.Whodundid.main.util.enhancedGui.guiObjectUtil.EObjectGroup;
import com.Whodundid.main.util.enhancedGui.guiUtil.FocusEvent;
import com.Whodundid.main.util.enhancedGui.guiUtil.ObjectModifyTypes;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedTopParent;
import com.Whodundid.main.util.miscUtil.Resources;
import com.Whodundid.main.util.playerUtil.PlayerFacing.Direction;
import com.Whodundid.main.util.storageUtil.EDimension;

//Dec 31, 2018
//Last edited: Jan 12, 2019
//First Added: Dec 17, 2018
//Author: Hunter Bragg

public class EGuiHeader extends EnhancedGuiObject {
	
	protected EGuiButton fileUpButton, moveButton, closeButton;
	public boolean fullClose = false;
	public boolean drawDefault = true;
	public boolean drawDisplayString = true;
	public String displayString = "";
	public int headerBorderColor = 0xff000000;
	public int headerMainColor = 0xff2D2D2D;
	public int headerStringColor = 0xb2b2b2;
	protected boolean drawHeader = true;
	
	protected EGuiHeader() {}
	public EGuiHeader(IEnhancedGuiObject parentIn) { this(parentIn, true, 19); }
	public EGuiHeader(IEnhancedGuiObject parentIn, boolean drawDefaultIn, int headerHeight) {
		if (parentIn != null) {
			EDimension dim = parentIn.getDimensions();
			init(parentIn, dim.startX, dim.startY - headerHeight, dim.width, headerHeight);
		}
		drawDefault = drawDefaultIn;
		
		if (drawDefault) {
			fileUpButton = new EGuiButton(this, endX - 52, startY + 2, 16, 16, "") {
				@Override
				public void performAction() {
					if (getPressedButton() == 0) { playPressSound(); if (getTopParent() != null) { getTopParent().closeGui(); } }
					/*else {
						EGuiDropDownList history = new EGuiDropDownList(this, mX, mY);
						Stack<EnhancedGui> histList = parentGui.getGuiHistory();
						for (EnhancedGui g : histList) {
							history.addListEntry(new DropDownListEntry(g.getClass().getSimpleName(), g));
						}
						history.setZLevel(1);
						objectInstance.addObject(history);
					}*/
				}
			};
			
			moveButton = new EGuiButton(this, endX - 35, startY + 2, 16, 16, "") {
				@Override
				public void performAction() {
					playPressSound();
					if (!parent.getParent().isPositionLocked()) {
						IEnhancedTopParent topParent = getTopParent();
						if (getPressedButton() == 0) {
							if (topParent.isMoving()) { topParent.clearModifyingObject(); }
							else {
								topParent.setModifyingObject(parent.getParent(), ObjectModifyTypes.move);
								topParent.setModifyMousePos(mX, mY);
							}
						} else if (getPressedButton() == 1) {
							topParent.clearModifyingObject();
							parent.getParent().resetPosition();
						}
					}
				}
			};
			
			closeButton = new EGuiButton(this, endX - 18, startY + 2, 16, 16, "") {
				@Override
				public void performAction() {
					if (getPressedButton() == 0) {
						playPressSound();
						if (fullClose) {
							mc.displayGuiScreen(null);
							if (mc.currentScreen == null) { mc.setIngameFocus(); }
						} else { parent.getParent().close(); }
					}
				}
			};
			
			fileUpButton.setButtonTexture(Resources.guiFileUpButton).setButtonSelTexture(Resources.guiFileUpButtonSel).setRunActionOnPress(true).setVisible(false);
			moveButton.setButtonTexture(Resources.guiMoveButton).setButtonSelTexture(Resources.guiMoveButtonSel).setRunActionOnPress(true).setPersistent(true);
			closeButton.setButtonTexture(Resources.guiCloseButton).setButtonSelTexture(Resources.guiCloseButtonSel).setRunActionOnPress(true).setPersistent(true);
			
			if (displayString.isEmpty()) { displayString = getTopParent() != null ? getTopParent().getClass().getSimpleName() : "missingno"; }
			
			EObjectGroup group = new EObjectGroup(getParent());
			group.addObject(this, fileUpButton, moveButton, closeButton);
			setObjectGroup(group);
			
			addObject(fileUpButton, moveButton, closeButton);
		}
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		if (drawHeader) {
			drawRect(startX, startY, startX + 1, startY + height, headerBorderColor); //left
			drawRect(startX + 1, startY, endX - 1, startY + 1, headerBorderColor); //top
			drawRect(endX - 1, startY, endX, startY + height, headerBorderColor); //right
			drawRect(startX + 1, startY + 1, endX - 1, startY + height, headerMainColor); //mid
			if (drawDisplayString) {
				drawString(displayString, startX + 4, startY + height / 2 - 3, headerStringColor);
			}
		}
		super.drawObject(mX, mY, ticks);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
		if (hasFocus()) {
			if (getObjectGroup() != null && getObjectGroup().getGroupParent() != null) {
				IEnhancedGuiObject groupParent = getObjectGroup().getGroupParent();
				if (groupParent.isResizeable()) {
					if (groupParent.getEdgeAreaMouseIsOn() != Direction.OUT) {
						groupParent.mousePressed(mX, mY, button);
						//getParent().bringToFront(); //bugged
					} else {
						headerClick(button);
						//getParent().bringToFront(); //bugged
					}
					return;
				}
			}
			headerClick(button);
			//getParent().bringToFront(); //bugged
		}
	}
	
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		if (hasFocus()) { getTopParent().clearModifyingObject(); }
		super.mouseReleased(mX, mY, button);
	}
	
	@Override
	public EGuiHeader setEnabled(boolean val) {
		drawHeader = val;
		if (drawDefault) {
			moveButton.setVisible(val).setPersistent(val);
			closeButton.setVisible(val).setPersistent(val);
			if (fileUpButton != null) { fileUpButton.setVisible(val && getTopParent().getGuiHistory() != null && !getTopParent().getGuiHistory().isEmpty()); }
		}
		for (IEnhancedGuiObject o : getObjects()) { o.setVisible(val); }
		return this;
	}
	
	@Override
	public void onFocusLost(FocusEvent eventIn) {
		getTopParent().clearModifyingObject();
	}
	
	protected void headerClick(int button) {
		if (moveButton != null && moveButton.checkDraw()) {
			IEnhancedTopParent topParent = getTopParent();
			if (button == 0) {
				topParent.setModifyingObject(parent, ObjectModifyTypes.move);
				topParent.setModifyMousePos(mX, mY);
			}
			else { topParent.clearModifyingObject(); }
		}
	}
	
	public EGuiHeader updateFileUpButtonVisibility() {
		if (getTopParent() != null && getTopParent().getGuiHistory() != null && !getTopParent().getGuiHistory().isEmpty() && fileUpButton != null) { fileUpButton.setVisible(true); }
		return this;
	}
	
	public EGuiHeader setDisplayStringColor(int colorIn) { headerStringColor = colorIn; return this; }
	public EGuiHeader setBorderColor(int colorIn) { headerBorderColor = colorIn; return this; }
	public EGuiHeader setMainColor(int colorIn) { headerMainColor = colorIn; return this; }
	public EGuiHeader setDisplayString(String stringIn) { displayString = stringIn; return this; }
	public EGuiHeader setFullClose(boolean val) { fullClose = val; return this; }
	public EGuiHeader drawDisplayString(boolean val) { drawDisplayString = val; return this; }
	public int getStringColor() { return headerStringColor; }
}
