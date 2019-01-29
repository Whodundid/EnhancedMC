package com.Whodundid.miniMap;

import java.awt.image.BufferedImage;
import java.util.List;
import com.Whodundid.main.global.subMod.IUseScreenLocation;
import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.miscUtil.NetPlayerComparator;
import com.Whodundid.main.util.miscUtil.ScreenLocation;
import com.Whodundid.main.util.miscUtil.WorldHelper;
import com.Whodundid.main.util.playerUtil.PlayerFacing;
import com.Whodundid.main.util.storageUtil.DynamicTextureHandler;
import com.Whodundid.main.util.storageUtil.StorageBox;
import com.Whodundid.main.util.storageUtil.StorageBoxHolder;
import com.google.common.collect.Ordering;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//Last edited: 12-14-18
//First Added: 11-16-17
//Author: Hunter Bragg

public class MiniMap extends SubMod implements IUseScreenLocation {
	
	private Minecraft mc = MainMod.getMC();
	MiniMapDrawer drawer;
	public DynamicTextureHandler handler, border;
	private long timeSinceLastDraw;
	public volatile int downOffsetY = 0;
	public int mapSize = 150;
	public int previewSize = 157;
	public double zoom = 1;
	public double zoomXHigh = mapSize, zoomYHigh = mapSize;
	public double zoomXLow = 0, zoomYLow = 0;
	public double previewXHigh = previewSize, previewYHigh = previewSize;
	public double previewXLow = 0, previewYLow = 0;
	public boolean mapImageCreated = false;
	public boolean trackPlayers = false;
	public boolean drawVertical = false;
	public boolean drawMap = true;
	public boolean drawCoords = true;	
	public boolean drawMapDirections = true;
	public boolean drawFacingDir = true;
	public boolean drawBig = false;
	public boolean drawWithChatOpen = true;
	public String findPlayer = "";
	public StorageBoxHolder<Integer, Integer> highlightedBlocks;
	ScreenLocation loc = ScreenLocation.TR;
	protected int xPos = 0, yPos = 0;
	
	public MiniMap() {
		super(SubModType.MINIMAP);
		softDependencies.add(SubModType.HOTKEYS);
		setMiniMapZoom(5.5);
	}
	
	@Override
	public void init() {
		drawer = new MiniMapDrawer(this);
		handler = new DynamicTextureHandler(mc.getTextureManager(), new BufferedImage(301, 301, BufferedImage.TYPE_INT_RGB));
		border = new DynamicTextureHandler(mc.getTextureManager(), new BufferedImage(75, 75, BufferedImage.TYPE_INT_RGB));
		timeSinceLastDraw = System.currentTimeMillis();
		highlightedBlocks = new StorageBoxHolder();
	}
	
	@Override
	public EnhancedGui getMainGui(boolean setPosition, StorageBox<Integer, Integer> pos, EnhancedGui oldGui) {
		if (oldGui != null) { return setPosition ? new MiniMapGui(pos.getObject(), pos.getValue(), oldGui) : new MiniMapGui(oldGui); }
		return setPosition ? new MiniMapGui(pos.getObject(), pos.getValue()) : new MiniMapGui();
	}
	
	@Override
	public void eventClientTick(TickEvent.ClientTickEvent e) {
		if (isEnabled()) {
			if (mc.thePlayer != null && mc.theWorld != null && mc.getRenderViewEntity() != null) {
				if (System.currentTimeMillis() - timeSinceLastDraw >= 75) {
					drawWorld(drawVertical);
					drawMapOutline();
					if (!trackPlayers) {
						int mWidth = handler.getTextureWidth() / 2;
						handler.GBI().setRGB(mWidth, mWidth, 0xFF0000);
					}					
					try {						
						handler.updateTextureData(handler.GBI());
					} catch (Exception q) { q.printStackTrace(); }
					if (!mapImageCreated) { mapImageCreated = true; }
					timeSinceLastDraw = System.currentTimeMillis();
				}
			}
		}
	}
	
