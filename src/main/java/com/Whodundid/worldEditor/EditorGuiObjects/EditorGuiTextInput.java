package com.Whodundid.worldEditor.EditorGuiObjects;

import java.awt.Color;
import java.text.DecimalFormat;
import com.Whodundid.worldEditor.Editor;
import com.Whodundid.worldEditor.EditorUtil.EditorGuiBase;
import com.Whodundid.worldEditor.EditorUtil.EditorSet;
import com.Whodundid.worldEditor.EditorUtil.EditorTools;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiTextField;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.miscUtil.NumberUtil;
import com.Whodundid.main.util.miscUtil.Resources;
import com.Whodundid.main.util.miscUtil.WorldHelper;
import com.Whodundid.main.util.storageUtil.Vector3D;
import com.Whodundid.main.util.storageUtil.Vector3DInt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

//Last edited: Nov 25, 2018
//First Added: unknown
//Author: Hunter Bragg

public class EditorGuiTextInput extends EditorGuiObject {
	
	public EGuiTextField xCoord, yCoord, zCoord, idInput;
	public EGuiButton tpTo, setButton;
	
	public EditorGuiTextInput(EditorGuiBase guiIn) {
		guiInstance = guiIn;
		init(guiIn, guiInstance.wPos - 474, guiInstance.hPos - 117, 240, 140);
	}
	
	@Override
	public void initObjects() {
		xCoord = new EGuiTextField(this, guiInstance.wPos - 442, guiInstance.hPos - 112, 40, 20);
		yCoord = new EGuiTextField(this, guiInstance.wPos - 397, guiInstance.hPos - 112, 40, 20);
		zCoord = new EGuiTextField(this, guiInstance.wPos - 352, guiInstance.hPos - 112, 40, 20);
		idInput = new EGuiTextField(this, guiInstance.wPos - 420, guiInstance.hPos - 10, 60, 20);
		
		tpTo = new EGuiButton(this, guiInstance.wPos - 308, guiInstance.hPos - 112, 70, 20, "Tp to");
		setButton = new EGuiButton(this, guiInstance.wPos - 339, guiInstance.hPos - 10, 90, 20, "Set Region");
		
		resetAllBoxesText();
		
		addObject(xCoord, yCoord, zCoord, idInput, tpTo, setButton);
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		mc.renderEngine.bindTexture(Resources.guiRCMBase);
		guiInstance.drawModalRectWithCustomSizedTexture(startX, startY, 0, 0, width, height, width, height);
		drawText(mX, mY, ticks);
		//System.out.println(mX + " " + mY);
		//guiInstance.drawString(mX + " " + mY, 50, 380, 0xffffff);
		//guiInstance.drawString("" + CursorHelper.getExactMouseLocationMC(), 50, 400, 0xffffff);
		super.drawObject(mX, mY, ticks);
	}
	
