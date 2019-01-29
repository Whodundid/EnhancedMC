package com.Whodundid.worldEditor.Editor2D;

import com.Whodundid.main.MainMod;
import com.Whodundid.main.util.enhancedGui.guiUtil.EGui;
import com.Whodundid.main.util.miscUtil.Resources;
import com.Whodundid.main.util.miscUtil.WorldHelper;
import com.Whodundid.main.util.playerUtil.PlayerFacing;
import com.Whodundid.main.util.storageUtil.StorageBox;
import com.Whodundid.main.util.storageUtil.Vector3D;
import com.Whodundid.main.util.storageUtil.Vector3DInt;
import com.Whodundid.worldEditor.Editor;
import com.Whodundid.worldEditor.EditorUtil.EditorGuiBase;
import com.Whodundid.worldEditor.EditorUtil.EditorTools;
import java.io.IOException;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

//Last edited: Dec 21, 2018
//First Added: Apr 14, 2018
//Author: Hunter Bragg

public class Editor2D {
	
	protected Minecraft mc = MainMod.getMC();
	public EditorGuiBase gui;
	public Vector3DInt grabPosition2D = new Vector3DInt();
	private EditorTools previousTool;
	
	public void setGui(EditorGuiBase guiIn) { gui = guiIn; }
	
	public void init() {
		
	}
	
	public void onGuiClosed() {
		
	}
	
	public void draw() {
		createMapImage();
		drawSelectionBox();
	}
	
	public void parseMousePosition(int mXIn, int mYIn) {
		
	}
	
	public void handleMousePress(int mX, int mY, int button) {
		if (gui.isThereRCM()) { 
			if (gui.editorRightClickMenu.isMouseInside(mX, mY)) {
				gui.editorRightClickMenu.mousePressed(mX, mY, button); 
			} else { gui.removeRCM(); }
		} else {
			if (button == 0 || button == 1 || button == 2) {
				if (Editor.insideEditor) {
					switch (Editor.getSelectedTool()) {
					case PAN:
						if (button == 2) {
							previousTool = Editor.getSelectedTool();
							Editor.setSelectedTool(EditorTools.PAN);
						}
						grabPosition2D.set(getWorldCoordsAtMouseLocation(mX, mY));
						break;
					default: break;
					}
				}
			}
			if (button == 0) {
				if (Editor.insideEditor) {
					switch (Editor.getSelectedTool()) {
					case SELECT:
						//System.out.println(getWorldCoordsAtMouseLocation(mX, mY));
						Editor.setPosition1(getWorldCoordsAtMouseLocation(mX, mY));
						Editor.setSelectionPos1(new StorageBox<Integer, Integer>(mX, mY));
						Editor.setSelectionPos2(new StorageBox<Integer, Integer>(mX, mY));
						break;
					default: break;
					}
				} else { gui.editorToolList.mousePressed(mX, mY, button); }
			} else if (button == 1) {
				if (gui.isThereRCM()) {
					if (!gui.editorRightClickMenu.isMouseInside(mX, mY)) { gui.removeRCM(); }
				} else {
					if (Editor.insideEditor) {
						switch (Editor.getSelectedTool()) {
						case SELECT: break;
						default: break;
						}
					}
				}
			}
		}
	}
	
	public void handleMouseRelease(int mX, int mY, int button) {
		if (!gui.isThereRCM()) {
			if (button == 0 || button == 1 || button == 2) {
				switch (Editor.getSelectedTool()) {
				case PAN:
					if (button == 2) { Editor.setSelectedTool(previousTool != null ? previousTool : EditorTools.SELECT); }
					grabPosition2D.clear();
					break;
				default: break;
				}
			}
			if (button == 0) {
				switch (Editor.getSelectedTool()) {
				case SELECT:
					if (Editor.insideEditor) {
						System.out.println(getWorldCoordsAtMouseLocation(mX, mY));
						Editor.setPosition2(getWorldCoordsAtMouseLocation(mX, mY));
					}
					break;
				default: break;
				}
			} else if (button == 1) {
				if (Editor.getSelectedTool() != EditorTools.PAN) { gui.addRCM(mX, mY).setClickedPos(getWorldCoordsAtMouseLocation(mX, mY)).requestFocus(); }
			} 
		}
	}
	
