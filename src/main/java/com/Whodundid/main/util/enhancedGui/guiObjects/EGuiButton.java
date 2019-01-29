package com.Whodundid.main.util.enhancedGui.guiObjects;

import com.Whodundid.main.util.enhancedGui.EnhancedGuiObject;
import com.Whodundid.main.util.enhancedGui.guiObjectUtil.EButtonGroup;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.main.util.miscUtil.Resources;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

//Last edited: Jan 2, 2019
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public class EGuiButton extends EnhancedGuiObject implements IEnhancedActionObject {
	
	public String displayString = "";
	public int color = 14737632;
	public int backgroundColor = 0xff000000;
	protected boolean usingBaseTexture = true, usingBaseSelTexture = true;
	protected boolean stretchBaseTextures = false;
	protected int pressedButton = -1;
	protected boolean runActionOnPress = false;
	protected boolean drawBackground = false;
	protected boolean drawDefault = true;
	protected boolean trueFalseButton = false;
	protected boolean drawString = true;
	protected Object storedObject = null;
	protected EButtonGroup buttonGroup = null;
	protected ResourceLocation btnTexture = Resources.guiButtonBase;
	protected ResourceLocation btnSelTexture = Resources.guiButtonSel;
	
	protected EGuiButton() {}
	public EGuiButton(IEnhancedGuiObject parentIn, int posX, int posY, int width, int height) { this(parentIn, posX, posY, width, height, ""); }
	public EGuiButton(IEnhancedGuiObject parentIn, int posX, int posY, int width, int height, String displayStringIn) {
		init(parentIn, posX, posY, width, height);
		displayString = displayStringIn;
	}

	@Override
	public void drawObject(int mX, int mY, float ticks) {
		GlStateManager.pushMatrix();
		if (drawBackground) { drawRect(startX, startY, endX, endY, backgroundColor); }
		int stringColor = isMouseHover ? 0xFFFFA0 : color;
		if (drawDefault) {
			GlStateManager.color(1.0f, 1.0f, 1.0f);
			if (isMouseHover) {
				if (btnSelTexture != null) { mc.renderEngine.bindTexture(btnSelTexture); }
			} else {
				if (btnTexture != null) { mc.renderEngine.bindTexture(btnTexture); }
			}
			GlStateManager.enableBlend();
	        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	        GlStateManager.blendFunc(770, 771);
	    	if (!isEnabled()) { stringColor = 0xaaaaaa; }
	    	if (usingBaseTexture || usingBaseSelTexture) {
	    		if (stretchBaseTextures) {
					drawModalRectWithCustomSizedTexture(startX, startY, 0, 0, width, height, width, height);
				} else {
					mc.renderEngine.bindTexture(Resources.guiButtons);
					int i = height > 20 ? 20 : height;
					int offset = isMouseHover ? 20 : 0;
					if (!isEnabled()) { offset = 0; }
					if (height < 20) {
						i = i >= 3 ? i - 2 : i;
						drawTexturedModalRect(startX, startY, 0, 0 + offset, width - 2, i);
	            		drawTexturedModalRect(startX + width - 2, startY, 198, 0 + offset, 2, i);
	            		drawTexturedModalRect(startX, startY + height - 2, 0, 18 + offset, width - 2, 2);
	            		drawTexturedModalRect(startX + width - 2, startY + height - 2, 198, 18 + offset, 2, 2);
					} else {
						drawTexturedModalRect(startX, startY, 0, 0 + offset, width - 2, i);
	            		drawTexturedModalRect(startX + width - 2, startY, 198, 0 + offset, 2, i);
					}
				}
			} else {
				drawModalRectWithCustomSizedTexture(startX, startY, 0, 0, width, height, width, height);
			}
		}
		if (drawString) { drawCenteredStringWithShadow(displayString, startX + width / 2 + 1, startY + (height - 8) / 2, stringColor); }
		GlStateManager.popMatrix();
		super.drawObject(mX, mY, ticks);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
		if (hasFocus()) { pressButton(button); }
    }
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 28) {
			if (getParent() != null) { getParent().actionPerformed(this); }
		}
		super.keyPressed(typedChar, keyCode);
	}
	
	//-------------------------------
	//IEnhancedActionObject overrides
	//-------------------------------
		
	//actions
	@Override public boolean runActionOnPress() { return runActionOnPress; }
	@Override public EGuiButton setRunActionOnPress(boolean value) { runActionOnPress = value; return this; }
	@Override public void performAction() {}
		
	//objects
	@Override public EGuiButton setStorredObject(Object objIn) { storedObject = objIn; return this; }
	@Override public Object getStorredObject() { return storedObject; }
	
	//------------------
	//EGuiButton methods
	//------------------
	
	protected void pressButton(int button) {
		if (enabled && checkDraw() && isMouseHover) {
			pressedButton = button;
			if (runActionOnPress) { performAction(); }
			else if (button == 0) {
				playPressSound();
				if (getParent() != null) { getParent().actionPerformed(this); }
			}
			if (buttonGroup != null) { buttonGroup.buttonPressed(this, button); }
		}
	}
	
	public void updateTrueFalseDisplay(boolean val) {
		if (trueFalseButton) { setDisplayString(val ? "True" : "False").setDisplayStringColor(val ? 0x55ff55 : 0xff5555); }
	}
		
	public void playPressSound() { mc.getSoundHandler().playSound(PositionedSoundRecord.create(Resources.buttonSound, 1.0F)); }
	public int getPressedButton() { return pressedButton; }
	public EButtonGroup getButtonGroup() { return buttonGroup; }
	public EGuiButton setButtonGroup(EButtonGroup groupIn) { buttonGroup = groupIn; return this; }
	public EGuiButton setDisplayString(String stringIn) { displayString = stringIn; return this; }
	public EGuiButton setDisplayStringColor(int colorIn) { color = colorIn; return this; }
	public EGuiButton setButtonTexture(ResourceLocation loc) { btnTexture = loc; if (loc != null) { usingBaseTexture = loc.equals(Resources.guiButtonBase); } return this; }
	public EGuiButton setButtonSelTexture(ResourceLocation loc) { btnSelTexture = loc; if (loc != null) { usingBaseSelTexture = loc.equals(Resources.guiButtonSel); } return this; }
	public EGuiButton setDrawBackground(boolean val) { drawBackground = val; return this; }
	public EGuiButton setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
	public EGuiButton setDrawDefault(boolean val) { drawDefault = val; return this; }
	public EGuiButton setTrueFalseButton(boolean val) { trueFalseButton = val; updateTrueFalseDisplay(false); return this; }
	public EGuiButton setDrawString(boolean val) { drawString = val; return this; }
}
