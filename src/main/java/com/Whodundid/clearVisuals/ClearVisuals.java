package com.Whodundid.clearVisuals;

import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.miscUtil.WorldHelper;
import com.Whodundid.main.util.storageUtil.StorageBox;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;

//Last edited: Dec 29, 2018
//First Added: Mar 22, 2018
//Author: Hunter Bragg

public class ClearVisuals extends SubMod {
	
	static Minecraft mc = MainMod.getMC();
	public float vanillaGama = 0.0f;
	protected boolean drawFire = false;
	protected boolean drawWaterOverlay = false;
	protected boolean clearLava = true;
	protected boolean clearWater = true;
	
	public ClearVisuals() {
		super(SubModType.CLEARVISUALS);
		config = new ClearVisualsConfig(this, "clearVisuals");
	}
	
	@Override
	public EnhancedGui getMainGui(boolean setPosition, StorageBox<Integer, Integer> pos, EnhancedGui oldGui) {
		if (oldGui != null) { return setPosition ? new ClearVisualsGui(pos.getObject(), pos.getValue(), oldGui) : new ClearVisualsGui(oldGui); }
		return setPosition ? new ClearVisualsGui(pos.getObject(), pos.getValue()) : new ClearVisualsGui();
	}
	
	@Override
	public void eventRenderFogTick(EntityViewRenderEvent.FogDensity e) {
		if (isEnabled()) {
			if (e.block.getMaterial() == Material.lava && clearLava) { e.density = 0; e.setCanceled(true); }
			if (e.block.getMaterial() == Material.water && clearWater) { e.density = 0; e.setCanceled(true); }
		}
	}
	
	@Override
	public void eventBlockOverlayTick(RenderBlockOverlayEvent e) {
		if (isEnabled()) {
			if (e.blockForOverlay != null) {
				switch (WorldHelper.getBlockID(e.blockForOverlay.getBlock())) {
				case 9: if (!drawWaterOverlay) { e.setCanceled(true); } break; //water overlay
				case 51: if (!drawFire) { e.setCanceled(true); } break; //fire
				default: break;
				}
			}
		}
	}
	
	@Override
	public void eventWorldLoad(WorldEvent.Load e) {
		config.loadConfig();
	}
	
	public ClearVisuals setFireVisibility(boolean val) { drawFire = val; return this; }
	public ClearVisuals setUnderWaterOverlayVisibility(boolean val) { drawWaterOverlay = val; return this; }
	public ClearVisuals setClearLava(boolean val) { clearLava = val; return this; }
	public ClearVisuals setClearWater(boolean val) { clearWater = val; return this; }
	public ClearVisuals setGama(float val) { mc.gameSettings.gammaSetting = val; return this; }
	public float getCurrentGama() { return mc.gameSettings.gammaSetting; }
	public boolean isFireDrawn() { return drawFire; }
	public boolean isWaterOverlayDrawn() { return drawWaterOverlay; }
	public boolean isClearLava() { return clearLava; }
	public boolean isClearWater() { return clearWater; }
}
