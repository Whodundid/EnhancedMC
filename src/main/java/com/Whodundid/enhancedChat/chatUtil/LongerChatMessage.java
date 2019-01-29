package com.Whodundid.enhancedChat.chatUtil;

import com.Whodundid.main.MainMod;
import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

//Last edited: Oct 20, 2018
//First Added: Oct 20, 2018
//Author: Hunter Bragg

public class LongerChatMessage {
	
	static Minecraft mc = MainMod.getMC();
	private C01PacketChatMessage message;
	private String m = "";
	
	public LongerChatMessage() { this(""); }
	public LongerChatMessage(String messageIn) {
		m = messageIn;
		buildMessage();
	}
	
	private void buildMessage() {
		message = new C01PacketChatMessage(m);
		if (m.length() > 100) {
			try {
				Field f = message.getClass().getDeclaredField("message");
				f.setAccessible(true);
				f.set(message, m);
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	public void sendMessage() {
		if (m.length() > 0) { sendMessage(null); }
		else { System.out.println("emptyMessage"); }
	}
	
	public void sendMessage(String messageIn) {
		if (messageIn != null) {
			m = messageIn;
			buildMessage();
		}
		if (mc.thePlayer != null) {	mc.thePlayer.sendQueue.addToSendQueue(message); }
	}
}
