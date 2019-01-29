package com.Whodundid.main.util.enhancedGui.guiObjects;

import com.Whodundid.main.util.enhancedGui.EnhancedGuiObject;
import com.Whodundid.main.util.enhancedGui.guiUtil.FocusEvent;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.main.util.storageUtil.StorageBox;
import java.text.DecimalFormat;
import net.minecraft.util.MathHelper;

//Last edited: Jan 2, 2019
//First Added: Oct 14, 2018
//Author: Hunter Bragg

public class EGuiSlider extends EnhancedGuiObject implements IEnhancedActionObject {

	protected float sliderValue = 0;
	public float lowVal = 0, highVal = 0;
	protected float pos = 0.0f;
	protected boolean vertical;
	protected boolean isSliding = false;
	protected double interval = 0;
	protected boolean runActionOnPress = false;
	protected Object storedObject = null;
	public String displayValue = "";
	public int displayValueColor = 0x00ff00;
	public int thumbSize = 8;
	private int thumbStartX = 0, thumbStartY = 0;
	private int thumbEndX = 0, thumbEndY = 0;
	protected StorageBox<Integer, Integer> mousePos = new StorageBox();
	
	public EGuiSlider(IEnhancedGuiObject parentIn, int xIn, int yIn, int widthIn, int heightIn, float lowValIn, float highValIn, boolean verticalIn) {
		this(parentIn, xIn, yIn, widthIn, heightIn, lowValIn, highValIn, 0.0f, verticalIn);
	}
	
	public EGuiSlider(IEnhancedGuiObject parentIn, int xIn, int yIn, int widthIn, int heightIn, float lowValIn, float highValIn, float startVal, boolean verticalIn) {
		init(parentIn, xIn, yIn, widthIn, heightIn);
		lowVal = lowValIn;
		highVal = highValIn;
		vertical = verticalIn;
		
		if (verticalIn) {
			thumbStartX = startX + 1;
			thumbStartY = startY + 1;
			thumbEndX = endX;
			thumbEndY = startY + thumbSize;
		} else {
			thumbStartX = startX + 1;
			thumbStartY = startY + 1;
			thumbEndX = startX + thumbSize;
			thumbEndY = endY - 1;
		}
		
		setSliderValue(startVal);
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		if (isSliding && mousePos != null && mousePos.getObject() != null && mousePos.getValue() != null) {
			if (vertical) { moveThumb(0, mY - mousePos.getValue()); }
			else { moveThumb(mX - mousePos.getObject(), 0); }
			mousePos.setValues(mX, mY);
		}
		
		drawRect(startX, startY, endX, endY, 0xff000000); //black
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, 0xff666666);
		
		drawRect(thumbStartX, thumbStartY, thumbEndX, thumbEndY, (isMouseInThumb(mX, mY) || isSliding) ? 0xffffffff : 0xffbbbbbb);
		
		displayValue = new DecimalFormat("0.00").format(sliderValue);
		
