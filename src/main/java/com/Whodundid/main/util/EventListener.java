package com.Whodundid.main.util;

import org.lwjgl.opengl.GL11;
import com.Whodundid.enhancedChat.chatUtil.ChatDrawer;
import com.Whodundid.enhancedChat.chatUtil.ChatUtil;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.util.miscUtil.WorldEditListener;
import com.Whodundid.main.util.playerUtil.PlayerFacing;
import com.Whodundid.mainMenu.CustomInGameMenu;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.WorldEvent;

//Last edited: Oct 26, 2018
//First Added: Oct 9, 2017
//Author: Hunter Bragg

public class EventListener {
	
	static Minecraft mc = MainMod.getMC();
	
	@SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventClientTick(e); }
    }
	
	@SubscribeEvent
	public void onRender(RenderWorldLastEvent e) {
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventLastRenderTick(e); }
		PlayerFacing.checkEyePosition(e);
	}
	
	@SubscribeEvent
	public void onInitGui(GuiScreenEvent.InitGuiEvent e) {
		if (e.gui instanceof GuiIngameMenu) { mc.displayGuiScreen(new CustomInGameMenu()); }
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventInitGui(e); }
	}
	
	@SubscribeEvent
	public void onPlayerRender(RenderPlayerEvent e) {
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventRenderPlayer(e); }
	}
	
	@SubscribeEvent
	public void onRenderFog(EntityViewRenderEvent.FogDensity e) {
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventRenderFogTick(e); }
	}
	
	@SubscribeEvent
	public void onBlockOverlayRender(RenderBlockOverlayEvent e) {
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventBlockOverlayTick(e); }
	}
	
	@SubscribeEvent
	public void living(LivingUpdateEvent e) {
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventLivingTick(e); }
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public  void onKeyEvent(KeyInputEvent e) {
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventKey(e); }
		MainMod.checkOpenSettings();
	}
	
	@SubscribeEvent
	public void onMouseEvent(MouseEvent e) {
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventMouse(e); }
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onOverlayRender(RenderGameOverlayEvent e) {
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventOverlayRenderTick(e); }
	}
	
	@SubscribeEvent
	public void onOverlayRender(RenderGameOverlayEvent.Text e) {
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventTextOverlayRenderTick(e); }
		ChatDrawer.updateTextRenderer();
	}
	
	@SubscribeEvent
	public void onPreOverlayRender(RenderGameOverlayEvent.Pre e) {
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onPostOverlayRender(RenderGameOverlayEvent.Post e) {
		GL11.glPushMatrix();
		GlStateManager.disableAlpha();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventPostOverlayRenderTick(e); }
		GlStateManager.enableAlpha();
		GL11.glPopMatrix();
	}
	
	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent e) {
		ChatUtil.readChat(e.message);
		WorldEditListener.checkForPositions();
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventChat(e); }
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload e) {
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventWorldUnload(e); }
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		mc.ingameGUI = MainMod.enhancedMCGui;
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventWorldLoad(e); }
	}
	
	@SubscribeEvent
    public void onServerJoin(EntityJoinWorldEvent e) {
		/*if (e.entity.equals(mc.thePlayer)) {
			for (Field f : mc.thePlayer.getClass().getDeclaredFields()) {
				if (f.getName().equals("sendQueue")) {
					f.setAccessible(true);
					NetHandlerPlayClient sendQueue = mc.thePlayer.sendQueue;
					try {
						f.set(mc.thePlayer, new ModdedNetHandlerPlayClient(mc, null, sendQueue.getNetworkManager(), sendQueue.getGameProfile()));
					} catch (Exception q) { q.printStackTrace(); }
					break;
				}
			}
		}*/
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventServerJoin(e); }
    }
}