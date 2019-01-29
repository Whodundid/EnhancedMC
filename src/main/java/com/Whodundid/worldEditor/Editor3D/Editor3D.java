package com.Whodundid.worldEditor.Editor3D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import com.Whodundid.worldEditor.Editor;
import com.Whodundid.worldEditor.Editor3D.chunkUtil.EditorChunkCache;
import com.Whodundid.worldEditor.Editor3D.renderingUtil.Calculator;
import com.Whodundid.worldEditor.Editor3D.renderingUtil.DPolygon;
import com.Whodundid.worldEditor.EditorUtil.EditorGuiBase;
import com.Whodundid.debug.DebugFunctions;
import com.Whodundid.debug.IDebugCommand;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.util.enhancedGui.guiUtil.EGui;
import com.Whodundid.main.util.playerUtil.PlayerFacing.Direction;
import com.Whodundid.main.util.storageUtil.StorageBox;
import com.Whodundid.main.util.storageUtil.Vector3D;
import com.Whodundid.main.util.storageUtil.Vector3DInt;

//Last edited: 11-25-18
//First Added: 9-13-18
//Author: Hunter Bragg

public class Editor3D {
	
	protected Minecraft mc = MainMod.getMC();
	public EditorGuiBase gui;
	private Graphics2D graphics, hiResGraphics;
	public ArrayList<DPolygon> polyList;
	private long startTime = System.currentTimeMillis();
	private boolean wasBlockSelected = false;
	public boolean[] keys = new boolean[10];
	private Vector3DInt lastCamPos;
	private int[] newOrder;
	public double maxFOV = 100, minFOV = 2500;
	public double yawSpeed = 3200, pitchSpeed = 5700;
	protected boolean redrawRequested = false;
	protected boolean anyMovementKeyIsPressed = false;
	protected StorageBox<Integer, Integer> mouseGrabPos = new StorageBox();
	
	public void setGui(EditorGuiBase guiIn) { gui = guiIn; }
	
	public void init() {
		graphics = Editor.imageHandler3D.GBI().createGraphics();
		hiResGraphics = Editor.imageHandler3DHiRes.GBI().createGraphics();
		polyList = new ArrayList();
		lastCamPos = new Vector3DInt();
		if (Editor.render3D) { open(); }
	}
	
	public void onGuiClosed() {
		if (graphics != null) { graphics.dispose(); }
		if (hiResGraphics != null) { hiResGraphics.dispose(); }
	}
	
	public void draw() {
		gui.drawRect(0, 0, 200, 55, 0xff222222);
		EGui.drawString("FOV: " + Editor.fieldOfView, 3, 3, 0x00FF00);
		String degree = new DecimalFormat("0.000").format(getFacingDegree());
		String pitch = new DecimalFormat("0.000").format(Editor.pitch);
		EGui.drawString("Facing pitch: " + pitch, 3, 13, 0x00FF00);
		EGui.drawString("Facing yaw: " + degree, 3, 23, 0x00FF00);
		EGui.drawString("Facing direction: " + getFacingDir(), 3, 33, 0x00FF00);
		String xPos = new DecimalFormat("0.000").format(Editor.getCamPos().x);
		String yPos = new DecimalFormat("0.000").format(Editor.getCamPos().y);
		String zPos = new DecimalFormat("0.000").format(Editor.getCamPos().z);
		EGui.drawString("x:" + xPos + " y:" + yPos + " z:" + zPos, 3, 43, 0x00FF00);
		
		if (Editor.chunkCache != null && hiResGraphics != null && graphics != null) {
			try {
				update();
				if (Editor.getRenderHiRes()) {
					hiResGraphics.setColor(new Color(140, 180, 200));
					hiResGraphics.fillRect(0, 0, 906, 906);
					
					Calculator.SetPrederterminedInfo();
					
					for (DPolygon d : Editor.chunkCache.getWorldVertexData()) { d.updatePolygon(); }
					
					int q = 0;
					if (q == 0) {
						setVertexDataRenderOrder();
						for (int i = 0; i < newOrder.length; i++) {
							Editor.chunkCache.getWorldVertexData().get(newOrder[i]).dPoly.drawPolygon(hiResGraphics);
						}
					} else {
						for (int i = 0; i < Editor.chunkCache.getWorldVertexData().size(); i++) {
							Editor.chunkCache.getWorldVertexData().get(i).dPoly.drawPolygon(hiResGraphics);
						}
					}
					Editor.imageHandler3DHiRes.updateTextureData(Editor.imageHandler3DHiRes.GBI());
				} else {
					graphics.setColor(new Color(140, 180, 200));
					graphics.fillRect(0, 0, 906, 906);

					Calculator.SetPrederterminedInfo();
					
					for (DPolygon d : Editor.chunkCache.getWorldVertexData()) { d.updatePolygon(); }
					
					setVertexDataRenderOrder();
					for (int i = 0; i < newOrder.length; i++) {
						Editor.chunkCache.getWorldVertexData().get(newOrder[i]).dPoly.drawPolygon(graphics);
					}
					Editor.imageHandler3D.updateTextureData(Editor.imageHandler3D.GBI());
				}
			} catch (Exception e) { e.printStackTrace(); }
		}
		if (wasBlockSelected && Editor.selectedBlock == null) {
			wasBlockSelected = false;
			requestRedraw();
		}
	}
	
