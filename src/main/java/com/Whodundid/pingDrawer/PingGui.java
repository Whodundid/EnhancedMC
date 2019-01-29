package com.Whodundid.pingDrawer;

import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.guiObjects.EScreenLocationSelector;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;

//Last edited: Dec 10, 2018
//First Added: Dec 10, 2018
//Author: Hunter Bragg

public class PingGui extends EnhancedGui {

	Ping pingMod = (Ping) RegisteredSubMods.getMod(SubModType.PING);
	EGuiButton chatDraw;
	EScreenLocationSelector locationSelector;
	
	public PingGui() { super(); }
	public PingGui(EnhancedGui oldGui) { super(oldGui); }
	public PingGui(int posX, int posY) { super(posX, posY); }
	public PingGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		chatDraw = new EGuiButton(this, wPos - 90, hPos + 100, 52, 20, pingMod.drawWithChatOpen ? "True" : "False").setDisplayStringColor(pingMod.drawWithChatOpen ? 0x55ff55 : 0xff5555);
		locationSelector = new EScreenLocationSelector(this, pingMod, wPos - 78, hPos - 83, 156);
		locationSelector.setDisplayName("Ping");
		
		addObject(locationSelector, chatDraw);
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		drawDefaultBackground();
		drawString("Draw while chat is open", wPos - 30, hPos + 106, 0xffbb00);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(chatDraw)) {
			pingMod.setDrawWithChatOpen(!pingMod.drawWithChatOpen);
			chatDraw.displayString = pingMod.drawWithChatOpen ? "True" : "False";
			chatDraw.color = pingMod.drawWithChatOpen ? 0x55ff55 : 0xff4444;
		}
		pingMod.getConfig().saveConfig();
	}
}
