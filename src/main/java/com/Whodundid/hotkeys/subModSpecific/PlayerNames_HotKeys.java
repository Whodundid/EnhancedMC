package com.Whodundid.hotkeys.subModSpecific;

import com.Whodundid.hotkeys.HotKeyManager;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import com.Whodundid.hotkeys.control.hotKeyUtil.SubModHotKeys;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.nameHistory.NameHistory;
import org.lwjgl.input.Keyboard;

//Last edited: 10-16-18
//First Added: 11-16-17
//Author: Hunter Bragg

public class PlayerNames_HotKeys extends SubModHotKeys {
	
	static NameHistory names = (NameHistory) RegisteredSubMods.getMod(SubModType.NAMEHISTORY);
	
	public PlayerNames_HotKeys(HotKeyManager manIn) {
		man = manIn;
		subModName = "names";
	}

	@Override protected void addKeys() {
		keys.add(new HotKey("checkPlayerName", new KeyComboAction(Keyboard.KEY_N), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Checks the previous name history of the player under the cross hair."); }
			@Override public void executeHotKeyAction() { names.startFetchingHistory(null, true, false); }
		});
	}
}
