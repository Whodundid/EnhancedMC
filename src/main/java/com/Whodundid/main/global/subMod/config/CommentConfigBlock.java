package com.Whodundid.main.global.subMod.config;

import com.Whodundid.main.util.storageUtil.EArrayList;
import com.Whodundid.main.util.storageUtil.StorageBoxHolder;
import java.util.ArrayList;

//Last edited: Dec 12, 2018
//First Added: Dec 12, 2018
//Author: Hunter Bragg

public class CommentConfigBlock extends ConfigBlock {
	
	public CommentConfigBlock() { super(); }
	public CommentConfigBlock(StorageBoxHolder<String, ArrayList<String>> elementsIn) { super(elementsIn); }
	
	public CommentConfigBlock addLine (String... comments) {
		for (String s : comments) { addLine(s); }
		return this;
	}
	public CommentConfigBlock addLine(String commentIn) {
		blockContents.add("**", new EArrayList<String>(commentIn));
		return this;
	}
}
