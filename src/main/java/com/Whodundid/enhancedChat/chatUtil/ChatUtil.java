package com.Whodundid.enhancedChat.chatUtil;

import com.Whodundid.main.MainMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

//Last edited: 10-20-18
//First Added: 3-22-18
//Author: Hunter Bragg

public class ChatUtil {
	
	static Minecraft mc = MainMod.getMC();
	private static boolean chatOpen = false;
	private static IChatComponent lastChat = null;
	
	public void checkIfChatWindowOpen() { chatOpen = mc.ingameGUI.getChatGUI().getChatOpen(); }
	public static boolean isChatOpen() { return chatOpen; }
	public static void readChat(IChatComponent chatMsg) { lastChat = chatMsg; }
	public static String getLastChatMsgFormatted() { return lastChat.getFormattedText(); }
	public static String getLastChatMsgUnformatted() { return lastChat.getUnformattedText(); }
	public static void sendLongerChatMessage(String messageIn) { new LongerChatMessage(messageIn).sendMessage(); }
}
