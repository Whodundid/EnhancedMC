package com.Whodundid.sls.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.sls.SkinSwitcher;
import com.Whodundid.sls.util.PartModes;

public class SLSGlobalConfig {
	
	static SkinSwitcher sls = (SkinSwitcher) RegisteredSubMods.getMod(SubModType.SLS);
	static int profileNum, intervalValue;
	static float versionNum;
	static String facing = "", resetMode = "", running = "";
	
	public static void updateGlobalConfig() {
		try {
			File config = new File("EnhancedMC/SkinLayerSwitcher/");
			if (!config.exists()) { config.mkdirs(); }
			PrintWriter writer = new PrintWriter("EnhancedMC/SkinLayerSwitcher/SLS_Mod_Config.cfg", "UTF-8");
			writer.println("** SLS Global Config **");
			writer.println("** NOTE: Only change these values if you know what you are doing!");
			writer.println("** Wrong values could potentially crash MC!");
			writer.println("** --------------------------");
			writer.println("VERSION: " + sls.version);
			writer.println("** --------------------------");
			writer.println("running: " + sls.globalOn); //move to skin profiles
			writer.println("intervalValue: " + sls.getCurrentInterval());
			writer.println("defaultProfile: " + sls.getDefaultLoadedProfile());
			writer.println("defaultResetMode: " + sls.resetMode.getMode());
			writer.println("defaultFacing: " + sls.getDefaultSkinFacing());
			writer.println("** --------------------------");
			writer.print("END");
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) { e.printStackTrace(); }
	}	

	public static void loadGlobalConfig(boolean onlyRunning) {
		File globalConfig = new File("EnhancedMC/SkinLayerSwitcher/SLS_Mod_Config.cfg");
		if (globalConfig.exists()) {
			boolean isEnd = false;
			resetValues();
			String command, configLine;
			try (Scanner fileReader = new Scanner(globalConfig)) {
				while (!isEnd) {
					configLine = fileReader.nextLine();
					Scanner line = new Scanner(configLine);
					command = line.next();
					switch (command) {
					case "**": break;
					case "VERSION:": versionNum = Float.valueOf(line.next()); break;
					case "running:": running = line.next(); break;
					case "intervalValue:": intervalValue = Integer.valueOf(line.next()); break;
					case "defaultProfile:": profileNum = Integer.valueOf(line.next()); break;
					case "defaultResetMode:": resetMode = line.next(); break;
					case "defaultFacing:": facing = line.next(); break;					
					case "END": isEnd = true;
					default: break;
					}
					line.close();
				}
				fileReader.close();
				//System.out.println("SLS Global Config Loaded");
				updateValues(onlyRunning);
				return;
			} catch (FileNotFoundException e) {}
		}
		updateGlobalConfig();
		loadGlobalConfig(false);
	}
	
	private static void resetValues() {
		profileNum = 1; intervalValue = 25;
		versionNum = Float.valueOf(sls.version);
		facing = "Front";
		resetMode = "none";
	}
	
	private static void updateValues(boolean onlyRunning) {
		if (versionNum < Float.valueOf(sls.version)) {
			System.out.println("WARNING! Using older configs in skin switcher could cause unpredicatble events!");
		}
		if (versionNum > Float.valueOf(sls.version)) {
			System.out.println("WARNING! Future config version detected! Unpredicatble results may occur!");
		}
		
		sls.globalOn = running.equalsIgnoreCase("true");
		
		if (!onlyRunning) {
			if (intervalValue < 0 || intervalValue > 100000) { intervalValue = 25; }
			if (profileNum < 1 || profileNum > 4) { profileNum = 1; }
			if (!facing.equalsIgnoreCase("front") && !facing.equalsIgnoreCase("back")) { facing = "front"; }
			
			switch (resetMode) {
			case "switch": sls.resetMode = PartModes.SW; break;
			case "blink": sls.resetMode = PartModes.BL; break;
			case "none": sls.resetMode = PartModes.N; break;
			}
			
			sls.currentChangeValue = intervalValue;
			sls.currentLoadedProfile = profileNum;
			sls.defaultLoadedProfile = profileNum;
			sls.skinFrontFacing = !facing.equalsIgnoreCase("back");
		}		
	}	
}