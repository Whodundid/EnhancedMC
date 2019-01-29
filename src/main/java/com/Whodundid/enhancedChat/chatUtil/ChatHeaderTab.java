package com.Whodundid.enhancedChat.chatUtil;

import com.Whodundid.enhancedChat.gameChat.InGameChatWindow;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiSelectionList;
import com.Whodundid.main.util.enhancedGui.guiUtil.ObjectModifyTypes;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedTopParent;
import com.Whodundid.main.util.miscUtil.NumberUtil;
import com.Whodundid.main.util.storageUtil.EArrayList;
import com.Whodundid.main.util.storageUtil.StorageBox;
import com.Whodundid.main.util.storageUtil.StorageBoxHolder;

//Jan 11, 2019
//Last edited: Jan 24, 2019
//First Added: Jan 11, 2019
//Author: Hunter Bragg

public class ChatHeaderTab extends EGuiButton {

	ChatWindowHeader parentHeader;
	protected ChatType type = ChatType.NONE;
	protected boolean isTypeEnabled = false;
	protected boolean pressed = false;
	protected boolean windowCreated = false;
	protected boolean isAddNewTab = false;
	protected int drawColor = 0xb2b2b2;
	protected StorageBox<Integer, Integer> mousePos = new StorageBox();
	protected StorageBox<Integer, Integer> startPos = new StorageBox();
	EGuiSelectionList tabSelectionList;
	
	public ChatHeaderTab(ChatWindowHeader parentHeaderIn, ChatType typeIn) {
		init(parentHeaderIn, 0, 0);
		parentHeader = parentHeaderIn;
		type = typeIn;
		if (type == ChatType.NONE) { isAddNewTab = true; }
		setDrawDefault(false);
		setDrawString(false);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		if (pressed) { checkForMove(mXIn, mYIn); }
		drawRect(startX, startY, startX + 1, endY, 0xaa909090);
		drawRect(startX + 1, startY, endX - 1, startY + 1, 0xaa909090);
		drawRect(endX - 1, startY, endX, endY, 0xaa404040);
		drawRect(startX + 1, endY - 1, endX - 1, endY, 0xaa404040);
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, 0xaa757575);
		drawCenteredStringWithShadow(isAddNewTab ? "+" : type.getChatType(), startX + width / 2, startY + 4, drawColor);
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
		if (button == 0) {
			if (isAddNewTab) {
				EArrayList<ChatType> activeChatTabs = parentHeader.getAllChatTabTypes();
				EArrayList<ChatType> selectableTypes = new EArrayList();
				for (ChatType t : ChatType.values()) { if (!activeChatTabs.contains(t)) { selectableTypes.add(t); } }
				openSelectionGui(selectableTypes);
			} else {
				IEnhancedTopParent topParent = getTopParent();
				topParent.setModifyMousePos(mX, mY);
				if (parentHeader.getAllChatTabs().size() > 1) {
					toggleEnabledValue();
					updateVisual();
					pressed = true;
					mousePos.setValues(mX, mY);
					startPos.setValues(startX, startY);
					topParent.setModifyingObject(this, ObjectModifyTypes.move);
				} else if (parentHeader.getAllChatTabs().size() == 1) {
					if (getParent().getObjectGroup() != null && getParent().getObjectGroup().getGroupParent() != null) {
						topParent.setModifyingObject(getParent().getObjectGroup().getGroupParent(), ObjectModifyTypes.move);
					}
				}
			}
		}
    }
	
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		if (hasFocus()) { getTopParent().clearModifyingObject(); }
		if (parentHeader.getAllChatTabs().size() > 1) {
			pressed = false;
			if (!checkMouseDrag(mX, mY) && startPos != null && startPos.getObject() != null && startPos.getValue() != null) {
				setDimensions(startPos.getObject(), startPos.getValue(), width, height);
			}
		}
		super.mouseReleased(mX, mY, button);
	}
	
	protected void checkForMove(int mX, int mY) {
		if (!windowCreated && checkMouseDrag(mX, mY)) {
			InGameChatWindow newWindow = new InGameChatWindow(getTopParent(), type);
			newWindow.setDimensions(mX - 22, mY + 10, 280, 152);
			newWindow.setPinned(true);
			getTopParent().addObject(newWindow);
			
			IEnhancedTopParent topParent = getTopParent();
			topParent.setModifyingObject(newWindow, ObjectModifyTypes.moveAlreadyClicked);
			topParent.setModifyMousePos(mX, mY);
			
			InGameChatWindow oldWindow = parentHeader.getParentChatWindow();
			EArrayList<ChatType> oldChatTypes = oldWindow.getChatTypes();
			if (oldChatTypes.contains(type)) {
				EArrayList<ChatType> newChatTypes = oldChatTypes;
				newChatTypes.remove(type);
				oldWindow.setChatTypes(newChatTypes);
			}
			windowCreated = true;
		}
	}
	
	protected boolean checkMouseDrag(int mX, int mY) {
		return NumberUtil.getDistance(mousePos, new StorageBox<Integer, Integer>(mX, mY)) > 20;
	}
	
	protected void openSelectionGui(EArrayList<ChatType> typesIn) {
		EArrayList<String> displayValues = new EArrayList();
		for (ChatType t : typesIn) { displayValues.add(t.getChatType()); }
		tabSelectionList = new EGuiSelectionList(this, StorageBoxHolder.createBox(displayValues, typesIn));
		
		addObject(tabSelectionList);
	}
	
	public ChatType getChatType() { return type; }
	public boolean isChatTypeEnabled() { return isTypeEnabled; }
	public ChatHeaderTab setChatTypeEnabled(boolean val) { isTypeEnabled = val; updateVisual(); return this; }
	
	protected void toggleEnabledValue() {
		isTypeEnabled = !isTypeEnabled;
		if (type != ChatType.ALL && parentHeader.getAllChatTabTypes().contains(ChatType.ALL)) {
			if (isTypeEnabled) { parentHeader.setChatTabsToEnabledValue(false, ChatType.ALL); }
			else {
				EArrayList<ChatHeaderTab> disabledTabs = new EArrayList();
				for (ChatHeaderTab t : parentHeader.getAllChatTabs()) {
					if (!t.isChatTypeEnabled()) { disabledTabs.add(t); }
				}
				if (disabledTabs.size() == parentHeader.getAllChatTabs().size()) { parentHeader.setChatTabsToEnabledValue(true, ChatType.ALL); }
			}
		} else if (type == ChatType.ALL) {
			EArrayList updatedTabs = parentHeader.getAllChatTabTypes();
			if (updatedTabs.contains(type)) { updatedTabs.remove(type); }
			if (isTypeEnabled) { parentHeader.setChatTabsToEnabledValue(false, updatedTabs); }
			else { parentHeader.setChatTabsToEnabledValue(true, updatedTabs); }
		}
		parentHeader.getParentChatWindow().updateEnabledChatTypes();
	}
	
	protected void updateVisual() { drawColor = isTypeEnabled ? 0xffd800 : 0xb2b2b2; }
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		System.out.println(object);
	}
}
