package com.Whodundid.enhancedChat.chatUtil;

import com.Whodundid.enhancedChat.gameChat.InGameChatWindow;
import com.Whodundid.main.util.enhancedGui.guiObjectUtil.EObjectGroup;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiHeader;
import com.Whodundid.main.util.enhancedGui.guiUtil.ObjectModifyTypes;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedTopParent;
import com.Whodundid.main.util.miscUtil.Resources;
import com.Whodundid.main.util.storageUtil.EArrayList;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;

//Last edited: Jan 11, 2019
//First Added: Jan 11, 2019
//Author: Hunter Bragg

public class ChatWindowHeader extends EGuiHeader {
	
	protected InGameChatWindow parentWindow;
	protected EArrayList<ChatHeaderTab> selectedChats = new EArrayList();
	protected EArrayList<ChatHeaderTab> newButtons = new EArrayList();
	protected EGuiButton pinButton;
	
	public ChatWindowHeader(InGameChatWindow parentIn) {
		super(parentIn);
		parentWindow = parentIn;
		drawDisplayString(false);
	}
	
	public ChatWindowHeader(InGameChatWindow parentIn, ChatType... typesIn) { this(parentIn, new EArrayList<ChatType>(typesIn)); }
	public ChatWindowHeader(InGameChatWindow parentIn, EArrayList<ChatType> typesIn) {
		super(parentIn);
		parentWindow = parentIn;
		drawDisplayString(false);
		createChatTabs(new EArrayList<ChatType>(typesIn));
		
		pinButton = new EGuiButton(this, endX - 52, startY + 2, 16, 16, "") {
			@Override
			public void performAction() {
				playPressSound();
				if (getPressedButton() == 0) {
					if (parentWindow != null) {
						parentWindow.setPinned(!parentWindow.isPinned());
						pinButton.setButtonTexture(parentWindow.isPinned() ? Resources.guiPinButtonOpen : Resources.guiPinButton);
						pinButton.setButtonSelTexture(parentWindow.isPinned() ? Resources.guiPinButtonOpenSel : Resources.guiPinButtonSel);
					}
				}
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
						/*if (topParent.isMoving()) {
							topParent.setModifyingObject(parent.getParent(), ObjectModifyTypes.move);
							topParent.setModifyMousePos(mX, mY);
						}*/
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
		
		if (parentWindow != null) {
			pinButton.setButtonTexture(parentWindow.isPinned() ? Resources.guiPinButtonOpen : Resources.guiPinButton);
			pinButton.setButtonSelTexture(parentWindow.isPinned() ? Resources.guiPinButtonOpenSel : Resources.guiPinButtonSel);
		} else {
			pinButton.setButtonTexture(Resources.guiPinButton).setButtonSelTexture(Resources.guiPinButtonSel);
		}
		pinButton.setDrawBackground(true).setBackgroundColor(0xffbb0000).setRunActionOnPress(true).setPersistent(true);
		moveButton.setButtonTexture(Resources.guiMoveButton).setButtonSelTexture(Resources.guiMoveButtonSel).setRunActionOnPress(true).setPersistent(true);
		closeButton.setButtonTexture(Resources.guiCloseButton).setButtonSelTexture(Resources.guiCloseButtonSel).setRunActionOnPress(true).setPersistent(true);
		
		EObjectGroup group = new EObjectGroup(getParent());
		group.addObject(this, pinButton, moveButton, closeButton);
		setObjectGroup(group);
		
		addObject(pinButton, moveButton, closeButton);
	}
	
	@Override
	public void initObjects() {
		
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		mX = mXIn; mY = mYIn;
		isMouseHover = isMouseInside(mXIn, mYIn) && getTopParent().getHighestZObjectUnderMouse() != null && getTopParent().getHighestZObjectUnderMouse().equals(this);
		if (!mouseEntered && isMouseHover) { mouseEntered = true; mouseEntered(mX, mY); }
		if (mouseEntered && !isMouseHover) { mouseEntered = false; mouseExited(mX, mY); }
		if (!objsToBeRemoved.isEmpty()) { removeObjects(); }
		if (!objsToBeAdded.isEmpty()) { addObjects(); }
		if (!newButtons.isEmpty()) { updateOnNextDraw(); }
		updateCursorImage();
		if (checkDraw()) {
			synchronized (guiObjects) {
				if (drawHeader) {
					drawRect(startX, startY, startX + 1, startY + height, headerBorderColor); //left
					drawRect(startX + 1, startY, endX - 1, startY + 1, headerBorderColor); //top
					drawRect(endX - 1, startY, endX, startY + height, headerBorderColor); //right
					drawRect(startX + 1, startY + 1, endX - 1, startY + height, headerMainColor); //mid
					if (drawDisplayString) {
						drawString(displayString, startX + 4, startY + height / 2 - 3, headerStringColor);
					}
				}
				for (ChatHeaderTab b : selectedChats) {
					b.drawObject(mXIn, mYIn, ticks);
				}
				for (int i = 0; i <= getTopParent().getHighestZLevel(); i++) {
					Iterator<IEnhancedGuiObject> it = guiObjects.iterator();
					while (it.hasNext()) {
						IEnhancedGuiObject o = it.next();
						if (o.checkDraw() && o.getZLevel() == i && !selectedChats.contains(o)) {
		    				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		    	        	o.drawObject(mX, mY, ticks);
		    			}
					}
		        }
			}
		}
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
	}
	
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		super.mouseReleased(mX, mY, button);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		
	}
	
	public EArrayList<ChatType> getAllChatTabTypes() {
		EArrayList<ChatType> chats = new EArrayList();
		for (ChatHeaderTab b : selectedChats) { chats.add(b.getChatType()); }
		return chats;
	}
	
	public EArrayList<ChatHeaderTab> getAllChatTabs() { return new EArrayList<ChatHeaderTab>(selectedChats); }
	
	public EArrayList<ChatType> getAllEnabledChatTypes() {
		EArrayList<ChatType> enabledChats = new EArrayList();
		for (ChatHeaderTab b : selectedChats) { if (b.isChatTypeEnabled()) { enabledChats.add(b.getChatType()); } }
		return enabledChats;
	}
	
	public EArrayList<ChatType> getAllDisabledChatTypes() {
		EArrayList<ChatType> disabledChats = new EArrayList();
		for (ChatHeaderTab b : selectedChats) { if (!b.isChatTypeEnabled()) { disabledChats.add(b.getChatType()); } }
		return disabledChats;
	}
	
	public ChatHeaderTab getChatTab(ChatType typeIn) {
		for (ChatHeaderTab b : selectedChats) { if (b.getChatType() == typeIn) { return b; } }
		return null;
	}
	
	protected void createChatTabs(EArrayList<ChatType> typesIn) {
		int bX = 2;
		ChatHeaderTab newTabObject = null;
		if (!typesIn.contains(ChatType.NONE)) { typesIn.add(ChatType.NONE); }
		for (ChatType t : typesIn) {
			ChatHeaderTab b = new ChatHeaderTab(this, t);
			if (t == ChatType.NONE) { newTabObject = b; }
			if (t != ChatType.NONE) { b.setDimensions(startX + bX, startY + 2, 41, height - 3); }
			selectedChats.add(b);
			addObject(b);
			bX += 42;
		}
		if (newTabObject != null) { newTabObject.setDimensions(startX + (42 * (selectedChats.size() - 1)) + 2, startY + 2, 16, height - 3); }
		checkForAll(selectedChats);
	}
	
	public void updateChatTabs() {
		EArrayList<ChatType> types = parentWindow.getChatTypes();
		int bX = 2;
		ChatHeaderTab newTabObject = null;
		if (!types.contains(ChatType.NONE)) { types.add(ChatType.NONE); }
		for (ChatType t : types) {
			ChatHeaderTab b = new ChatHeaderTab(this, t);
			if (t == ChatType.NONE) { newTabObject = b; }
			if (t != ChatType.NONE) { b.setDimensions(startX + bX, startY + 2, 41, height - 3); }
			newButtons.add(b);
			bX += 42;
		}
		if (newTabObject != null) { newTabObject.setDimensions(startX + (42 * (newButtons.size() - 1)) + 2, startY + 2, 16, height - 3); }
		//System.out.println(newTabObject.getDimensions());
		checkForAll(newButtons);
	}
	
	private void checkForAll(EArrayList<ChatHeaderTab> tabsIn) {
		boolean containsAll = false;
		for (ChatHeaderTab b : tabsIn) {
			if (b.getChatType() == ChatType.ALL) { containsAll = true; break; }
		}
		if (containsAll) {
			for (ChatHeaderTab b : tabsIn) {
				if (b.getChatType() == ChatType.ALL) { b.setChatTypeEnabled(true); }
				else { b.setChatTypeEnabled(false); }
			}
		} else {
			for (ChatHeaderTab b : tabsIn) { b.setChatTypeEnabled(true); }
		}
	}
	
	protected void clearChatTabs() {
		selectedChats.clear();
		for (IEnhancedGuiObject o : guiObjects) {
			if (o instanceof ChatHeaderTab) { removeObject(o); }
		}
	}
	
	protected void updateOnNextDraw() {
		clearChatTabs();
		for (ChatHeaderTab b : newButtons) {
			selectedChats.add(b);
			addObject(b);
		}
		newButtons.clear();
		parentWindow.updateEnabledChatTypes();
	}
	
	public ChatWindowHeader setChatTabsToEnabledValue(boolean val, ChatType... typesIn) { return setChatTabsToEnabledValue(val, new EArrayList<ChatType>(typesIn)); }
	public ChatWindowHeader setChatTabsToEnabledValue(boolean val, EArrayList<ChatType> typesIn) {
		for (ChatType t : typesIn) {
			ChatHeaderTab b = getChatTab(t);
			if (b != null) { b.setChatTypeEnabled(val); }
		}
		return this;
	}
	
	public InGameChatWindow getParentChatWindow() { return parentWindow; }
}
