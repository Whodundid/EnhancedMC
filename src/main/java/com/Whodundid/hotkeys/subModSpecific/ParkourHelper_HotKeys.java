package com.Whodundid.hotkeys.subModSpecific;

import org.lwjgl.input.Keyboard;
import com.Whodundid.enhancedChat.chatUtil.ChatDrawer;
import com.Whodundid.hotkeys.HotKeyManager;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import com.Whodundid.hotkeys.control.hotKeyUtil.SubModHotKeys;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.playerUtil.PlayerFacing;
import com.Whodundid.parkourHelper.HelperBlock;
import com.Whodundid.parkourHelper.ParkourAI;

//Last edited: 10-16-18
//First Added: 3-23-18
//Author: Hunter Bragg

public class ParkourHelper_HotKeys extends SubModHotKeys {
	
	static long inputDelay = 0;
	
	public ParkourHelper_HotKeys(HotKeyManager manIn) {
		man = manIn;
		subModName = "parkour";
	}

	@Override protected void addKeys() {
		keys.add(new HotKey("alignFacing", new KeyComboAction(Keyboard.KEY_M), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Aligns the player's viewing direction with the closest axis."); }
			@Override public void executeHotKeyAction() {
				if (System.currentTimeMillis() - inputDelay >= 200) {
					PlayerFacing.setFacingDir(PlayerFacing.getCompassFacingDir());
					inputDelay = System.currentTimeMillis();
				}
			}
		});
		
		keys.add(new HotKey("toggleSprint", new KeyComboAction(Keyboard.KEY_LCONTROL, mc.gameSettings.keyBindSprint.getKeyCode()), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Toggles auto sprint."); }
			@Override public void executeHotKeyAction() {
				ParkourAI parkour = (ParkourAI) RegisteredSubMods.getMod(SubModType.PARKOUR);
				parkour.getBackgroundChecker().kill();
				parkour.setEdgeJumping(false);
				parkour.setHelperBlock(null);
				ChatDrawer.clearAddedChat();
				parkour.setConstantlySprinting(!parkour.isConstantlySprinting());
				inputDelay = System.currentTimeMillis();
			}
		});
		
		keys.add(new HotKey("toggleParkourAI", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_LSHIFT, mc.gameSettings.keyBindSprint.getKeyCode()), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Toggles the running state of the parkour AI."); }
			@Override public void executeHotKeyAction() {
				ParkourAI parkour = (ParkourAI) RegisteredSubMods.getMod(SubModType.PARKOUR);
				if (!parkour.isEdgeJumping()) {
					parkour.setConstantlySprinting(true);
					parkour.setEdgeJumping(true);
					ChatDrawer.clearAddedChat();
				} else {
					parkour.getBackgroundChecker().kill();
					parkour.setConstantlySprinting(false);
					parkour.setEdgeJumping(false);
					parkour.setHelperBlock(null);
					ChatDrawer.clearAddedChat();
				}
			}
		});
		
		keys.add(new HotKey("setHelperBlock", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_GRAVE), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Manually define a helper block location for the parkour AI."); }
			@Override public void executeHotKeyAction() {
				ParkourAI parkour = (ParkourAI) RegisteredSubMods.getMod(SubModType.PARKOUR);
				if (PlayerFacing.getFacingBlockPos() != null) {
					if (parkour.getHelperBlock() == null || !(PlayerFacing.getFacingBlockPos().equals(parkour.getHelperBlock().getHelperBlockLocation()))) {
						parkour.setHelperBlock(new HelperBlock(PlayerFacing.getFacingBlockPos()));
						return;
					}
				}
				parkour.setHelperBlock(null);
			}
		});
	}
}
