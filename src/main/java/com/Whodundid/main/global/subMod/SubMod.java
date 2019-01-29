package com.Whodundid.main.global.subMod;

import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.config.SubModConfig;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.storageUtil.EArrayList;
import com.Whodundid.main.util.storageUtil.StorageBox;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

//Last edited: Dec 27, 2018
//First Added: Oct 15, 2018
//Author: Hunter Bragg

public abstract class SubMod {
	
	protected Minecraft mc = MainMod.getMC();
	protected EnhancedGui mainGui;
	protected EArrayList<EnhancedGui> guis = new EArrayList();
	protected EArrayList<SubModType> dependencies = new EArrayList();
	protected EArrayList<SubModType> softDependencies = new EArrayList();
	private SubModType mod;
	protected SubModConfig config;
	protected boolean enabled = false;
	
	public SubMod(SubModType modIn) {
		mod = modIn;
	}
	
	public SubModType getModType() { return mod; }
	public boolean isEnabled() { return enabled; }
	public boolean hasConfig() { return config != null; }
	public EArrayList<EnhancedGui> getGuis() { return guis; }
	public EArrayList<SubModType> getDependencies() { return dependencies; }
	public EArrayList<SubModType> getSoftDependencies() { return softDependencies; }
	public EnhancedGui getMainGui(boolean setPosition, StorageBox<Integer, Integer> pos, EnhancedGui oldGui) { return null; }
	public SubModConfig getConfig() { return config; }
	public SubMod setEnabled(boolean valueIn) { enabled = valueIn; return this; }
	
	protected SubMod setMainGui(EnhancedGui guiIn) {
		EnhancedGui oldGui = mainGui;
		if (oldGui != null) {
			if (mc.currentScreen.equals(oldGui)) { mc.displayGuiScreen(null); }
			if (guis.contains(oldGui)) {
				Iterator<EnhancedGui> it = guis.iterator();
				while (it.hasNext()) {
					if (oldGui.equals(it.next())) { it.remove(); break; } 
				}
				oldGui = null;
			}
		}
		mainGui = guiIn;
		guis.add(mainGui);
		return this;
	}
	
	protected SubMod addGui(EnhancedGui... guiIn) {
		for (EnhancedGui gui : guiIn) { if (!guis.contains(gui)) { guis.add(gui); } }
		return this;
	}
	
	public void initSubModOnLoad() {}
	
	public void preInit() {}
	public void init() {}
	public void postInit() {}
	
	public void eventClientTick(TickEvent.ClientTickEvent e) {}
	public void eventLastRenderTick(RenderWorldLastEvent e) {}
	public void eventOverlayRenderTick(RenderGameOverlayEvent e) {}
	public void eventTextOverlayRenderTick(RenderGameOverlayEvent.Text e) {}
	public void eventPostOverlayRenderTick(RenderGameOverlayEvent.Post e) {}
	public void eventRenderFogTick(EntityViewRenderEvent.FogDensity e) {}
	public void eventBlockOverlayTick(RenderBlockOverlayEvent e) {}
	public void eventRenderPlayer(RenderPlayerEvent e) {}
	public void eventLivingTick(LivingUpdateEvent e) {}
	public void eventMouse(MouseEvent e) {}
	public void eventKey(KeyInputEvent e) {}
	public void eventChat(ClientChatReceivedEvent e) {}
	public void eventWorldLoad(WorldEvent.Load e) {}
	public void eventWorldUnload(WorldEvent.Unload e) {}
	public void eventInitGui(GuiScreenEvent.InitGuiEvent e) {}
	public void eventServerJoin(EntityJoinWorldEvent e) {}
}
