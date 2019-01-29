package com.Whodundid.main.util.miscUtil;

import com.Whodundid.enhancedChat.chatUtil.ChatUtil;
import com.Whodundid.main.util.storageUtil.Vector3D;

//Last edited: 10-15-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class WorldEditListener {
	
	static Vector3D pos1 = new Vector3D(), pos2 = new Vector3D();
	static boolean pos1Set = false, pos2Set = false;
	
	public static Vector3D getPos1() { return pos1; }
	public static Vector3D getPos2() { return pos2; }
	public static boolean posSet() { return (pos1Set && pos2Set); }
	public static void reset() { pos1.clear(); pos2.clear(); pos1Set = false; pos2Set = false; }
	
	public static void checkForPositions() {
		try {
			if (ChatUtil.getLastChatMsgUnformatted().contains("(BUILD) pos1 set to (")) {
				parse(true, ChatUtil.getLastChatMsgUnformatted().replace("(", "").replace(")", "").split(","));
			} else if (ChatUtil.getLastChatMsgUnformatted().contains("(BUILD) pos2 set to (")) {
				parse(false, ChatUtil.getLastChatMsgUnformatted().replace("(", "").replace(")", "").split(","));
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private static void parse(boolean isPos1, String[] in) {
		String iso = in[2].substring(1);
		String[] in2 = iso.split(" ");
		double x = Double.parseDouble(in[0].substring(18));
		double y = Double.parseDouble(in[1].substring(1));
		double z = Double.parseDouble(in2[0]);
		if (isPos1) { pos1.set(x, y, z); pos1Set = true; }
		else { pos2.set(x, y, z); pos2Set = true; }
	}
}
