package com.Whodundid.pingDrawer;

import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.global.subMod.config.CommentConfigBlock;
import com.Whodundid.main.global.subMod.config.ConfigBlock;
import com.Whodundid.main.global.subMod.config.SubModConfig;
import com.Whodundid.main.global.subMod.config.SubModConfigFile;
import com.Whodundid.main.util.miscUtil.ScreenLocation;
import com.Whodundid.main.util.storageUtil.EArrayList;
import com.Whodundid.main.util.storageUtil.StorageBox;
import com.Whodundid.main.util.storageUtil.StorageBoxHolder;
import java.util.ArrayList;

//Last edited: Dec 12, 2018
//First Added: Dec 12, 2018
//Author: Hunter Bragg

public class PingConfig extends SubModConfig {

	Ping mod;
	
	public PingConfig(SubMod modIn, String... configNamesIn) {
		super(modIn, configNamesIn);
		mod = (Ping) modIn;
	}
	
	@Override
	public boolean saveConfig(String configName) {
		CommentConfigBlock commentBlock = new CommentConfigBlock().addLine("Ping Config", "Screen location, custom position (x, y) values, drawWithChatOpen");
		ConfigBlock screenLoc = new ConfigBlock("ScreenLocation:", mod.getScreenLocation().toString()).createEmptyLine(false);
		ConfigBlock customPos = new ConfigBlock("Position:", mod.getLocation().getObject(), mod.getLocation().getValue()).createEmptyLine(false);
		ConfigBlock drawChat = new ConfigBlock("Draw with Chat:", mod.drawWithChatOpen);
		
		SubModConfigFile configFile = new SubModConfigFile(mod, configName);
		return configFile.createConfig(new EArrayList().add(commentBlock, screenLoc, customPos, drawChat));
	}

	@Override
	public boolean loadConfig(String configName) {
		try {
			SubModConfigFile configFile = new SubModConfigFile(mod, configName);
			if (!configFile.exists()) { return false; }
			
			StorageBoxHolder<String, ArrayList<String>> configValues = configFile.getConfigContents();
			if (configValues.size() > 0) {
				ScreenLocation loc = ScreenLocation.valueOf(configValues.getBoxWithObj("ScreenLocation:").getValue().get(0));
				if (loc.equals(ScreenLocation.CUSTOM)) {
					ArrayList<String> values = configValues.getBoxWithObj("Position:").getValue();
					if (values != null && values.size() == 2) {
						int x, y;
						x = Integer.parseInt(values.get(0));
						y = Integer.parseInt(values.get(1));
						mod.setLocation(x, y);
					} else { return false; }
				} else { mod.setLocation(loc); }
				mod.setDrawWithChatOpen(Boolean.parseBoolean(configValues.getBoxWithObj("Draw with Chat:").getValue().get(0)));
				return true;
			}
		} catch (Exception e) { e.printStackTrace(); }
		return false;
	}
}
