package com.Whodundid.sls.util;

import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.sls.SkinSwitcher;

public class PartStats {
	
	static SkinSwitcher sls = (SkinSwitcher) RegisteredSubMods.getMod(SubModType.SLS);
	
	public static String getPartMode(LayerTypes type) {
		if (isPartSwitching(type)) { return "switching"; }
		else if (isPartBlinking(type)) { return "blinking"; }
		else { return "unspecified"; }
	}
	
	public static boolean getPartEnabled(LayerTypes type) { return sls.getPart(type).isEnabled(); }
	public static boolean getPartState(LayerTypes type) { return sls.getPart(type).getState(); }
	public static boolean isPartSwitching(LayerTypes type) { return sls.getPart(type).isSwitching(); }
	public static boolean isPartBlinking(LayerTypes type) { return sls.getPart(type).isBlinking(); }
	public static boolean doesPartHasOffset(LayerTypes type) { return sls.getPart(type).hasOffset(); }
	public static boolean isPartStateFlipped(LayerTypes type) { return sls.getPart(type).getStateFlipped(); }
	
	public static int getPartSwitchSpeed(LayerTypes type) { return sls.getPart(type).getSwitchSpeed(); }
	public static int getPartBlinkSpeed(LayerTypes type) { return sls.getPart(type).getBlinkSpeed(); }
	public static int getPartBlinkDuration(LayerTypes type) { return sls.getPart(type).getBlinkDuration(); }
	public static int getPartOffset(LayerTypes type) { return sls.getPart(type).getOffset(); }
}