	@Override
	public void eventPostOverlayRenderTick(RenderGameOverlayEvent.Post e) {
		if (isEnabled()) { drawer.draw(); }
	}
	
	private void drawMapOutline() {
		for (int x = 0; x < border.GBI().getWidth(); x++) {
			border.GBI().setRGB(x, 0, 0xC8C8C8);
			border.GBI().setRGB(x, border.GBI().getHeight() - 1, 0xC8C8C8);
			border.GBI().setRGB(0, x, 0xC8C8C8);
			border.GBI().setRGB(border.GBI().getWidth() - 1, x, 0xC8C8C8);
		}
		if (!drawVertical) {
			border.GBI().setRGB(37, 0, 0xFF0000);
			border.GBI().setRGB(36, 0, 0xFF5500);
			border.GBI().setRGB(38, 0, 0xFF5500);
		}
	}
	
	private void drawWorld(boolean vertical) {
		BlockPos center = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getEntityBoundingBox().minY, mc.getRenderViewEntity().posZ);
		try {
			if (vertical) { drawVerticalWorld(center); } 
			else { drawHorizontalWorld(center); }
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private void drawHorizontalWorld(BlockPos center) {
		int w = handler.getTextureWidth() / 2;
		int mWidth = (int) Math.ceil(w / (this.zoomXHigh / w));
		for (int z = center.getZ() - mWidth; z < center.getZ() + mWidth + 1; z++) {
			for (int x = center.getX() - mWidth; x < center.getX() + mWidth + 1; x++) {				
				/*BlockPos pos = new BlockPos(x, 255, z);
				int i = 255;
				while (i > 0 && WorldHelper.getBlockID(pos) == 0) {
					i--;
					pos = new BlockPos(x, i, z);
				}*/
				BlockPos pos = new BlockPos(x, center.getY() - downOffsetY, z);
				IBlockState state = mc.theWorld.getBlockState(pos);
				int color = 0;
				if (WorldHelper.checkBlockForMapDraw(pos, state)) {
					color = WorldHelper.getCorrectMapColor(pos, state);
					if (WorldHelper.isFenceBlock(state) || (WorldHelper.getBlockID(state.getBlock()) == 107 && state.getBlock().getMetaFromState(state) == 2)) {
						//color = 0xff0000;
					}
				}
				//System.out.println(handler.getTextureWidth());
				handler.GBI().setRGB(x - center.getX() + w, z - center.getZ() + w, color);
				if (trackPlayers) {
					drawPlayerLocations(x, downOffsetY, z, center);
				}
			}
		}
	}
	
	private void drawVerticalWorld(BlockPos center) {
		if (PlayerFacing.isXFacing()) {
			for (int x = center.getX() - 36; x < center.getX() + 37; x++) {
				for (int y = center.getY() - 36; y < center.getY() + 37; y++) {
					BlockPos pos = new BlockPos(x, y, center.getZ());
					IBlockState state = mc.theWorld.getBlockState(pos);
					int color = 0;
					if (WorldHelper.checkBlockForMapDraw(pos, state)) {
						color = WorldHelper.getCorrectMapColor(pos, state);
					}
					
					handler.GBI().setRGB(x - center.getX() + 37, y - center.getY() + 37, color);
					if (trackPlayers) {
						drawPlayerLocations(x, y, center.getZ(), center);
					}					
				}
			}
		} else {
			for (int z = center.getZ() - 36; z < center.getZ() + 37; z++) {
				for (int y = center.getY() - 36; y < center.getY() + 37; y++) {
					BlockPos pos = new BlockPos(center.getX(), y, z);
					IBlockState state = mc.theWorld.getBlockState(pos);
					int color = 0;
					if (WorldHelper.checkBlockForMapDraw(pos, state)) {
						color = WorldHelper.getCorrectMapColor(pos, state);
					}
					
					handler.GBI().setRGB(z - center.getZ() + 37, y - center.getY() + 37, color);
					if (trackPlayers) {
						drawPlayerLocations(center.getX(), y, z, center);
					}					
				}
			}
		}
	}
	
	private void drawPlayerLocations(int x, int y, int z, BlockPos center) {
		List<EntityPlayer> list = mc.theWorld.playerEntities;
		NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
		Ordering<NetworkPlayerInfo> order = Ordering.from(new NetPlayerComparator());
		List<NetworkPlayerInfo> nameList = order.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
		
		for (EntityPlayer p : list) {
			if (drawVertical) {
				if (PlayerFacing.isXFacing()) {
					if (p.getPosition().getX() == x && p.getPosition().getY() == y) {
						handler.GBI().setRGB(x - center.getX() + 37, y - center.getY() + 37, returnColor(nameList, p.getName()));
					}
				} else {
					if (p.getPosition().getZ() == z && p.getPosition().getY() == y) {
						handler.GBI().setRGB(z - center.getZ() + 37, y - center.getY() + 37, returnColor(nameList, p.getName()));
					}
				}
			} else {
				int mWidth = handler.getTextureWidth() / 2;
				if (p.getPosition().getX() == x && p.getPosition().getZ() == z) {
					handler.GBI().setRGB(x - center.getX() + mWidth, z - center.getZ() + mWidth, returnColor(nameList, p.getName()));
				}
			}			
		}
	}
	
	private int returnColor(List<NetworkPlayerInfo> in, String parseName) {
		String ownColor = getOwnColor(in);
		for (NetworkPlayerInfo info : in) {
			String name;
			name = (info.getDisplayName() != null) ? info.getDisplayName().getUnformattedText() : ScorePlayerTeam.formatPlayerName(info.getPlayerTeam(), info.getGameProfile().getName());
			if (name.contains(parseName)) {
				if (name.contains(findPlayer)) {
					return 0x00ffdc;
				}
				if (name.length() > 3) {
					if (name.substring(1, 2).equals(ownColor)) {
						return 0x00ff00;
					}
				}				
			}
		}
		return 0xFF0000;
	}
	
	private String getOwnColor(List<NetworkPlayerInfo> in) {
		try {
			for (NetworkPlayerInfo info : in) {
				String name;
				name = (info.getDisplayName() != null) ? info.getDisplayName().getUnformattedText() : ScorePlayerTeam.formatPlayerName(info.getPlayerTeam(), info.getGameProfile().getName());
				if (name.contains(mc.thePlayer.getName())) {
					return name.substring(1, 2);
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
		return "0";
	}
	
	public void zoomMiniMap(int scrollChange) {
		double currentZoom = zoom;
		zoom = currentZoom += (scrollChange * 0.25);
		setMiniMapZoom(zoom);
	}
	
	public void setMiniMapZoom(double zoomIn) {
		zoom = zoomIn;
		if (zoom < 1) { zoom = 1; }
		if (zoom > 10) { zoom = 10; }
		
		zoomXHigh = mapSize * zoom;
		zoomYHigh = mapSize * zoom;
		previewXHigh = previewSize * zoom;
		previewYHigh = previewSize * zoom;
		
		zoomXLow = (zoomXHigh - mapSize) / 2;
		zoomYLow = (zoomYHigh - mapSize) / 2;
		previewXLow = (previewXHigh - previewSize) / 2;
		previewYLow = (previewYHigh - previewSize) / 2;
	}
	
	public int getMapRadius() { int w = handler.getTextureWidth() / 2; int mWidth = (int) Math.ceil(w / (zoomXHigh / w)); return mWidth; }
	
	@Override public void setLocation(ScreenLocation locIn) { loc = locIn; }
	@Override public void setLocation(int xIn, int yIn) { loc = ScreenLocation.CUSTOM; xPos = xIn; yPos = yIn; }
	@Override public StorageBox<Integer, Integer> getLocation() { return new StorageBox<Integer, Integer>(xPos, yPos); }
	@Override public ScreenLocation getScreenLocation() { return loc; }
	@Override public EnhancedGui getScreenLocationGui() { return null; }
	
	public MiniMap setDrawWithChatOpen(boolean val) { drawWithChatOpen = val; return this; }
	public boolean isMapImageCreated() { return mapImageCreated; }
}
