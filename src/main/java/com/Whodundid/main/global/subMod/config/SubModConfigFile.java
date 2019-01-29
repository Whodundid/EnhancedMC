package com.Whodundid.main.global.subMod.config;

import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.util.storageUtil.EArrayList;
import com.Whodundid.main.util.storageUtil.StorageBox;
import com.Whodundid.main.util.storageUtil.StorageBoxHolder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

//Last edited: Dec 12, 2018
//First Added: Dec 12, 2018
//Author: Hunter Bragg

public class SubModConfigFile {
	
	protected SubMod mod;
	protected String configName = "";
	protected StorageBoxHolder<String, ArrayList<String>> configValues;
	
	public SubModConfigFile(SubMod modIn, String name) {
		mod = modIn;
		configName = name;
		configValues = new StorageBoxHolder<String, ArrayList<String>>();
	}
	
	public boolean exists() { return mod.getConfig().getConfigFileLocation(configName).exists(); }
	
	public StorageBoxHolder getConfigContents() {
		if (configValues.isEmpty()) { parseFile(); }
		return configValues;
	}
	
	private void parseFile() {
		File configFile = mod.getConfig().getConfigFileLocation(configName);
		if (configFile.exists()) {
			Scanner reader = null;
			try {
				reader = new Scanner(configFile);
				while (reader.hasNextLine()) {
					String line = reader.nextLine();
					if (line.length() >= 2 && line.substring(0, 2).equals("**")) { continue; } //comment identifier
					if (line.equals("END")) { break; } //config end identifier
					if (line.length() == 1) { break; } //ignore one character long lines
					
					String identifier = "", restOfLine = "";
					if (line.length() > 0) {
						for (int i = 0; i < line.length(); i++) {
							if (line.charAt(i) == ':') {
								identifier = line.substring(0, i + 1);
								restOfLine = line.substring(i + 1, line.length());
								break;
							}
						}
						if (!identifier.isEmpty() && !restOfLine.isEmpty()) {
							String[] lineContents = restOfLine.split(" ");
							if (lineContents.length > 1) {
								ArrayList<String> values = new ArrayList();
								for (int i = 1; i < lineContents.length; i++) { values.add(lineContents[i]); }
								configValues.add(identifier, values);
							}
						}
					}
				}
			} catch (FileNotFoundException e) { e.printStackTrace(); }
			finally {
				if (reader != null) { reader.close(); }
			}
		}
	}
	
	public boolean createConfig(EArrayList<ConfigBlock> configContentsIn) { return createConfig(new ArrayList(configContentsIn)); }
	public boolean createConfig(ArrayList<ConfigBlock> configContentsIn) {
		PrintWriter saver = null;
		try {
			saver = new PrintWriter(mod.getConfig().getConfigFileLocation(configName), "UTF-8");
			for (ConfigBlock block : configContentsIn) {
				StorageBoxHolder<String, ArrayList<String>> blockContents = block.getBlockContents();
				for (StorageBox<String, ArrayList<String>> line : blockContents) {
					saver.print(line.getObject() + " ");
					ArrayList<String> values = line.getValue();
					for (int i = 0; i < values.size(); i++) {
						if (i != values.size() - 1) { saver.print(values.get(i) + " "); }
						else { saver.print(values.get(i)); }
					}
					saver.println();
				}
				if (block.createEmptyLineAfterBlock()) { saver.println(); }
			}
			saver.print("END");
		} catch (Exception e) { e.printStackTrace(); return false; }
		finally { if (saver != null) { saver.close(); } }
		return true;
	}
}
