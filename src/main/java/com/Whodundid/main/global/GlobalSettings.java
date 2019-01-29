package com.Whodundid.main.global;

import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.global.subMod.SubModType;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

//Last edited: 10-16-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class GlobalSettings {
	
	public static void setAll(boolean state) {
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.setEnabled(state); }
	}
	
	public static SubMod getSubMod(SubModType modIn) {
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) {
			if (m.getModType().equals(modIn)) { return m; }
		}
		return null;
	}
	
	public static boolean getSubModEnabledValue(SubModType modIn) {
		SubMod m = getSubMod(modIn);
		return m != null ? m.isEnabled() : false;
	}
	
	public static void updateSetting(SubMod modIn, boolean state) {
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) {
			if (m.equals(modIn)) {
				m.setEnabled(state);
				updateConfig();
				break;
			}
		}
	}
	
	public static void updateSetting(SubModType modIn, boolean state) {
		SubMod m = getSubMod(modIn);
		if (m != null) {
			m.setEnabled(state);
			updateConfig();
		}
	}
	
	private static void updateConfig() {
		try {
			File settings = new File("EnhancedMC/");
			if (!settings.exists()) { settings.mkdirs(); }
			PrintWriter saver = new PrintWriter("EnhancedMC/globalSettings.cfg" , "UTF-8");
			for (SubModType m : SubModType.values()) {
				if (!m.equals(SubModType.ALL)) {
					saver.println(m.toString() + " " + getSubModEnabledValue(m));
				}
			}
			saver.close();
		} catch (Exception e) { e.printStackTrace(); }		
	}
	
	public static void loadConfig() {
		try {
			File loadedSettings = new File("EnhancedMC/globalSettings.cfg");
			if (loadedSettings.exists()) {
				Scanner loader = new Scanner(loadedSettings);
				while (loader.hasNextLine()) {
					Scanner line = new Scanner(loader.nextLine());
					updateSetting(SubModType.getSubModFromString(line.next()), Boolean.parseBoolean(line.next()));
					line.close();
				}
				loader.close();
			} else { updateConfig(); }
		} catch (Exception e) { e.printStackTrace(); }
	}
}
