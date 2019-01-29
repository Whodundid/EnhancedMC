package com.Whodundid.main.global.subMod.config;

import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubMod;
import java.io.File;
import java.util.ArrayList;

//Last edited: Dec 12, 2018
//First Added: Dec 12, 2018
//Author: Hunter Bragg

public abstract class SubModConfig {

	protected SubMod mod;
	protected ArrayList<String> configNames;
	
	public SubModConfig(SubMod modIn, String... configNamesIn) {
		mod = modIn;
		configNames = new ArrayList();
		for (String name : configNamesIn) { configNames.add(name); }
		
		//check if directory exists
		File basePath = RegisteredSubMods.getModConfigBaseFileLocation(mod.getModType());
		if (!basePath.exists()) { basePath.mkdirs(); }
	}
	
	public boolean saveConfig() {
		for (String config : getConfigNames()) { if (!saveConfig(config)) { return false; } }
		return true;
	}
	
	public boolean loadConfig() {
		for (String config : getConfigNames()) { if (!loadConfig(config)) { return false; } }
		return true;
	}
	
	public boolean saveConfig(String configName) { return false; }
	public boolean loadConfig(String configName) { return true; }
	
	public File getConfigFileLocation(String configName) { return new File(RegisteredSubMods.getModConfigBaseFileLocation(mod.getModType()).getAbsolutePath() + "/" + configName + ".cfg"); }
	public ArrayList<String> getConfigNames() { return new ArrayList<String>(configNames); }
	public int getNumberOfConfigFiles() { return configNames.size(); }
	
}
