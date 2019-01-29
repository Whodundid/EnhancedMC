package com.Whodundid.enhancedChat.gameChat;

import com.Whodundid.enhancedChat.ChatOrganizer;
import com.Whodundid.enhancedChat.EnhancedChat;
import com.Whodundid.enhancedChat.chatUtil.ChatType;
import com.Whodundid.enhancedChat.chatUtil.ChatUtil;
import com.Whodundid.enhancedChat.chatUtil.ChatWindowHeader;
import com.Whodundid.enhancedChat.chatUtil.TimedChatLine;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.guiObjectUtil.EObjectGroup;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiScrollBar;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiTextField;
import com.Whodundid.main.util.enhancedGui.guiObjects.InnerEnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiUtil.FocusEvent;
import com.Whodundid.main.util.enhancedGui.guiUtil.FocusType;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.main.util.miscUtil.ChatLineWrapper;
import com.Whodundid.main.util.playerUtil.PlayerFacing.Direction;
import com.Whodundid.main.util.storageUtil.EArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.ClientCommandHandler;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

//Jan 13, 2019
//Jan 21, 2019
//Last edited: Jan 22, 2019
//First Added: Dec 17, 2018
//Author: Hunter Bragg

public class InGameChatWindow extends InnerEnhancedGui {
	
	public EnhancedChat chatMod = (EnhancedChat) RegisteredSubMods.getMod(SubModType.ENHANCEDCHAT);
	public ChatOrganizer chatOrganizer = chatMod.getChatOrganizer();
	public EArrayList<String> sentWindowHistory = new EArrayList();
	public EArrayList<TimedChatLine> windowHistory = new EArrayList();
	public EArrayList<TimedChatLine> totalLines = new EArrayList();
	protected EArrayList<ChatType> selectedChats = new EArrayList();
	protected ChatWindowHeader header;
	protected EGuiTextField entryField;
	protected EGuiScrollBar scrollBar;
	protected String recipientName = "", resizeText = "";
	protected int scrollPos = 0, updateCounter = 0;
	protected boolean pinned = false;
	protected boolean loadChatsOnInit = false;
	
	public InGameChatWindow(IEnhancedGuiObject parentIn) { this(parentIn, ChatType.ALL); }
	public InGameChatWindow(IEnhancedGuiObject parentIn, ChatType... typesIn) {
		ScaledResolution res = new ScaledResolution(mc);
		init(parentIn, 1, res.getScaledHeight() - 4 - 240, 375, 242);
		setResizeable(true);
		setMinimumWidth(281).setMinimumHeight(26);
		selectedChats.add(typesIn);
		EArrayList<ChatType> types = new EArrayList<ChatType>(typesIn);
		if (!types.contains(ChatType.NONE)) {
			if (types.contains(ChatType.ALL)) { totalLines.addAll(chatOrganizer.getChat(ChatType.ALL)); }
			else { for (ChatType t : types) { totalLines.addAll(chatOrganizer.getChat(t)); } }
			resizeLines();
		}
	}

	@Override
	public void initObjects() {
		header = new ChatWindowHeader(this, selectedChats);
		header.setMainColor(0xd0727272);
		header.setMinimumWidth(280);
		
		entryField = new EGuiTextField(this, startX + 2, endY - 11, width - 9, 10) {
			@Override
			public void keyPressed(char typedChar, int keyCode) {
				super.keyPressed(typedChar, keyCode);
				if (keyCode == 28) {
					if (!entryField.getText().isEmpty()) { sendChatMessage(entryField.getText()); }
					entryField.setText("");
					resizeText = "";
				} else { resizeText = entryField.getText(); }
			}
		};
		entryField.setEnableBackgroundDrawing(false);
		entryField.setMaxStringLength(256);
		entryField.setAlwaysDrawCursor(true);
		entryField.setMinimumWidth(291);
		entryField.setText(resizeText);
		
		scrollBar = new EGuiScrollBar(this, getLineCount(), windowHistory.size(), 2, height - entryField.height - 1, true, Direction.W);
		scrollBar.setMinimumHeight(100 - (header.height + entryField.height - 1));
		int scrollBarPos = windowHistory.size() - scrollPos;
		scrollBarPos = scrollBarPos < getLineCount() ? getLineCount() : scrollBarPos;
		scrollBar.setScrollBarPos(scrollBarPos);
		
		EObjectGroup group = new EObjectGroup(getParent());
		group.addObject(this, header, entryField, scrollBar);
		setObjectGroup(group);
		
		addObject(header, entryField, scrollBar);
		
		if (loadChatsOnInit) { header.updateChatTabs(); loadChatsOnInit = false; }
		hasBeenInitialized = true;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawRect(startX + (chatMod.getShowTimeStamps() ? 47 : 1), startY + 1, endX - 1, endY - 12 - 3, 0x88000000); //main chat
		if (chatMod.getShowTimeStamps()) { drawRect(startX + 1, startY + 1, startX + 47, endY - 12 - 3, 0xdd000000); } //timestamp area
		
		drawRect(startX, startY, startX + 1, endY, 0xff000000); //right black bar
		drawRect(endX - 1, startY, endX, endY, 0xff000000); //left black bar
		drawRect(startX, endY - 1, endX, endY, 0xff000000); //top black bar
		drawRect(startX, endY - 15, endX, endY - 14, 0xff000000); //bottom black bar
		
		drawRect(startX + 1, endY - 14, endX - 1, endY - 1, 0x44000000);
		
		scrollPos = (windowHistory.size() - scrollBar.getScrollPos());
		//System.out.println("this one: " + scrollPos);
		scrollPos = scrollPos < 0 ? 0 : scrollPos;
		scrollBar.setVisible(windowHistory.size() > getLineCount());
		//System.out.println(windowHistory.size() - scrollBar.getScrollPos());
		
		drawChat();
		
		drawRect(startX + 1, startY, endX, startY + 1, 0xff000000); //top black bar
		
		super.drawObject(mXIn, mYIn, ticks);
		
		IChatComponent c = getChatComponent(Mouse.getX(), Mouse.getY());
		if (c != null) { handleComponentHover(c, mXIn, mYIn); }
	}
	
