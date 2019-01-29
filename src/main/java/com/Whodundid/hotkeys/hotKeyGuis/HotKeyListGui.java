package com.Whodundid.hotkeys.hotKeyGuis;

import com.Whodundid.debug.IDebugCommand;
import com.Whodundid.hotkeys.HotKeyManager;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.CommandHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.DebugHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.GuiOpenerHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.HandHeldCommandHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ModActivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ModDeactivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ScriptHotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjectUtil.TextAreaLine;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiDialogueBox;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiTextArea;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiDialogueBox.DialogueBoxTypes;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiLabel;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.miscUtil.EUtil;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

//Last edited: Jan 7, 2019
//First Added: Jan 7, 2019
//Author: Hunter Bragg

public class HotKeyListGui extends EnhancedGui {
	
	HotKeyManager mod = (HotKeyManager) RegisteredSubMods.getMod(SubModType.HOTKEYS);
	EGuiTextArea keyList;
	EGuiButton edit, sortList, delete, toggleEnabled;
	EGuiDialogueBox msgBox;
	SubMod selectedMod;
	HotKey currentKey;
	boolean hasCategory = false, hasArg1 = false, hasArg2 = false, hasDescription = false;
	String keyName = "";
	String keyType = "";
	String keys = "";
	String keyCategory = "";
	String keyArg1String = "", keyArg1 = "";
	String keyArg2String = "", keyArg2 = "";
	EGuiLabel desc;
	int listVerticalPos = 0;
	
	public HotKeyListGui() { super(); }
	public HotKeyListGui(SubMod modIn) { super(); selectedMod = modIn; }
	public HotKeyListGui(EnhancedGui oldGui) { super(oldGui); }
	public HotKeyListGui(EnhancedGui oldGui, SubMod modIn) { super(oldGui); selectedMod = modIn; }
	public HotKeyListGui(int posX, int posY) { super(posX, posY); }
	public HotKeyListGui(int posX, int posY, SubMod modIn) { super(posX, posY); selectedMod = modIn; }
	public HotKeyListGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	public HotKeyListGui(int posX, int posY, EnhancedGui oldGui, SubMod modIn) { super(posX, posY, oldGui); selectedMod = modIn; }
	
