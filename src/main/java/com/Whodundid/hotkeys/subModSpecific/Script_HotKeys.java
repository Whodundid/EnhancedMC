package com.Whodundid.hotkeys.subModSpecific;

import com.Whodundid.hotkeys.HotKeyManager;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import com.Whodundid.hotkeys.control.hotKeyUtil.SubModHotKeys;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.miscUtil.ChatBuilder;
import com.Whodundid.scripts.ScriptManager;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

//Last edited: 10-16-18
//First Added: 11-16-17
//Author: Hunter Bragg

public class Script_HotKeys extends SubModHotKeys {
	
	static ScriptManager scriptMan = (ScriptManager) RegisteredSubMods.getMod(SubModType.SCRIPTS);
	
	public Script_HotKeys(HotKeyManager manIn) {
		man = manIn;
		subModName = "script";
	}

	@Override protected void addKeys() {
		keys.add(new HotKey("killScript", new KeyComboAction(Keyboard.KEY_EQUALS), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Toggles the script global kill value. If global kill is true, all actively running scripts are killed."); }
			@Override public void executeHotKeyAction() {
				scriptMan = (ScriptManager) RegisteredSubMods.getMod(SubModType.SCRIPTS);
				scriptMan.toggleGlobalKill();
				//if (!scriptMan.getCurrentlyRunningScripts().isEmpty()) { scriptMan.removeAllCurrentlyRunningScripts(); }
				mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.GREEN + "Global Kill Status: " + scriptMan.getGlobalKill()).build());
			}
		});
	}
}