	@Override
	public void updateScreen() {
		updateCounter++;
		super.updateScreen();
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		entryField.requestFocus();
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		if (hasFocus()) { clickOnChat(button); }
		if (button == 1) { System.out.println(windowHistory.size() + " " + getLineCount()); }
		super.mousePressed(mX, mY, button);
	}
	
	@Override
	public void mouseScrolled(int change) {
		scroll(change);
		super.mouseScrolled(change);
	}
	
	@Override
	public void setText(String textIn, boolean overwrite) {
		if (overwrite) { entryField.setText(textIn); }
		else { entryField.setText(entryField.text + textIn); }
	}
	
	@Override public void onFocusGained(FocusEvent eventIn) {
		if (eventIn.type.equals(FocusType.mousePress)) { mousePressed(eventIn.mX, eventIn.mY, eventIn.actionCode); }
		else { entryField.requestFocus(); }
	}
	
	@Override
	public InGameChatWindow resize(int xIn, int yIn, Direction areaIn) {
		super.resize(xIn, yIn, areaIn);
		if (xIn != 0 || yIn != 0) { resizeLines(); }
		return this;
	}
	
	@Override
	public InGameChatWindow setDimensions(int startXIn, int startYIn, int widthIn, int heightIn) {
		super.setDimensions(startXIn, startYIn, widthIn, heightIn);
		resizeLines();
		return this;
	}
	
	@Override
	public InGameChatWindow resetPosition() {
		setDimensions(startXPos, startYPos, startWidth, startHeight);
		reInitObjects();
		resizeLines();
		return this;
	}
	
	protected void resizeLines() {
		windowHistory.clear();
		int i = MathHelper.floor_float(getChatWidth() / mc.gameSettings.chatScale);
		for (int q = totalLines.size() - 1; q >= 0; q--) {
			TimedChatLine l = totalLines.get(q);
			List<IChatComponent> list = ChatLineWrapper.makeList(l.getChatComponent(), i, false, false);
			for (IChatComponent c : list) {
				TimedChatLine newLine = new TimedChatLine(l.getUpdatedCounter(), c, l.getChatLineID()).setTimeStamp(l.getTimeStamp());
				windowHistory.add(0, newLine);
			}
		}
	}
	
	protected void drawChat() {
		GlStateManager.pushMatrix();
		GlStateManager.popMatrix();
		float scale = mc.gameSettings.chatScale;
		if (windowHistory.size() > 0) {
			if (windowHistory.size() < getLineCount()) {
				int q = 0;
				//System.out.println("a");
				for (int i = windowHistory.size() - 1; i >= 0; i--) {
					TimedChatLine line = windowHistory.get(i + scrollPos);
					if (chatMod.getShowTimeStamps()) { drawString(line.getTimeStamp(), startX + 4, startY + 3 + q * 9, 0x888888); }
					drawStringWithShadow(line.getChatComponent().getFormattedText(), startX + (chatMod.getShowTimeStamps() ? 50 : 5), startY + 3 + q * 9, 0xffffff);
					q++;
				}
			} else {
				//System.out.println("b");
				for (int i = 0; i + scrollPos < windowHistory.size() && i < getLineCount(); i++) {
					TimedChatLine line = windowHistory.get(i + scrollPos);
					if (chatMod.getShowTimeStamps()) {
						int xPos = startX + (windowHistory.size() == getLineCount() ? 4 : 5);
						drawString(line.getTimeStamp(), xPos, endY - 24 - i * 9, 0x888888);
					}
					drawStringWithShadow(line.getChatComponent().getFormattedText(), startX + (chatMod.getShowTimeStamps() ? 50 : 5), endY - 24 - i * 9, 0xffffff);
				}
			}
		}
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
	}
	
