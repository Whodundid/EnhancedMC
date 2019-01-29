package com.Whodundid.worldEditor.EditorUtil;

import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.worldEditor.Editor;
import com.Whodundid.worldEditor.EditorGuiObjects.EditorBlockPalette;
import com.Whodundid.worldEditor.EditorGuiObjects.EditorCursor;
import com.Whodundid.worldEditor.EditorGuiObjects.EditorGuiRCM;
import com.Whodundid.worldEditor.EditorGuiObjects.EditorGuiTextInput;
import com.Whodundid.worldEditor.EditorGuiObjects.EditorGuiToolList;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.BlockPos;

//Last edited: Dec 10, 2018
//First Added: Nov 25, 2018
//Author: Hunter Bragg

public class EditorGuiBase extends EnhancedGui {

	public EditorCursor editorCursor;
	public EditorGuiTextInput editorTextInput;
	public EditorGuiToolList editorToolList;
	public EditorGuiRCM editorRightClickMenu;
	public EditorBlockPalette editorBlockPalette;
	public EGuiButton draw3D, drawVertical, toggleHiRes, copyButton, pasteButton, renderBiomeMap;
	protected ScaledResolution res;
	public int imgWidth = 453;
	public int imgHeight = 453;
	public int drawWidth = 453;
	public int drawHeight = 453;
	public int locX = 231;
	public int locY = 251;
	public double zoomXHigh = 453, zoomYHigh = 453;
	public double zoomXLow = 0, zoomYLow = 0;
	public int centerX, centerY;
	
	@Override
	public void initGui() {
		super.initGui();
		
		enableHeader(false);
		
		res = new ScaledResolution(mc);
		centerX = res.getScaledWidth() / 2;
		centerY = res.getScaledHeight() / 2;
		startX = centerX - locX;
		startY = centerY - locY;
		endX = startX + imgWidth;
		endY = startY + imgHeight;
		
		zoomEditor(0);
		createBorder();
		
		if (!Editor.firstCenterSet) {
			BlockPos pos = mc.thePlayer.getPosition();
			Editor.setCenter(pos.getX(), pos.getY(), pos.getZ());
			Editor.camPos.set(pos.getX(), pos.getZ(), pos.getY());
			Editor.firstCenterSet = true;
		}
		
		Editor.setEditorGuiReference(this);
		Editor.get2D().init();
		Editor.get3D().init();
	}
	
	@Override
	public void initObjects() {
		editorCursor = new EditorCursor(this);
		editorTextInput = new EditorGuiTextInput(this);
		editorToolList = new EditorGuiToolList(this);
		editorBlockPalette = new EditorBlockPalette(this);
		
		draw3D = new EGuiButton(this, wPos - 359, hPos - 168, 110, 20, (!Editor.render3D) ? "Render 3D" : "Render by Layer");
		drawVertical = new EGuiButton(this, wPos - 472, hPos - 168, 110, 20, (!Editor.renderVertical) ? "Render Horizontal" : "Render Vertical");
		renderBiomeMap = new EGuiButton(this, wPos - 472, hPos - 192, 110, 20, (!Editor.renderBiomeMap) ? "Show Biome View" : "Hide Biome View");
		
		toggleHiRes = new EGuiButton(this, wPos - 359, hPos - 145, 110, 20, (Editor.render3DHighRes) ? "Render Low Res" : "Render High Res");
		
		
		draw3D.setObjectID(1);
		drawVertical.setObjectID(2);
		renderBiomeMap.setObjectID(3);
		toggleHiRes.setObjectID(4);
		
		addObject(editorCursor, editorTextInput, editorToolList, editorBlockPalette);
		addObject(draw3D, drawVertical, renderBiomeMap, toggleHiRes);
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		int halfWidth = res.getScaledWidth() / 2, halfHeight = res.getScaledHeight() / 2;
		Editor.insideEditor = (mX >= startX && mX <= endX - 1 && mY >= startY + 3 && mY <= endY + 3);
		
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		mc.renderEngine.bindTexture(Editor.imageHandlerBorder.getTextureLocation());
		drawCustomSizedTexture(halfWidth - locX - 3, halfHeight - locY, 0, 0, imgWidth + 6, imgHeight + 6, drawWidth + 6, drawHeight + 6);
		if (Editor.render3D) {
			mc.renderEngine.bindTexture(Editor.render3DHighRes ? Editor.imageHandler3DHiRes.getTextureLocation() : Editor.imageHandler3D.getTextureLocation());
			drawCustomSizedTexture(halfWidth - locX, halfHeight - locY + 3, 0, 0, 453, 453, 453, 453);
		} else {
			mc.renderEngine.bindTexture(Editor.imageHandler2D.getTextureLocation());
			if (Editor.getRenderVertical()) {
				drawCustomSizedTexture(halfWidth - locX, halfHeight - locY + 3, 0, 0, 453, 453, -453, -453);
			} else {
				drawCustomSizedTexture(halfWidth - locX, halfHeight - locY + 3, zoomXLow, zoomYLow, drawWidth, drawHeight, zoomXHigh, zoomYHigh);
			}
		}
		drawMainWindow();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
	}
	
	protected void drawMainWindow() {
		if (Editor.getRender3D()) {
			Editor.get3D().draw();
		} else {
			Editor.get2D().draw();
		}
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(draw3D)) {
			Editor.setRender3D(!Editor.getRender3D());
			draw3D.displayString = (!Editor.getRender3D()) ? "Render 3D" : "Render by Layer";
			if (Editor.render3D) { Editor.get3D().open(); }
		}
		if (object.equals(drawVertical)) {
			Editor.setRenderVertical(!Editor.getRenderVertical());
			if (Editor.getRenderVertical()) { Editor.setRender3D(false); }
			drawVertical.displayString = (!Editor.getRenderVertical()) ? "Render Horizontal" : "Render Vertical";
		}
		if (object.equals(toggleHiRes)) {
			Editor.setRenderHiRes(!Editor.render3DHighRes);
			toggleHiRes.displayString = (Editor.render3DHighRes) ? "Render Low Res" : "Render High Res";
		}
		
