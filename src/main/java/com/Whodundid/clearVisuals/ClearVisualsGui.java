package com.Whodundid.clearVisuals;

import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiContainer;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiSlider;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiTextField;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;

public class ClearVisualsGui extends EnhancedGui {
	
	protected ClearVisuals mod = (ClearVisuals) RegisteredSubMods.getMod(SubModType.CLEARVISUALS);
	protected EGuiSlider gamaSlider;
	protected EGuiTextField gamaEntry;
	protected EGuiButton drawFire, drawWaterOverlay, clearLava, clearWater, resetGama;
	protected EGuiContainer gama, rendering;
	
	public ClearVisualsGui() { super(); }
	public ClearVisualsGui(EnhancedGui oldGui) { super(oldGui); }
	public ClearVisualsGui(int posX, int posY) { super(posX, posY); }
	public ClearVisualsGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		rendering = new EGuiContainer(this, startX + 2, startY + 2, width - 4, 128) {
			{
				setDisplayString("Rendering");
				drawFire = new EGuiButton(rendering, startX + 10, startY + 26, 55, 18, mod.isFireDrawn() ? "True" : "False").setDisplayStringColor(mod.isFireDrawn() ? 0x55ff55 : 0xff5555);
				drawWaterOverlay = new EGuiButton(rendering, startX + 10, startY + 51, 55, 18, mod.isWaterOverlayDrawn() ? "True" : "False").setDisplayStringColor(mod.isWaterOverlayDrawn() ? 0x55ff55 : 0xff5555);
				clearLava = new EGuiButton(rendering, startX + 10, startY + 76, 55, 18, mod.isClearLava() ? "True" : "False").setDisplayStringColor(mod.isClearLava() ? 0x55ff55 : 0xff5555);
				clearWater = new EGuiButton(rendering, startX + 10, startY + 101, 55, 18, mod.isClearWater() ? "True" : "False").setDisplayStringColor(mod.isClearWater() ? 0x55ff55 : 0xff5555);
				addObject(drawFire, drawWaterOverlay, clearLava, clearWater);
			}
			@Override
			public void drawObject(int mXIn, int mYIn, float ticks) {
				super.drawObject(mXIn, mYIn, ticks);
				drawStringWithShadow("Draw Fire", midX - 25, startY + 31, 0xb2b2b2);
				drawStringWithShadow("Draw Water Overlay", midX - 25, startY + 56, 0xb2b2b2);
				drawStringWithShadow("Clear Lava", midX - 25, startY + 81, 0xb2b2b2);
				drawStringWithShadow("Clear Water", midX - 25, startY + 106, 0xb2b2b2);
			}
			@Override
			public void actionPerformed(IEnhancedActionObject object) {
				if (object.equals(drawFire)) { 
					mod.setFireVisibility(!mod.isFireDrawn());
					drawFire.setDisplayString(mod.isFireDrawn() ? "True" : "False").setDisplayStringColor(mod.isFireDrawn() ? 0x55ff55 : 0xff5555);
				}
				if (object.equals(drawWaterOverlay)) { 
					mod.setUnderWaterOverlayVisibility(!mod.isWaterOverlayDrawn());
					drawWaterOverlay.setDisplayString(mod.isWaterOverlayDrawn() ? "True" : "False").setDisplayStringColor(mod.isWaterOverlayDrawn() ? 0x55ff55 : 0xff5555);
				}
				if (object.equals(clearLava)) { 
					mod.setClearLava(!mod.isClearLava());
					clearLava.setDisplayString(mod.isClearLava() ? "True" : "False").setDisplayStringColor(mod.isClearLava() ? 0x55ff55 : 0xff5555);
				}
				if (object.equals(clearWater)) {
					mod.setClearWater(!mod.isClearWater());
					clearWater.setDisplayString(mod.isClearWater() ? "True" : "False").setDisplayStringColor(mod.isClearWater() ? 0x55ff55 : 0xff5555);
				}
				mod.getConfig().saveConfig();
			}
		};
		
		gama = new EGuiContainer(this, startX + 2, startY + 131, width - 4, 123) {
			{
				setDisplayString("Gama Control");
				gamaSlider = new EGuiSlider(gama, startX + 10, startY + 25, width - 24, 18, -12, 12, mod.getCurrentGama(), false);
				resetGama = new EGuiButton(gama, startX + 10, startY + 76, width - 24, 18, "Reset Gama");
				gamaEntry = new EGuiTextField(gama, startX + 11, startY + 51, 50, 17) {
					{ setText("" + gamaSlider.displayValue); }
					@Override
					public void keyPressed(char typedChar, int keyCode) {
						super.keyPressed(typedChar, keyCode);
						if (keyCode == 28) {
							if (!gamaEntry.getText().isEmpty()) {
								mod.setGama(Float.parseFloat(gamaEntry.getText()));
								gamaSlider.setSliderValue(mod.getCurrentGama());
								mod.getConfig().saveConfig();
							}
						}
					}
				};
				addObject(gamaSlider, resetGama, gamaEntry);
			}
			@Override
			public void drawObject(int mXIn, int mYIn, float ticks) {
				super.drawObject(mXIn, mYIn, ticks);
				drawStringWithShadow("Custom Gama Entry", midX - 25, startY + 57, 0xb2b2b2);
				if (mod.getCurrentGama() < 0) {
					drawStringWithShadow("Note: Negative gama values can", startX + 10, endY - 23, 0xffd800);
					drawStringWithShadow("produce rather odd sights.", startX + 10, endY - 14, 0xffd800);
				}
				if (mod.getCurrentGama() <= 0.009 && mod.getCurrentGama() >= 0) {
					drawStringWithShadow("Minecraft equivalent: Moody", startX + 10, endY - 19, 0xffd800);
				}
				if (mod.getCurrentGama() <= 1.009 && mod.getCurrentGama() >= 1) {
					drawStringWithShadow("Minecraft equivalent: Bright", startX + 10, endY - 19, 0xffd800);
				}
				if (mod.getCurrentGama() < 1 && mod.getCurrentGama() > 0.009) {
					String percentValue = gamaSlider.displayValue.substring(2);
					if (percentValue.startsWith("0")) { percentValue = percentValue.substring(1); }
					drawStringWithShadow("Minecraft equivalent: Brightnes +" + percentValue + "%", startX + 6, endY - 19, 0xffd800);
				}
			}
			@Override
			public void actionPerformed(IEnhancedActionObject object) {
				if (object.equals(resetGama)) {
					mod.setGama(mod.vanillaGama);
					gamaSlider.setSliderValue(mod.vanillaGama);
				}
				if (object.equals(gamaSlider)) {
					mod.setGama(gamaSlider.getSliderValue());
					gamaEntry.setText("" + gamaSlider.displayValue);
				}
				mod.getConfig().saveConfig();
			}
		};
		
		addObject(rendering, gama);
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		drawDefaultBackground();
	}
}
