package com.Whodundid.sls.gui;

import java.io.IOException;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.miscUtil.ChatBuilder;
import com.Whodundid.main.util.miscUtil.Resources;
import com.Whodundid.sls.SkinSwitcher;
import com.Whodundid.sls.config.SLSGlobalConfig;
import com.Whodundid.sls.config.SLSLoadSkinConfigProfile;
import com.Whodundid.sls.config.SLSSaveSkinConfigProfile;
import com.Whodundid.sls.util.GlobalModes;
import com.Whodundid.sls.util.LayerTypes;
import com.Whodundid.sls.util.PartStats;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class SLSMainGui extends EnhancedGui {
	
	SkinSwitcher sls = (SkinSwitcher) RegisteredSubMods.getMod(SubModType.SLS);
	ResourceLocation playerSkin;
	String skinType;
	EGuiButton headBtn, jackBtn, larmBtn, rarmBtn, llegBtn, rlegBtn, capeBtn;
	EGuiButton optionsBtn, stateBtn, valueUpBtn, valueDownBtn, flipBtn;
	EGuiButton resetProfileBtn, loadProfileBtn;
	String headState, chestState, larmState, rarmState, llegState, rlegState, capeState;
	String hRate, cRate, caRate, laRate, raRate, llRate, rlRate, state;
	int gss, gbs, gbd;
	int hSpeed, jSpeed, caSpeed, laSpeed, raSpeed, llSpeed, rlSpeed;
	int hBSpeed, jBSpeed, caBSpeed, laBSpeed, raBSpeed, llBSpeed, rlBSpeed;
	int hBD, jBD, caBD, laBD, raBD, llBD, rlBD;
	boolean oneClick, alreadyPressed;
	
	public SLSMainGui() { super(); }
	public SLSMainGui(EnhancedGui oldGui) { super(oldGui); }
	public SLSMainGui(int posX, int posY) { super(posX, posY); }
	public SLSMainGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	
	@Override
	public void initGui() {
		super.initGui();
		playerSkin = mc.thePlayer.getLocationSkin();
		skinType = mc.thePlayer.getSkinType();
	}
	
	@Override
	public void initObjects() {
		if (sls.getGlobalSwitchingStatus()) state = "All Switch";
		else if (sls.getGlobalBlinkingStatus()) state = "All Blink";
		else if (sls.globalOn) { state = "Individual"; }
		else { state = "Off"; }
		
		larmBtn = new EGuiButton(this, wPos + 47, hPos - 35, 46, 20, sls.skinFrontFacing ? "L Arm" : "R Arm");
		rarmBtn = new EGuiButton(this, wPos - 93, hPos - 35, 46, 20, sls.skinFrontFacing ? "R Arm" : "L Arm");
		llegBtn = new EGuiButton(this, wPos + 47, hPos + 16, 46, 20, sls.skinFrontFacing ? "R Leg" : "L Leg");
		rlegBtn = new EGuiButton(this, wPos - 93, hPos + 16, 46, 20, sls.skinFrontFacing ? "L Leg" : "R Leg");
		headBtn = new EGuiButton(this, wPos - 23, hPos - 121, 46, 20, "Hat");
		jackBtn = new EGuiButton(this, wPos - 93, hPos - 86, 46, 20, "Jacket");
		capeBtn = new EGuiButton(this, wPos + 47, hPos - 86, 46, 20, "Cape");
		
		optionsBtn = new EGuiButton(this, wPos + 41, hPos + 76, 55, 20, "Options");
		flipBtn = new EGuiButton(this, wPos - 22, hPos + 76, 55, 20, "Flip All");
		stateBtn = new EGuiButton(this, wPos - 95, hPos + 76, 65, 20, state);
		valueUpBtn = new EGuiButton(this, wPos - 95, hPos - 123, 58, 20, "All + " + sls.currentChangeValue);
		valueDownBtn = new EGuiButton(this, wPos + 37, hPos - 123, 58, 20, "All - " + sls.currentChangeValue);
		resetProfileBtn = new EGuiButton(this, wPos - 1, hPos + 104, 45, 20, "Reset");
		loadProfileBtn = new EGuiButton(this, wPos + 51, hPos + 104, 45, 20, "Load");
		
		addObject(larmBtn, rarmBtn, llegBtn, rlegBtn, headBtn, jackBtn, capeBtn);
		addObject(optionsBtn, flipBtn, stateBtn, valueUpBtn, valueDownBtn, resetProfileBtn, loadProfileBtn);
	}
	
	@Override
	public void drawObject(int x, int y, float ticks) {
		mc.renderEngine.bindTexture(skinType.equals("slim") ? Resources.SLSsettingsGuiAlex : Resources.SLSsettingsGuiSteve);
		drawTexturedModalRect(wPos - width / 2, hPos - height / 2, 0, 0, width, height);
		drawMouseEnteredBtn();
		drawSelectedProfile();
		drawLoadedProfile();
		drawSkin();
		drawDisabledParts();
		drawRates();
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(optionsBtn)) { openOptionsGui(); }
		if (object.equals(stateBtn)) {
			if (sls.currentMode == GlobalModes.SW) sls.currentMode = GlobalModes.BL;
			else if (sls.currentMode == GlobalModes.BL) sls.currentMode = GlobalModes.IN;
			else if (sls.currentMode == GlobalModes.IN) sls.currentMode = GlobalModes.OFF;
			else if (sls.currentMode == GlobalModes.OFF) sls.currentMode = GlobalModes.SW;
			changeStateBtn();
		}
		if (object.equals(valueUpBtn)) {
			changePartRateValues(sls.currentChangeValue);
			if (sls.getGlobalSwitchingStatus()) {
				sls.currentMode = GlobalModes.SW;
				changeStateBtn();
			} else if (sls.getGlobalBlinkingStatus()) {
				sls.currentMode = GlobalModes.BL;
				changeStateBtn();
			}
		}
		if (object.equals(valueDownBtn)) {
			changePartRateValues(-sls.currentChangeValue);
			if (sls.getGlobalSwitchingStatus()) {
				sls.currentMode = GlobalModes.SW;
				changeStateBtn();
			} else if (sls.getGlobalBlinkingStatus()) {
				sls.currentMode = GlobalModes.BL;
				changeStateBtn();
			}
		}
		if (object.equals(headBtn)) { sls.currentPart = LayerTypes.H; openPartGUI(); }
		if (object.equals(jackBtn)) { sls.currentPart = LayerTypes.J; openPartGUI(); }
		if (object.equals(capeBtn)) { sls.currentPart = LayerTypes.CA; openPartGUI(); }
		if (object.equals(larmBtn)) { sls.currentPart = sls.skinFrontFacing ? LayerTypes.LA : LayerTypes.RA; openPartGUI(); }
		if (object.equals(rarmBtn)) { sls.currentPart = sls.skinFrontFacing ? LayerTypes.RA : LayerTypes.LA; openPartGUI(); }
		if (object.equals(llegBtn)) { sls.currentPart = sls.skinFrontFacing ? LayerTypes.LL : LayerTypes.RL; openPartGUI(); }
		if (object.equals(rlegBtn)) { sls.currentPart = sls.skinFrontFacing ? LayerTypes.RL : LayerTypes.LL; openPartGUI(); }
		if (object.equals(flipBtn)) { delayFlipBtnInputs(); }
		if (object.equals(resetProfileBtn)) {
			if (sls.currentLoadedProfile == sls.currentSkinProfile) {
				sls.getPartModifier().resetAllParts();
				switch (sls.resetMode) {
				case SW: sls.setGlobalSwitch(true); stateBtn.displayString = "All Switch"; break;
				case BL: sls.setGlobalBlink(true); stateBtn.displayString = "All Blink"; break;
				case IN: sls.setGlobalSwitch(false); sls.setGlobalBlink(false); stateBtn.displayString = "Individual"; break;
				case N: sls.setGlobalSwitch(false); sls.setGlobalBlink(false); stateBtn.displayString = "Off"; break;
				}
			} else { SLSLoadSkinConfigProfile.createNewConfig(String.valueOf(sls.currentSkinProfile)); }		
			mc.thePlayer.addChatMessage(ChatBuilder.of("Reset skin profile: " + sls.currentSkinProfile).setColor(EnumChatFormatting.AQUA).build());
		}
		if (object.equals(loadProfileBtn)) { tryToLoadProfile(); }
	}
	
	private void changeStateBtn() {
		switch (sls.currentMode) {
		case SW: sls.globalOn = true; sls.setGlobalSwitch(true); stateBtn.displayString = "All Switch";  break;
		case BL: sls.setGlobalBlink(true); stateBtn.displayString = "All Blink"; break;
		case IN: sls.setGlobalSwitch(false); sls.setGlobalBlink(false); stateBtn.displayString = "Individual"; break;
		case OFF: sls.globalOn = false; stateBtn.displayString = "Off"; break;
		}
		SLSGlobalConfig.updateGlobalConfig();
		SLSSaveSkinConfigProfile.updateProfile(sls.currentLoadedProfile);
	}
	
	private void drawMouseEnteredBtn() {
		mc.renderEngine.bindTexture(Resources.SLSsettingsGuiSteve);
		//rotate button
		if (mY >= hPos + 57 && mY <= hPos + 67 && mX >= wPos + 24 && mX <= wPos + 43) { drawTexturedModalRect(wPos + 25, hPos + 58, 201, 55, 18, 9); }
		//profile buttons
		if (mY >= hPos + 104 && mY <= hPos + 124) {
			if (mX >= wPos - 96 && mX <= wPos - 77) { drawTexturedModalRect(wPos - 95, hPos + 105, 201, 65, 18, 18); }
			if (mX >= wPos - 74 && mX <= wPos - 55) { drawTexturedModalRect(wPos - 73, hPos + 105, 201, 84, 18, 18); }
			if (mX >= wPos - 52 && mX <= wPos - 33) { drawTexturedModalRect(wPos - 51, hPos + 105, 201, 103, 18, 18); }
			if (mX >= wPos - 30 && mX <= wPos - 11) { drawTexturedModalRect(wPos - 29, hPos + 105, 201, 122, 18, 18); }
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		//Profile buttons
		if (mouseY >= (hPos + 104) && mouseY <= (hPos + 124)) {
			if (mouseX >= (wPos - 96) && mouseX <= (wPos - 77)) { createDoubleClickTimer(200, 1); }
			if (mouseX >= (wPos - 74) && mouseX <= (wPos - 55)) { createDoubleClickTimer(200, 2); }
			if (mouseX >= (wPos - 52) && mouseX <= (wPos - 33)) { createDoubleClickTimer(200, 3); }
			if (mouseX >= (wPos - 30) && mouseX <= (wPos - 11)) { createDoubleClickTimer(200, 4); }
		}
		//Skin buttons
		if (mouseY >= (hPos - 71) && mouseY <= (hPos - 40)) {
			if (mouseX >= (wPos - 17) && mouseX <= (wPos + 16)) {
				sls.getPartModifier().setPartEnabled(LayerTypes.H, !sls.getPart(LayerTypes.H).isEnabled());
				resetProfileBtn.playPressSound();
			}
		} if (mouseY >= (hPos - 38) && mouseY <= (hPos + 10)) {
			if (skinType.equals("slim")) {
				if (mouseX >= (wPos + 18) && mouseX <= (wPos + 29)) {
					sls.getPartModifier().setPartEnabled(sls.skinFrontFacing ? LayerTypes.LA : LayerTypes.RA, !sls.getPart(sls.skinFrontFacing ? LayerTypes.LA : LayerTypes.RA).isEnabled());
					resetProfileBtn.playPressSound();
				}
			} else {
				if (mouseX >= (wPos + 18) && mouseX <= (wPos + 34)) {
					sls.getPartModifier().setPartEnabled(sls.skinFrontFacing ? LayerTypes.LA : LayerTypes.RA, !sls.getPart(sls.skinFrontFacing ? LayerTypes.LA : LayerTypes.RA).isEnabled());
					resetProfileBtn.playPressSound();
				}
			}		
			if (mouseX >= (wPos - 16) && mouseX <= (wPos + 15)) {
				sls.getPartModifier().setPartEnabled(LayerTypes.J, !sls.getPart(LayerTypes.J).isEnabled());
				resetProfileBtn.playPressSound();
			}
			if (skinType.equals("slim")) {
				if (mouseX >= (wPos - 30) && mouseX <= (wPos - 19)) {
					sls.getPartModifier().setPartEnabled(sls.skinFrontFacing ? LayerTypes.RA : LayerTypes.LA, !sls.getPart(sls.skinFrontFacing ? LayerTypes.RA : LayerTypes.LA).isEnabled());
					resetProfileBtn.playPressSound();
				}
			} else {
				if (mouseX >= (wPos - 34) && mouseX <= (wPos - 19)) {
					sls.getPartModifier().setPartEnabled(sls.skinFrontFacing ? LayerTypes.RA : LayerTypes.LA, !sls.getPart(sls.skinFrontFacing ? LayerTypes.RA : LayerTypes.LA).isEnabled());
					resetProfileBtn.playPressSound();
				}
			}
		} if (mouseY >= (hPos + 12) && mouseY <= (hPos + 60)) {
			if (mouseX >= (wPos - 24) && mouseX <= (wPos - 2)) {
				sls.getPartModifier().setPartEnabled(sls.skinFrontFacing ? LayerTypes.RL : LayerTypes.LL, !sls.getPart(sls.skinFrontFacing ? LayerTypes.RL : LayerTypes.LL).isEnabled());
				resetProfileBtn.playPressSound();
			}
			if (mouseX >= (wPos + 2) && mouseX <= (wPos + 16)) {
				sls.getPartModifier().setPartEnabled(sls.skinFrontFacing ? LayerTypes.LL : LayerTypes.RL, !sls.getPart(sls.skinFrontFacing ? LayerTypes.LL : LayerTypes.RL).isEnabled());
				resetProfileBtn.playPressSound();
			}
		}
		//Rotate Skin Button
		if (mouseY >= hPos + 57 && mouseY <= hPos + 67) {
			if (mouseX >= wPos + 24 && mouseX <= wPos + 43) {
				sls.skinFrontFacing = !sls.skinFrontFacing;
				if (sls.skinFrontFacing) {
					llegBtn.displayString = "L Leg";
					rlegBtn.displayString = "R Leg";
					larmBtn.displayString = "L Arm";
					rarmBtn.displayString = "R Arm";
				} else {
					llegBtn.displayString = "R Leg";
					rlegBtn.displayString = "L Leg";
					larmBtn.displayString = "R Arm";
					rarmBtn.displayString = "L Arm";
				}
				SLSGlobalConfig.updateGlobalConfig();
				resetProfileBtn.playPressSound();
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	private void createDoubleClickTimer(int time, int btnPressed) {
		if (oneClick) {
			if (btnPressed <= 4) { tryToLoadProfile(); }
			resetProfileBtn.playPressSound();
	        oneClick = false;
		} else {
			oneClick = true;
			if (btnPressed <= 4) { sls.currentSkinProfile = btnPressed; }
			resetProfileBtn.playPressSound();
			Timer t = new Timer("doubleclickTimer", false);
			t.schedule(new TimerTask() { @Override public void run() { oneClick = false; } }, time);
		}
	}
	
	private void tryToLoadProfile() {
		if (sls.currentLoadedProfile != sls.currentSkinProfile) {
			sls.currentLoadedProfile = sls.currentSkinProfile;
			sls.loadSelectedProfile();
			mc.thePlayer.addChatMessage(ChatBuilder.of("Loading skin profile: " + sls.currentSkinProfile).setColor(EnumChatFormatting.GREEN).build());
			changeStateBtn();
		}
	}
	
	private void delayFlipBtnInputs() {
		if (!alreadyPressed) {
			sls.toggleGlobalBlinkFlipped();
			alreadyPressed = true;
			Timer t = new Timer("doubleclickTimer", false);
			t.schedule(new TimerTask() { @Override public void run() { alreadyPressed = false; } }, 1500);
		}
	}
	
	private void changePartRateValues(int changeRate) {
		if (sls.getGlobalSwitchingStatus()) { testValues(null, changeRate + sls.getGlobalSwitchingSpeed()); return; }
		else if (sls.getGlobalBlinkingStatus()) { testValues(null, changeRate + sls.getGlobalBlinkingSpeed()); return; }
		for (LayerTypes t : LayerTypes.values()) {
			testValues(t, changeRate + (sls.getPart(LayerTypes.H).isSwitching() ? PartStats.getPartSwitchSpeed(LayerTypes.H) : PartStats.getPartBlinkSpeed(LayerTypes.H)));
		}
	}
	
	private void testValues(LayerTypes type, int changeValue) {
		changeValue = changeValue > sls.MaxRate ? sls.MaxRate : changeValue;
		changeValue = changeValue < sls.MinRate ? sls.MinRate : changeValue;
		if (sls.getGlobalSwitchingStatus()) { sls.setGlobalSwitchSpeed(changeValue); }
		else if (sls.getGlobalBlinkingStatus()) { sls.setGlobalBlinkDelay(changeValue); }
		else {
			if (sls.getPart(type).isSwitching()) { sls.getPartModifier().setPartSwitchSpeed(type, changeValue); }
			else if (sls.getPart(type).isBlinking()) { sls.getPartModifier().setPartBlinkSpeed(type, changeValue); }
		}
	}
	
	private void drawRates() {
		gss = sls.getGlobalSwitchingSpeed();
		gbs = sls.getGlobalBlinkingSpeed();
		gbd = sls.getGlobalBlinkDuration();
		
		if (sls.getGlobalSwitchingStatus()) {
			hSpeed = gss; jSpeed = gss; caSpeed = gss; laSpeed = gss; raSpeed = gss; llSpeed = gss; rlSpeed = gss;
		} else if (sls.getGlobalBlinkingStatus()) {
			hBSpeed = gbs; jBSpeed = gbs; caBSpeed = gbs; laBSpeed = gbs; raBSpeed = gbs; llBSpeed = gbs; rlBSpeed = gbs;
			hBD = gbd; jBD = gbd; caBD = gbd; laBD = gbd; raBD = gbd; llBD = gbd; rlBD = gbd;
		} else if (sls.globalOn) {
			hSpeed = sls.getPart(LayerTypes.H).getSwitchSpeed();
			hBSpeed = sls.getPart(LayerTypes.H).getBlinkSpeed();
			hBD = sls.getPart(LayerTypes.H).getBlinkDuration();
			jSpeed = sls.getPart(LayerTypes.J).getSwitchSpeed();
			jBSpeed = sls.getPart(LayerTypes.J).getBlinkSpeed();
			jBD = sls.getPart(LayerTypes.J).getBlinkDuration();
			caSpeed = sls.getPart(LayerTypes.CA).getSwitchSpeed();
			caBSpeed = sls.getPart(LayerTypes.CA).getBlinkSpeed();
			caBD = sls.getPart(LayerTypes.CA).getBlinkDuration();
			laSpeed = sls.getPart(LayerTypes.LA).getSwitchSpeed();
			laBSpeed = sls.getPart(LayerTypes.LA).getBlinkSpeed();
			laBD = sls.getPart(LayerTypes.LA).getBlinkDuration();
			raSpeed = sls.getPart(LayerTypes.RA).getSwitchSpeed();
			raBSpeed = sls.getPart(LayerTypes.RA).getBlinkSpeed();
			raBD = sls.getPart(LayerTypes.RA).getBlinkDuration();
			llSpeed = sls.getPart(LayerTypes.LL).getSwitchSpeed();
			llBSpeed = sls.getPart(LayerTypes.LL).getBlinkSpeed();
			llBD = sls.getPart(LayerTypes.LL).getBlinkDuration();
			rlSpeed = sls.getPart(LayerTypes.RL).getSwitchSpeed();
			rlBSpeed = sls.getPart(LayerTypes.RL).getBlinkSpeed();
			rlBD = sls.getPart(LayerTypes.RL).getBlinkDuration();
		}
		if (sls.getGlobalSwitchingStatus()) {
			drawString("SS: " + hSpeed, wPos - 19 + getOffset(hSpeed), hPos - 98, 0xFFFFFF);
			drawString("SS: " + jSpeed, wPos - 90 + getOffset(jSpeed), hPos - 63, 0xFFFFFF);
			drawString("SS: " + caSpeed, wPos + 51 + getOffset(caSpeed), hPos - 63, 0xFFFFFF);
			drawString("SS: " + laSpeed, wPos + 51 + getOffset(laSpeed), hPos - 12, 0xFFFFFF);
			drawString("SS: " + raSpeed, wPos - 90 + getOffset(raSpeed), hPos - 12, 0xFFFFFF);
			drawString("SS: " + llSpeed, wPos + 51 + getOffset(llSpeed), hPos + 39, 0xFFFFFF);
			drawString("SS: " + rlSpeed, wPos - 90 + getOffset(rlSpeed), hPos + 39, 0xFFFFFF);
		} else if (sls.getGlobalBlinkingStatus()) {
			drawString("BS: " + hBSpeed, wPos - 19 + getOffset(hBSpeed), hPos - 98, 0xFFFFFF);
			drawString("BD: " + hBD, wPos - 19 + getOffset(hBD), hPos - 89, 0xFFFFFF);
			drawString("BS: " + jBSpeed, wPos - 90 + getOffset(jBSpeed), hPos - 63, 0xFFFFFF);
			drawString("BD: " + jBD, wPos - 90 + getOffset(jBD), hPos - 54, 0xFFFFFF);
			drawString("BS: " + caBSpeed, wPos + 51 + getOffset(caBSpeed), hPos - 63, 0xFFFFFF);
			drawString("BD: " + caBD, wPos + 51 + getOffset(caBD), hPos - 54, 0xFFFFFF);
			drawString("BS: " + laBSpeed, wPos + 51 + getOffset(laBSpeed), hPos - 12, 0xFFFFFF);
			drawString("BD: " + laBD, wPos + 51 + getOffset(laBD), hPos - 3, 0xFFFFFF);
			drawString("BS: " + raBSpeed, wPos - 90 + getOffset(raBSpeed), hPos - 12, 0xFFFFFF);
			drawString("BD: " + raBD, wPos - 90 + getOffset(raBD), hPos - 3, 0xFFFFFF);
			drawString("BS: " + llBSpeed, wPos + 51 + getOffset(llBSpeed), hPos + 39, 0xFFFFFF);
			drawString("BD: " + llBD, wPos + 51 + getOffset(llBD), hPos + 48, 0xFFFFFF);
			drawString("BS: " + rlBSpeed, wPos - 90 + getOffset(rlBSpeed), hPos + 39, 0xFFFFFF);
			drawString("BD: " + rlBD, wPos - 90 + getOffset(rlBD), hPos + 48, 0xFFFFFF);
		} else if (sls.globalOn) {
			if (sls.getPart(LayerTypes.H).isSwitching()) {
				drawString("SS: " + hSpeed, wPos - 19 + getOffset(hSpeed), hPos - 98, 0xFFFFFF);
			} else if (sls.getPart(LayerTypes.H).isBlinking()){
				drawString("BS: " + hBSpeed, wPos - 19 + getOffset(hBSpeed), hPos - 98, 0xFFFFFF);
				drawString("BD: " + hBD, wPos - 19 + getOffset(hBD), hPos - 89, 0xFFFFFF);
			}
			if (sls.getPart(LayerTypes.J).isSwitching()) {
				drawString("SS: " + jSpeed, wPos - 90 + getOffset(jSpeed), hPos - 63, 0xFFFFFF);
			} else if (sls.getPart(LayerTypes.J).isBlinking()) {
				drawString("BS: " + jBSpeed, wPos - 90 + getOffset(jBSpeed), hPos - 63, 0xFFFFFF);
				drawString("BD: " + jBD, wPos - 90 + getOffset(jBD), hPos - 54, 0xFFFFFF);
			}
			if (sls.getPart(LayerTypes.CA).isSwitching()) {
				drawString("SS: " + caSpeed, wPos + 51 + getOffset(caSpeed), hPos - 63, 0xFFFFFF);
			} else if (sls.getPart(LayerTypes.CA).isBlinking()) {
				drawString("BS: " + caBSpeed, wPos + 51 + getOffset(caBSpeed), hPos - 63, 0xFFFFFF);
				drawString("BD: " + caBD, wPos + 51 + getOffset(caBD), hPos - 54, 0xFFFFFF);
			}
			if (sls.getPart(LayerTypes.LA).isSwitching()) {
				drawString("SS: " + laSpeed, wPos + (sls.skinFrontFacing ? 51 : -91) + getOffset(laSpeed), hPos - 12, 0xFFFFFF);
			} else if (sls.getPart(LayerTypes.LA).isBlinking()) {
				if (sls.skinFrontFacing) {
					drawString("BS: " + laBSpeed, wPos + 51 + getOffset(laBSpeed), hPos - 12, 0xFFFFFF);
					drawString("BD: " + laBD, wPos + 51 + getOffset(laBD), hPos - 3, 0xFFFFFF);
				} else {
					drawString("BS: " + laBSpeed, wPos - 90 + getOffset(laBSpeed), hPos - 12, 0xFFFFFF);
					drawString("BD: " + laBD, wPos - 90 + getOffset(laBD), hPos - 3, 0xFFFFFF);
				}
			}
			if (sls.getPart(LayerTypes.RA).isSwitching()) {
				drawString("SS: " + raSpeed, wPos + (sls.skinFrontFacing ? -90 : 51) + getOffset(raSpeed), hPos - 12, 0xFFFFFF);
			} else if (sls.getPart(LayerTypes.RA).isBlinking()) {
				if (sls.skinFrontFacing) {
					drawString("BS: " + raBSpeed, wPos - 90 + getOffset(raBSpeed), hPos - 12, 0xFFFFFF);
					drawString("BD: " + raBD, wPos - 90 + getOffset(raBD), hPos - 3, 0xFFFFFF);
				} else {
					drawString("BS: " + raBSpeed, wPos + 51 + getOffset(raBSpeed), hPos - 12, 0xFFFFFF);
					drawString("BD: " + raBD, wPos + 51 + getOffset(raBD), hPos - 3, 0xFFFFFF);
				}
			}
			if (sls.getPart(LayerTypes.LL).isSwitching()) {
				drawString("SS: " + llSpeed, wPos + (sls.skinFrontFacing ? 51 : -91) + getOffset(llSpeed), hPos + 39, 0xFFFFFF);
			} else if (sls.getPart(LayerTypes.LL).isBlinking()) {
				if (sls.skinFrontFacing) {
					drawString("BS: " + llBSpeed, wPos + 51 + getOffset(llBSpeed), hPos + 39, 0xFFFFFF);
					drawString("BD: " + llBD, wPos + 51 + getOffset(llBD), hPos + 48, 0xFFFFFF);
				} else {
					drawString("BS: " + llBSpeed, wPos - 90 + getOffset(llBSpeed), hPos + 39, 0xFFFFFF);
					drawString("BD: " + llBD, wPos - 90 + getOffset(llBD), hPos + 48, 0xFFFFFF);
				}				
			}
			if (sls.getPart(LayerTypes.RL).isSwitching()) {
				drawString("SS: " + rlSpeed, wPos + (sls.skinFrontFacing ? -90 : 51) + getOffset(rlSpeed), hPos + 39, 0xFFFFFF);
			} else if (sls.getPart(LayerTypes.RL).isBlinking()) {
				if (sls.skinFrontFacing) {
					drawString("BS: " + rlBSpeed, wPos - 90 + getOffset(rlBSpeed), hPos + 39, 0xFFFFFF);
					drawString("BD: " + rlBD, wPos - 90 + getOffset(rlBD), hPos + 48, 0xFFFFFF);
				} else {
					drawString("BS: " + rlBSpeed, wPos + 51 + getOffset(rlBSpeed), hPos + 39, 0xFFFFFF);
					drawString("BD: " + rlBD, wPos + 51 + getOffset(rlBD), hPos + 48, 0xFFFFFF);
				}
			}
		}
	}
	
	private void drawDisabledParts() {
		mc.renderEngine.bindTexture(Resources.SLSsettingsGuiSteve);
		if (!sls.getPart(LayerTypes.H).isEnabled()) { drawTexturedModalRect(wPos - 7, hPos - 62, 201, 40, 14, 14); }
		if (!sls.getPart(LayerTypes.J).isEnabled()) { drawTexturedModalRect(wPos - 7, hPos - 20, 201, 40, 14, 14); }
		if (!sls.getPart(LayerTypes.LA).isEnabled()) { drawTexturedModalRect(wPos + (sls.skinFrontFacing ? 19 : -33), hPos - 20, 201, 40, 14, 14); }
		if (!sls.getPart(LayerTypes.RA).isEnabled()) { drawTexturedModalRect(wPos + (sls.skinFrontFacing ? -33 : 19), hPos - 20, 201, 40, 14, 14); }
		if (!sls.getPart(LayerTypes.LL).isEnabled()) { drawTexturedModalRect(wPos + (sls.skinFrontFacing ? 2 : -16), hPos + 30, 201, 40, 14, 14); }
		if (!sls.getPart(LayerTypes.RL).isEnabled()) { drawTexturedModalRect(wPos + (sls.skinFrontFacing ? -16 : 2), hPos + 30, 201, 40, 14, 14); }
	}
	
	private void drawSelectedProfile() {
		mc.renderEngine.bindTexture(Resources.SLSsettingsGuiSteve);
		drawTexturedModalRect(wPos - 96 + ((sls.currentSkinProfile - 1) * 22), hPos + 104, 201, 0, 20, 20);	
	}
	
	private void drawLoadedProfile() {
		mc.renderEngine.bindTexture(Resources.SLSsettingsGuiSteve);
		drawTexturedModalRect(wPos - 95 + ((sls.currentLoadedProfile - 1) * 22), hPos + 105, 201, 21, 18, 18);
	}
	
	private void drawSkin() {
		mc.renderEngine.bindTexture(playerSkin);
		boolean isAlex = skinType.equals("slim");
		Set parts = mc.gameSettings.getModelParts();
		if (sls.skinFrontFacing) {
			//Head Front
			drawTexturedModalRect(wPos - 16, hPos - 71, 32, 32, 32, 32);
			//Chest Front
			drawTexturedModalRect(wPos - 16, hPos - 37, 80, 80, 32, 48);
			//Left Leg Front
			drawTexturedModalRect(wPos - 17, hPos + 13, 16, 80, 16, 48);
			//Right Leg Front
			drawTexturedModalRect(wPos + 1, hPos + 13, 80, 208, 16, 48);
			if (parts.contains(LayerTypes.H.getMCType())) { drawTexturedModalRect(wPos - 16, hPos - 71, 160, 32, 32, 32); }
			if (parts.contains(LayerTypes.J.getMCType())) { drawTexturedModalRect(wPos - 16, hPos - 37, 80, 144, 32, 48); }
			if (parts.contains(LayerTypes.RL.getMCType())) { drawTexturedModalRect(wPos - 17, hPos + 13, 16, 144, 16, 48); }
			if (parts.contains(LayerTypes.LL.getMCType())) { drawTexturedModalRect(wPos + 1, hPos + 13, 16, 208, 16, 48); }
			if (isAlex) {
				//Left Arm Front
				drawTexturedModalRect(wPos + 18, hPos - 37, 144, 208, 12, 48);
				//Right Arm Front
				drawTexturedModalRect(wPos - 30, hPos - 37, 176, 80, 12, 48);
				if (parts.contains(LayerTypes.LA.getMCType())) { drawTexturedModalRect(wPos + 18, hPos - 37, 208, 208, 12, 48); }
				if (parts.contains(LayerTypes.RA.getMCType())) { drawTexturedModalRect(wPos - 30, hPos - 37, 176, 144, 12, 48); }
			} else { 
				//Left Arm Front
				drawTexturedModalRect(wPos + 18, hPos - 37, 144, 208, 16, 48);
				//Right Arm Front
				drawTexturedModalRect(wPos - 34, hPos - 37, 176, 80, 16, 48);
				if (parts.contains(LayerTypes.LA.getMCType())) { drawTexturedModalRect(wPos + 18, hPos - 37, 208, 208, 16, 48); }
				if (parts.contains(LayerTypes.RA.getMCType())) { drawTexturedModalRect(wPos - 34, hPos - 37, 176, 144, 16, 48); }
			}
		} else {
			//Head Back
			drawTexturedModalRect(wPos - 16, hPos - 71, 96, 32, 32, 32);
			//Chest Back
			drawTexturedModalRect(wPos - 16, hPos - 37, 128, 80, 32, 48);
			//Left Leg Back
			drawTexturedModalRect(wPos - 17, hPos + 13, 112, 208, 16, 48);
			//Right Leg Back
			drawTexturedModalRect(wPos + 1, hPos + 13, 48, 80, 16, 48);
			if (parts.contains(LayerTypes.H.getMCType())) {	drawTexturedModalRect(wPos - 16, hPos - 71, 224, 32, 32, 32); }
			if (parts.contains(LayerTypes.J.getMCType())) { drawTexturedModalRect(wPos - 16, hPos - 37, 128, 144, 32, 48); }
			if (parts.contains(LayerTypes.LL.getMCType())) { drawTexturedModalRect(wPos - 17, hPos + 13, 48, 208, 16, 48); }
			if (parts.contains(LayerTypes.RL.getMCType())) { drawTexturedModalRect(wPos + 1, hPos + 13, 48, 144, 16, 48); }
			if (isAlex) {
				//Left Arm Back
				drawTexturedModalRect(wPos - 30, hPos - 37, 172, 208, 12, 48);
				//Right Arm Back
				drawTexturedModalRect(wPos + 18, hPos - 37, 204, 80, 12, 48);
				if (parts.contains(LayerTypes.LA.getMCType())) { drawTexturedModalRect(wPos - 30, hPos - 37, 236, 208, 12, 48); }
				if (parts.contains(LayerTypes.RA.getMCType())) { drawTexturedModalRect(wPos + 18, hPos - 37, 204, 144, 12, 48);	}
			} else {
				//Left Arm Back
				drawTexturedModalRect(wPos - 34, hPos - 37, 176, 208, 16, 48);
				//Right Arm Back
				drawTexturedModalRect(wPos + 18, hPos - 37, 208, 80, 16, 48);
				if (parts.contains(LayerTypes.LA.getMCType())) { drawTexturedModalRect(wPos - 34, hPos - 37, 240, 208, 16, 48); }
				if (parts.contains(LayerTypes.RA.getMCType())) { drawTexturedModalRect(wPos + 18, hPos - 37, 208, 144, 16, 48); }
			}
		}
	}
	
	private int getOffset(int value) { return (11 - 3 * Integer.toString(value).length()); }
	private void openPartGUI() { mc.displayGuiScreen(new SLSPartGui(startX, startY, guiInstance)); }
	private void openOptionsGui() { mc.displayGuiScreen(new SLSGlobalOptionsGui(startX, startY, guiInstance)); }
}
