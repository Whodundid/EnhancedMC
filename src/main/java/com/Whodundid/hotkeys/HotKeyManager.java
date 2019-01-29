package com.Whodundid.hotkeys;

import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.hotKeyGuis.HotKeyCreatorGui;
import com.Whodundid.hotkeys.hotKeyGuis.HotKeyGuiMain;
import com.Whodundid.hotkeys.keySaveLoad.HotKeyBuilder;
import com.Whodundid.hotkeys.keySaveLoad.KeyLoader;
import com.Whodundid.hotkeys.keySaveLoad.KeySaver;
import com.Whodundid.hotkeys.subModSpecific.Global_HotKeys;
import com.Whodundid.hotkeys.subModSpecific.ParkourHelper_HotKeys;
import com.Whodundid.hotkeys.subModSpecific.PlayerNames_HotKeys;
import com.Whodundid.hotkeys.subModSpecific.Script_HotKeys;
import com.Whodundid.hotkeys.subModSpecific.Voxel_HotKeys;
import com.Whodundid.hotkeys.subModSpecific.WorldEdit_HotKeys;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.storageUtil.EArrayList;
import com.Whodundid.main.util.storageUtil.StorageBox;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;

//Last edited: Oct 16, 2018
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public class HotKeyManager extends SubMod {
	
	static Minecraft mc = MainMod.getMC();
	protected EArrayList<HotKey> registeredHotKeys;
	protected EArrayList<HotKey> builtInHotKeys;
	protected EArrayList<HotKey> userMadeHotKeys;
	protected KeySaver saver;
	protected KeyLoader loader;
	protected HotKeyBuilder builder;
	public Global_HotKeys globalKeys;
	public ParkourHelper_HotKeys parkourKeys;
	public PlayerNames_HotKeys namesKeys;
	public Script_HotKeys scriptKeys;
	public Voxel_HotKeys voxelKeys;
	public WorldEdit_HotKeys worldEditKeys;
	
	public HotKeyManager() {
		super(SubModType.HOTKEYS);
		registeredHotKeys = new EArrayList();
		builtInHotKeys = new EArrayList();
		userMadeHotKeys = new EArrayList();
		
		saver = new KeySaver(this);
		loader = new KeyLoader(this);
		builder = new HotKeyBuilder(this);
		
		globalKeys = new Global_HotKeys(this);
		parkourKeys = new ParkourHelper_HotKeys(this);
		namesKeys = new PlayerNames_HotKeys(this);
		scriptKeys = new Script_HotKeys(this);
		voxelKeys = new Voxel_HotKeys(this);
		worldEditKeys = new WorldEdit_HotKeys(this);
		
		registerDefaultSubModKeys();
		
		loadHotKeys();
	}
	
	@Override
	public EnhancedGui getMainGui(boolean setPosition, StorageBox<Integer, Integer> pos, EnhancedGui oldGui) {
		if (oldGui != null) { return setPosition ? new HotKeyGuiMain(pos.getObject(), pos.getValue(), oldGui) : new HotKeyCreatorGui(oldGui); }
		return setPosition ? new HotKeyGuiMain(pos.getObject(), pos.getValue()) : new HotKeyCreatorGui();
	}
	
	private void registerDefaultSubModKeys() {
		globalKeys.rebuildKeys().registerKeys();
		parkourKeys.rebuildKeys().registerKeys();
		namesKeys.rebuildKeys().registerKeys();
		scriptKeys.rebuildKeys().registerKeys();
		voxelKeys.rebuildKeys().registerKeys();
		worldEditKeys.rebuildKeys().registerKeys();
	}
	
	private void reBuildSubModHotKeys() {
		globalKeys.rebuildKeys();
		parkourKeys.rebuildKeys();
		namesKeys.rebuildKeys();
		scriptKeys.rebuildKeys();
		voxelKeys.rebuildKeys();
		worldEditKeys.rebuildKeys();
	}
	
	public boolean registerHotKey(HotKey keyIn) {
		synchronized (registeredHotKeys) {
			if (!doesListAlreadyContain(keyIn, registeredHotKeys)) { registeredHotKeys.add(keyIn); }
			if (keyIn.isBuiltIn()) {
				if (!doesListAlreadyContain(keyIn, builtInHotKeys)) { builtInHotKeys.add(keyIn); return true; }
			} else { 
				if (!doesListAlreadyContain(keyIn, userMadeHotKeys)) { userMadeHotKeys.add(keyIn); return true; }
			}
		}
		return false;
	}
	
	private boolean doesListAlreadyContain(HotKey keyIn, EArrayList<HotKey> list) {
		for (HotKey k : list) {
			if (k.getKeyName().equals(keyIn.getKeyName())) {
				if (k.getBuiltInSubModType().equals(keyIn.getBuiltInSubModType())) { return true; }
			}
		}
		return false;
	}
	
	public boolean unregisterHotKey(int... keyCodes) {
		synchronized (registeredHotKeys) {
			Iterator<HotKey> it = registeredHotKeys.iterator();
			while (it.hasNext()) {
				HotKey key = it.next();
				if (key.getKeyCombo().checkKeys(keyCodes)) {
					if (!key.isBuiltIn()) {
						it.remove();
						Iterator<HotKey> userRemove = userMadeHotKeys.iterator();
						while (userRemove.hasNext()) {
							if (userRemove.next().getKeyCombo().checkKeys(keyCodes)) {
								userRemove.remove();
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean unregisterHotKey(String keyName) {
		synchronized (registeredHotKeys) {
			Iterator<HotKey> it = registeredHotKeys.iterator();
			while (it.hasNext()) {
				HotKey key = it.next();
				if (key.getKeyName().equals(keyName)) {
					if (!key.isBuiltIn()) {
						it.remove();
						Iterator<HotKey> userRemove = userMadeHotKeys.iterator();
						while (userRemove.hasNext()) {
							if (userRemove.next().getKeyName().equals(keyName)) {
								userRemove.remove();
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean unregisterHotKey(HotKey keyIn) {
		synchronized (registeredHotKeys) {
			Iterator<HotKey> it = registeredHotKeys.iterator();
			while (it.hasNext()) {
				HotKey key = it.next();
				if (key.equals(keyIn)) {
					if (!key.isBuiltIn()) {
						it.remove();
						Iterator<HotKey> userRemove = userMadeHotKeys.iterator();
						while (userRemove.hasNext()) {
							if (userRemove.next().equals(keyIn)) {
								userRemove.remove();
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public HotKey getHotKey(int... keyCodes) {
		for (HotKey k : registeredHotKeys) {
			if (k.getKeyCombo().checkKeys(keyCodes)) { return k; }
		}
		return null;
	}
	
	public HotKey getHotKey(String keyNameIn) {
		for (HotKey k : registeredHotKeys) {
			if (k.getKeyName().equals(keyNameIn)) { return k; }
		}
		return null;
	}
	
	public boolean checkIfKeyComboAlreadyExists(int... keyCodes) {
		for (HotKey k : registeredHotKeys) {
			if (k.getKeyCombo().checkKeys(keyCodes)) { return true; }
		}
		return false;
	}
	
	public synchronized void saveHotKeys() {
		saver.saveKeysToFile();
	}
	
	public synchronized void loadHotKeys() {
		registeredHotKeys.clear();
		reBuildSubModHotKeys();
		loader.loadKeysFromFile();
		
		for (HotKey k : builtInHotKeys) { registerHotKey(k); }
		for (HotKey k : userMadeHotKeys) { registerHotKey(k); }
	}
	
	@Override
	public void eventKey(KeyInputEvent e) {
		if (isEnabled()) {
			try {
				if (Keyboard.isCreated()) {
					if (Keyboard.getEventKeyState()) {
						int keyCode = Keyboard.getEventKey();
						EArrayList<Integer> checkKeys = new EArrayList();
						
						boolean isCtrl = (Minecraft.isRunningOnMac && (keyCode == 219 || keyCode == 220)) || keyCode == 29 || keyCode == 157;
						boolean isShift = keyCode == 42 || keyCode == 54;
						boolean isAlt = keyCode == 56 || keyCode == 184;
						
						boolean mcCtrl = GuiScreen.isCtrlKeyDown();
						
						if (mcCtrl && mc.gameSettings.keyBindForward.isPressed()) { KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false); }
						if (mcCtrl && mc.gameSettings.keyBindLeft.isPressed()) { KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false); }
						if (mcCtrl && mc.gameSettings.keyBindBack.isPressed()) { KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false); }
						if (mcCtrl && mc.gameSettings.keyBindRight.isPressed()) { KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false); }
						if (mcCtrl && mc.gameSettings.keyBindJump.isPressed()) { KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false); }
						if (mcCtrl && mc.gameSettings.keyBindSneak.isPressed()) { KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false); }
						
						//control keys
						if (Minecraft.isRunningOnMac) {
							if (Keyboard.isKeyDown(219)) { checkKeys.add(219); }
							if (Keyboard.isKeyDown(220)) { checkKeys.add(220); }
						} else {
							if (Keyboard.isKeyDown(29)) { checkKeys.add(29); }
							if (Keyboard.isKeyDown(157)) { checkKeys.add(157); }
						}
						
						//shift keys
						if (Keyboard.isKeyDown(42)) { checkKeys.add(42); }
						if (Keyboard.isKeyDown(54)) { checkKeys.add(54); }
						
						//alt keys
						if (Keyboard.isKeyDown(56)) { checkKeys.add(56); }
						if (Keyboard.isKeyDown(184)) { checkKeys.add(184); }
						
						if (!isCtrl && !isShift && !isAlt) { checkKeys.add(keyCode); }
						
						synchronized (registeredHotKeys) {
							for (int i = 0; i < registeredHotKeys.size(); i++) {
								HotKey key = registeredHotKeys.get(i);
								if (key.getKeyCombo() != null) {
									if (key.getKeyCombo().checkKeys(checkKeys) && key.isEnabled()) {
										registeredHotKeys.get(i).executeHotKeyAction();
										//System.out.println(registeredHotKeys.get(i).getKeyDescription());
									}
								}
							}
						}
					}
				}
			} catch (Exception q) { q.printStackTrace(); }
		}
	}
	
	public List<HotKey> getRegisteredHotKeys() { return Collections.unmodifiableList(registeredHotKeys); }
	public List<HotKey> getBuiltInHotKeys() { return Collections.unmodifiableList(builtInHotKeys); }
	public List<HotKey> getUserMadeHotKeys() { return Collections.unmodifiableList(userMadeHotKeys); }
	public KeySaver getKeySaver() { return saver; }
	public KeyLoader getKeyLoader() { return loader; }
	public HotKeyBuilder getKeyBuilder() { return builder; }
}
