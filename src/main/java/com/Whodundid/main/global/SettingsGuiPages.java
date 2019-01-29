package com.Whodundid.main.global;

import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import java.util.ArrayList;

//Last edited: 10-16-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class SettingsGuiPages {

	static int currentPage = 0;
	private ArrayList<Page> pages = new ArrayList();
	
	public PageEntry addOptionToPages(SubMod modIn) {
		if (pages.isEmpty()) { pages.add(new Page()); }
		for (Page p : pages) {
			if (!p.isFull()) { return p.addToList(modIn); }
		}
		Page newPage = new Page();
		PageEntry entry = newPage.addToList(modIn);
		pages.add(newPage);
		return entry;
	}
	
	public PageEntry getPageEntryFromSubMod(SubMod modIn) {
		for (Page p : pages) {
			for (int i = 0; i < p.getSize(); i++) {
				PageEntry entry = p.getPageEntryAtPos(i);
				if (entry.getSubMod().equals(modIn)) { return entry; }
			}
		}
		return null;
	}
	
	private void loadPage(int pageNum) {
		if (pageNum >= 0 && pageNum < pages.size()) {
			Page p = pages.get(pageNum);
			p.hidePage = false;
		}
	}
	
	public synchronized void clearPages() { pages.clear(); }
	public void setCurrentPage(int pageNum) { currentPage = pageNum; loadPage(pageNum); }
	
	public ArrayList<Page> getPages() { return pages; }
	public Page getPage(int pageNum) { return pages.get(pageNum); }
	public SubMod getModAtPagePos(int pos) { return pages.get(currentPage).getPageEntryAtPos(pos).getSubMod(); }
	public PageEntry getPageEntryAtPos(int pos) { return pages.get(currentPage).getPageEntryAtPos(pos); }
	public int getNumberOfPages() { return pages.size(); }
	
	public void nextPage() { setCurrentPage(currentPage < pages.size() - 1 ? currentPage++ : pages.size() - 1); }
	public void previousPage() { setCurrentPage(currentPage > 0 ? currentPage-- : 0); }
	
	public class Page {
		int maxLength = 9;
		ArrayList<PageEntry> pageContents = new ArrayList();
		public boolean hidePage = false;
		
		public PageEntry addToList(SubMod subModIn) {
			PageEntry entry = new PageEntry(subModIn);
			return (pageContents.add(entry)) ? entry : null;
		}
		
		public PageEntry getPageEntryAtPos(int pos) { return pageContents.get(pos); }
		public boolean isFull() { return pageContents.size() == maxLength; }
		public void setMaxLength(int lengthIn) { maxLength = lengthIn; }
		public int getMaxLength() { return maxLength; }
		public int getSize() { return pageContents.size(); }
	}
	
	public class PageEntry {
		public SubMod mod;
		public EGuiButton modGui, enable;
		
		public PageEntry(SubMod modIn) {
			mod = modIn;
		}
		
		public SubMod getSubMod() { return mod; }
	}
}