	public void handleMouseDrag(int mX, int mY, int button, long timeSinceLastClick) {
		if (!gui.isThereRCM()) {
			if (button == 0) {
				switch (Editor.getSelectedTool()) {
				case SELECT:
					if (Editor.insideEditor) {
						//Editor.setPosition2(getWorldCoordsAtMouseLocation(mX, mY));
						Editor.setSelectionPos2(new StorageBox<Integer, Integer>(mX, mY));
					}
					break;
				default: break;
				}
			}
			if (button == 0 || button == 1 || button == 2) {
				switch (Editor.getSelectedTool()) {
				case PAN:
					if (Editor.insideEditor) {
						Vector3DInt newPos = getWorldCoordsAtMouseLocation(mX, mY);
						Vector3D center = Editor.getCenter();
						if (Editor.getRenderVertical()) {
							Editor.setCenter(center.x, newPos.y + (grabPosition2D.z), center.z);
						} else {
							if (gui.isShiftKeyDown()) {
								Editor.setCenter(center.x + (grabPosition2D.x - newPos.x), newPos.y, center.z);
							} else if (gui.isCtrlKeyDown()) {
								Editor.setCenter(center.x, newPos.y, center.z + (grabPosition2D.z - newPos.z));
							} else {
								Editor.setCenter(center.x + (grabPosition2D.x - newPos.x), newPos.y, center.z + (grabPosition2D.z - newPos.z));
							}
						}
						gui.editorTextInput.resetAllBoxesText();
					}
					break;
				default: break;
				}
			}
		}
	}
	
	public void handleMouseScroll(int change) {
		Vector3D pos = Editor.getCenter();
		int w = Editor.imageHandler2D.getTextureWidth() / 2;
		int mWidth = (int) Math.ceil(w / (gui.zoomXHigh / 3 / w));
		if (gui.isShiftKeyDown()) { Editor.setCenter(pos.x - change * mWidth / 10, pos.y, pos.z); }
		else if (gui.isAltKeyDown()) {  Editor.setCenter(pos.x, pos.y - change, pos.z); }
		else if (gui.isCtrlKeyDown()) { gui.zoomEditor(change); }
		else { Editor.setCenter(pos.x, pos.y, pos.z - change * mWidth / 10); }
		gui.editorTextInput.resetAllBoxesText();
	}
	
	public void handleKeyPress(char typedChar, int keyCode) {
		if (gui.isCtrlKeyDown()) {
			switch (keyCode) {
			case 21: mc.thePlayer.sendChatMessage("//redo"); break;
			case 44: mc.thePlayer.sendChatMessage("//undo"); break;
			case 46: Editor.copyRegion(); break;
			case 47: Editor.pasteRegion(); break;
			case 32: Editor.setSelectionPos1(null); Editor.setSelectionPos2(null); Editor.pos1.clear(); Editor.pos2.clear(); break;
			}
		} else {
			switch (keyCode) {
			case 1: if (gui.editorRightClickMenu != null) { gui.editorRightClickMenu = null; } else { Editor.closeEditor(); } break;
			//case 2: if (editor.Editor.insideEditor) { Editor.setPosition1(getWorldCoordsAtMouseLocation(mouseInput.mX, mouseInput.mY)); } break;
			//case 3: if (editor.Editor.insideEditor) { Editor.setPosition2(getWorldCoordsAtMouseLocation(mouseInput.mX, mouseInput.mY)); } break;
			case 17: Editor.setCenter(Editor.getCenter().getX(), Editor.getCenter().getY(), Editor.getCenter().getZ() - 0.5); break;
			case 30: Editor.setCenter(Editor.getCenter().getX() - 0.5, Editor.getCenter().getY(), Editor.getCenter().getZ()); break;
			case 31: Editor.setCenter(Editor.getCenter().getX(), Editor.getCenter().getY(), Editor.getCenter().getZ() + 0.5); break;
			case 32: Editor.setCenter(Editor.getCenter().getX() + 0.5, Editor.getCenter().getY(), Editor.getCenter().getZ()); break;
			case 35: Editor.setCenter((int) Math.floor(mc.thePlayer.posX), (int) Math.floor(mc.thePlayer.posY), (int) Math.floor(mc.thePlayer.posZ)); break;
			}
		}
	}
	
	public void handleKeyRelease(char typedChar, int keyCode) {
		
	}
	