	public void parseMousePosition(int mX, int mY) {
		switch (Editor.getSelectedTool()) {
		case SELECT:
			if (!gui.isThereRCM()) {
				if (Editor.insideEditor) {
					setPolygonOver(mX - gui.startX, mY - gui.startY); 
				}
			}
			break;
		case PAN:
		default: break;
		}
	}
	
	public void handleMousePress(int mX, int mY, int button) {
		if (Editor.insideEditor) {
			if (button == 0) {
				switch (Editor.getSelectedTool()) {
				case SELECT:
					if (getSelectedBlockPosition() != null) {
						Vector3DInt pos = getSelectedBlockPosition();
						Editor.setPosition1(new Vector3DInt(pos.x, pos.z, pos.y));
					}
					break;
				case PAN:
					mouseGrabPos.setValues(mX, mY);
				default: break;
				}
				if (gui.isThereRCM()) { gui.removeRCM(); }
			} else if (button == 1) {
				if (!gui.isThereRCM()) { gui.addRCM(mX, mY); }
				else { gui.removeRCM(); }
			}
		} else {
			if (gui.isThereRCM()) { gui.removeRCM(); }
		}
	}
	
	public void handleMouseRelease(int mX, int mY, int button) {
		if (Editor.insideEditor) {
			if (button == 0) {
				switch (Editor.getSelectedTool()) {
				case SELECT:
					if (getSelectedBlockPosition() != null) {
						Vector3DInt pos = getSelectedBlockPosition();
						Editor.setPosition2(new Vector3DInt(pos.x, pos.z, pos.y));
					}
					break;
				default: break;
				}
			}
		}
	}
	
	public void handleMouseDrag(int mX, int mY, int button, long timeSinceLastClick) {
		if (Editor.insideEditor) {
			if (button == 0) {
				switch (Editor.getSelectedTool()) {
				case PAN:
					if (mouseGrabPos != null && mouseGrabPos.getObject() != null && mouseGrabPos.getValue() != null) {
						int difX = mX - mouseGrabPos.getObject();
						int difY = mY - mouseGrabPos.getValue();
						
						if (gui.isShiftKeyDown()) {
							Editor.yaw += difX / yawSpeed * 22.30;
						} else if (gui.isCtrlKeyDown()) {
							Editor.pitch -= difY / pitchSpeed * 30;
						} else {
							Editor.pitch -= difY / pitchSpeed * 30;
							Editor.yaw += difX / yawSpeed * 22.30;
						}
						
						if (Editor.pitch > 0.999) { Editor.pitch = 0.999; }
						if (Editor.pitch < -0.999) { Editor.pitch = -0.999; }
						
						updateView();
						mouseGrabPos.setValues(mX, mY);
					}
					break;
				default: break;
				}
			}
		}
	}
	
	public void handleMouseScroll(int change) {
		if (change > 0) {
			if (Editor.fieldOfView < minFOV) { Editor.fieldOfView += 25 * change; } 
			else { Editor.fieldOfView = minFOV; }
			//requestRedraw();
		} else if (change < 0) {
			if (Editor.fieldOfView > maxFOV) { Editor.fieldOfView += 25 * change; } 
			else { Editor.fieldOfView = maxFOV; }
			//requestRedraw();
		}
	}
	
