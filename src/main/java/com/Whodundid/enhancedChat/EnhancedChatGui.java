package com.Whodundid.enhancedChat;

import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;

//Last edited: Jan 18, 2019
//First Added: Jan 18, 2019
//Author: Hunter Bragg

public class EnhancedChatGui extends EnhancedGui {
	
	EnhancedChat chatMod = (EnhancedChat) RegisteredSubMods.getMod(SubModType.ENHANCEDCHAT);
	EGuiButton showChatInfo, showTimeStamps, enableChatWindows, enableChatOrganizer;
	
	public EnhancedChatGui() { super(); }
	public EnhancedChatGui(EnhancedGui oldGui) { super(oldGui); }
	public EnhancedChatGui(int posX, int posY) { super(posX, posY); }
	public EnhancedChatGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	
	@Override
	public void initGui() {
		centerGuiWithDimensions(256, 256);
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		showChatInfo = new EGuiButton(this, startX + 10, startY + 10, 55, 20);
		showTimeStamps = new EGuiButton(this, startX + 10, startY + 40, 55, 20);
		enableChatWindows = new EGuiButton(this, startX + 10, startY + 70, 55, 20);
		enableChatOrganizer = new EGuiButton(this, startX + 10, startY + 100, 55, 20);
		
		showChatInfo.setTrueFalseButton(true).updateTrueFalseDisplay(chatMod.getShowMoreChatInfo());
		showTimeStamps.setTrueFalseButton(true).updateTrueFalseDisplay(chatMod.getShowTimeStamps());
		enableChatWindows.setTrueFalseButton(true).updateTrueFalseDisplay(chatMod.getEnableChatWindows());
		enableChatOrganizer.setTrueFalseButton(true).updateTrueFalseDisplay(chatMod.getEnableChatOrganizer());
		
		addObject(showChatInfo, showTimeStamps, enableChatWindows, enableChatOrganizer);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawDefaultBackground();
		
		drawStringWithShadow("Display Extended Chat Information", showChatInfo.endX + 8, showChatInfo.midY - 4, 0xb2b2b2);
		drawStringWithShadow("Display Chat Timestamps", showTimeStamps.endX + 8, showTimeStamps.midY - 4, 0xb2b2b2);
		drawStringWithShadow("Enable Chat Windows", enableChatWindows.endX + 8, enableChatWindows.midY - 4, 0xb2b2b2);
		drawStringWithShadow("Enable Chat Organizer", enableChatOrganizer.endX + 8, enableChatOrganizer.midY - 4, 0xb2b2b2);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object == showChatInfo) {
			chatMod.setShowMoreChatInfo(!chatMod.getShowMoreChatInfo());
			showChatInfo.updateTrueFalseDisplay(chatMod.getShowMoreChatInfo());
		}
		if (object == showTimeStamps) {
			chatMod.setShowTimeStamps(!chatMod.getShowTimeStamps());
			showTimeStamps.updateTrueFalseDisplay(chatMod.getShowTimeStamps());
		}
		if (object == enableChatWindows) {
			chatMod.setShowChatWindows(!chatMod.getEnableChatWindows());
			enableChatWindows.updateTrueFalseDisplay(chatMod.getEnableChatWindows());
			if (!chatMod.getEnableChatWindows()) { MainMod.getInGameGui().removeAllChatWindows(); }
		}
		if (object == enableChatOrganizer) {
			chatMod.setEnableChatOrganizer(!chatMod.getEnableChatOrganizer());
			enableChatOrganizer.updateTrueFalseDisplay(chatMod.getEnableChatOrganizer());
		}
	}
}