		drawCenteredString(displayValue, midX, midY - 2, displayValueColor);
		super.drawObject(mX, mY, ticks);
	}
	
	@Override
	public void move(int newX, int newY) {
		thumbStartX += newX;
		thumbStartY += newY;
		thumbEndX += newX;
		thumbEndY += newY;
		super.move(newX, newY);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button ) {
		if (button == 0) {
			if (isMouseInThumb(mX, mY)) {
				isSliding = true;
				mousePos.setValues(mX, mY);
			} else {
				calculateSliderPos(true);
				isSliding = true;
			}
		}
		super.mousePressed(mX, mY, button);
	}
	
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		isSliding = false;
		super.mouseReleased(mX, mY, button);
	}
	
	@Override
	public void onFocusLost(FocusEvent eventIn) {
		isSliding = false;
	}
	
	//-------------------------------
	//IEnhancedActionObject overrides
	//-------------------------------
			
	//actions
	@Override public boolean runActionOnPress() { return runActionOnPress; }
	@Override public EGuiSlider setRunActionOnPress(boolean value) { runActionOnPress = value; return this; }
	@Override public void performAction() {}
			
	//objects
	@Override public EGuiSlider setStorredObject(Object objIn) { storedObject = objIn; return this; }
	@Override public Object getStorredObject() { return storedObject; }
		
	//------------------
	//EGuiSlider methods
	//------------------
	
	private void moveThumb(int newX, int newY) {
		if (vertical) {
			if (!(thumbStartY + newY < startY) && (thumbEndY + newY < endY + 1)) {
				thumbStartY += newY;
				thumbEndY += newY;
			} else {
				if (newY < 0) {
					thumbStartY = startY;
					thumbEndY = startY + thumbSize;
				} else if (newY > 0) {
					thumbStartY = endY - thumbSize;
					thumbEndY = endY;
				}
			}
		} else {
			if (!(thumbStartX + newX < startX + 1) && (thumbEndX + newX < endX)) {
				thumbStartX += newX;
				thumbEndX += newX;
			} else {
				if (newX < 0) {
					thumbStartX = startX + 1;
					thumbEndX = startX + thumbSize;
				} else if (newX > 0) {
					thumbStartX = endX - thumbSize;
					thumbEndX = endX - 1;
				}
			}
		}
		calculateSliderPos(true);
	}
	
	public EGuiSlider setSliderValue(float valIn) {
		sliderValue = valIn;
		pos = MathHelper.clamp_float((valIn - lowVal) / (highVal - lowVal), 0.0f, 1.0f);
		displayValue = new DecimalFormat("0.00").format(sliderValue);
		if (valIn >= lowVal && valIn <= highVal) { calculateSliderPos(false); }
		else if (valIn > highVal) {
			if (vertical) {
				thumbStartX = startX + 1;
				thumbStartY = endY - thumbSize - 1;
				thumbEndX = endX - 1;
				thumbEndY = thumbStartY + thumbSize;
			} else {
				thumbStartX = endX - thumbSize - 1;
				thumbStartY = startY + 1;
				thumbEndX = thumbStartX + thumbSize;
				thumbEndY = endY - 1;
			}
		}
		else if (valIn < lowVal) {
			thumbStartX = startX + 1;
			thumbStartY = startY + 1;
			if (vertical) {
				thumbEndX = endX - 1;
				thumbEndY = thumbStartY + thumbSize;
			} else {
				thumbEndX = thumbStartX + thumbSize;
				thumbEndY = endY - 1;
			}
		}
		return this;
	}
	
	private void calculateSliderPos(boolean calc) {
		if (calc) {
			pos = MathHelper.clamp_float((float)(mX - startX - thumbSize / 2) / (width - thumbSize), 0f, 1f);
			sliderValue = lowVal + (highVal - lowVal) * pos;
			displayValue = new DecimalFormat("0.00").format(sliderValue);
		}
		if (vertical) {
			thumbStartX = startX + 1;
			thumbStartY = startY + 1 + (int)(pos * (width - thumbSize - 2));
			thumbEndX = endX - 1;
			thumbEndY = thumbStartX + thumbSize;
		} else {
			thumbStartX = startX + 1 + (int)(pos * (width - thumbSize - 2));
			thumbStartY = startY + 1;
			thumbEndX = thumbStartX + thumbSize;
			thumbEndY = endY - 1;
		}
		if (getParent() != null) { getParent().actionPerformed(this); }
	}
	
	public boolean isMouseInThumb(int mX, int mY) { return mX >= thumbStartX && mX <= thumbEndX && mY >= thumbStartY && mY <= thumbEndY; }
	
	public float getSliderValue() { return sliderValue; }
	public boolean drawVertical() { return vertical; }
	public int getThumbSize() { return thumbSize; }
	public float getLowVal() { return lowVal; }
	public float getHighVal() { return highVal; }
	public EGuiSlider setDisplayValueColor(int colorIn) { displayValueColor = colorIn; return this; }
	public EGuiSlider setThumbSize(int sizeIn) { thumbSize = sizeIn; return this; }
	public EGuiSlider setHighVal(int valIn) { highVal = valIn; return this; }
	public EGuiSlider setLowVal(int valIn) { lowVal = valIn; return this; }
}
