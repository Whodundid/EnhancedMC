package com.Whodundid.hotkeys.subModSpecific;

import org.lwjgl.input.Keyboard;
import com.Whodundid.hotkeys.HotKeyManager;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import com.Whodundid.hotkeys.control.hotKeyUtil.SubModHotKeys;
import com.Whodundid.scripts.builtInScripts.Script_Rotate360;

//Last edited: 10-13-18
//First Added: 11-16-17
//Author: Hunter Bragg

public class WorldEdit_HotKeys extends SubModHotKeys {
	
	public WorldEdit_HotKeys(HotKeyManager manIn) {
		man = manIn;
		subModName = "worldedit";
	}

	@Override protected void addKeys() {
		keys.add(new HotKey("rotate360Script", new KeyComboAction(Keyboard.KEY_O), KeyActionType.BUILTINCODE, subModName) {
			{ setKeyDescription("Rotates and pastes a world edit clipboard selection around the player in 360 degrees."); }
			@Override public void executeHotKeyAction() {
				if (!Voxel_HotKeys.voxel) { new Script_Rotate360().startScript(null); }
			}
		});
		
		keys.add(new HotKey("paste-a", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_LSHIFT, Keyboard.KEY_V), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				if (!Voxel_HotKeys.voxel) {
					if (mc.thePlayer.getHeldItem() != null) {
						String itemName = mc.thePlayer.getHeldItem().getDisplayName();
						if (!itemName.equals("Arrow") && !itemName.equals("Gunpowder")) { mc.thePlayer.sendChatMessage("//paste -a"); }
					} else { mc.thePlayer.sendChatMessage("//paste -a"); }
				}
			}
		});
		
		keys.add(new HotKey("copy", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_C), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				if (!Voxel_HotKeys.voxel) {
					if (mc.thePlayer.getHeldItem() != null) {
						String itemName = mc.thePlayer.getHeldItem().getDisplayName();
						if (!itemName.equals("Arrow") && !itemName.equals("Gunpowder")) { mc.thePlayer.sendChatMessage("//copy"); }
					} else { mc.thePlayer.sendChatMessage("//copy"); }
				}
			}
		});
		
		keys.add(new HotKey("move", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_LSHIFT, Keyboard.KEY_W), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				if (!Voxel_HotKeys.voxel) { mc.thePlayer.sendChatMessage("//move"); }
			}
		});
		
		keys.add(new HotKey("stack", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_LSHIFT, Keyboard.KEY_S), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				if (!Voxel_HotKeys.voxel) { mc.thePlayer.sendChatMessage("//stack"); }
			}
		});
		
		keys.add(new HotKey("paste", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_V), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				if (!Voxel_HotKeys.voxel) { mc.thePlayer.sendChatMessage("//paste"); }
			}
		});
		
		keys.add(new HotKey("rotate-90", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_A), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				if (!Voxel_HotKeys.voxel) { mc.thePlayer.sendChatMessage("//rotate -90"); }
			}
		});
		
		keys.add(new HotKey("rotate90", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_D), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				if (!Voxel_HotKeys.voxel) { mc.thePlayer.sendChatMessage("//rotate 90"); }
			}
		});
		
		keys.add(new HotKey("redo", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_Y), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				if (!Voxel_HotKeys.voxel) { mc.thePlayer.sendChatMessage("//redo"); }
			}
		});
		
		keys.add(new HotKey("cut", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_X), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				if (!Voxel_HotKeys.voxel) {
					if (mc.thePlayer.getHeldItem() != null && !mc.thePlayer.getHeldItem().getDisplayName().equals("Arrow")) { mc.thePlayer.sendChatMessage("//cut"); }
				}
			}
		});
		
		keys.add(new HotKey("expand 1", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_W), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				if (!Voxel_HotKeys.voxel) { mc.thePlayer.sendChatMessage("//expand 1"); }
			}
		});
		
		keys.add(new HotKey("flip", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_S), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				if (!Voxel_HotKeys.voxel) { mc.thePlayer.sendChatMessage("//flip"); }
			}
		});
		
		keys.add(new HotKey("undo", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_Z), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				if (!Voxel_HotKeys.voxel) {
					if (mc.thePlayer.getHeldItem() != null) {
						if (mc.thePlayer.getHeldItem().getDisplayName().equals("Feather")) { mc.thePlayer.sendChatMessage("/gp undo"); } 
						mc.thePlayer.sendChatMessage("//undo");
					} else { mc.thePlayer.sendChatMessage("//undo"); }
				}
			}
		});
	}
}
