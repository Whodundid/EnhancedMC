package com.Whodundid.main.global.subMod.config;

import com.Whodundid.main.util.storageUtil.EArrayList;
import com.Whodundid.main.util.storageUtil.StorageBoxHolder;
import java.util.ArrayList;

//Last edited: Dec 12, 2018
//First Added: Dec 12, 2018
//Author: Hunter Bragg

public class ConfigBlock {
	
	protected StorageBoxHolder<String, ArrayList<String>> blockContents;
	protected boolean createEmptyLine = true;
	
	public ConfigBlock() { blockContents = new StorageBoxHolder<String, ArrayList<String>>().setAllowDuplicates(true); }
	public ConfigBlock(StorageBoxHolder<String, ArrayList<String>> elementsIn) { blockContents = elementsIn; }
	
	public ConfigBlock(String identifier, int... val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (int i : val) { list.add(Integer.toString(i)); }
		blockContents.add(identifier, list);
	}
	
	public ConfigBlock(String identifier, long... val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (long i : val) { list.add(Long.toString(i)); }
		blockContents.add(identifier, list);
	}
	
	public ConfigBlock(String identifier, double... val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (double i : val) { list.add(Double.toString(i)); }
		blockContents.add(identifier, list);
	}
	
	public ConfigBlock(String identifier, char... val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (char i : val) { list.add(Character.toString(i)); }
		blockContents.add(identifier, list);
	}
	
	public ConfigBlock(String identifier, String... val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (String i : val) { list.add(i); }
		blockContents.add(identifier, list);
	}
	
	public ConfigBlock(String identifier, boolean... val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (boolean i : val) { list.add(Boolean.toString(i)); }
		blockContents.add(identifier, list);
	}
	
	public ConfigBlock createEmptyLine(boolean valIn) { createEmptyLine = valIn; return this; }
	public boolean createEmptyLineAfterBlock() { return createEmptyLine; }
	public StorageBoxHolder<String, ArrayList<String>> getBlockContents() { return blockContents; }
}