	protected void clickOnChat(int button) {
		if (button == 0) {
            IChatComponent ichatcomponent = getChatComponent(Mouse.getX(), Mouse.getY());
            if (handleComponentClick(ichatcomponent)) {
            	entryField.requestFocus();
            	return;
            }
        }
	}
	
	public void scroll(int change) {
		if (!isShiftKeyDown()) { change *= 7; }
		scrollPos += change;
		int i = windowHistory.size();
		if (scrollPos <= 0) { scrollPos = 0; }
		int barChange = windowHistory.size() - scrollPos;
		barChange = barChange < getLineCount() ? getLineCount() : barChange;
		scrollBar.setScrollBarPos(barChange);
	}
	
	public void clearChatMessages() {
		windowHistory.clear();
		totalLines.clear();
		sentWindowHistory.clear();
	}
	
	public void addChatLine(TimedChatLine lineIn) {
		int i = MathHelper.floor_float(getChatWidth() / mc.gameSettings.chatScale);
		List<IChatComponent> list = ChatLineWrapper.makeList(lineIn.getChatComponent(), i, false, false);
		boolean flag = scrollPos > 0;
		for (IChatComponent ichatcomponent : list) {
			if (flag && scrollPos > 0) { scroll(0); }
			TimedChatLine l = new TimedChatLine(updateCounter, ichatcomponent, lineIn.getChatLineID()).setTimeStamp(lineIn.getTimeStamp());
			windowHistory.add(0, l);
			scrollBar.setHighVal(windowHistory.size());
			if (scrollPos == 0) { scrollBar.setScrollBarPos(windowHistory.size()); }
		}
		totalLines.add(0, lineIn);
	}
	
	public void printChatMessageWithOptionalDeletion(IChatComponent chatComponent, int chatLineId) {
		setChatLine(chatComponent, chatLineId, mc.ingameGUI.getUpdateCounter(), false);
		//logger.info("[CHAT] " + chatComponent.getUnformattedText());
	}

	private void setChatLine(IChatComponent chatComponent, int chatLineId, int updateCounter, boolean deletion) {
		if (chatLineId != 0) { deleteChatLine(chatLineId); }
		int i = MathHelper.floor_float(getChatWidth() / mc.gameSettings.chatScale);
		List<IChatComponent> list = ChatLineWrapper.makeList(chatComponent, i, false, false);
		boolean flag = scrollPos > 0;
		for (IChatComponent ichatcomponent : list) {
			if (flag && scrollPos > 0) { scroll(0); }
			TimedChatLine l = new TimedChatLine(updateCounter, ichatcomponent, chatLineId);
			windowHistory.add(0, l);
			scrollBar.setHighVal(windowHistory.size());
			if (scrollPos == 0) { scrollBar.setScrollBarPos(windowHistory.size()); }
		}
		if (!deletion) {
			totalLines.add(0, new TimedChatLine(updateCounter, chatComponent, chatLineId));
		}
	}

	public void refreshChat() {
		windowHistory.clear();
		resetScroll();
		for (int i = totalLines.size() - 1; i >= 0; --i) {
			TimedChatLine chatline = totalLines.get(i);
			setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
		}
	}
	
	public void addToSentMessages(String msgIn) {
		if (sentWindowHistory.isEmpty() || !sentWindowHistory.get(sentWindowHistory.size() - 1).equals(msgIn)) { sentWindowHistory.add(msgIn); }
	}
	
	public void resetScroll() { scrollPos = 0; scrollBar.setScrollBarPos(getLineCount()); }
	
	public void deleteChatLine(int lineIdIn) {
		Iterator<TimedChatLine> iterator = windowHistory.iterator();
		while (iterator.hasNext()) {
			TimedChatLine chatline = iterator.next();
			if (chatline.getChatLineID() == lineIdIn) {
				iterator.remove();
			}
		}
		iterator = totalLines.iterator();
		while (iterator.hasNext()) {
			TimedChatLine chatline1 = iterator.next();
			if (chatline1.getChatLineID() == lineIdIn) {
				iterator.remove();
				break;
			}
		}
	}
	
