package com.Whodundid.worldEditor.EditorGuiObjects;

import com.Whodundid.main.util.miscUtil.CursorHelper;
import com.Whodundid.main.util.miscUtil.Resources;
import com.Whodundid.worldEditor.Editor;
import com.Whodundid.worldEditor.EditorUtil.EditorGuiBase;
import com.Whodundid.worldEditor.EditorUtil.EditorTools;

//Last edited: Nov 25, 2018
//First Added: Nov 25, 2018
//Author: Hunter Bragg

public class EditorCursor extends EditorGuiObject {
	
	protected boolean cursorVisibility = true;
	
	public EditorCursor(EditorGuiBase guiIn) {
		guiInstance = guiIn;
		cursorVisibility = CursorHelper.isVisible;
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		updateCursor();
		switch (Editor.getSelectedTool()) {
		case PAN:
			if (Editor.insideEditor) {
				if (Editor.render3D) {
					mc.renderEngine.bindTexture(Resources.editorOrbit);
					drawModalRectWithCustomSizedTexture(mX - 8, mY - 8, 0, 0, 16, 16, 16, 16);
				} else {
					if (!guiInstance.isThereRCM()) {
						if (guiInstance.leftClick || guiInstance.middleClick || guiInstance.rightClick) {
							mc.renderEngine.bindTexture(Resources.editorPanG);
							drawModalRectWithCustomSizedTexture(mX - 9, mY, 0, 0, 15, 13, 15, 13);
						} else { 
							mc.renderEngine.bindTexture(Resources.editorPanU);
							drawModalRectWithCustomSizedTexture(mX - 9, mY - 3, 0, 0, 16, 16, 16, 16);
						}
					}
				}
			}
			break;
		default: break;
		}
	}
	
	public void updateCursor() {
		switch (Editor.getSelectedTool()) {
		case PAN:
			CursorHelper.setCursorVisibility(!Editor.insideEditor || guiInstance.isThereRCM());
			cursorVisibility = false;
			break;
		default: break;
		}
		if (!cursorVisibility) {
			if (Editor.getSelectedTool().equals(EditorTools.PAN) && !(!Editor.insideEditor || guiInstance.isThereRCM())) {
			} else {
				cursorVisibility = true;
				CursorHelper.setCursorVisibility(true);
			}
		}
	}
}
