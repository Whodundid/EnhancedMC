package com.Whodundid.hotkeys.control.hotKeyTypes;

import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import com.Whodundid.main.global.GlobalSettings;
import com.Whodundid.main.global.subMod.SubModType;

//Last edited: 9-30-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class ModDeactivatorHotKey extends HotKey {
	
	public SubModType modType;
	
	public ModDeactivatorHotKey(String keyNameIn, KeyComboAction keysIn, SubModType modIn) { this(keyNameIn, keysIn, modIn, false, "", null); }
	public ModDeactivatorHotKey(String keyNameIn, KeyComboAction keysIn, SubModType modIn, boolean builtInVal) { this(keyNameIn, keysIn, modIn, builtInVal, "", null); }
	public ModDeactivatorHotKey(String keyNameIn, KeyComboAction keysIn, SubModType modIn, String descriptionIn) { this(keyNameIn, keysIn, modIn, false, descriptionIn, null); }
	public ModDeactivatorHotKey(String keyNameIn, KeyComboAction keysIn, SubModType modIn, boolean builtInVal, String descriptionIn, String builtInSubModTypeIn) {
		super(keyNameIn, keysIn, builtInVal, KeyActionType.MOD_DEACTIVATOR, builtInSubModTypeIn);
		if (descriptionIn != null && !descriptionIn.isEmpty()) { description = descriptionIn; }
		modType = modIn;
	}
	
	public SubModType getSubMod() { return modType; }
	
	@Override public void executeHotKeyAction() { GlobalSettings.updateSetting(modType, false); }
	
	@Override public String getHotKeyStatistics() {
		String base = super.getHotKeyStatistics();
		base += ("; " + SubModType.getModName(modType));
		return base;
	}
}
