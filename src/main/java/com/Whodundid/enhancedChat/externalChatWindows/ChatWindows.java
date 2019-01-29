package com.Whodundid.enhancedChat.externalChatWindows;

import com.Whodundid.enhancedChat.chatUtil.ChatType;
import com.Whodundid.main.util.storageUtil.EArrayList;
import java.util.Iterator;

//Last edited: Oct 26, 2018
//First Added: Oct 26, 2018
//Author: Hunter Bragg

public class ChatWindows {
	
	private static final EArrayList<ChatWindowFrame> windows = new EArrayList();
	
	public static void openChatWindow(ChatType type) {
		if (!isChatOpen(type)) {
			Thread windowBuilder = new Thread() {
				@Override public void run() { windows.add(new ChatWindowFrame(type)); }
			};
			windowBuilder.start();
		} else { getChatWindow(type).getInstance().toFront(); }
	}
	
	public static void closeChatWindow(ChatType type) {
		if (isChatOpen(type)) {
			ChatWindowFrame f = getChatWindow(type);
			Iterator<ChatWindowFrame> it = windows.iterator();
			while (it.hasNext()) {
				if (f.equals(it.next())) { it.remove(); }
			}
			f.killInstance();
		}
	}
	
	public static boolean isChatOpen(ChatType type) {
		for (ChatWindowFrame f : windows) {
			if (f.getChatType().equals(type)) { return true; }
		}
		return false;
	}
	
	public static ChatWindowFrame getChatWindow(ChatType type) {
		if (isChatOpen(type)) {
			for (ChatWindowFrame f : windows) {
				if (f.getChatType().equals(type)) { return f; }
			}
		}
		return null;
	}
}
