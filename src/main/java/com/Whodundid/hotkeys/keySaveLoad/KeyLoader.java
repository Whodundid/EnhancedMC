package com.Whodundid.hotkeys.keySaveLoad;

import com.Whodundid.debug.IDebugCommand;
import com.Whodundid.hotkeys.HotKeyManager;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.scripts.scriptBase.Script;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

//Last edited: Oct 5, 2018
//First Added: Sep 25, 2018
//Author: Hunter Bragg

public class KeyLoader {
	
	static Minecraft mc = MainMod.getMC();
	HotKeyManager man;
	
	public KeyLoader(HotKeyManager manIn) {
		man = manIn;
	}
	
	public void loadKeysFromFile() {
		loadBuiltInKeys();
		loadUserMadeKeys();
	}
	
	public void loadBuiltInKeys() {
		File builtInKeys = new File("EnhancedMC/HotKeys/BuiltIn.cfg");
		if (builtInKeys.exists()) {
			boolean isEnd = false;
			String command, configLine;
			try (Scanner fileReader = new Scanner(builtInKeys)) {
				while (!isEnd && fileReader.hasNextLine()) {
					configLine = fileReader.nextLine();
					Scanner line = new Scanner(configLine);
					if (line.hasNext()) {
						command = line.next();
						
						if (command.equals("END")) { isEnd = true; }
						if (!command.equals("**")) {
							for (HotKey k : man.getBuiltInHotKeys()) {
								if (k.getKeyName().equals(command)) {
									String[] keysIn = line.next().split(",");
									int[] keyCodes = new int[keysIn.length];
									
									for (int i = 0; i < keysIn.length; i++) {
										keyCodes[i] = Integer.valueOf(keysIn[i]);
									}
									
									k.getKeyCombo().setKeys(keyCodes);
									
									k.setEnabled(Boolean.parseBoolean(line.next()));
									break;
								}
							}
						}
					}
					
					line.close();
				}
				fileReader.close();
			} catch (Exception e) { e.printStackTrace(); }
		} else {
			try {
				man.getKeySaver().saveBuiltInKeys();
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public void loadUserMadeKeys() {
		File userMadeKeys = new File("EnhancedMC/HotKeys/UserMade.cfg");
		if (userMadeKeys.exists()) {
			
			HotKeyBuilder builder = man.getKeyBuilder();
			String keyName = "";
			KeyActionType keyType = KeyActionType.UNDEFINED;
			KeyComboAction keys = null;
			String keyDesc = "";
			boolean keyEnabled = false;
			Script script = null;
			String readCommand = "";
			
			boolean isEnd = false;
			String command, configLine;
			try (Scanner fileReader = new Scanner(userMadeKeys)) {
				while (!isEnd && fileReader.hasNextLine()) {
					configLine = fileReader.nextLine();
					Scanner line = new Scanner(configLine);
					if (line.hasNext()) {
						command = line.next();
						switch (command) {
						case "**": break;
						case "END": isEnd = true; break;
						case "KEYDEF": while (line.hasNext()) { keyName += line.next(); } break;
						case "KEYTYPE": keyType = KeyActionType.getActionTypeFromString(line.next()); break;
						case "KEYS":
							String[] readKeys = line.next().split(",");
							int[] iKeys = new int[readKeys.length];
							for (int i = 0; i < readKeys.length; i++) { iKeys[i] = Integer.valueOf(readKeys[i]); }
							keys = new KeyComboAction(iKeys);
							builder.setBuilderKeys(keys);
							break;
						case "KEYDESC":
							while (line.hasNext()) {
								String moreDesc = line.next();
								if (line.hasNext()) { keyDesc += (moreDesc + " "); } 
								else { keyDesc += moreDesc; }
							}
							break;
						case "KEYENABLED": keyEnabled = Boolean.parseBoolean(line.next()); break;
						case "COMMAND":
							while (line.hasNext()) {
								while (line.hasNext()) {
									String moreDesc = line.next();
									if (line.hasNext()) { readCommand += (moreDesc + " "); } 
									else { readCommand += moreDesc; }
								}
							}
							builder.setBuilderCommand(readCommand);
							break;
						case "ITEM": builder.setBuilderCommandAndItemArgs(readCommand, Integer.parseInt(line.next())); break;
						case "DEBUGFUNC": builder.setBuilderDebugCommand(IDebugCommand.valueOf(line.next())); break;
						case "GUI":
							Class<?> guiClass = Class.forName(line.next());
							builder.setBuilderGuiToBeOpened(guiClass);
							break;
						case "KEYBIND":
							String keyBindIn = line.next();
							KeyBinding keyBind = null;
							for (KeyBinding k : mc.gameSettings.keyBindings) {
								if (k.getKeyDescription().equals(keyBindIn)) {
									keyBind = k;
									break;
								}
							}
							builder.setBuilderKeyBindingIn(keyBind, Boolean.parseBoolean(line.next()));
							break;
						case "MOD": builder.setBuilderSubMod(SubModType.getSubModFromString(line.next())); break;
						case "SCRIPT": break;
						case "SCRIPTARGS": builder.setBuilderScriptToBeRun(script, line.next().split(",")); break;
						case "KEYDEFEND":
							builder.buildHotKey(keyName, keyDesc, keyEnabled, keyType);
							builder.clearBuilderArgs();
							keyName = "";
							keyType = KeyActionType.UNDEFINED;
							keys = null;
							keyDesc = "";
							keyEnabled = false;
							script = null;
							readCommand = "";
							break;
						}
					}
					line.close();
				}
				fileReader.close();
			} catch (Exception e) { e.printStackTrace(); }
		} else {
			try {
				man.getKeySaver().saveUserMadeKeys();
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
}
