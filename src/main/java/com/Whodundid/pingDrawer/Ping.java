package com.Whodundid.pingDrawer;

import com.Whodundid.main.global.subMod.IUseScreenLocation;
import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.miscUtil.NetPlayerComparator;
import com.Whodundid.main.util.miscUtil.ScreenLocation;
import com.Whodundid.main.util.storageUtil.StorageBox;
import com.google.common.collect.Ordering;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//Last edited: Dec 10, 2018
//First Added: Oct 19, 2018
//Author: Hunter Bragg

public class Ping extends SubMod implements IUseScreenLocation {

	protected Entity lastJoinedEntity;
	protected int ping = 0;
	protected int xPos = 0, yPos = 0;
	protected boolean hasPing = false;
	public boolean drawWithChatOpen = false;
	List<EntityPlayer> list;
	NetHandlerPlayClient nethandlerplayclient;
	Ordering<NetworkPlayerInfo> order;
	List<NetworkPlayerInfo> nameList;
	ScreenLocation loc = ScreenLocation.BL;
	
	public Ping() {
		super(SubModType.PING);
		config = new PingConfig(this, "pingConfig");
	}
	
	@Override
	public EnhancedGui getMainGui(boolean setPosition, StorageBox<Integer, Integer> pos, EnhancedGui oldGui) {
		if (oldGui != null) { return setPosition ? new PingGui(pos.getObject(), pos.getValue(), oldGui) : new PingGui(oldGui); }
		return setPosition ? new PingGui(pos.getObject(), pos.getValue()) : new PingGui();
	}
	
	@Override
	public void eventServerJoin(EntityJoinWorldEvent e) {
		lastJoinedEntity = e.entity;
		if (lastJoinedEntity.equals(mc.thePlayer)) {
			hasPing = e.world.isRemote;
			ping = 0;
		}
	}
	
	@Override
	public void eventClientTick(TickEvent.ClientTickEvent e) {
		if (isEnabled() && hasPing) {
			if (mc.theWorld != null && mc.thePlayer != null) {
				list = mc.theWorld.playerEntities;
				nethandlerplayclient = mc.thePlayer.sendQueue;
				order = Ordering.from(new NetPlayerComparator());
				nameList = order.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
				if (!nameList.isEmpty()) {
					for (NetworkPlayerInfo i : nameList) {
						String name = i.getGameProfile().getName();
						if (name.equals(mc.thePlayer.getName())) { ping = i.getResponseTime(); }
					}
				}
			}
		}
	}
	
	@Override
	public void eventTextOverlayRenderTick(RenderGameOverlayEvent.Text e) {
		if (isEnabled() && hasPing && !(mc.currentScreen instanceof PingSetLocationGui)) {
			if (!drawWithChatOpen && mc.ingameGUI.getChatGUI().getChatOpen()) { return; }
			ScaledResolution res = new ScaledResolution(mc);
			if (mc.theWorld != null && !mc.isSingleplayer()) {
				String msg = ping != 0 ? "PING: " + ping + " ms" : "Calculating..";
				int l = mc.fontRendererObj.getStringWidth(msg);
				int drawPosX = 0, drawPosY = 0;
				switch (loc) {
				case BL: drawPosX = 0; drawPosY = res.getScaledHeight(); break;
				case BR: drawPosX = res.getScaledWidth() - l - 1; drawPosY = res.getScaledHeight(); break;
				case TL: drawPosX = 0; drawPosY = 11; break;
				case TR: drawPosX = res.getScaledWidth() - l - 1; drawPosY = 11; break;
				case C: drawPosX = res.getScaledWidth() / 2 - (l / 2); drawPosY = res.getScaledHeight() / 2 - 11; break;
				case CUSTOM: drawPosX = xPos; drawPosY = yPos; break;
				}
				Gui.drawRect(drawPosX, drawPosY, drawPosX + l + 1, drawPosY - 10, Integer.MIN_VALUE);
				mc.fontRendererObj.drawString(msg, drawPosX + 1, drawPosY - 9, 0x00ff00);
			}
		}
	}
	
	@Override public void setLocation(ScreenLocation locIn) { loc = locIn; }
	@Override public void setLocation(int xIn, int yIn) { loc = ScreenLocation.CUSTOM; xPos = xIn; yPos = yIn; }
	@Override public StorageBox<Integer, Integer> getLocation() { return new StorageBox<Integer, Integer>(xPos, yPos); }
	@Override public ScreenLocation getScreenLocation() { return loc; }
	@Override public EnhancedGui getScreenLocationGui() { return new PingSetLocationGui(); }
	
	public Ping setDrawWithChatOpen(boolean val) { drawWithChatOpen = val; return this; }
	public Entity getLastJoinedEntity() { return lastJoinedEntity; }
	public int getClientServerPing() { return ping; }
	public boolean doesClientHavePing() { return hasPing; }
}