		if (object.equals(renderBiomeMap)) {
			Editor.setRenderBiomeMap(!Editor.renderBiomeMap);
			renderBiomeMap.displayString = (!Editor.renderBiomeMap) ? "Show Biome View" : "Hide Biome View";
		}
	}
	
	@Override
	public void parseMousePosition(int mXIn, int mYIn) {
		super.parseMousePosition(mXIn, mYIn);
		if (Editor.render3D) { Editor.get3D().parseMousePosition(mXIn, mYIn); }
		else { Editor.get2D().parseMousePosition(mXIn, mYIn); }
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		if (Editor.render3D) { Editor.get3D().handleMousePress(mXIn, mYIn, button); }
		else { Editor.get2D().handleMousePress(mXIn, mYIn, button); }
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		super.mouseReleased(mXIn, mYIn, button);
		if (Editor.render3D) { Editor.get3D().handleMouseRelease(mXIn, mYIn, button); }
		else { Editor.get2D().handleMouseRelease(mXIn, mYIn, button); }
	}
	
	@Override
	public void mouseDragged(int mXIn, int mYIn, int button, long timeSinceLastClick) {
		super.mouseDragged(mXIn, mYIn, button, timeSinceLastClick);
		if (Editor.render3D) { Editor.get3D().handleMouseDrag(mXIn, mYIn, button, timeSinceLastClick); }
		else { Editor.get2D().handleMouseDrag(mXIn, mYIn, button, timeSinceLastClick); }
	}
	
	@Override
	public void mouseScrolled(int change) {
		super.mouseScrolled(change);
		if (Editor.render3D) { Editor.get3D().handleMouseScroll(change); }
		else { Editor.get2D().handleMouseScroll(change); }
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (Editor.render3D) { Editor.get3D().handleKeyPress(typedChar, keyCode); }
		else { Editor.get2D().handleKeyPress(typedChar, keyCode); }
	}
	
	@Override
	public void keyReleased(char typedChar, int keyCode) {
		if (Editor.render3D) { Editor.get3D().handleKeyRelease(typedChar, keyCode); }
		else { Editor.get2D().handleKeyRelease(typedChar, keyCode); }
	}
	
	@Override
	public void onGuiClosed() {
		Editor.get2D().onGuiClosed();
		Editor.get3D().onGuiClosed();
		Editor.closeEditor();
	}
	
	private void createBorder() {
		for (int x = 0; x < Editor.imageHandlerBorder.GBI().getWidth(); x++) {
			if (x % 2 == 1) {
				Editor.imageHandlerBorder.GBI().setRGB(x, 0, 0x330000);
				Editor.imageHandlerBorder.GBI().setRGB(x, Editor.imageHandlerBorder.GBI().getHeight() - 1, 0x330000);
				Editor.imageHandlerBorder.GBI().setRGB(0, x, 0x330000);
				Editor.imageHandlerBorder.GBI().setRGB(Editor.imageHandlerBorder.GBI().getWidth() - 1, x, 0x330000);				
			} else {
				Editor.imageHandlerBorder.GBI().setRGB(x, 0, 0xCC0000);
				Editor.imageHandlerBorder.GBI().setRGB(x, Editor.imageHandlerBorder.GBI().getHeight() - 1, 0xCC0000);
				Editor.imageHandlerBorder.GBI().setRGB(0, x, 0xCC0000);
				Editor.imageHandlerBorder.GBI().setRGB(Editor.imageHandlerBorder.GBI().getWidth() - 1, x, 0xCC0000);
			}
		}
		Editor.imageHandlerBorder.GBI().setRGB(76, 0, 0x00FF00);
		Editor.imageHandlerBorder.GBI().setRGB(0, 76, 0x00FF00);
		Editor.imageHandlerBorder.GBI().setRGB(152, 76, 0x00FF00);
		Editor.imageHandlerBorder.GBI().setRGB(76, 152, 0x00FF00);
		Editor.imageHandlerBorder.updateTextureData(Editor.imageHandlerBorder.GBI());
	}
	
	public void zoomEditor(int scrollChange) {
		double currentZoom = Editor.getZoomScale();
		Editor.setZoomScale(currentZoom += (scrollChange * 0.25));
		if (Editor.getZoomScale() < 1) { Editor.setZoomScale(1); }
		if (Editor.getZoomScale() > 25) { Editor.setZoomScale(25); }
		
		zoomXHigh = imgWidth * Editor.getZoomScale();
		zoomYHigh = imgHeight * Editor.getZoomScale();
		
		zoomXLow = (zoomXHigh - drawWidth) / 2;
		zoomYLow = (zoomYHigh - drawHeight) / 2;
	}

	public boolean isThereRCM() { return editorRightClickMenu != null; }
	public EditorGuiRCM getRCM() { return editorRightClickMenu; }
	public void removeRCM() { removeObject(editorRightClickMenu); editorRightClickMenu = null; clearFocusedObject(); }
	
	public EditorGuiRCM addRCM(int mX, int mY) {
		editorRightClickMenu = new EditorGuiRCM(this, mX, mY);
		addObject(editorRightClickMenu);
		return editorRightClickMenu;
	}
}
