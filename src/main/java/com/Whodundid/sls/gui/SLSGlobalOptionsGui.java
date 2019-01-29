package com.Whodundid.sls.gui;

import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.miscUtil.Resources;
import com.Whodundid.sls.SkinSwitcher;
import com.Whodundid.sls.config.SLSGlobalConfig;
import com.Whodundid.sls.config.SLSSaveSkinConfigProfile;
import com.Whodundid.sls.util.PartModes;

public class SLSGlobalOptionsGui extends EnhancedGui {

	SkinSwitcher sls = (SkinSwitcher) RegisteredSubMods.getMod(SubModType.SLS);
	EGuiButton intervalBtn, loadProfileBtn, defaultProfileResetBtn, defaultFacingBtn, saveStateBtn, resetBtn, doneBtn;
	String isFront, resetMode, saveStates;
	int currentProfile;
	
	public SLSGlobalOptionsGui() { super(); }
	public SLSGlobalOptionsGui(EnhancedGui oldGui) { super(oldGui); }
	public SLSGlobalOptionsGui(int posX, int posY) { super(posX, posY); }
	public SLSGlobalOptionsGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	
	@Override
	public void initGui() {
		super.initGui();		
	}
	
	@Override
	public void initObjects() {
		currentProfile = sls.defaultLoadedProfile;
		isFront = (sls.skinFrontFacing) ? "Front" : "Back";
		saveStates = (sls.savePartStates) ? "True" : "False";
		switch (sls.resetMode) {
		case SW: resetMode = "Switch"; break;
		case BL: resetMode = "Blink"; break;
		case IN: resetMode = "Indv."; break;
		default: resetMode = "Off"; break;
		}
		
		doneBtn = new EGuiButton(this, wPos + 51, hPos + 104, 45, 20, "Done");
		resetBtn = new EGuiButton(this, wPos - 1, hPos + 104, 45, 20, "Reset");
		intervalBtn = new EGuiButton(this, wPos + 53, hPos - 90, 40, 20, "" + sls.currentChangeValue);
		loadProfileBtn = new EGuiButton(this, wPos + 53, hPos - 59, 40, 20, "" + sls.defaultLoadedProfile);
		defaultProfileResetBtn = new EGuiButton(this, wPos + 53, hPos - 28, 40, 20, resetMode);
		defaultFacingBtn = new EGuiButton(this, wPos + 53, hPos + 3, 40, 20, isFront);
		saveStateBtn = new EGuiButton(this, wPos + 53, hPos + 67, 40, 20, saveStates);
		
		addObject(doneBtn, resetBtn, intervalBtn, loadProfileBtn, defaultProfileResetBtn, defaultFacingBtn, saveStateBtn);
	}
	
	@Override
	public void drawObject(int x, int y, float ticks) {
		mc.renderEngine.bindTexture(Resources.SLSguiTexture);
		drawTexturedModalRect(wPos - width / 2, hPos - height / 2, 0, 0, width, height);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(doneBtn)) { returnToMainGui(); }
		if (object.equals(intervalBtn)) { changeIntervalValue(); }
		if (object.equals(loadProfileBtn)) { changeProfile(); }
		if (object.equals(defaultFacingBtn)) { changeFacing(); }
		if (object.equals(defaultProfileResetBtn)) {
			switch (sls.resetMode) {
			case SW: sls.resetMode = PartModes.BL; defaultProfileResetBtn.displayString = "Blink"; break;
			case BL: sls.resetMode = PartModes.IN; defaultProfileResetBtn.displayString = "Indv."; break;
			case IN: sls.resetMode = PartModes.N; defaultProfileResetBtn.displayString = "Off"; break;
			default: sls.resetMode = PartModes.SW; defaultProfileResetBtn.displayString = "Switch"; break;
			}
			SLSGlobalConfig.updateGlobalConfig();
		}
		if (object.equals(saveStateBtn)) {
			sls.savePartStates = !sls.savePartStates;
			this.saveStateBtn.displayString = (sls.savePartStates) ? "True" : "False";
			SLSSaveSkinConfigProfile.updateProfile(sls.currentLoadedProfile);
		}
		if (object.equals(resetBtn)) {
			sls.resetMode = PartModes.N; this.defaultProfileResetBtn.displayString = "Indv.";
			sls.currentChangeValue = 100; intervalBtn.displayString = "" + sls.currentChangeValue;
			currentProfile = 1; sls.defaultLoadedProfile = 1; loadProfileBtn.displayString = "" + currentProfile;
			defaultFacingBtn.displayString = "Front"; isFront = "Front"; sls.rotateSkin(isFront);
			saveStateBtn.displayString = "True"; sls.savePartStates = true;
			SLSGlobalConfig.updateGlobalConfig();
			SLSSaveSkinConfigProfile.updateProfile(sls.currentLoadedProfile);
		}
	}
	
	private void changeIntervalValue() {
		int currentInterval = sls.currentChangeValue;
		switch (currentInterval) {
		case 25: sls.currentChangeValue = 50; break;
		case 50: sls.currentChangeValue = 100; break;
		case 100: sls.currentChangeValue = 250; break;
		case 250: sls.currentChangeValue = 500; break;
		case 500: sls.currentChangeValue = 1000; break;
		case 1000: sls.currentChangeValue = 25; break;
		}
		intervalBtn.displayString = "" + sls.currentChangeValue;
		SLSGlobalConfig.updateGlobalConfig();
	}
	
	private void changeProfile() {
		if (currentProfile == 4) { currentProfile = 1; }
		else if (currentProfile < 4) { currentProfile += 1; }
		loadProfileBtn.displayString = "" + currentProfile;
		sls.defaultLoadedProfile = currentProfile;
		SLSGlobalConfig.updateGlobalConfig();
	}
	
	private void changeFacing() {
		isFront = isFront.equals("Front") ? "Back" : "Front";
		defaultFacingBtn.displayString = isFront;
		sls.rotateSkin(isFront);
		SLSGlobalConfig.updateGlobalConfig();
	}
	
	private void returnToMainGui() {
    	sls.currentPart = null;
    	closeGui();
	}
}