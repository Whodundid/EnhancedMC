package com.Whodundid.clearVisuals;

import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.global.subMod.config.CommentConfigBlock;
import com.Whodundid.main.global.subMod.config.ConfigBlock;
import com.Whodundid.main.global.subMod.config.SubModConfig;
import com.Whodundid.main.global.subMod.config.SubModConfigFile;
import com.Whodundid.main.util.storageUtil.EArrayList;
import com.Whodundid.main.util.storageUtil.StorageBoxHolder;
import java.util.ArrayList;

//Last edited: Dec 29, 2018
//First Added: Dec 29, 2018
//Author: Hunter Bragg

public class ClearVisualsConfig extends SubModConfig {

	ClearVisuals mod;
	
	public ClearVisualsConfig(SubMod modIn, String... configNamesIn) {
		super(modIn, configNamesIn);
		mod = (ClearVisuals) modIn;
	}
	
	@Override
	public boolean saveConfig(String configName) {
		EArrayList<ConfigBlock> configLines = new EArrayList();
		configLines.add(new CommentConfigBlock().addLine("Clear Visuals Config"));
		configLines.add(new ConfigBlock("Gama Value:", mod.getCurrentGama()).createEmptyLine(false));
		configLines.add(new ConfigBlock("Draw Fire:", mod.isFireDrawn()).createEmptyLine(false));
		configLines.add(new ConfigBlock("Draw Water Overlay:", mod.isWaterOverlayDrawn()).createEmptyLine(false));
		configLines.add(new ConfigBlock("Clear Lava:", mod.isClearLava()).createEmptyLine(false));
		configLines.add(new ConfigBlock("Clear Water:", mod.isClearWater()));
		
		SubModConfigFile configFile = new SubModConfigFile(mod, configName);
		return configFile.createConfig(configLines);
	}
	
	@Override
	public boolean loadConfig(String configName) {
		try {
			SubModConfigFile configFile = new SubModConfigFile(mod, configName);
			if (!configFile.exists()) { return false; }
			
			StorageBoxHolder<String, ArrayList<String>> configValues = configFile.getConfigContents();
			if (configValues.size() > 0) {
				float gamaIn = Float.parseFloat(configValues.getBoxWithObj("Gama Value:").getValue().get(0));
				boolean drawFire = Boolean.parseBoolean(configValues.getBoxWithObj("Draw Fire:").getValue().get(0));
				boolean drawWaterOverlay = Boolean.parseBoolean(configValues.getBoxWithObj("Draw Water Overlay:").getValue().get(0));
				boolean clearLava = Boolean.parseBoolean(configValues.getBoxWithObj("Clear Lava:").getValue().get(0));
				boolean clearWater = Boolean.parseBoolean(configValues.getBoxWithObj("Clear Water:").getValue().get(0));
				mod.setGama(gamaIn);
				mod.setFireVisibility(drawFire);
				mod.setUnderWaterOverlayVisibility(drawWaterOverlay);
				mod.setClearLava(clearLava);
				mod.setClearWater(clearWater);
				return true;
			}
		} catch (Exception e) { e.printStackTrace(); }
		return false;
	}
}
