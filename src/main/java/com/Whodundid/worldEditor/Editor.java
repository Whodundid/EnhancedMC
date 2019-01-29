package com.Whodundid.worldEditor;

import com.Whodundid.worldEditor.Editor2D.Editor2D;
import com.Whodundid.worldEditor.Editor3D.Editor3D;
import com.Whodundid.worldEditor.Editor3D.block.EditorBlock;
import com.Whodundid.worldEditor.Editor3D.chunkUtil.EditorChunkCache;
import com.Whodundid.worldEditor.EditorScripts.EditorScript_Paste;
import com.Whodundid.worldEditor.EditorUtil.EditorGuiBase;
import com.Whodundid.worldEditor.EditorUtil.EditorTools;
import java.awt.image.BufferedImage;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.miscUtil.CursorHelper;
import com.Whodundid.main.util.playerUtil.PlayerTraits;
import com.Whodundid.main.util.storageUtil.DynamicTextureHandler;
import com.Whodundid.main.util.storageUtil.StorageBox;
import com.Whodundid.main.util.storageUtil.Vector3D;
import com.Whodundid.main.util.storageUtil.Vector3DInt;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//Last edited: 11-25-18
//First Added: 4-18-18
//Author: Hunter Bragg

public class Editor extends SubMod {
	
	static Minecraft mc = MainMod.getMC();
	private static EditorGuiBase guiInstance;
	public static boolean editorOpen = false;
	public static EditorBlock selectedBlock = null;
	public static Vector3DInt copyStartPosition;
	public static Vector3D playerCopyPosition;
	public static Vector3DInt pos1 = new Vector3DInt(), pos2 = new Vector3DInt();
	public static EditorTools selectedTool = EditorTools.SELECT;
	public static boolean insideEditor = false;
	public static boolean render3D = false;
	public static boolean renderVertical = false;
	public static boolean renderVertNS = true;
	public static boolean renderBiomeMap = false;
	public static double zoom = 0.5;
	public static StorageBox<Integer, Integer> selectionPos1, selectionPos2;
	public static Vector3D center = new Vector3D();
	public static boolean firstCenterSet = false;
	public static double pitch = 0, yaw = 0;
	public static double movementSpeed = 0.5, fieldOfView = 525;
	public static boolean moveWithWorld = true;
	public static boolean drawOutlines = true;
	public static boolean render3DHighRes = true;
	public static Vector3D camPos = new Vector3D(), camLook = new Vector3D(0, 0, 0), light = new Vector3D(0, 0, -200);
	public static EditorChunkCache chunkCache;
	public static int drawWidth = 453;
	public static int drawHeight = 453;
	public static int imgWidth = 453;
	public static int imgHeight = 453;
	public static DynamicTextureHandler imageHandler2D, imageHandler3D, imageHandler3DHiRes, imageHandlerBorder;
	public static Editor2D editor2D;
	public static Editor3D editor3D;
	
	public Editor() {
		super(SubModType.WORLDEDITOR);
		dependencies.add(SubModType.SCRIPTS);
	}
	
	@Override
	public void init() {
		imageHandler2D = new DynamicTextureHandler(mc.getTextureManager(), new BufferedImage(301, 301, BufferedImage.TYPE_INT_RGB));
		imageHandler3D = new DynamicTextureHandler(mc.getTextureManager(), new BufferedImage(453, 453, BufferedImage.TYPE_INT_RGB));
		imageHandler3DHiRes = new DynamicTextureHandler(mc.getTextureManager(), new BufferedImage(906, 906, BufferedImage.TYPE_INT_RGB));
		imageHandlerBorder = new DynamicTextureHandler(mc.getTextureManager(), new BufferedImage(153, 153, BufferedImage.TYPE_INT_RGB));
		editor2D = new Editor2D();
		editor3D = new Editor3D();
	}
	
	@Override
	public void eventClientTick(TickEvent.ClientTickEvent e) {
		editorOpen = mc.currentScreen instanceof EditorGuiBase;
	}
	
	@Override
	public void eventWorldUnload(WorldEvent.Unload e) {
		editorOpen = false;
		guiInstance = null;
		resetFirstCenterSet();
	}
	
	@Override
	public EnhancedGui getMainGui(boolean setPosition, StorageBox<Integer, Integer> pos, EnhancedGui oldGui) {
		return new EditorGuiBase();
	}
	
	public synchronized static void openEditor() {
		mc.displayGuiScreen(guiInstance = new EditorGuiBase());
		editorOpen = true;
	}
	
	public synchronized static void closeEditor() {
		editorOpen = false;
		guiInstance = null;
		CursorHelper.setCursorVisible();
	}
	
	public static void setPosition1(Vector3DInt positionIn) {
		pos1 = positionIn;
		if (pos1 != null) {
			mc.thePlayer.sendChatMessage("//pos1 " + pos1.x + "," + pos1.y + "," + pos1.z);
		}
	}
	
