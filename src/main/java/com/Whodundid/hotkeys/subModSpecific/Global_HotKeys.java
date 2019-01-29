package com.Whodundid.hotkeys.subModSpecific;

import org.lwjgl.input.Keyboard;
import com.Whodundid.debug.DebugFunctions;
import com.Whodundid.debug.IDebugCommand;
import com.Whodundid.enhancedChat.gameChat.ModdedChat;
import com.Whodundid.hotkeys.HotKeyManager;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import com.Whodundid.hotkeys.control.hotKeyUtil.SubModHotKeys;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.miscUtil.ChatBuilder;
import com.Whodundid.mainMenu.CustomInGameMenu;
import com.Whodundid.miniMap.MiniMap;
import com.Whodundid.worldEditor.Editor;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.EnumChatFormatting;

//Last edited: Jan 10, 2019
//10-16-18
//First Added: Nov 16, 2017
//Author: Hunter Bragg

public class Global_HotKeys extends SubModHotKeys {
	
	static int speed = 1;
	
	public Global_HotKeys(HotKeyManager manIn) {
		man = manIn;
		subModName = "global";
	}
	
	@Override public void addKeys() {
		keys.add(new HotKey("openInGameMenu", new KeyComboAction(Keyboard.KEY_ESCAPE), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Opens the in game main menu."); }
			@Override public void executeHotKeyAction() {
				mc.displayGuiScreen(new CustomInGameMenu());
			}
		});
		
		keys.add(new HotKey("openChat", new KeyComboAction(mc.gameSettings.keyBindChat.getKeyCode()), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Opens the chat gui or the EnhancedChat window if it is enabled."); }
			@Override public void executeHotKeyAction() {
				if (RegisteredSubMods.getMod(SubModType.ENHANCEDCHAT).isEnabled()) { mc.displayGuiScreen(new ModdedChat()); }
				else { mc.displayGuiScreen(new GuiChat()); }
			}
		});
		
		keys.add(new HotKey("openCommandChat", new KeyComboAction(Keyboard.KEY_SLASH), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Opens the chat gui and adds a '/'"); }
			@Override public void executeHotKeyAction() {
				if (RegisteredSubMods.getMod(SubModType.ENHANCEDCHAT).isEnabled()) { mc.displayGuiScreen(new ModdedChat("/")); }
				else { mc.displayGuiScreen(new GuiChat("/")); }
			}
		});
		
		keys.add(new HotKey("baseDebug", new KeyComboAction(Keyboard.KEY_GRAVE), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Runs debug function: 0."); }
			@Override public void executeHotKeyAction() {
				DebugFunctions.runDebugFunction(IDebugCommand.DEBUG_0);
			}
		});
		
		keys.add(new HotKey("build", new KeyComboAction(Keyboard.KEY_SEMICOLON), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Connects to the Hypixel build server."); }
			@Override public void executeHotKeyAction() {
				mc.thePlayer.sendChatMessage("/build");
			}
		});
		
		keys.add(new HotKey("toggleVoxel", new KeyComboAction(Keyboard.KEY_PERIOD), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Toggles between World Edit and Voxel hotkeys."); }
			@Override public void executeHotKeyAction() {
				Voxel_HotKeys.voxel = !Voxel_HotKeys.voxel;
				String s = Voxel_HotKeys.voxel ? "Voxel" : "World Edit";
				mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.ITALIC + s).build());
			}
		});
		
		keys.add(new HotKey("zoomInMiniMap", new KeyComboAction(Keyboard.KEY_I), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Zooms MiniMap in."); }
			@Override public void executeHotKeyAction() {
				MiniMap map = (MiniMap) RegisteredSubMods.getMod(SubModType.MINIMAP);
				//map.zoomInMap = !map.zoomInMap;
			}
		});
		
		keys.add(new HotKey("zoomOutMiniMap", new KeyComboAction(Keyboard.KEY_I), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Zooms MiniMap out."); }
			@Override public void executeHotKeyAction() {
				MiniMap map = (MiniMap) RegisteredSubMods.getMod(SubModType.MINIMAP);
				//map.zoomInMap = !map.zoomInMap;
			}
		});
		
		keys.add(new HotKey("drawBigMiniMap", new KeyComboAction(Keyboard.KEY_LMENU, Keyboard.KEY_H), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Toggles large minimap."); }
			@Override public void executeHotKeyAction() {
				MiniMap map = (MiniMap) RegisteredSubMods.getMod(SubModType.MINIMAP);
				map.drawBig = !map.drawBig;
			}
		});
		
		keys.add(new HotKey("openCloseEditor", new KeyComboAction(Keyboard.KEY_L), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Open/Close the WorldEditor interface."); }
			@Override public void executeHotKeyAction() {
				if (RegisteredSubMods.getMod(SubModType.WORLDEDITOR).isEnabled()) {
					if (Editor.isEditorOpen()) { Editor.closeEditor(); }
					else { Editor.openEditor(); }
				}
			}
		});
		
		keys.add(new HotKey("speedUp", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_LSHIFT, Keyboard.KEY_C), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Increases player move/flight speed by 1."); }
			@Override public void executeHotKeyAction() { 
				if (speed < 10) {
					speed++;
					mc.thePlayer.sendChatMessage("/speed " + speed);				
				} else { mc.thePlayer.addChatMessage(ChatBuilder.of("10 is highest").build()); }
			}
		});
		
		keys.add(new HotKey("speedDown", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_LSHIFT, Keyboard.KEY_X), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Decreases player move/flight speed by 1."); }
			@Override public void executeHotKeyAction() { 
				if (speed > 1) {
					speed -= 1;
					mc.thePlayer.sendChatMessage("/speed " + speed);
				} else {
					mc.thePlayer.addChatMessage(ChatBuilder.of("1 is lowest").build());
					mc.thePlayer.sendChatMessage("/speed " + speed);
				}
			}
		});
	}
}