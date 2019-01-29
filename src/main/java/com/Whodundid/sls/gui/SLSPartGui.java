package com.Whodundid.sls.gui;

import java.io.IOException;
import java.util.Set;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.miscUtil.Resources;
import com.Whodundid.sls.SkinSwitcher;
import com.Whodundid.sls.util.LayerTypes;
import com.Whodundid.sls.util.SkinPart;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class SLSPartGui extends EnhancedGui {
	
	SkinSwitcher sls;
	ResourceLocation playerSkin;
	ResourceLocation playerCape;
	GameSettings mcSettings;
	String skinType;
	EGuiButton resetBtn, doneBtn;
	EGuiButton fiveUpBtn, twoFiveUpBtn, hundredUpBtn, fiveHundredUpBtn;
	EGuiButton fiveDownBtn, twoFiveDownBtn, hundredDownBtn, fiveHundredDownBtn;
	SkinPart part;
	String enabled, mode;
	Set modelParts;
	boolean SSpeedSel, BDelaySel, BDurationSel, OffsetSel;
	boolean ssIsSelectable, bdelayIsSelectable, bdurationIsSelectable;
	boolean isEnumEnabled;
	
	public SLSPartGui() { super(); }
	public SLSPartGui(EnhancedGui oldGui) { super(oldGui); }
	public SLSPartGui(int posX, int posY) { super(posX, posY); }
	public SLSPartGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	
	@Override
	public void initGui() {
		super.initGui();
		sls = (SkinSwitcher) RegisteredSubMods.getMod(SubModType.SLS);
		mcSettings = mc.gameSettings;
		
		part = sls.getPart(sls.currentPart);
		enabled = (part.isEnabled()) ? "Enabled" : "Disabled";
		mode = (part.isSwitching()) ? "Switch" : "Blink";
		
		playerSkin = mc.thePlayer.getLocationSkin();
		playerCape = mc.thePlayer.getLocationCape();
		skinType = mc.thePlayer.getSkinType();
		
		modelParts = mcSettings.getModelParts();
		isEnumEnabled = modelParts.contains(part.getType().getMCType());
		SSpeedSel = true;
		BDelaySel = true;
		
		
	}
	
	@Override
	public void initObjects() {
		doneBtn = new EGuiButton(this, wPos + 56, hPos + 104, 40, 20, "Done");
		resetBtn = new EGuiButton(this, wPos + 9, hPos + 104, 40, 20, "Reset");
		fiveUpBtn = new EGuiButton(this, wPos + 70, hPos + 76, 25, 20, "+5");
		twoFiveUpBtn = new EGuiButton(this, wPos + 39, hPos + 76, 25, 20, "+25");
		hundredUpBtn = new EGuiButton(this, wPos + 2, hPos + 76, 30, 20, "+100");
		hundredDownBtn = new EGuiButton(this, wPos - 36, hPos + 76, 30, 20, "-100");
		twoFiveDownBtn = new EGuiButton(this, wPos - 67, hPos + 76, 25, 20, "-25");
		fiveDownBtn = new EGuiButton(this, wPos - 94, hPos + 76, 20, 20, "-5");
		
		addObject(doneBtn, resetBtn, fiveUpBtn, twoFiveUpBtn, hundredUpBtn, hundredDownBtn, twoFiveDownBtn, fiveDownBtn);
	}

	@Override
	public void drawObject(int x, int y, float ticks) {
		mc.renderEngine.bindTexture(Resources.SLSpartGui);
		drawTexturedModalRect(wPos - width / 2, hPos - height / 2, 0, 0, width, height);
		drawPartEnabled();
		drawMouseEntered();
		drawPartTitle();
		drawPartStats();
		drawCheckedBoxes();
		drawSelectedBoxes();
		drawPartVisual();
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(doneBtn)) { returnToMainGui(); }
		if (object.equals(fiveUpBtn)) { addToValues(5); }
		if (object.equals(twoFiveUpBtn)) { addToValues(25); }
		if (object.equals(hundredUpBtn)) { addToValues(100); }
		if (object.equals(hundredDownBtn)) { addToValues(-100); }
		if (object.equals(twoFiveDownBtn)) { addToValues(-25); }
		if (object.equals(fiveDownBtn)) { addToValues(-5);}
		if (object.equals(resetBtn)) { sls.getPartModifier().resetPartValues(part.getType()); }
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		sls.currentPart = null;
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseX >= wPos - 91 && mouseX <= wPos) {
			if (mouseY >= hPos - 94 && mouseY <= hPos - 82) {
				part.toggleSwitching();
				if (!part.isSwitching() && !part.isBlinking()) { part.setNoMode(); }
				resetBtn.playPressSound();
			}
			if (mouseY >= hPos - 65 && mouseY <= hPos - 54) {
				part.toggleBlinking();
				if (!part.isSwitching() && !part.isBlinking()) { part.setNoMode(); }
				resetBtn.playPressSound();
			}
			if (mouseY >= hPos - 34 && mouseY <= hPos - 22) {
				if (ssIsSelectable) {
					SSpeedSel = !SSpeedSel;
					resetBtn.playPressSound();
				}
			}
			if (mouseY >= hPos - 6 && mouseY <= hPos + 6) {
				if (bdelayIsSelectable) {
					BDelaySel = !BDelaySel;
					resetBtn.playPressSound();
				}
			}
			if (mouseY >= hPos + 22 && mouseY <= hPos + 34) {
				if (bdurationIsSelectable) {
					BDurationSel = !BDurationSel;
					resetBtn.playPressSound();
				}
			}
			if (mouseY >= hPos + 50 && mouseY <= hPos + 62) {
				OffsetSel = !OffsetSel;
				resetBtn.playPressSound();
			}
			if (mouseY >= hPos + 108 && mouseY <= hPos + 120) {
				sls.getPartModifier().flipPartState(part.getType());
				resetBtn.playPressSound();
			}
		}
		if (mouseY >= hPos - 127 && mouseY <= hPos - 105) {
			if (mouseX >= wPos + 48 && mouseX <= wPos + 98) {
				mcSettings.setModelPartEnabled(part.getType().getMCType(), isEnumEnabled = !isEnumEnabled);
				resetBtn.playPressSound();
			}
		}		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	private void drawMouseEntered() {
		mc.renderEngine.bindTexture(Resources.SLSpartGui);
		if (mY >= hPos - 127 && mY <= hPos - 105 && mX >= wPos + 48 && mX <= wPos + 98) {
			if (isEnumEnabled) { drawTexturedModalRect(wPos + 54, hPos - 122, 201, 151, 12, 13); }
			else { drawTexturedModalRect(wPos + 81, hPos - 122, 201, 151, 12, 13); }
		}
		if (mX >= wPos - 91 && mX <= wPos) {
			if (mY >= hPos - 94 && mY <= hPos - 82) drawTexturedModalRect(wPos - 91, hPos - 94, 223, 15, 12, 12);
			if (mY >= hPos - 65 && mY <= hPos - 53) drawTexturedModalRect(wPos - 91, hPos - 65, 223, 15, 12, 12);
			if (mY >= hPos - 34 && mY <= hPos - 22) drawTexturedModalRect(wPos - 91, hPos - 34, 223, 15, 12, 12);
			if (mY >= hPos - 6 && mY <= hPos + 6) drawTexturedModalRect(wPos - 91, hPos - 6, 223, 15, 12, 12);
			if (mY >= hPos + 22 && mY <= hPos + 34) drawTexturedModalRect(wPos - 91, hPos + 22, 223, 15, 12, 12);
			if (mY >= hPos + 50 && mY <= hPos + 62) drawTexturedModalRect(wPos - 91, hPos + 50, 223, 15, 12, 12);
			if (mY >= hPos + 108 && mY <= hPos + 120) drawTexturedModalRect(wPos - 91, hPos + 108, 223, 15, 12, 12);
		}
	}
	
	private void addToValues(int value) {
		int changeSwitch = part.getSwitchSpeed() + value;
		int changeBlink = part.getBlinkSpeed() + value;
		int changeBlinkDur = part.getBlinkDuration() + value;
		int changeOffset = part.getOffset() + value;
		
		if (part.isSwitching()) {
			if (SSpeedSel) {
				if (changeSwitch < sls.MaxRate) {
					sls.getPartModifier().setPartSwitchSpeed(part.getType(), changeSwitch >= sls.MinRate ? changeSwitch : sls.MinRate);
				} else { sls.getPartModifier().setPartSwitchSpeed(part.getType(), sls.MaxRate); }
			}
		} else if (part.isBlinking()) {
			if (BDelaySel) {
				if (changeBlink < sls.MaxRate) {
					sls.getPartModifier().setPartBlinkSpeed(part.getType(), changeBlink >= sls.MinRate ? changeBlink : sls.MinRate);
				} else { sls.getPartModifier().setPartBlinkSpeed(part.getType(), sls.MaxRate); }
			}
			if (BDurationSel) {
				if (changeBlinkDur < sls.MaxRate) {
					sls.getPartModifier().setPartBlinkDuration(part.getType(), changeBlinkDur >= sls.MinBlinkDurRate ? changeBlinkDur : sls.MinBlinkDurRate);
				} else { sls.getPartModifier().setPartBlinkDuration(part.getType(), sls.MaxRate); }
			}
		}		
		if (OffsetSel) {
			if (changeOffset < sls.MaxRate) {
				sls.getPartModifier().setPartOffset(part.getType(), changeOffset >= 0 ? changeOffset : 0);
			} else { sls.getPartModifier().setPartOffset(part.getType(), sls.MaxRate); }
		}
	}
	
	private void drawCheckedBoxes() {
		mc.renderEngine.bindTexture(Resources.SLSpartGui);
		if (part.isSwitching()) { drawTexturedModalRect(wPos - 91, hPos - 97, 201, 0, 14, 14); }
		if (part.isBlinking()) { drawTexturedModalRect(wPos - 91, hPos - 68, 201, 0, 14, 14); }
		if (part.getStateFlipped()) { drawTexturedModalRect(wPos - 91, hPos + 105, 201, 0, 14, 14); }
	}
	
	private void drawSelectedBoxes() {
		mc.renderEngine.bindTexture(Resources.SLSpartGui);
		if (OffsetSel) { drawTexturedModalRect(wPos - 89, hPos + 52, 214, 15, 8, 8); }
		if (part.isSwitching()) {
			ssIsSelectable = true;
			bdelayIsSelectable = false;
			bdurationIsSelectable = false;
			drawTexturedModalRect(wPos - 91, hPos - 6, 201, 15, 12, 12);
			drawTexturedModalRect(wPos - 91, hPos + 22, 201, 15, 12, 12);			
			if (SSpeedSel) { drawTexturedModalRect(wPos - 89, hPos - 32, 214, 15, 8, 8); }
		} else if (part.isBlinking()) {
			ssIsSelectable = false;
			bdelayIsSelectable = true;
			bdurationIsSelectable = true;
			drawTexturedModalRect(wPos - 91, hPos - 34, 201, 15, 12, 12);
			if (BDelaySel) { drawTexturedModalRect(wPos - 89, hPos - 4, 214, 15, 8, 8); }
			if (BDurationSel) { drawTexturedModalRect(wPos - 89, hPos + 24, 214, 15, 8, 8); }
		} else {
			ssIsSelectable = false;
			bdelayIsSelectable = false;
			bdurationIsSelectable = false;
			drawTexturedModalRect(wPos - 91, hPos - 6, 201, 15, 12, 12);
			drawTexturedModalRect(wPos - 91, hPos + 22, 201, 15, 12, 12);
			drawTexturedModalRect(wPos - 91, hPos - 34, 201, 15, 12, 12);
		}
	}
	
	private void drawPartStats() {
		drawString("" + part.getSwitchSpeed() + " ms", wPos + 50, hPos - 31, 0xFFFFFF);
		drawString("" + part.getBlinkSpeed() + " ms", wPos + 50, hPos - 3, 0xFFFFFF);
		drawString("" + part.getBlinkDuration() + " ms", wPos + 50, hPos + 25, 0xFFFFFF);
		drawString("" + part.getOffset() + " ms", wPos + 50, hPos + 53, 0xFFFFFF);
	}
	
	private void drawPartTitle() {
		mc.renderEngine.bindTexture(Resources.SLSpartGui);
		switch (part.getType()) {
		case H: 
			drawTexturedModalRect(wPos - 35, hPos - 121, 201, 87, 22, 14);
			break;
		case J:
			drawTexturedModalRect(wPos - 45, hPos - 121, 201, 117, 44, 14);
			break;
		case CA:
			drawTexturedModalRect(wPos - 39, hPos - 121, 201, 102, 31, 14);
			break;
		case LA:
			drawTexturedModalRect(wPos - 67, hPos - 121, 201, 28, 30, 14);
			drawTexturedModalRect(wPos - 29, hPos - 121, 201, 72, 47, 14);
			break;
		case RA:
			drawTexturedModalRect(wPos - 69, hPos - 121, 201, 43, 35, 14);
			drawTexturedModalRect(wPos - 26, hPos - 121, 201, 72, 47, 14);
			break;
		case LL:
			drawTexturedModalRect(wPos - 55, hPos - 121, 201, 28, 30, 14);
			drawTexturedModalRect(wPos - 18, hPos - 120, 201, 58, 22, 13);
			break;
		case RL:
			drawTexturedModalRect(wPos - 58, hPos - 121, 201, 43, 35, 14);
			drawTexturedModalRect(wPos - 16, hPos - 120, 201, 58, 22, 13);
			break;
		}
	}
	
	private void drawPartVisual() {
		mc.renderEngine.bindTexture(playerSkin);
		boolean isAlex = skinType.equals("slim");
		Set parts = mc.gameSettings.getModelParts();
		switch (part.getType()) {
		case H:
			if (sls.skinFrontFacing) {
				drawTexturedModalRect(wPos + 51, hPos - 89, 32, 32, 32, 32); 
				if (parts.contains(LayerTypes.H.getMCType())) { drawTexturedModalRect(wPos + 51, hPos - 89, 160, 32, 32, 32); }
			} else {
				drawTexturedModalRect(wPos + 51, hPos - 89, 96, 32, 32, 32);
				if (parts.contains(LayerTypes.H.getMCType())) { drawTexturedModalRect(wPos + 51, hPos - 89, 224, 32, 32, 32); }
			}			
			break;
		case J:
			if (sls.skinFrontFacing) {
				drawTexturedModalRect(wPos + 51, hPos - 98, 80, 80, 32, 48);
				if (parts.contains(LayerTypes.J.getMCType())) { drawTexturedModalRect(wPos + 51, hPos - 98, 80, 144, 32, 48); }
			} else {
				drawTexturedModalRect(wPos + 51, hPos - 98, 128, 80, 32, 48);
				if (parts.contains(LayerTypes.J.getMCType())) {	drawTexturedModalRect(wPos + 51, hPos - 98, 128, 144, 32, 48); }
			}
			break;
		case CA:
			if (playerCape != null) {
				mc.renderEngine.bindTexture(playerCape);
				drawModalRectWithCustomSizedTexture(wPos + 51, hPos - 98, 0, 0, 128, 128, 128, 128);
			}
			break;
		case RA:
			if (isAlex) {
				if (sls.skinFrontFacing) {
					drawTexturedModalRect(wPos + 62, hPos - 98, 176, 80, 12, 48);
					if (parts.contains(LayerTypes.RA.getMCType())) { drawTexturedModalRect(wPos + 62, hPos - 98, 176, 144, 12, 48); }
				} else {
					drawTexturedModalRect(wPos + 62, hPos - 98, 204, 80, 12, 48);
					if (parts.contains(LayerTypes.RA.getMCType())) { drawTexturedModalRect(wPos + 62, hPos - 98, 204, 144, 12, 48); }
				}
			} else {
				if (sls.skinFrontFacing) {
					drawTexturedModalRect(wPos + 59, hPos - 98, 176, 80, 16, 48);
					if (parts.contains(LayerTypes.RA.getMCType())) { drawTexturedModalRect(wPos + 59, hPos - 98, 176, 144, 16, 48); }
				} else {
					drawTexturedModalRect(wPos + 59, hPos - 98, 208, 80, 16, 48);
					if (parts.contains(LayerTypes.RA.getMCType())) { drawTexturedModalRect(wPos + 59, hPos - 98, 208, 144, 16, 48); }
				}
			}
			break;
		case LA:
			if (isAlex) {
				if (sls.skinFrontFacing) {
					drawTexturedModalRect(wPos + 62, hPos - 98, 144, 208, 12, 48);
					if (parts.contains(LayerTypes.LA.getMCType())) { drawTexturedModalRect(wPos + 62, hPos - 98, 208, 208, 12, 48); }
				} else {
					drawTexturedModalRect(wPos + 62, hPos - 98, 172, 208, 12, 48);
					if (parts.contains(LayerTypes.LA.getMCType())) { drawTexturedModalRect(wPos + 62, hPos - 98, 236, 208, 12, 48); }
				}
			} else {
				if (sls.skinFrontFacing) {
					drawTexturedModalRect(wPos + 59, hPos - 98, 144, 208, 16, 48);
					if (parts.contains(LayerTypes.LA.getMCType())) { drawTexturedModalRect(wPos + 59, hPos - 98, 208, 208, 16, 48); }
				} else {
					drawTexturedModalRect(wPos + 59, hPos - 98, 176, 208, 16, 48);
					if (parts.contains(LayerTypes.LA.getMCType())) { drawTexturedModalRect(wPos + 59, hPos - 98, 240, 208, 16, 48); }
				}
			}
			break;
		case LL:
			if (sls.skinFrontFacing) {
				drawTexturedModalRect(wPos + 59, hPos - 98, 80, 208, 16, 48);
				if (parts.contains(LayerTypes.LL.getMCType())) { drawTexturedModalRect(wPos + 59, hPos - 98, 16, 208, 16, 48); }
			} else {
				drawTexturedModalRect(wPos + 59, hPos - 98, 112, 208, 16, 48);
				if (parts.contains(LayerTypes.LL.getMCType())) { drawTexturedModalRect(wPos + 59, hPos - 98, 48, 208, 16, 48); }
			}
			break;
		case RL:
			if (sls.skinFrontFacing) {
				drawTexturedModalRect(wPos + 59, hPos - 98, 16, 80, 16, 48);
				if (parts.contains(LayerTypes.RL.getMCType())) { drawTexturedModalRect(wPos + 59, hPos - 98, 16, 144, 16, 48); }
			} else {
				drawTexturedModalRect(wPos + 59, hPos - 98, 48, 80, 16, 48);
				if (parts.contains(LayerTypes.RL.getMCType())) { drawTexturedModalRect(wPos + 59, hPos - 98, 48, 144, 16, 48); }
			}
			break;
		default: break;
		}
	}
	
	private void drawPartEnabled() {
		mc.renderEngine.bindTexture(Resources.SLSpartGui);
		if (!isEnumEnabled) { drawTexturedModalRect(wPos + 51, hPos - 124, 201, 132, 45, 18); }
	}
	
	private void returnToMainGui() {
    	sls.currentPart = null;
    	closeGui();
	}
}