	@Override
	public void initGui() {
		centerGuiWithDimensions(450, 275);
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		edit = new EGuiButton(this, startX + 9, endY - 28, 70, 20, "Edit Key");
		delete = new EGuiButton(this, startX + 175, endY - 28, 70, 20, "Delete Key");
		toggleEnabled = new EGuiButton(this, startX + 92, endY - 28, 70, 20, "");
		sortList = new EGuiButton(this, endX - 178, endY - 28, 150, 20, "Sort list by..");
		desc = new EGuiLabel(this, startX + 28, startY + 205, "").enableWordWrap(true, 208);
		
		toggleEnabled.setVisible(false);
		
		keyList = new EGuiTextArea(this, endX - 198, startY + 20, 190, 220).setDrawLineNumbers(true);
		
		addObject(edit, delete, toggleEnabled, sortList, keyList, desc);
		
		buildKeyList();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawDefaultBackground();
		
		drawCenteredStringWithShadow("Registered Hotkeys", endX - keyList.width / 2 - 8, startY + 7, 0xb2b2b2);
		drawCenteredStringWithShadow("Selected HotKey's Values", startX + 126, startY + 7, 0xb2b2b2);
		
		drawRect(startX + 9, startY + 20, startX + 245, endY - 35, 0xff000000);
		drawRect(startX + 10, startY + 21, startX + 244, endY - 36, 0xff2D2D2D);
		
		if (keyList.getCurrentLine() != null && keyList.getCurrentLine().getStoredObj() != null) {
			HotKey k = (HotKey) keyList.getCurrentLine().getStoredObj();
			loadKeyValues(k);
			drawRect(startX + 10, startY + 48, startX + 244, startY + 49, 0xff000000);
			drawRect(startX + 10, startY + 76, startX + 244, startY + 77, 0xff000000);
			drawRect(startX + 10, startY + 104, startX + 244, startY + 105, 0xff000000);
			drawRect(startX + 10, startY + 132, startX + 244, startY + 133, 0xff000000);
			if (hasArg2) { drawRect(startX + 10, startY + 160, startX + 244, startY + 161, 0xff000000); }
			drawRect(startX + 10, startY + 188, startX + 244, startY + 189, 0xff000000);
		} else { resetValues(); }
		
		drawKeyValues();
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(edit)) {
			if (keyList.getCurrentLine() != null && keyList.getCurrentLine().getStoredObj() != null) {
				HotKey key = (HotKey) keyList.getCurrentLine().getStoredObj();
				mc.displayGuiScreen(new HotKeyCreatorGui(this, key));
			}
		}
		if (object.equals(delete)) {
			if (keyList.getCurrentLine() != null && keyList.getCurrentLine().getStoredObj() != null) {
				HotKey key = (HotKey) keyList.getCurrentLine().getStoredObj();
				if (key.isBuiltIn()) {
					msgBox = new EGuiDialogueBox(guiInstance, wPos - 125, hPos - 48, 250, 75, DialogueBoxTypes.ok);
					msgBox.setDisplayString("HotKey Deletion Error");
					msgBox.setMessage("Cannot delete a built in hotkey.").setMessageColor(0xff5555);
					guiInstance.addObject(msgBox);
				}
			}
		}
		if (object.equals(toggleEnabled)) {
			listVerticalPos = keyList.getCurrentVerticalPos();
			Object storredObject = null;
			if (keyList.getCurrentLine() != null && keyList.getCurrentLine().getStoredObj() != null) {
				storredObject = keyList.getCurrentLine().getStoredObj();
				HotKey k = (HotKey) storredObject;
				k.setEnabled(!k.isEnabled());
			}
			resetValues();
			buildKeyList();
			TextAreaLine l = keyList.getLineWithObject(storredObject);
			if (l != null) { keyList.setSelectedLine(l); }
		}
	}
	
	protected void buildKeyList() {
		keyList.clear();
		boolean anyDisabled = false;
		boolean anyEnabled = false;
		for (HotKey k : mod.getRegisteredHotKeys()) {
			if (!k.isEnabled()) { anyDisabled = true; break; }
		}
		for (HotKey k : mod.getRegisteredHotKeys()) {
			if (k.isEnabled()) { anyEnabled = true; break; }
		}
		if (anyDisabled) { keyList.addTextLine("Disabled HotKeys:").setLineNumberColor(0xb2b2b2).setTextColor(0x00ffdc); }
		for (HotKey k : mod.getRegisteredHotKeys()) {
			if (!k.isEnabled()) {
				keyList.addTextLine("   " + EnumChatFormatting.RED + k.getKeyName(), k).setLineNumberColor(0xb2b2b2);
			}
		}
		if (anyEnabled) { 
			TextAreaLine l = keyList.addTextLine("Enabled HotKeys:");
			l.setLineNumberColor(0xb2b2b2).setTextColor(0x00ffdc);
		}
		for (HotKey k : mod.getRegisteredHotKeys()) {
			if (k.isEnabled()) {
				keyList.addTextLine("   " + EnumChatFormatting.GREEN + k.getKeyName(), k).setLineNumberColor(0xb2b2b2);
			}
		}
		//keyList.setDocumentVerticalPos(listVerticalPos);
	}
	
	protected void drawKeyValues() {
		if (!keyName.isEmpty()) { drawStringWithShadow("Name:", startX + 14, startY + 24, 0xffbb00); }
		else {
			drawCenteredStringWithShadow("Click on a hotkey from the registered", startX + 126, startY + 120, 0xffbb00);
			drawCenteredStringWithShadow("hotkeys list to see its values.", startX + 126, startY + 132, 0xffbb00);
		}
		if (!keyType.isEmpty()) {
			drawStringWithShadow("Type:", startX + 14, startY + 52, 0xffbb00);
			drawStringWithShadow("Keys:", startX + 14, startY + 80, 0xffbb00);
			drawStringWithShadow("Category:", startX + 14, startY + 108, 0xffbb00);
			if (!keyArg1String.isEmpty()) { drawStringWithShadow(keyArg1String, startX + 14, startY + 136, 0xffbb00); }
			else { drawStringWithShadow("", startX + 14, startY + 136, 0xb2b2b2); }
			if (!keyArg2String.isEmpty()) { drawStringWithShadow(keyArg2String, startX + 14, startY + 164, 0xffbb00); }
			else { drawStringWithShadow("", startX + 14, startY + 164, 0xb2b2b2); }
			drawStringWithShadow("Description:", startX + 14, startY + 192, 0xffbb00);
			
			drawStringWithShadow(keyName, startX + 28, startY + 37, 0x00ffdc);
			drawStringWithShadow(keyType, startX + 28, startY + 65, 0x00ffdc);
			if (!keys.isEmpty()) { drawStringWithShadow(keys, startX + 28, startY + 93, 0x00ffdc); }
			else { drawStringWithShadow("No keys set.", startX + 28, startY + 93, 0xb2b2b2); }
			if (hasCategory) { drawStringWithShadow(keyCategory, startX + 28, startY + 121, 0x00ffdc); }
			else { drawStringWithShadow("No category.", startX + 28, startY + 121, 0xb2b2b2); }
			if (hasArg1) { drawStringWithShadow(keyArg1, startX + 28, startY + 149, 0x00ffdc); }
			else { drawStringWithShadow("", startX + 28, startY + 149, 0xb2b2b2); }
			if (hasArg2) { drawStringWithShadow(keys, startX + 28, startY + 177, 0x00ffdc); }
			else { drawStringWithShadow("", startX + 28, startY + 177, 0xb2b2b2); }
			if (!hasDescription) { drawStringWithShadow("No description set.", startX + 28, startY + 205, 0xb2b2b2); }
		}
	}
	
	public void loadKeyValues(HotKey keyIn) {
		resetValues();
		if (keyIn != null) {
			hasCategory = false; hasArg1 = false; hasArg2 = false; hasDescription = false;
			KeyActionType type = keyIn.getHotKeyType();
			
			keyName = keyIn.getKeyName();
			keyType = KeyActionType.getStringFromType(keyIn.getHotKeyType());
			
			keys = EUtil.keysToString(keyIn.getKeyCombo().getKeys());
			
			switch (type) {
			case BUILTINCODE: break;
			case COMMAND:
				hasArg1 = true;
				keyArg1String = "Command:";
				keyArg1 = ((CommandHotKey) keyIn).getCommand();
				break;
			case COMMAND_PLAYER_HANDHELD_CONDITION:
				hasArg1 = true;
				keyArg1String = "Command:";
				keyArg1 = ((HandHeldCommandHotKey) keyIn).getCommand();
				hasArg2 = true;
				keyArg2String = "Item id";
				keyArg2 = ((HandHeldCommandHotKey) keyIn).getItemID() + "";
				break;
			case DEBUG:
				hasArg1 = true;
				keyArg1String = "Debug Command:";
				keyArg1 = IDebugCommand.getDebugCommandName(((DebugHotKey) keyIn).getDebugFunction());
				break;
			case GUI_OPENER:
				hasArg1 = true;
				keyArg1String = "Gui to be opened:";
				keyArg1 = ((GuiOpenerHotKey) keyIn).getGuiDisplayName();
				break;
			case MC_KEYBIND_MODIFIER: break;
			case MOD_ACTIVATOR:
				hasArg1 = true;
				keyArg1String = "SubMod to be activated:";
				keyArg1 = SubModType.getModName(((ModActivatorHotKey) keyIn).getSubMod());
				break;	
			case MOD_DEACTIVATOR:
				hasArg1 = true;
				keyArg1String = "SubMod to be deactivated:";
				keyArg1 = SubModType.getModName(((ModDeactivatorHotKey) keyIn).getSubMod());
			case SCRIPT:
				hasArg1 = true;
				keyArg1String = "Script to be run:";
				keyArg1 = ((ScriptHotKey) keyIn).getScript().getScriptName();
				hasArg2 = true;
				keyArg2String = "Script arguments:";
				keyArg2 = ((ScriptHotKey) keyIn).getScriptArgs() + "";
				break;
			case UNDEFINED: break;
			default: break;
			}
			
			if (!keyIn.getKeyDescription().isEmpty() && !keyIn.getKeyDescription().equals("No description set.")) {
				hasDescription = true;
				desc.setDisplayString(keyIn.getKeyDescription()).setDisplayStringColor(0x00ffdc);
			}
			
			toggleEnabled.setVisible(true);
			toggleEnabled.setDisplayString(keyIn.isEnabled() ? "Enabled" : "Disabled");
			toggleEnabled.setDisplayStringColor(keyIn.isEnabled() ? 0x55ff55 : 0xff5555);
		}
	}
	
	public void resetValues() {
		keyName = "";
		keyType = "";
		keys = "";
		keyCategory = "";
		keyArg1String = "";
		keyArg2String = "";
		desc.setDisplayString("");
		toggleEnabled.setVisible(false);
	}
	
	public void loadSubModsKeys(SubMod modIn) {
		
	}
	
	public void deleteKey(HotKey keyIn) {
		
	}
	
	public void reloadKeys() {
		
	}
	
	public void saveKeys() {
		
	}
	
	public void resetKeys() {
		
	}
}
