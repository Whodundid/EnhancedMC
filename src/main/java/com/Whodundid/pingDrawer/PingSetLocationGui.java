package com.Whodundid.pingDrawer;

import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;

//Last edited: Dec 10, 2018
//First Added: Dec 10, 2018
//Author: Hunter Bragg

public class PingSetLocationGui extends EnhancedGui {

	Ping pingMod = (Ping) RegisteredSubMods.getMod(SubModType.PING);
	
	public PingSetLocationGui() { super(); }
	public PingSetLocationGui(EnhancedGui oldGui) { super(oldGui); }
	public PingSetLocationGui(int posX, int posY) { super(posX, posY); }
	public PingSetLocationGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	
	@Override
	public void initGui() {
		super.initGui();
		enableHeader(false);
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		drawCenteredString("Move to desired location.", wPos, hPos - 25, 0x55ff55);
		drawCenteredString("Press left click to confirm, Esc to cancel.", wPos, hPos - 15, 0x55ff55);
		String msg = pingMod.doesClientHavePing() ? "PING: " + pingMod.getClientServerPing() + " ms" : "Calculating..";
		int l = fontRendererObj.getStringWidth(msg);
		drawRect(mX, mY + 1, mX + l + 1, mY - 9, Integer.MIN_VALUE);
		fontRendererObj.drawString(msg, mX + 1, mY - 8, 0x00ff00);
	}
	
	@Override
	public void mouseClicked(int mX, int mY, int button) {
		if (button == 0) {
			pingMod.setLocation(mX, mY + 1);
			pingMod.getConfig().saveConfig();
			closeGui();
		}
	}
}