	public void handleKeyPress(char typedChar, int keyCode) {
		switch (keyCode) {
		//case 1: Editor.closeEditor(); break;
		
		case 17: keys[0] = true; break;
		case 30: keys[1] = true; break;
		case 31: keys[2] = true; break;
		case 32: keys[3] = true; break;
		case 57: keys[4] = true; break;
		case 42: keys[5] = true; break;
		case 200: keys[6] = true; break;
		case 203: keys[7] = true; break;
		case 208: keys[8] = true; break;
		case 205: keys[9] = true; break;
		
		case 39: open(); break;
		
		case 24: Editor.setDraw3DOutlines(!Editor.drawOutlines); break; //requestRedraw();
		case 33: Editor.setMoveWithWorld(!Editor.moveWithWorld); break;
		case 37:
			moveTo(new Vector3DInt(mc.thePlayer.getPosition().getX(), mc.thePlayer.getPosition().getY() + 2, mc.thePlayer.getPosition().getZ()));
			open();
			break;
		case 41: DebugFunctions.runDebugFunction(IDebugCommand.DEBUG_3); break;
		default: break;
		}
		
		//guiInstance.editorToolList.keyPressed(typedChar, keyCode);
	}
	
	public void handleKeyRelease(char typedChar, int keyCode) {
		switch (keyCode) {
		case 17: keys[0] = false; break;
		case 30: keys[1] = false; break;
		case 31: keys[2] = false; break;
		case 32: keys[3] = false; break;
		case 57: keys[4] = false; break;
		case 42: keys[5] = false; break;
		case 200: keys[6] = false; break;
		case 203: keys[7] = false; break;
		case 208: keys[8] = false; break;
		case 205: keys[9] = false; break;
		default: break;
		}
	}
	
	public void reset() {
		if (Editor.chunkCache != null) { Editor.chunkCache.destroyChunks(); }
		Editor.selectedBlock = null;
		moveTo(0, 0, 0);
	}
	
	public void open() {
		Editor.chunkCache = new EditorChunkCache();
		Editor.chunkCache.AssembleCache();
		requestRedraw();
	}
	
	public void update() {
		if (System.currentTimeMillis() - startTime >= 50) {
			checkMovementKeys();
			cameraMovement();
			startTime = System.currentTimeMillis();
		}
	}
	
