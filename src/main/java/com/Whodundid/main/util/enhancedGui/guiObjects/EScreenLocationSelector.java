package com.Whodundid.main.util.enhancedGui.guiObjects;

import com.Whodundid.main.global.subMod.IUseScreenLocation;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.EnhancedGuiObject;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.main.util.miscUtil.ScreenLocation;
import java.util.Stack;

//Last edited: Jan 2, 2019
//First Added: Dec 14, 2018
//Author: Hunter Bragg

public class EScreenLocationSelector extends EnhancedGuiObject implements IEnhancedActionObject {
	
	protected boolean runActionOnPress = false;
	protected Object storedObject = null;
	protected IUseScreenLocation obj;
	protected EGuiButton bLeft, bRight, tLeft, tRight, center, custom, chatDraw;
	protected int heightRatio = 0, widthRatio = 0;
	public String drawName = "";
	
	public EScreenLocationSelector(IEnhancedGuiObject parentIn, IUseScreenLocation objIn, int posX, int posY, int size) {
		init(parentIn, posX, posY, size, size - 10);
		obj = objIn;
		
		heightRatio = (int) (height * 0.75);
		widthRatio = (int) (width * 0.5);
	}
	
	@Override
	public void initObjects() {
		bLeft = new EGuiButton(this, startX + 4, startY + heightRatio - 19, 23, 15, "BL").setStorredObject(ScreenLocation.BL);
		bRight = new EGuiButton(this, endX - 27, startY + heightRatio - 19, 23, 15, "BR").setStorredObject(ScreenLocation.BR);
		tLeft = new EGuiButton(this, startX + 4, startY + 4, 23, 15, "TL").setStorredObject(ScreenLocation.TL);
		tRight = new EGuiButton(this, endX - 27, startY + 4, 23, 15, "TR").setStorredObject(ScreenLocation.TR);
		center = new EGuiButton(this, startX + width / 2 - 11, startY + (heightRatio / 2) - 7, 23, 15, "C").setStorredObject(ScreenLocation.C);
		custom = new EGuiButton(this, startX + width / 2 - (95 / 2), endY, 95, 16, "Custom location").setStorredObject(ScreenLocation.CUSTOM);
		
		addObject(bLeft, bRight, tLeft, tRight, center, custom);
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		drawRect(startX, startY, endX, startY + heightRatio, -0x00ffffff);
		drawRect(startX + widthRatio - (widthRatio / 16), startY + heightRatio, startX + widthRatio + (widthRatio / 16), endY - (heightRatio / 8), -0x00ffffff);
		drawRect(startX + widthRatio - (widthRatio / 2), endY - (heightRatio / 8), startX + widthRatio + (widthRatio / 2), endY - (heightRatio / 8) + 3, -0x00ffffff);
		drawRect(startX + 3, startY + 3, endX - 3, startY + heightRatio - 3, 0xffC9FFFF);
		
		drawStringWithShadow("Select a location to draw " + drawName + ".", midX - fontRenderer.getStringWidth("Select a location to draw " + drawName + ".") / 2, startY - heightRatio / 5 - 12, 0xb2b2b2);
		String msg = "";
		switch (obj.getScreenLocation()) {
		case BL: msg = "Bottom Left"; break;
		case BR: msg = "Bottom Right"; break;
		case TL: msg = "Top Left"; break;
		case TR: msg = "Top Right"; break;
		case C: msg = "Center"; break;
		case CUSTOM: msg = "Custom (" + obj.getLocation().getObject() + ", " + obj.getLocation().getValue() + ")"; break;
		}
		drawStringWithShadow("Current Location: ", midX - fontRenderer.getStringWidth("Current Location: " + msg) / 2, startY - heightRatio / 5, 0xffd800);
		drawStringWithShadow(msg, midX - fontRenderer.getStringWidth("Current Location: " + msg) / 2 + fontRenderer.getStringWidth("Current Location: "), startY - heightRatio / 5, 0x00ff00);
		
		super.drawObject(mX, mY, ticks);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(bLeft)) { obj.setLocation(ScreenLocation.BL); }
		if (object.equals(bRight)) { obj.setLocation(ScreenLocation.BR); }
		if (object.equals(tLeft)) { obj.setLocation(ScreenLocation.TL); }
		if (object.equals(tRight)) { obj.setLocation(ScreenLocation.TR); }
		if (object.equals(center)) { obj.setLocation(ScreenLocation.C); }
		if (object.equals(custom)) {
			EnhancedGui newGui = obj.getScreenLocationGui();
			Stack<EnhancedGui> history = getTopParent().getGuiHistory();
			if (mc.currentScreen instanceof EnhancedGui) { history.push((EnhancedGui) mc.currentScreen); }
			if (history != null) { newGui.sendGuiHistory(history); }
			mc.displayGuiScreen(newGui);
		}
		if (getParent() != null) { getParent().actionPerformed(this); }
	}
	
	public EScreenLocationSelector setDisplayName(String nameIn) { drawName = nameIn; return this; }

	//-------------------------------
	//IEnhancedActionObject overrides
	//-------------------------------
			
	//actions
	@Override public boolean runActionOnPress() { return runActionOnPress; }
	@Override public EScreenLocationSelector setRunActionOnPress(boolean value) { runActionOnPress = value; return this; }
	@Override public void performAction() {}
			
	//objects
	@Override public EScreenLocationSelector setStorredObject(Object objIn) { storedObject = objIn; return this; }
	@Override public Object getStorredObject() { return storedObject; }
}