	private void createMapImage() {
		try {
			Vector3DInt center = new Vector3DInt(Editor.getCenter());
			if (Editor.getRenderVertical()) {
				if (PlayerFacing.isXFacing()) {
					for (int x = center.getX() - 75; x < center.getX() + 76; x++) {
						for (int y = center.getY() - 75; y < center.getY() + 76; y++) {
							BlockPos pos = new BlockPos(x, y, center.getZ());
							IBlockState state = mc.theWorld.getBlockState(pos);
							int color = 0;
							if (WorldHelper.checkBlockForMapDraw(pos, state)) {
								color = WorldHelper.getCorrectMapColor(pos, state);
							}
							Editor.imageHandler2D.GBI().setRGB(x - center.getX() + 75, y - center.getY() + 75, color);					
						}
					}
				} else {
					for (int z = center.getZ() - 75; z < center.getZ() + 76; z++) {
						for (int y = center.getY() - 75; y < center.getY() + 76; y++) {
							BlockPos pos = new BlockPos(center.getX(), y, z);
							IBlockState state = mc.theWorld.getBlockState(pos);
							int color = 0;
							if (WorldHelper.checkBlockForMapDraw(pos, state)) {
								color = WorldHelper.getCorrectMapColor(pos, state);
							}
							Editor.imageHandler2D.GBI().setRGB(z - center.getZ() + 75, y - center.getY() + 75, color);				
						}
					}
				}
			} else {
				int w = Editor.imageHandler2D.getTextureWidth() / 2;
				int mWidth = (int) Math.ceil(w / (gui.zoomXHigh / 3 / w));
				for (int z = center.getZ() - mWidth; z < center.getZ() + mWidth + 1; z++) {
					for (int x = center.getX() - mWidth; x < center.getX() + mWidth + 1; x++) {
						BlockPos pos = new BlockPos(x, center.getY(), z);
						IBlockState state = mc.theWorld.getBlockState(pos);
						int color = 0;
						if (Editor.renderBiomeMap) {
							pos = new BlockPos(x, 0, z);
							color = mc.theWorld.getBiomeGenForCoords(pos).color;
						} else {
							if (WorldHelper.checkBlockForMapDraw(pos, state)) {
								color = WorldHelper.getCorrectMapColor(pos, state);
							}
						}
						Editor.imageHandler2D.GBI().setRGB(x - center.getX() + w, z - center.getZ() + w, color);
					}
				}
				Editor.imageHandler2D.GBI().setRGB(w, w, 0xFF0000);
			}
			Editor.imageHandler2D.updateTextureData(Editor.imageHandler2D.GBI());
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private void drawSelectionBox() {
		try {
			if (Editor.getSelectinPos1() != null && Editor.getSelectinPos2() != null) {
				int width = Editor.getSelectinPos2().getObject() - Editor.getSelectinPos1().getObject();
				int height = Editor.getSelectinPos2().getValue() - Editor.getSelectinPos1().getValue();
				int x = Editor.getSelectinPos1().getObject();
				int y = Editor.getSelectinPos1().getValue();
				if (width < 0) {
					x = Editor.getSelectinPos2().getObject();
					width *= -1;
				}
				if (height < 0) {
					y = Editor.getSelectinPos2().getValue();
					height *= -1;
				}
				
				mc.renderEngine.bindTexture(Resources.editorSelectionBase);
				gui.drawTexturedModalRect(x, y, 1, 1, width, 1);
				gui.drawTexturedModalRect(x, y, 1, 1, 1, height);
				gui.drawTexturedModalRect(x, y + height, 1, 1, width + 1, 1);
				gui.drawTexturedModalRect(x + width, y, 1, 1, 1, height + 1);
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public StorageBox<Integer, Integer> getMouseCoordsAtWorldLocation(int mX, int mY) {
		if (Editor.insideEditor && !Editor.getRender3D()) {
			int posX = mX - gui.startX;
			int posY = mY - gui.startY - 2;
			
			
			//System.out.println(ratioX + " " + ratioY);
		}
		return new StorageBox(mX, mY);
	}
	
	public Vector3DInt getWorldCoordsAtMouseLocation(int mX, int mY) {
		if (Editor.insideEditor && !Editor.getRender3D()) {
			int posX = Mouse.getX() - gui.startX * 2;
			int posY = (Display.getHeight() - Mouse.getY()) - gui.startY * 2 - 8;
			
			EGui.drawString(posX + " " + posY, 50, 360, 0xffffff);
			
			double ratioX = ((posX * Editor.imageHandler2D.GBI().getWidth() / 2) / (gui.drawWidth * Editor.getZoomScale()));
			double ratioY = ((posY * Editor.imageHandler2D.GBI().getHeight() / 2) / (gui.drawHeight * Editor.getZoomScale()));
			
			EGui.drawString(ratioX + " " + ratioY, 50, 370, 0xffffff);
			
			double unParsedX = Editor.getCenter().getX();
			double unParsedZ = Editor.getCenter().getZ();
			
			double playerX = unParsedX < 0 ? Math.floor(unParsedX) + 0.5: Math.ceil(unParsedX) - 0.5;
			double playerZ = unParsedZ < 0 ? Math.floor(unParsedZ) + 0.5: Math.ceil(unParsedZ) - 0.5;
			
			EGui.drawString(playerX + " " + playerZ, 50, 380, 0xff00ff);
			
			double imgEdgeL = (playerX - ((Editor.imageHandler2D.GBI().getWidth() / 2) / Editor.getZoomScale()));
			double imgEdgeT = (playerZ - ((Editor.imageHandler2D.GBI().getHeight() / 2) / Editor.getZoomScale()));
			
			EGui.drawString(imgEdgeL + " " + imgEdgeT, 50, 390, 0xff00ff);
			
			double returnX = imgEdgeL + ratioX;
			double returnZ = imgEdgeT + ratioY;
			
			returnX = returnX < 0 ? Math.floor(returnX) : Math.ceil(returnX);
			returnZ = returnZ < 0 ? Math.floor(returnZ) : Math.ceil(returnZ);
			
			if (Editor.getRenderVertical()) {
				return new Vector3DInt(returnZ, returnX, Editor.getCenter().getZ());
			}
			return new Vector3DInt(returnX, Editor.getCenter().getY(), returnZ);
		}
		return null;
	}
}