	public void setPolygonOver(int mX, int mY) {
		if (Editor.chunkCache != null) {
			try {
				Editor.selectedBlock = null;
				if (newOrder != null) {
					for (int i = newOrder.length - 1; i >= 0; i--) {
						DPolygon p = Editor.chunkCache.getWorldVertexData().get(newOrder[i]);
						if (p.dPoly.visible && p.draw && p.dPoly.MouseOver(mX, mY)) {
							//System.out.println(polyList.get(i).AvgDist);
							Editor.selectedBlock = Editor.chunkCache.getWorldVertexData().get(newOrder[i]).parentBlock;
							wasBlockSelected = true;
							requestRedraw();
							break;
						}
					}
				}
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	private void setVertexDataRenderOrder() {
		int vertexes = Editor.chunkCache.getWorldVertexData().size();
		double[] polys = new double[vertexes];
		newOrder = new int[vertexes];

		for (int i = 0; i < vertexes; i++) {
			polys[i] = Editor.chunkCache.getWorldVertexData().get(i).AvgDist;
			newOrder[i] = i;
		}
		
	    double temp;
	    int tempr;
		for (int a = 0; a < polys.length - 1; a++) {
			for (int b = 0; b < polys.length - 1; b++) {
				if (polys[b] < polys[b + 1]) {
					temp = polys[b];
					tempr = newOrder[b];
					newOrder[b] = newOrder[b + 1];
					polys[b] = polys[b + 1];
					   
					newOrder[b + 1] = tempr;
					polys[b + 1] = temp;
				}
				
			}
		}
	}
	
	public void checkMovementKeys() {
		anyMovementKeyIsPressed = false;
		for (int i = 0; i < keys.length; i++) {
			if (keys[i]) { 
				anyMovementKeyIsPressed = true;
				requestRedraw();
				break;
			}
		}
	}
	
	private void cameraMovement() {
		if (anyMovementKeyIsPressed) {
			Editor.selectedBlock = null;
			Vector3D viewVector = Vector3D.difference(Editor.camLook, Editor.camPos);
			Vector3D verticalVector = new Vector3D(0, 0, 1);
			Vector3D sideViewVector = viewVector.crossProduct(verticalVector);
			double xMove = 0, yMove = 0, zMove = 0;
			double moveSpeed = Editor.getMovementSpeed();
			
			if (keys[4]) { zMove = Editor.camLook.z; }
			if (keys[5]) { zMove = -Editor.camLook.z; }
			
			if (keys[0]) {
				xMove += viewVector.x;
				yMove += viewVector.y;
			}

			if (keys[1]) {
				xMove += sideViewVector.x;
				yMove += sideViewVector.y;
			}
			
			if (keys[2]) {
				xMove -= viewVector.getX();
				yMove -= viewVector.getY();
			}

			if (keys[3]) {
				xMove -= sideViewVector.getX();
				yMove -= sideViewVector.getY();
			}
			
			if (keys[6]) {
				Editor.pitch += 150 / pitchSpeed;
				if (Editor.pitch > 0.99999) { Editor.pitch = 0.99999; }
			}
			if (keys[8]) {
				Editor.pitch -= 150 / pitchSpeed;
				if (Editor.pitch < -0.99999) { Editor.pitch = -0.99999; }
			}
			if (keys[7]) { Editor.yaw -= 150 / yawSpeed; }
			if (keys[9]) { Editor.yaw += 150 / yawSpeed; }
			
			Vector3D moveVector = new Vector3D(xMove, yMove, zMove).normalize();
			Vector3D camPos = Editor.camPos;
			moveTo(camPos.x + moveVector.x * moveSpeed, camPos.y + moveVector.y * moveSpeed, camPos.z + moveVector.z * moveSpeed);
		}
	}
	
	public void moveTo(Vector3D vecIn) { moveTo(vecIn.x, vecIn.z, vecIn.y); }
	public void moveTo(Vector3DInt vecIn) { moveTo(vecIn.x, vecIn.z, vecIn.y); }

	public void moveTo(double x, double y, double z) {
		Editor.camPos.x = x;
		Editor.camPos.y = y;
		Editor.camPos.z = z > 0 ? z : 0.5;
		if (lastCamPos.x != (int) (Math.floor(x)) || lastCamPos.y != (int) (Math.floor(y)) || lastCamPos.z != (int) (Math.floor(z))) {
			lastCamPos.x = (int) Math.floor(x);
			lastCamPos.y = (int) Math.floor(y);
			lastCamPos.z = (int) Math.floor(z);
			Editor.setCenter(new Vector3D(Editor.camPos.x, Editor.camPos.z, Editor.camPos.y));
			//if (Editor.getGui().editorTextInput != null ) { Editor.getGui().editorTextInput.resetAllBoxesText(); }
			if (Editor.moveWithWorld) { }
		}
		updateView();
	}
	
	public void updateView() {
		double r = Math.sqrt(1 - (Editor.pitch * Editor.pitch));
		Editor.camLook.x = Editor.camPos.x + r * Math.cos(Editor.yaw);
		Editor.camLook.y = Editor.camPos.y + r * Math.sin(Editor.yaw);
		Editor.camLook.z = Editor.camPos.z + Editor.pitch;
		requestRedraw();
	}
	
	public Vector3DInt getSelectedBlockPosition() {
		Vector3DInt pos = null;
		if (Editor.selectedBlock != null) { pos = Editor.selectedBlock.position; }
		if (pos != null) { return new Vector3DInt(pos.x, pos.z, pos.y); }
		return null;
	}
	
	public double getFacingDegree() { return Math.abs((Editor.yaw * (180 / Math.PI) % 360)); }
	
	public Direction getFacingDir() {
		double dir = getFacingDegree();
		if (0 <= dir && dir < 22.5) { return Direction.W; } 
        else if (22.5 <= dir && dir < 67.5) { return Direction.SW; }
        else if (67.5 <= dir && dir < 112.5) { return Direction.S; }
        else if (112.5 <= dir && dir < 157.5) { return Direction.SE; }
        else if (157.5 <= dir && dir < 202.5) { return Direction.E; }
        else if (202.5 <= dir && dir < 247.5) { return Direction.NE; }
        else if (247.5 <= dir && dir < 292.5) { return Direction.N; }
        else if (292.5 <= dir && dir < 337.5) { return Direction.NW; }
        else if (337.5 <= dir && dir < 360.0) { return Direction.W; }
        else { return Direction.OUT; }
	}
	
	public void requestRedraw() { redrawRequested = true; }
}