	private void drawText(int mX, int mY, float ticks) {
		//String mousePos = "mX: " + editor.mouseInput.mX + ", mY: " + editor.mouseInput.mY;
		String worldPos = "", blockStats = "", biome = "";
		Vector3DInt p = new Vector3DInt();
		
		if (Editor.insideEditor) {
			if (!Editor.getRender3D()) {
				if (Editor.getSelectedTool().equals(EditorTools.SELECT)) {
					if (!Editor.render3D) {
						p = Editor.get2D().getWorldCoordsAtMouseLocation(mX, mY);
					}
					IBlockState state = WorldHelper.getBlockState(p);
					String x = new DecimalFormat("0.00").format(p.getX());
					String y = new DecimalFormat("0.00").format(p.getY());
					String z = new DecimalFormat("0.00").format(p.getZ());
					worldPos = "x: " + p.x + ", y: " + p.y + ", z: " + p.z;
					blockStats = state.getBlock().getLocalizedName() + " (" + WorldHelper.getBlockID(state.getBlock()) + ":" + state.getBlock().getMetaFromState(state) + ")";
					biome = "Biome: " + mc.theWorld.getBiomeGenForCoords(new BlockPos(p.x, p.y, p.z)).biomeName;
				}
			} else {
				//if (editor.renderer.selectedBlock != null) {
				//	p = editor.renderer.selectedBlock.position;
				//	IBlockState state = WorldHelper.getBlockState(p.x, p.y, p.z);
				//	worldPos = "x: " + p.x + ", y: " + p.y + ", z: " + p.z;
				//	blockStats = state.getBlock().getLocalizedName() + " (" + WorldHelper.getBlockID(state.getBlock()) + ":" + state.getBlock().getMetaFromState(state) + ")";
				//	biome = "Biome: " + mc.theWorld.getBiomeGenForCoords(new BlockPos(p.x, p.z, p.y)).biomeName;
				//}
			}
		}
		
		guiInstance.drawString(mc.fontRendererObj, "XYZ:", guiInstance.wPos - 467, guiInstance.hPos - 106, Color.GREEN.getRGB());
		
		//guiInstance.drawString(mc.fontRendererObj, mousePos, guiInstance.wPos - 467, guiInstance.hPos - 85, Color.GREEN.getRGB());
		guiInstance.drawString(mc.fontRendererObj, worldPos, guiInstance.wPos - 467, guiInstance.hPos - 75, /*(Editor.getCenter().compare(p)) ? 0x00BEFF :*/ 0x00FF00);
		guiInstance.drawString(mc.fontRendererObj, blockStats, guiInstance.wPos - 467, guiInstance.hPos - 65, 0x00FF00);
		guiInstance.drawString(mc.fontRendererObj, biome, guiInstance.wPos - 467, guiInstance.hPos - 55, Color.GREEN.getRGB());
		
		guiInstance.drawString(mc.fontRendererObj, "Zoom: x" + Editor.getZoomScale(), guiInstance.wPos - 460, guiInstance.hPos - 137, 0x00ff00);
		
		guiInstance.drawString(mc.fontRendererObj, "pos1: " + Editor.getPos1(), guiInstance.wPos - 460, guiInstance.hPos + 37, 0x00ff00);
		guiInstance.drawString(mc.fontRendererObj, "pos2: " + Editor.getPos2(), guiInstance.wPos - 460, guiInstance.hPos + 47, 0x00ff00);
	}
	
	private void parseTextBoxesInput(int input) {
		if (input < 3) {
			if (NumberUtil.isInteger(xCoord.getText()) && NumberUtil.isInteger(yCoord.getText()) && NumberUtil.isInteger(zCoord.getText())) {
				double passX, passY, passZ;
				passX = (!xCoord.getText().isEmpty()) ? Double.parseDouble(xCoord.getText()) : Editor.getCenter().x;
				passY = (!yCoord.getText().isEmpty()) ? Double.parseDouble(yCoord.getText()) : Editor.getCenter().y;
				passZ = (!zCoord.getText().isEmpty()) ? Double.parseDouble(zCoord.getText()) : Editor.getCenter().z;
				Editor.setCenter(passX, passY, passZ);
			}
			resetAllBoxesText();
		}
	}
	
	public void resetAllBoxesText() {
		Vector3D center = Editor.getCenter();
		xCoord.setText(String.valueOf(center.x > 0 ? Math.floor(center.x) : Math.ceil(center.x)));
		yCoord.setText(String.valueOf(center.y > 0 ? Math.floor(center.y) : Math.ceil(center.y)));
		zCoord.setText(String.valueOf(center.z > 0 ? Math.floor(center.z) : Math.ceil(center.z)));
		
		xCoord.setSelectionPos(xCoord.getText().length());
		yCoord.setSelectionPos(yCoord.getText().length());
		zCoord.setSelectionPos(zCoord.getText().length());
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		try {
			if (object.equals(tpTo)) {
				double x = Double.parseDouble(xCoord.getText());
				double y = Double.parseDouble(yCoord.getText());
				double z = Double.parseDouble(zCoord.getText());
				x = x < 0 ? x - 0.5 : x + 0.5;
				z = z < 0 ? z - 0.5 : z + 0.5;
				mc.thePlayer.sendChatMessage("/tp " + x + " " + (y + 1) + " " + z);
			}
			if (object.equals(setButton)) {
				EditorSet.set((Editor) RegisteredSubMods.getMod(SubModType.WORLDEDITOR));
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
}
