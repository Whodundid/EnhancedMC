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
import com.Whodundid.hotkeys.control.hotKeyUtil.*;
import com.Whodundid.hotkeys.keySaveLoad.HotKeyBuilder;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.*;
import com.Whodundid.main.util.enhancedGui.guiObjects.*;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiDialogueBox.DialogueBoxTypes;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.miscUtil.Resources;
import com.Whodundid.main.util.miscUtil.EUtil;
import com.Whodundid.scripts.scriptBase.Script;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;

//Last edited: Jan 9, 2019
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public class HotKeyCreatorGui extends EnhancedGui {
	
	HotKeyManager man = (HotKeyManager) RegisteredSubMods.getMod(SubModType.HOTKEYS);
	EGuiButton create, cancel, setEnabled, selectType, selectArg1;
	EGuiButton select, cancelSel;
	EGuiTextField keyNameEntry, mainArgEntry, secondaryArgEntry, descriptionEntry;
	KeyEntryTextField keysEntry;
	EGuiDropDownList trueFalseList;
	EGuiTextArea selectionList;
	InnerEnhancedGui selectionGui;
	EGuiDialogueBox msgBox;
	HotKey key;
	KeyActionType selectedHotKeyType;
	Class selectedGui;
	Script selectedScript;
	IDebugCommand selectedDebug;
	SubModType selectedMod;
	KeyBinding selectedKeyBind;
	
	public HotKeyCreatorGui() { super(); }
	public HotKeyCreatorGui(HotKey keyIn) { super(); key = keyIn; }
	public HotKeyCreatorGui(EnhancedGui oldGui) { super(oldGui); }
	public HotKeyCreatorGui(EnhancedGui oldGui, HotKey keyIn) { super(oldGui); key = keyIn; }
	public HotKeyCreatorGui(int posX, int posY) { super(posX, posY); }
	public HotKeyCreatorGui(int posX, int posY, HotKey keyIn) { super(posX, posY); key = keyIn; }
	public HotKeyCreatorGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	public HotKeyCreatorGui(int posX, int posY, EnhancedGui oldGui, HotKey keyIn) { super(posX, posY, oldGui); key = keyIn; }
	
	protected enum SelectionType { Type, Gui, Script, Debug, Mod, Keybind; }
	
	@Override
	public void initGui() {
		centerGuiWithDimensions(256, 278);
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		//trueFalseList = new EGuiDropDownList(this, wPos - 91, hPos + 37, 17).setFixedWidth(true, 70);
		
		keyNameEntry = new EGuiTextField(this, startX + 23, startY + 38, width - 48, 13).setTextWhenEmpty("enter a key name");
		keysEntry = new KeyEntryTextField(this, startX + 23, startY + 112, width - 48, 13);
		mainArgEntry = new EGuiTextField(this, startX + 23, startY + 149, width - 48, 13).setMaxStringLength(100);
		secondaryArgEntry = new EGuiTextField(this, startX + 23, startY + 186, width - 48, 13).setMaxStringLength(200);
		descriptionEntry = new EGuiTextField(this, startX + 23, startY + 223, width - 48, 13).setMaxStringLength(200).setTextWhenEmpty("enter a hotkey description");
		
		create = new EGuiButton(this, startX + 9, endY - 28, 53, 20, key != null ? "Edit" : "Create");
		setEnabled = new EGuiButton(this, startX + 101, endY - 28, 53, 20, key != null ? key.isEnabled() ? "Enabled" : "Disabled" : "Enabled");
		cancel = new EGuiButton(this, startX + 194, endY - 28, 53, 20, "Cancel");
		selectType = new EGuiButton(this, startX + 23, startY + 73, 140, 17, "Select a type");
		selectArg1 = new EGuiButton(this, startX + 23, startY + 147, 140, 17, "Arg1");
		
		setEnabled.setDisplayStringColor(key != null ? key.isEnabled() ? 0x55ff55 : 0xff5555 : 0x55ff55);
		keysEntry.setTextWhenEmpty("enter keys");
		
		//trueFalseList.addListEntry(new DropDownListEntry<Boolean>(Boolean.TRUE.toString(), Boolean.TRUE));
		//trueFalseList.addListEntry(new DropDownListEntry<Boolean>(Boolean.FALSE.toString(), Boolean.FALSE));
		
		setEnabled.setVisible(true);
		
		//addObject(trueFalseList);
		addObject(keyNameEntry, keysEntry, mainArgEntry, secondaryArgEntry, descriptionEntry, setEnabled);
		addObject(create, selectType, selectArg1, cancel);
		
		if (key != null) { loadKeyValues(key); }
		else { updateVisibleObjects(); }
	}

	@Override
	public void drawObject(int mX, int mY, float ticks) {
		drawDefaultBackground();
		
		drawCenteredStringWithShadow("Hotkey Creation", wPos, startY + 8, 0xb2b2b2);
		
		drawRect(startX + 9, startY + 20, endX - 9, endY - 35, 0xff000000);
		drawRect(startX + 10, startY + 21, endX - 10, endY - 36, 0xff2D2D2D);
		
		drawRect(startX + 10, startY + 57, endX - 9, startY + 58, 0xff000000);
		drawRect(startX + 10, startY + 94, endX - 9, startY + 95, 0xff000000);
		drawRect(startX + 10, startY + 131, endX - 9, startY + 132, 0xff000000);
		drawRect(startX + 10, startY + 168, endX - 9, startY + 169, 0xff000000);
		drawRect(startX + 10, startY + 205, endX - 9, startY + 206, 0xff000000);
		//drawRect(startX + 10, startY + 242, endX - 9, startY + 243, 0xff000000);
		
		drawBuilder(mX, mY, ticks);
	}
	
	private void drawBuilder(int mX, int mY, float ticks) {
		drawStringWithShadow("Name: ", startX + 13, startY + 24, 0xffd800);
		drawStringWithShadow("Type: ", startX + 13, startY + 61, 0xffd800);
		drawStringWithShadow("Keys: ", startX + 13, startY + 98, 0xffd800);
		
		String firstArgName = "", secondArgName = "";
		String firstDisplay = "", secondDisplay = "";
		if (selectedHotKeyType != null) {
			switch (selectedHotKeyType) {
			case BUILTINCODE: firstDisplay = ""; secondDisplay = ""; break;
			case COMMAND: firstArgName = "Command: "; firstDisplay = ""; break;
			case COMMAND_PLAYER_HANDHELD_CONDITION: firstArgName = "Command: "; firstDisplay = ""; secondArgName = "Test Item ID: "; secondDisplay = ""; break;
			case DEBUG: firstArgName = "Debug Command: "; firstDisplay = ""; break;
			case GUI_OPENER: firstArgName = "Gui to be opened: "; firstDisplay = ""; break;
			case MC_KEYBIND_MODIFIER: firstArgName = "MC KeyBind: "; secondArgName = "New KeyBind Value: (true / false)"; firstDisplay = ""; secondDisplay = ""; break;
			case MOD_ACTIVATOR: firstArgName = "Mod: "; firstDisplay = ""; break;
			case MOD_DEACTIVATOR: firstArgName = "Mod: "; firstDisplay = ""; break;
			case SCRIPT: firstArgName = "Script: "; secondArgName = "Script Arguments: "; firstDisplay = ""; secondDisplay = ""; break;
			default: break;
			}
		}
		
		drawStringWithShadow(firstArgName, startX + 13, startY + 135, 0xffd800);
		drawStringWithShadow(firstDisplay, startX + 23, startY + 149, 0xb2b2b2);
		drawStringWithShadow(secondArgName, startX + 13, startY + 172, 0xffd800);
		drawStringWithShadow(secondDisplay, startX + 23, startY + 186, 0xb2b2b2);
		drawStringWithShadow("Description: ", startX + 13, startY + 209, 0xffd800);
	}
	
	private void loadKeyValues(HotKey keyIn) {
		keyNameEntry.setText(keyIn.getKeyName());
		selectedHotKeyType = keyIn.getHotKeyType();
		selectType.setDisplayString(KeyActionType.getStringFromType(selectedHotKeyType));
		keysEntry.setText(EUtil.keysToString(keyIn.getKeyCombo().getKeys()));
		
		mainArgEntry.setVisible(false);
		secondaryArgEntry.setVisible(false);
		selectArg1.setVisible(false);
		
		switch (selectedHotKeyType) {
		case COMMAND:
			mainArgEntry.setText(((CommandHotKey) keyIn).getCommand()).setVisible(true);
			break;
		case COMMAND_PLAYER_HANDHELD_CONDITION:
			mainArgEntry.setText(((GuiOpenerHotKey) keyIn).getGuiDisplayName()).setVisible(true);
			secondaryArgEntry.setText(((HandHeldCommandHotKey) keyIn).getItemID() + "").setVisible(true);
			break;
		case DEBUG:
			selectArg1.setDisplayString(IDebugCommand.getDebugCommandName(((DebugHotKey) keyIn).getDebugFunction())).setVisible(true);
			break;
		case GUI_OPENER:
			selectArg1.setDisplayString(((GuiOpenerHotKey) keyIn).getGuiDisplayName()).setVisible(true);
			break;
		case MC_KEYBIND_MODIFIER: break;
		case MOD_ACTIVATOR:
			selectArg1.setDisplayString(SubModType.getModName(((ModActivatorHotKey) keyIn).getSubMod())).setVisible(true);
			break;
		case MOD_DEACTIVATOR:
			selectArg1.setDisplayString(SubModType.getModName(((ModDeactivatorHotKey) keyIn).getSubMod())).setVisible(true);
			break;
		case SCRIPT:
			selectArg1.setDisplayString(((ScriptHotKey) keyIn).getScript().getScriptName()).setVisible(true);
			secondaryArgEntry.setText(((ScriptHotKey) keyIn).getScriptArgs() + "").setVisible(true);
			break;
		default: break;
		}
		descriptionEntry.setText(keyIn.getKeyDescription());
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(selectType)) {
			if (selectedHotKeyType != KeyActionType.BUILTINCODE) {
				openSelectionGui(SelectionType.Type);
			} else {
				msgBox = new EGuiDialogueBox(this, wPos - 150, hPos - 48, 300, 75, DialogueBoxTypes.ok);
				msgBox.setMessage("Cannot modify a Built In hotkey's key type!").setMessageColor(0xff5555);
				msgBox.setDisplayString("Edit Error");
				addObject(msgBox);
			}
		}
		if (object.equals(selectArg1)) {
			switch (selectedHotKeyType) {
			case DEBUG: openSelectionGui(SelectionType.Debug); break;
			case GUI_OPENER: openSelectionGui(SelectionType.Gui); break;
			case MC_KEYBIND_MODIFIER: openSelectionGui(SelectionType.Keybind); break;
			case MOD_ACTIVATOR:
			case MOD_DEACTIVATOR: openSelectionGui(SelectionType.Mod); break;
			case SCRIPT: openSelectionGui(SelectionType.Script); break;
			default: break;
			}
		}
		if (object.equals(setEnabled)) {
			
		}
		if (object.equals(create)) {
			HotKeyBuilder builder = man.getKeyBuilder();
			try {
				if (selectedHotKeyType != null) {
					switch (selectedHotKeyType) {
					case COMMAND:
						builder.setBuilderCommand(mainArgEntry.getText()); 
						break;
					case COMMAND_PLAYER_HANDHELD_CONDITION:
						builder.setBuilderCommandAndItemArgs(mainArgEntry.getText(), Integer.parseInt(secondaryArgEntry.getText()));
						break;
					case DEBUG:
						builder.setBuilderDebugCommand(selectedDebug);
						break;
					case GUI_OPENER:
						builder.setBuilderGuiToBeOpened(selectedGui);
						break;
					case MC_KEYBIND_MODIFIER:
						builder.setBuilderKeyBindingIn(selectedKeyBind, (Boolean) trueFalseList.getSelectedEntry().getEntryObject());
						break;
					case MOD_ACTIVATOR:
					case MOD_DEACTIVATOR: 
						builder.setBuilderSubMod(selectedMod); 
						break;
					case SCRIPT:
						builder.setBuilderScriptToBeRun(selectedScript, secondaryArgEntry.getText().split(","));
						break;
					default: break;
					}
					
					if (keyNameEntry.getText().isEmpty()) { throw new MissingHotKeyArgumentException("KeyName not defined."); }
					if (keysEntry.getKeys().length == 0) { throw new MissingHotKeyArgumentException("No keys defined."); }
					
					if (key != null) {
						//if (man.unregisterHotKey(key)) {
						//	
						//} else {
						//	
						//}
					} else {
						if (builder.buildHotKey(keyNameEntry.getText(), descriptionEntry.getText(), true, selectedHotKeyType)) {
							clearEntryData();
						}
					}
				}
			} catch (MissingHotKeyArgumentException e) {
				//actionHistory.addTextLine("Error: " + e.getMessage(), 0xff5555).setLineNumberColor(0xffaa00);
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	public void clearEntryData() {
		keyNameEntry.setText("");
		keysEntry.setText("");
		mainArgEntry.setText("");
		secondaryArgEntry.setText("");
		descriptionEntry.setText("");
	}
	
	protected void openSelectionGui(SelectionType typeIn) {
		selectionGui = new InnerEnhancedGui(this, startX + ((width - 200) / 2), startY + ((height - 230) / 2) + 8, 200, 230) {
			@Override
			public void initGui() {
				requestFocus();
				setFocusLockObject(selectionGui);
				
				header = new EGuiHeader(this);
				header.setDisplayString("Select a " + typeIn.toString());
				
				select = new EGuiButton(this, startX + 10, endY - 28, 80, 20, "Select");
				cancelSel = new EGuiButton(this, startX + 110, endY - 28, 80, 20, "Cancel");
				
				selectionList = new EGuiTextArea(this, startX + 10, startY + 10, 180, 185, false).setDrawLineNumbers(true);
				
				addObject(header, select, cancelSel, selectionList);
				
				switch (typeIn) {
				case Debug: for (IDebugCommand c : IDebugCommand.values()) { selectionList.addTextLine(EnumChatFormatting.GREEN + IDebugCommand.getDebugCommandName(c), 0xffffff, c); } break;
				case Gui: for (Class c : Resources.guis) { selectionList.addTextLine(EnumChatFormatting.GREEN + c.getSimpleName(), 0xffffff, c); } break;
				case Keybind: for (KeyBinding k : mc.gameSettings.keyBindings) { selectionList.addTextLine(EnumChatFormatting.GREEN + k.getKeyDescription(), 0xffffff, k); } break;
				case Mod: for (SubModType m : SubModType.values()) { selectionList.addTextLine(EnumChatFormatting.GREEN + SubModType.getModName(m), 0xffffff, m); } break;
				case Script: break;
				case Type:
					for (KeyActionType t : KeyActionType.values()) {
						if (t.canUserCreate()) { selectionList.addTextLine(EnumChatFormatting.GREEN + KeyActionType.getStringFromType(t), 0xffffff, t); }
					}
					break;
				default: break;
				}
				
				if (!selectionList.getTextDocument().isEmpty()) {
					if (selectedHotKeyType != null) { selectionList.setSelectedLine(selectionList.getLineWithObject(selectedHotKeyType)); }
					else { selectionList.setSelectedLine(selectionList.getTextLineWithLineNumber(1)); }
				}
				
				bringToFront();
			}
			@Override
			public void drawObject(int mXIn, int mYIn, float ticks) {
				drawDefaultBackground();
				super.drawObject(mXIn, mYIn, ticks);
			}
			@Override
			public void actionPerformed(IEnhancedActionObject object) {
				if (object.equals(select)) {
					if (selectionList.getCurrentLine() != null && selectionList.getCurrentLine().getStoredObj() != null) {
						Object o = selectionList.getCurrentLine().getStoredObj();
						switch (typeIn) {
						case Debug: selectedDebug = (IDebugCommand) o; selectArg1.setDisplayString(IDebugCommand.getDebugCommandName(selectedDebug)).setVisible(true); break;
						case Gui: selectedGui = (Class) o; selectArg1.setDisplayString(selectedGui.getSimpleName()).setVisible(true); break;
						case Keybind: selectedKeyBind = (KeyBinding) o; selectArg1.setDisplayString(selectedKeyBind.getKeyDescription()).setVisible(true); break;
						case Mod: selectedMod = (SubModType) o; selectArg1.setDisplayString(SubModType.getModName(selectedMod)).setVisible(true); break;
						case Script: selectedScript = (Script) o; selectArg1.setDisplayString(selectedScript.getScriptName()).setVisible(true); break;
						case Type:
							selectedHotKeyType = (KeyActionType) o;
							selectType.setDisplayString(KeyActionType.getStringFromType(selectedHotKeyType)).setVisible(true);
							updateVisibleObjects(selectedHotKeyType);
							break;
						default: break;
						}
					}
					close();
				}
				if (object.equals(cancelSel)) { close(); }
			}
		};
		
		addObject(selectionGui);
	}
	
	public void updateVisibleObjects() { updateVisibleObjects(null); }
	public void updateVisibleObjects(KeyActionType typeIn) {
		try {
			mainArgEntry.setVisible(false);
			secondaryArgEntry.setVisible(false);
			selectArg1.setVisible(false);
			
			KeyActionType testType;
			testType = selectedHotKeyType != null ? typeIn : selectedHotKeyType;
			
			if (testType != null) {
				switch (testType) {
				case COMMAND: mainArgEntry.setTextWhenEmpty("enter command").setVisible(true); break;
				case COMMAND_PLAYER_HANDHELD_CONDITION:
					mainArgEntry.setTextWhenEmpty("enter command").setVisible(true);
					secondaryArgEntry.setTextWhenEmpty("enter an item id to test for").setVisible(true);
					break;
				case GUI_OPENER: selectArg1.setDisplayString("Select a Gui").setVisible(true); break;
				case SCRIPT:
					selectArg1.setDisplayString("Select a Script").setVisible(true);
					secondaryArgEntry.setTextWhenEmpty("enter script arguments").setVisible(true);
					break;
				case DEBUG: selectArg1.setDisplayString("Select a Debug Command").setVisible(true); break;
				case MOD_ACTIVATOR:
				case MOD_DEACTIVATOR: selectArg1.setDisplayString("Select a Mod").setVisible(true); break;
				case MC_KEYBIND_MODIFIER: selectArg1.setDisplayString("Select a KeyBind").setVisible(true); trueFalseList.setVisible(true); break;
				default: break;
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
}
