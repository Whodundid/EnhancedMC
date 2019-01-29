package com.Whodundid.main.global;

import com.Whodundid.debug.ExperimentGui;
import com.Whodundid.main.global.SettingsGuiPages.Page;
import com.Whodundid.main.global.SettingsGuiPages.PageEntry;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.global.subMod.SubModErrorDialogueBox;
import com.Whodundid.main.global.subMod.SubModErrorType;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.main.util.miscUtil.EUtil;
import com.Whodundid.main.util.storageUtil.EArrayList;
import com.Whodundid.main.util.storageUtil.StorageBox;

//Last edited: Dec 28, 2018
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public class SettingsGui extends EnhancedGui {
	
	SettingsGuiPages pages;
	EGuiButton nextPage, previousPage, experimentGui;
	SubModErrorDialogueBox errorBox;
	protected int pageToBeLoaded = -1;
	
	public SettingsGui() { super(); }
	public SettingsGui(int posX, int posY) { super(posX, posY); }
	public SettingsGui(int posX, int posY, int page) { super(posX, posY); pageToBeLoaded = page; }
	
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		nextPage = new EGuiButton(this, wPos + 40, hPos + 103, 55, 20, "Next");
		previousPage = new EGuiButton(this, wPos - 95, hPos + 103, 55, 20, "Previous");
		experimentGui = new EGuiButton(this, 1, res.getScaledHeight() - 21, 85, 20, "ExperimentGui");
		
		EUtil.setObjectPersistence(true, nextPage, previousPage, experimentGui);
		experimentGui.setPositionLocked(true);
		
		pages = new SettingsGuiPages();
		
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { pages.addOptionToPages(m); }
		pages.setCurrentPage(0);
		
		assembleEntryButtons();
		loadPage(pageToBeLoaded > 0 ? pageToBeLoaded : 0);
		
		addObject(nextPage, previousPage, experimentGui);
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		drawDefaultBackground();
		drawRect(wPos - 99, hPos - 109, wPos + 99, hPos + 98, -0x00cfcfcf);
		drawRect(startX, hPos + 98, endX, hPos + 99, 0xff000000);
		drawRect(startX, hPos - 110, endX, hPos - 109, 0xff000000);
		drawRect(wPos + 30, hPos - 109, wPos + 31, hPos + 98, 0xff000000);
		drawCenteredStringWithShadow("" + (pages.currentPage + 1), wPos, hPos + 110, 0xffd800);
		drawCenteredStringWithShadow("Enhanced MC Settings", wPos, hPos - 122, 0xffbb00);
	}
	
	private void assembleEntryButtons() {
		int i = 0;
		for (int x = 0; x < pages.getNumberOfPages(); x++) {
			Page p = pages.getPage(x);
			for (int q = 0; q < p.getSize(); q++) {
				PageEntry entry = p.getPageEntryAtPos(q);
				addObject(entry.modGui = new EGuiButton(this, wPos - 89, hPos - 104 + (i * 22), 110, 20, SubModType.getModName(entry.getSubMod().getModType())) {
					{ setRunActionOnPress(true); }
					@Override public void performAction() {
						if (getPressedButton() == 0) {
							playPressSound();
							if (entry.getSubMod() != null) {
								StorageBox potato = new StorageBox(guiInstance.startX, guiInstance.startY);
								EnhancedGui gui = entry.getSubMod().getMainGui(true, potato, guiInstance);
								if (gui != null) { mc.displayGuiScreen(gui); }
								else {
									errorBox = new SubModErrorDialogueBox(guiInstance, wPos - 125, hPos - 48, 250, 75, SubModErrorType.NOGUI, entry.getSubMod());
									guiInstance.addObject(errorBox);
								}
							} 
						}
					}
				});
				addObject(entry.enable = new EGuiButton(this, wPos + 39, hPos - 104 + (i * 22), 50, 20, entry.getSubMod().isEnabled() ? "Enabled" : "Disabled") {
					{ setRunActionOnPress(true); setDisplayStringColor(entry.getSubMod().isEnabled() ? 0x55ff55 : 0xff5555); }
					@Override public void performAction() {
						if (getPressedButton() == 0) {
							playPressSound();
							if (entry.getSubMod() != null) {
								if (!entry.getSubMod().isEnabled()) {
									EArrayList<SubModType> allDependencies = RegisteredSubMods.getAllModDependencies(entry.getSubMod());
									EArrayList<SubMod> disabledDependancies = new EArrayList();
									allDependencies.forEach((t) -> { SubMod m = RegisteredSubMods.getMod(t); if (!m.isEnabled()) { disabledDependancies.add(m); } });
									if (!disabledDependancies.isEmpty()) {
										errorBox = new SubModErrorDialogueBox(guiInstance, wPos - 125, hPos - 48, 250, 75, SubModErrorType.ENABLE, entry.getSubMod());
										errorBox.createErrorMessage(disabledDependancies);
										guiInstance.addObject(errorBox);
										return;
									}
								} else {
									EArrayList<SubModType> allDependents = RegisteredSubMods.getAllDependantsOfMod(entry.getSubMod());
									EArrayList<SubMod> enabledDependants = new EArrayList();
									allDependents.forEach(t -> { SubMod m = RegisteredSubMods.getMod(t); if (m.isEnabled()) { enabledDependants.add(m); } }); 
									if (!enabledDependants.isEmpty()) {
										errorBox = new SubModErrorDialogueBox(guiInstance, wPos - 125, hPos - 48, 250, 75, SubModErrorType.DISABLE, entry.getSubMod());
										errorBox.createErrorMessage(enabledDependants);
										guiInstance.addObject(errorBox);
										return;
									}
								}
							}
							GlobalSettings.updateSetting(entry.getSubMod(), !entry.getSubMod().isEnabled());
							setDisplayString(entry.getSubMod().isEnabled() ? "Enabled" : "Disabled");
							setDisplayStringColor(entry.getSubMod().isEnabled() ? 0x55ff55 : 0xff4444);
						}
					}
				});
				entry.modGui.setVisible(false);
				entry.enable.setVisible(false);
				i++;
			}
			i = 0;
		}
	}
	
	private void loadPage(int pageNum) {
		for (IEnhancedGuiObject o : getObjects()) { o.setVisible(false); }
		pages.setCurrentPage(pageNum);
		nextPage.setEnabled(true);
		previousPage.setEnabled(true);
		if (pageNum == 0) { previousPage.setEnabled(false); }
		if (pageNum == pages.getNumberOfPages() - 1) { nextPage.setEnabled(false); }
		if (pages.getNumberOfPages() > 0) {
			for (int i = 0; i < pages.getPage(pages.currentPage).getSize(); i++) {
				PageEntry entry = pages.getPage(pages.currentPage).getPageEntryAtPos(i);
				entry.modGui.setVisible(true);
				entry.enable.setVisible(true);
			}
		}
	}
	
	public SettingsGui reloadCurrentPage() {
		setPageToBeLoaded(pages.currentPage);
		guiObjects.clear();
		useCustomPosition = true;
		initGui();
		return this;
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.runActionOnPress()) { object.performAction(); }
		else {
			if (object.equals(nextPage)) {
				if (pages.currentPage + 1 < pages.getNumberOfPages()) {
					pages.currentPage += 1;
					loadPage(pages.currentPage);
				}
			}
			if (object.equals(previousPage)) {
				if (pages.currentPage > 0 && pages.currentPage < pages.getNumberOfPages()) {
					pages.currentPage -= 1;
					loadPage(pages.currentPage);
				}
			}
			if (object.equals(experimentGui)) {
				mc.displayGuiScreen(new ExperimentGui(guiInstance));
			}
		}
	}
	
	public int getCurrentPageNum() { return pages.currentPage; }
	public SettingsGui setPageToBeLoaded(int pageNumIn) { pageToBeLoaded = pageNumIn; return this; }
}
