package com.Whodundid.main.util.enhancedGui.guiObjects;

import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.main.util.storageUtil.StorageBox;
import com.Whodundid.main.util.storageUtil.StorageBoxHolder;
import net.minecraft.client.gui.ScaledResolution;

//Last edited: Jan 24, 2019
//First Added: Jan 23, 2019
//Author: Hunter Bragg

public class EGuiSelectionList extends InnerEnhancedGui implements IEnhancedActionObject {
	
	EGuiButton select, cancelSel;
	EGuiTextArea selectionList;
	Object defaultSelectionObject = null;
	StorageBoxHolder<String, ?> listContents = null;
	Object selectedObject = null;
	
	public EGuiSelectionList(IEnhancedGuiObject parentIn, StorageBoxHolder<String, ?> objectListIn) { this(parentIn, true, objectListIn, null); }
	public EGuiSelectionList(IEnhancedGuiObject parentIn, StorageBoxHolder<String, ?> objectListIn, Object selObjIn) { this(parentIn, true, objectListIn, selObjIn); }
	public EGuiSelectionList(IEnhancedGuiObject parentIn, int xPos, int yPos, StorageBoxHolder<String, ?> objectListIn) { this(parentIn, xPos, yPos, 200, 230, objectListIn, null); }
	public EGuiSelectionList(IEnhancedGuiObject parentIn, int xPos, int yPos, StorageBoxHolder<String, ?> objectListIn, Object selObjIn) { this(parentIn, xPos, yPos, 200, 230, objectListIn, selObjIn); }
	public EGuiSelectionList(IEnhancedGuiObject parentIn, int xPos, int yPos, int widthIn, int heightIn, StorageBoxHolder<String, ?> objectListIn) { this(parentIn, xPos, yPos, widthIn, heightIn, objectListIn, null); }
	public EGuiSelectionList(IEnhancedGuiObject parentIn, int xPos, int yPos, int widthIn, int heightIn, StorageBoxHolder<String, ?> objectListIn, Object selObjIn) {
		init(parentIn, xPos, yPos, widthIn, heightIn);
		listContents = objectListIn;
		defaultSelectionObject = selObjIn;
	}
	protected EGuiSelectionList(IEnhancedGuiObject parentIn, boolean noPos, StorageBoxHolder<String, ?> objectListIn, Object selObjIn) {
		ScaledResolution res = new ScaledResolution(mc);
		init(parentIn, (res.getScaledWidth() - 200) / 2, (res.getScaledHeight() - 230) / 2, 200, 230);
		listContents = objectListIn;
		defaultSelectionObject = selObjIn;
	}
	
	@Override
	public void initGui() {
		requestFocus();
		getTopParent().setFocusLockObject(this);
		
		header = new EGuiHeader(this);
		header.setDisplayString("Select a Chat Type.");
		
		select = new EGuiButton(this, startX + 10, endY - 28, 80, 20, "Select");
		cancelSel = new EGuiButton(this, endX - 90, endY - 28, 80, 20, "Cancel");
		
		selectionList = new EGuiTextArea(this, startX + 10, startY + 10, width - 20, height - 45, false).setDrawLineNumbers(true);
		
		addObject(header, select, cancelSel, selectionList);
		
		for (StorageBox<String, ?> b : listContents) {
			selectionList.addTextLine(b.getObject(), 0xffffff, b.getValue());
		}
		
		if (!selectionList.getTextDocument().isEmpty()) { selectionList.setSelectedLine(selectionList.getTextLineWithLineNumber(1)); }
	}
	
	@Override
	public void onObjectAddedToParent() {
		bringToFront();
		System.out.println("called");
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawDefaultBackground();
		select.setEnabled(selectionList.getCurrentLine() != null);
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		System.out.println("hello: " + mXIn + " " + mYIn);
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(select)) {
			if (selectionList.getCurrentLine() != null && selectionList.getCurrentLine().getStoredObj() != null) {
				selectedObject = selectionList.getCurrentLine().getStoredObj();
				getParent().actionPerformed(this);
				close();
			}
		}
		if (object.equals(cancelSel)) { close(); }
	}
	
	@Override public boolean runActionOnPress() { return false; }
	@Override public EGuiSelectionList setRunActionOnPress(boolean val) { return null; }
	@Override public EGuiSelectionList setStorredObject(Object objIn) { selectedObject = objIn; return this; }
	@Override public Object getStorredObject() { return selectedObject; }
	@Override public void performAction() { }
}
