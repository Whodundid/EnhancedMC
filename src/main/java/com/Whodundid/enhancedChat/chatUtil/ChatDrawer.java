package com.Whodundid.enhancedChat.chatUtil;

import com.Whodundid.main.MainMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//Last edited: 10-16-18
//First Added: 4-15-17
//Author: Hunter Bragg

@SideOnly(Side.CLIENT)
public abstract class ChatDrawer {
	private static Minecraft mc = MainMod.getMC();
	static ScaledResolution res;
	static FontRenderer fontRender = mc.fontRendererObj;
	static String msg = "";
	static int x = 0;
	static int y = 0;
	static int color = 0;

	public static ScaledResolution getCurrentResoultion() { return res == null ? new ScaledResolution(mc) : res; }
	
	public static void updateTextRenderer() {
		res = new ScaledResolution(mc);
		if (msg != null || !msg.equals("")) { drawMsgOnScreen(); }
	}
	
	public static void clearAddedChat() { addChatToBeDrawnToScreen("", 0, 0, 0); }

	public static void addChatToBeDrawnToScreen(String textIn, int xPos, int yPos, int colorIn) {
		msg = textIn;
		x = xPos;
		y = yPos;
		color = colorIn;
	}

	private static void drawMsgOnScreen() { fontRender.drawStringWithShadow(msg, x, y, color); }
}