	public IChatComponent getChatComponent(int mXIn, int mYIn) {
		ScaledResolution res = new ScaledResolution(mc);
		int i = res.getScaleFactor();
		float f = mc.gameSettings.chatScale;
		int j = mXIn / i - startX - (chatMod.getShowTimeStamps() ? 50 : 3); //timestamp bar width
		int k = (Display.getHeight() - mYIn) / i - startY;
		k = windowHistory.size() > getLineCount() ? (height - k - entryField.height - 7) : k - 7;
		
		k = (k > height - entryField.height - 7) ? -1 : k;
		j = MathHelper.floor_float(j / f);
		k = MathHelper.floor_float(k / f);
		
		if (j >= 0 && k >= 0) {
			int l = Math.min(getLineCount(), windowHistory.size());
			if (j <= endX - 1 && k < mc.fontRendererObj.FONT_HEIGHT * l + l) {
				int i1 = k / mc.fontRendererObj.FONT_HEIGHT + scrollPos;
				i1 = windowHistory.size() <= getLineCount() ? windowHistory.size() - 1 - i1 : i1;
				if (i1 >= 0 && i1 < windowHistory.size()) {
					TimedChatLine chatLine = windowHistory.get(i1);
					int j1 = 0;
					for (IChatComponent ichatcomponent : chatLine.getChatComponent()) {
						if (ichatcomponent instanceof ChatComponentText) {
							j1 += mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText) ichatcomponent).getChatComponentText_TextValue(), false));
							if (j1 > j) {
								//System.out.println(ichatcomponent);
								return ichatcomponent;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	public InGameChatWindow setChatTypes(ChatType... typesIn) { return setChatTypes(new EArrayList<ChatType>().addAll(typesIn)); }
	public InGameChatWindow setChatTypes(EArrayList<ChatType> typesIn) {
		selectedChats.clear();
		for (ChatType t : typesIn) {
			switch (t) {
			case ALL: selectedChats.add(ChatType.ALL); break;
			case GUILD: selectedChats.add(ChatType.GUILD); break;
			case LOBBY: selectedChats.add(ChatType.LOBBY); break;
			case PARTY: selectedChats.add(ChatType.PARTY); break;
			case PRIVATE: selectedChats.add(ChatType.PRIVATE); break;
			default: break;
			}
		}
		if (hasBeenInitialized()) { header.updateChatTabs(); }
		else { loadChatsOnInit = true; }
		return this;
	}
	
	@Override public ChatWindowHeader getHeader() { return header; }
	
	public EArrayList<ChatType> getEnabledChatTypes() { return header.getAllEnabledChatTypes(); }
	
	public void sendChatMessage(String msg) { sendChatMessage(msg, true); }
	public void sendChatMessage(String msg, boolean addToChat) {
        if (addToChat) { sentWindowHistory.add(msg); }
        if (ClientCommandHandler.instance.executeCommand(mc.thePlayer, msg) != 0) { return; }
        ChatUtil.sendLongerChatMessage(msg);
    }
	
	public void updateEnabledChatTypes() {
		if (hasBeenInitialized()) {
			totalLines.clear();
			windowHistory.clear();
			EArrayList<ChatType> enabledChats = header.getAllEnabledChatTypes();
			if (enabledChats.contains(ChatType.ALL)) { totalLines.addAll(chatOrganizer.getChat(ChatType.ALL)); }
			else {
				for (ChatType t : enabledChats) { totalLines.addAll(chatOrganizer.getChat(t)); }
				sortTotalLinesByTime();
			}
			resizeLines();
			scrollBar.setHighVal(windowHistory.size());
			scrollPos = scrollBar.getHighVal();
			scrollBar.setScrollBarPos(scrollPos);
		}
	}
	
	protected void sortTotalLinesByTime() {
		Collections.sort(totalLines);
		Collections.reverse(totalLines);
	}
	
	public InGameChatWindow updateInitText(String textIn) { resizeText = textIn; return this; }
	public void printChatMessage(IChatComponent chatComponent) { printChatMessageWithOptionalDeletion(chatComponent, 0); }
	public EArrayList<ChatType> getChatTypes() { return new EArrayList(selectedChats); }
	public int getChatWidth() { return width - 52; }
	public int getChatHeight() { return height - 12; }
	public int getLineCount() { return getChatHeight() / 9; }
	public EGuiTextField getEntryField() { return entryField; }
	public boolean isPinned() { return pinned; }
	public InGameChatWindow setPinned(boolean val) { pinned = val; return this; }
	public String getRecipientName() { return recipientName; }
	public InGameChatWindow setRecipientName(String nameIn) { recipientName = nameIn; return this; }
}