	public static void setPosition2(Vector3DInt positionIn) {
		pos2 = positionIn;
		if (pos2 != null) {
			mc.thePlayer.sendChatMessage("//pos2 " + pos2.x + "," + pos2.y + "," + pos2.z);
		}
	}
	
	public static void copyRegion() {
		mc.thePlayer.sendChatMessage("//copy");
		setCopyPosition(new Vector3DInt(pos1));
		setPlayerCopyPosition(new Vector3D(PlayerTraits.getPlayerLocation()));
	}
	
	public static void pasteRegion() {
		try {
			new EditorScript_Paste().startScript(null);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	//Editor setters
	public static void setCopyPosition(Vector3DInt vecIn) { copyStartPosition = vecIn; }
	public static void setPlayerCopyPosition(Vector3D vecIn) { playerCopyPosition = vecIn; }
	public static void setSelectedTool(EditorTools toolIn) { selectedTool = toolIn; }
	public static void setRender3D(boolean val) { render3D = val; }
	public static void setRenderVertical(boolean val) { renderVertical = val; }
	public static void setRenderVertNS(boolean val) { renderVertNS = val; }
	public static void setRenderBiomeMap(boolean val) { renderBiomeMap = val; }
	public static void setZoomScale(double zoomIn) { zoom = zoomIn; }
	public static void setSelectionPos1(StorageBox<Integer, Integer> posIn) { selectionPos1 = posIn; }
	public static void setSelectionPos2(StorageBox<Integer, Integer> posIn) { selectionPos2 = posIn; }
	public static void setCenter(int x, int y, int z) { setCenter(new Vector3D(x, y, z)); }
	public static void setCenter(double x, double y, double z) { setCenter(new Vector3D(x, y, z)); }
	public static void setCenter(Vec3 centerIn) { setCenter(new Vector3D(centerIn)); }
	public static void setCenter(Vec3i centerIn) { setCenter(new Vector3D(centerIn)); }
	public static void setCenter(Vector3D centerIn) { center = centerIn; }
	public static void setPitch(double pitchIn) { pitch = pitchIn; }
	public static void setYaw(double yawIn) { yaw = yawIn; }
	public static void setMovementSpeed(double speedIn) { movementSpeed = speedIn; }
	public static void setfieldOfView(double fieldOfViewIn) { fieldOfView = fieldOfViewIn; }
	public static void setMoveWithWorld(boolean val) { moveWithWorld = val; }
	public static void setDraw3DOutlines(boolean val) { drawOutlines = val; }
	public static void setRenderHiRes(boolean val) { render3DHighRes = val; }
	public static void setEditorGuiReference(EditorGuiBase guiIn) { editor2D.setGui(guiIn); editor3D.setGui(guiIn); }
		
	//Editor getters
	public static Vector3D getCamPos() { return new Vector3D(camPos.x, camPos.z, camPos.y); }
	public static Vector3DInt getCopyPosition() { return copyStartPosition; }
	public static Vector3D getPlayerCopyPosition() { return playerCopyPosition; }
	public static Vector3DInt getPos1() { return pos1; }
	public static Vector3DInt getPos2() { return pos2; }
	public static EditorTools getSelectedTool() { return selectedTool; }
	public static boolean getRender3D() { return render3D; }
	public static boolean getRenderVertical() { return renderVertical; }
	public static boolean getRenderVertNS() { return renderVertNS; }
	public static boolean getRenderBiomeMap() { return renderBiomeMap; }
	public static double getZoomScale() { return zoom; }
	public static StorageBox<Integer, Integer> getSelectinPos1() { return selectionPos1; }
	public static StorageBox<Integer, Integer> getSelectinPos2() { return selectionPos2; }
	public static Vector3D getCenter() { return center; }
	public static void resetFirstCenterSet() { firstCenterSet = false; }
	public static double getPitch() { return pitch; }
	public static double getYaw() { return yaw; }
	public static double getMovementSpeed() { return movementSpeed; }
	public static double getFieldOfView() { return fieldOfView; }
	public static boolean getMoveWithWorld() { return moveWithWorld; }
	public static boolean getDrawOutlines() { return drawOutlines; }
	public static boolean getRenderHiRes() { return render3DHighRes; }
	public static EditorChunkCache getChunkCache() { return chunkCache; }
	public static Vector3D getCamLook() { return camLook; }
	public static Vector3D getLight() { return light; }
	public static boolean isMouseInsideEditor() { return insideEditor; }
	public static boolean isEditorOpen() { return editorOpen; }
	public static EditorGuiBase getGuiInstance() { return guiInstance; }
	public static Editor2D get2D() { return editor2D; }
	public static Editor3D get3D() { return editor3D; }
	
	public static synchronized void reset() {
		editor2D = new Editor2D();
		editor3D = new Editor3D();
		if (editorOpen && guiInstance != null) { setEditorGuiReference(guiInstance); }
	}
}
