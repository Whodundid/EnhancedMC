package com.Whodundid.hotkeys.subModSpecific;

import org.lwjgl.input.Keyboard;
import com.Whodundid.hotkeys.HotKeyManager;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import com.Whodundid.hotkeys.control.hotKeyUtil.SubModHotKeys;
import com.Whodundid.main.util.miscUtil.ChatBuilder;

//Last edited: 10-13-18
//First Added: 11-16-17
//Author: Hunter Bragg

public class Voxel_HotKeys extends SubModHotKeys {
	
	public static int curSize = 3;
	public static boolean ssUsedLast = false;
	public static boolean voxel = false;
	
	public Voxel_HotKeys(HotKeyManager manIn) {
		man = manIn;
		subModName = "voxel";
	}

	@Override protected void addKeys() {
		keys.add(new HotKey("redo", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_Y), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				if (voxel) { mc.thePlayer.sendChatMessage("/redo"); }
			}
		});
		
		keys.add(new HotKey("undo", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_Z), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				if (voxel) {
					if (mc.thePlayer.getHeldItem() != null) {
						if (mc.thePlayer.getHeldItem().getDisplayName().equals("Feather")) {
							mc.thePlayer.sendChatMessage("/gp undo");
							return;
						} 
						mc.thePlayer.sendChatMessage("/u");
					} else { mc.thePlayer.sendChatMessage("/u"); }
				}
			}
		});
		
		keys.add(new HotKey("decreseSize", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_X), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				if (voxel) {
					if (mc.thePlayer.getHeldItem().getDisplayName().equals("Wooden Axe")) {
						mc.thePlayer.sendChatMessage("//cut");
					} else {
						if (curSize <= 1) {
							curSize = 1;
							mc.thePlayer.addChatMessage(ChatBuilder.of("Cannot be below 1.").build());
						} else {
							curSize -= 1;
							mc.thePlayer.sendChatMessage("/b " + curSize);
						}
					}
				} else if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getDisplayName().equals("Arrow")) {
					if (curSize <= 1) {
						curSize = 1;
						mc.thePlayer.addChatMessage(ChatBuilder.of("Cannot be below 1.").build());
					} else {
						curSize -= 1;
						mc.thePlayer.sendChatMessage("/b " + curSize);
					}
				}
			}
		});
		
		keys.add(new HotKey("increaseSize", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_C), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				if (voxel) {
					if (mc.thePlayer.getHeldItem() != null) {
						String itemName = mc.thePlayer.getHeldItem().getDisplayName();
						if (itemName.equals("Wooden Axe")) { mc.thePlayer.sendChatMessage("//copy"); }
						else { curSize += 1; mc.thePlayer.sendChatMessage("/b " + curSize); }
					} else { curSize += 1; mc.thePlayer.sendChatMessage("/b " + curSize); }
				} else if (mc.thePlayer.getHeldItem() != null) {
					String itemName = mc.thePlayer.getHeldItem().getDisplayName();
					if (itemName.equals("Arrow") || itemName.equals("Gunpowder")) { curSize += 1; mc.thePlayer.sendChatMessage("/b " + curSize); }
				}
			}
		});
		
		keys.add(new HotKey("singleSnipeFillStone", new KeyComboAction(Keyboard.KEY_LCONTROL, Keyboard.KEY_LSHIFT, Keyboard.KEY_V), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				if (voxel) {
					if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getDisplayName().equals("Wooden Axe")) {
						mc.thePlayer.sendChatMessage("//paste -a");
					} else {
						mc.thePlayer.sendChatMessage("/b s");
						mc.thePlayer.sendChatMessage("/v 1");
					}
				} else if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getDisplayName().equals("Arrow")) {
					mc.thePlayer.sendChatMessage("/b s");
					mc.thePlayer.sendChatMessage("/v 1");
				}
			}
		});
		
		keys.add(new HotKey("fill", new KeyComboAction(Keyboard.KEY_Z), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				wasSSUsedLast();
				mc.thePlayer.sendChatMessage("/b e fill");
			}
		});
		
		keys.add(new HotKey("smooth", new KeyComboAction(Keyboard.KEY_X), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				wasSSUsedLast();
				mc.thePlayer.sendChatMessage("/b e smooth");
			}
		});
		
		keys.add(new HotKey("blendBall", new KeyComboAction(Keyboard.KEY_C), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				wasSSUsedLast();
				mc.thePlayer.sendChatMessage("/b bb");
			}
		});
		
		keys.add(new HotKey("ball", new KeyComboAction(Keyboard.KEY_B), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				wasSSUsedLast();
				mc.thePlayer.sendChatMessage("/b b");
			}
		});
		
		keys.add(new HotKey("singleSnipe", new KeyComboAction(Keyboard.KEY_V), KeyActionType.BUILTINCODE, subModName) {
			@Override public void executeHotKeyAction() {
				ssUsedLast = true;
				mc.thePlayer.sendChatMessage("/b s");
				mc.thePlayer.sendChatMessage("/v 0");
			}
		});
	}
	
	public void wasSSUsedLast() {
		if (ssUsedLast) {
			mc.thePlayer.sendChatMessage("/v 1");
			ssUsedLast = false;
		}
	}
}