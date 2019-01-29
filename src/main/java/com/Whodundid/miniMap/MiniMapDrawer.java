package com.Whodundid.miniMap;

import com.Whodundid.main.MainMod;
import com.Whodundid.main.util.enhancedGui.guiUtil.EGui;
import com.Whodundid.main.util.miscUtil.Resources;
import com.Whodundid.main.util.playerUtil.PlayerFacing;
import com.Whodundid.main.util.playerUtil.PlayerTraits;
import com.Whodundid.main.util.storageUtil.Vector3D;
import com.Whodundid.worldEditor.Editor;
import java.text.DecimalFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class MiniMapDrawer extends EGui {
	
	static Minecraft mc = MainMod.getMC();
	protected MiniMap map;
	ScaledResolution res;
	int cornerMapX, cornerMapY;
	float xPos, yPos;
	
	public MiniMapDrawer(MiniMap mapIn) {
		map = mapIn;
	}
	
	public void draw() {
		res = new ScaledResolution(mc);
		try {
			if (!mc.gameSettings.showDebugInfo && !Editor.isEditorOpen()) {
				if (map.drawMap) { drawMap(); }
				if (!map.drawBig) {
					if (map.drawCoords) { drawCoords(); }
					if (map.drawMapDirections) { drawDirections(); }
					if (map.drawFacingDir) { drawFacingDir(); }
				}
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.renderEngine.bindTexture(icons);
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private void drawMap() {
		if (map.handler.getDynamicTexture() != null) {
			cornerMapX = res.getScaledWidth() / 2 - 174;
			cornerMapY = 20 - res.getScaledHeight() / 2;
			
			GlStateManager.enableRescaleNormal();
			GlStateManager.pushMatrix();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			mc.renderEngine.bindTexture(map.handler.getTextureLocation());
			
			if (map.drawBig) {
				xPos = res.getScaledWidth() / 2;
				yPos = res.getScaledHeight() / 2;
			} else {
				xPos = res.getScaledWidth() / 2 + cornerMapX + 75;
				yPos = res.getScaledHeight() / 2 + cornerMapY + 75;
			}
			
			GlStateManager.translate(xPos, yPos, 0);
			GlStateManager.rotate(180 + ((!map.drawVertical) ? -PlayerFacing.getDegreeFacingDir() : 0), 0, 0, 45);
			GlStateManager.translate(-xPos, -yPos, 0);
			
			//if (map.drawBig) { drawModalRectWithCustomSizedTexture((res.getScaledWidth() / 2 - 231), (res.getScaledHeight() / 2 - 231), 0, 0, 462, 462, 462, 462); }
			//else { drawModalRectWithCustomSizedTexture((res.getScaledWidth() / 2 + cornerMapX), (res.getScaledHeight() / 2 + cornerMapY), 0, 0, 150, 150, 150, 150); }
			drawCustomSizedTexture((res.getScaledWidth() / 2 + cornerMapX), (res.getScaledHeight() / 2 + cornerMapY), 
					map.zoomXLow, map.zoomYLow, map.mapSize, map.mapSize, map.zoomXHigh, map.zoomYHigh);
			
			if (map.drawVertical) {
				mc.renderEngine.bindTexture(Resources.facingLine);
				if (!map.drawBig) {
					if (!PlayerFacing.isPositiveXZFacing()) { drawCustomSizedTexture(res.getScaledWidth() - 110, 95, 0, 0, 10, 1, 1, 1); } 
					else { drawCustomSizedTexture(res.getScaledWidth() - 100, 95, 0, 0, 10, 1, 1, 1); }
				} else {
					if (!PlayerFacing.isPositiveXZFacing()) { drawCustomSizedTexture(res.getScaledWidth() / 2 - 20, res.getScaledHeight() / 2 + 1, 0, 0, 20, 1, 1, 1); } 
					else { drawCustomSizedTexture(res.getScaledWidth() / 2, res.getScaledHeight() / 2 + 1, 0, 0, 20, 1, 1, 1); }
				}
			}
			//GlStateManager.disableBlend();
			GlStateManager.popMatrix();
			GlStateManager.disableRescaleNormal();
		}
	}
	
	private void drawDirections() {
		if (!map.drawVertical) {
			float rotationFix = 57.1f;
			float facingDeg = PlayerFacing.getDegreeFacingDir();
			float xPosN = (float) (Math.sin(facingDeg / rotationFix) + 85 * Math.cos(facingDeg / rotationFix)) + (res.getScaledWidth() - 102);
			float yPosN = (float) (Math.cos(facingDeg / rotationFix) - 85 * Math.sin(facingDeg / rotationFix)) + 93;
			mc.fontRendererObj.drawStringWithShadow("W", xPosN, yPosN, 0xFFFFFF);
			float xPosE = (float) (Math.sin(facingDeg / rotationFix - 51.85) + 85 * Math.cos(facingDeg / rotationFix - 51.85)) + (res.getScaledWidth() - 102);
			float yPosE = (float) (Math.cos(facingDeg / rotationFix - 51.85) - 85 * Math.sin(facingDeg / rotationFix - 51.85)) + 93;
			mc.fontRendererObj.drawStringWithShadow("N", xPosE, yPosE, 0xFFFFFF);
			float xPosS = (float) (Math.sin(facingDeg / rotationFix - 47.11) + 85 * Math.cos(facingDeg / rotationFix - 47.11)) + (res.getScaledWidth() - 102);
			float yPosS = (float) (Math.cos(facingDeg / rotationFix - 47.11) - 85 * Math.sin(facingDeg / rotationFix - 47.11)) + 93;
			mc.fontRendererObj.drawStringWithShadow("E", xPosS, yPosS, 0xFFFFFF);
			float xPosW = (float) (Math.sin(facingDeg / rotationFix - 54.96) + 85 * Math.cos(facingDeg / rotationFix - 54.96)) + (res.getScaledWidth() - 102);
			float yPosW = (float) (Math.cos(facingDeg / rotationFix - 54.96) - 85 * Math.sin(facingDeg / rotationFix - 54.96)) + 93;
			mc.fontRendererObj.drawStringWithShadow("S", xPosW, yPosW, 0xFFFFFF);		
		}
	}
	
	private void drawFacingDir() {
		String facingStr = "";
		facingStr += PlayerFacing.getCompassFacingDir();
		facingStr += " : ";
		if (PlayerFacing.isPositiveXZFacing()) { facingStr += "+"; }
		else { facingStr += "-"; }
		if (PlayerFacing.isXFacing()) { facingStr += "X"; }
		else { facingStr += "Z"; }
		mc.fontRendererObj.drawStringWithShadow("Facing " + facingStr, res.getScaledWidth() - 263, 10, 0xFFFFFF);
	}
	
	private void drawCoords() {
		Vector3D p = PlayerTraits.getPlayerLocation();
		String x = new DecimalFormat("0.00").format(p.getX());
		String y = new DecimalFormat("0.00").format(p.getY());
		String z = new DecimalFormat("0.00").format(p.getZ());
		mc.fontRendererObj.drawStringWithShadow(x + ", " + y + ", " + z, res.getScaledWidth() - 283, 21, 0xFFFFFF);
	}
}
