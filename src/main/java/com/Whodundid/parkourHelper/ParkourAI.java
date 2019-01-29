package com.Whodundid.parkourHelper;

import java.text.DecimalFormat;
import com.Whodundid.enhancedChat.chatUtil.ChatType;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.miscUtil.Resources;
import com.Whodundid.main.util.miscUtil.WorldHelper;
import com.Whodundid.main.util.playerUtil.PlayerFacing;
import com.Whodundid.main.util.storageUtil.StorageBox;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

//Last edited: 10-16-18
//First Added: 11-16-17
//Author: Hunter Bragg

public class ParkourAI extends SubMod {
	
	static Minecraft mc = MainMod.getMC();
	FontRenderer fontRender;
	BackgroundChecker checker;
	HelperBlock helperBlock;
	boolean constantSprint = false;
	boolean edgeJump = false;
	public volatile double playerDist = 0;
	public volatile double jumpPos = 0;
	public volatile double jumpOffset = 0;
	protected String testDisplay = "";
	protected int displayColor = 0xffffff;
	
	public ParkourAI() {
		super(SubModType.PARKOUR);
		dependencies.add(SubModType.HOTKEYS, SubModType.SCRIPTS);
		checker = new BackgroundChecker(this);
	}
	
	@Override
	public void init() {
		fontRender = mc.fontRendererObj;
	}
	
	@Override
	public EnhancedGui getMainGui(boolean setPosition, StorageBox<Integer, Integer> pos, EnhancedGui oldGui) {
		if (oldGui != null) { return setPosition ? new ParkourGui(pos.getObject(), pos.getValue(), oldGui) : new ParkourGui(oldGui); }
		return setPosition ? new ParkourGui(pos.getObject(), pos.getValue()) : new ParkourGui();
	}
	
	@Override
	public void eventClientTick(TickEvent.ClientTickEvent e) {
		if (isEnabled()) {
			if (constantSprint && mc.thePlayer != null) {
				if (!mc.thePlayer.isSprinting() && !mc.isGamePaused() && mc.inGameHasFocus) { KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true); }		
			}
			if (edgeJump && mc.thePlayer != null) {
				if (!checker.running) { (new Thread(checker)).start(); }
    		} else {
    			if (checker.running) { checker.kill(); }
    		}
		}
	}
	
	@Override
	public void eventPostOverlayRenderTick(RenderGameOverlayEvent.Post e) {
		if (fontRender != null && mc.getRenderViewEntity() != null) {
			if (isConstantlySprinting()) {
				ScaledResolution res = new ScaledResolution(mc);
				mc.renderEngine.bindTexture(Resources.guiBase);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				Gui.drawModalRectWithCustomSizedTexture(-1, res.getScaledHeight() - 28, 0, 0, 370, 28, 370, 256);
				//Gui.drawModalRectWithCustomSizedTexture(res.getScaledWidth() - 112, 300, 0, 0, 138, 11, 138, 11);
				fontRender.drawStringWithShadow(testDisplay, 3, res.getScaledHeight() - 26, displayColor);
				//fontRender.drawStringWithShadow(BackgroundChecker.maxHeight + "", res.getScaledWidth() - 110, 302, 0x00FFFF);
				fontRender.drawStringWithShadow("" + Mouse.getX(), 50, 300, 0xFFFFFF);
				//fontRender.drawStringWithShadow("" + PlayerFacing.getDegreeFacingDir(), 500, 300, 0xFFFFFF);
				//fontRender.drawStringWithShadow("" + Math.cos(PlayerFacing.getDegreeFacingDir() / 360), 500, 311, 0xFFFFFF);
				//fontRender.drawStringWithShadow("" + Math.sin(PlayerFacing.getDegreeFacingDir() / 360), 500, 322, 0xFFFFFF);
				String posX = new DecimalFormat("0.0000000000").format(mc.getRenderViewEntity().posX);
				String posY = new DecimalFormat("0.0000000000").format(mc.getRenderViewEntity().getEntityBoundingBox().minY);
				String posZ = new DecimalFormat("0.0000000000").format(mc.getRenderViewEntity().posZ);				
				fontRender.drawString(posX + " / " + posY + " / " + posZ + " - " + PlayerFacing.getCompassFacingDir().name() + "; " + PlayerFacing.getCompassFacingDir().getXZFacing(), 3, res.getScaledHeight() - 17, 0x00FFFF);
				if (helperBlock != null) {
					BlockPos pos = helperBlock.getHelperBlockLocation();
					String changeX = new DecimalFormat("0.000").format(Math.abs(mc.getRenderViewEntity().posX - pos.getX()));
					String changeY = new DecimalFormat("0.000").format(Math.abs(mc.getRenderViewEntity().getEntityBoundingBox().minY - pos.getY()));
					String changeZ = new DecimalFormat("0.000").format(Math.abs(mc.getRenderViewEntity().posZ - pos.getZ()));
					if (isEdgeJumping()) {
						fontRender.drawStringWithShadow("Sprint & Jump, HelperBlockLoc: " + helperBlock.getHelperBlock().getLocalizedName() + "; (" + changeX + ", " + changeY + ", " + changeZ + ")", 3, res.getScaledHeight() - 9, ChatType.LOBBY.getChatDisplayColor());			
					} else {
						fontRender.drawStringWithShadow("Sprinting, HelperBlockLoc: " + helperBlock.getHelperBlock().getLocalizedName() + "; (" + changeX + ", " + changeY + ", " + changeZ + ")", 3, res.getScaledHeight() - 9, ChatType.LOBBY.getChatDisplayColor());
					}
				} else {
					if (isEdgeJumping()) {
						fontRender.drawStringWithShadow("Sprinting, HelperBlock not set!", 3, res.getScaledHeight() - 9, ChatType.STAFF.getChatDisplayColor());
					} else {
						fontRender.drawStringWithShadow("Sprinting", 3, res.getScaledHeight() - 9, ChatType.LOBBY.getChatDisplayColor());
					}
				}
			}
		}
		mc.renderEngine.bindTexture(Gui.icons);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
	}
	
	@Override
	public void eventWorldUnload(WorldEvent.Unload e) {
		checker.kill();
		setEdgeJumping(false);
		setConstantlySprinting(false);
	}
	
	public static boolean testBlockJumpHeights(int blockY, double playerYPos) {
		return (playerYPos == blockY || //base 0.0 height
			playerYPos == blockY - 0.984375 || //lilipad
			playerYPos == blockY - 0.9375 || //carpet
			playerYPos == blockY - 0.875 || //repeater & snow layer
			playerYPos == blockY - 0.8125 || //trapdoor
			playerYPos == blockY - 0.75 || //snow layer
			playerYPos == blockY - 0.625 || //daylight sensor & snowlayer & flower pot
			playerYPos == blockY - 0.5 || //half block
			playerYPos == blockY - 0.4375 || //bed
			playerYPos == blockY - 0.375 || //snow layer
			playerYPos == blockY - 0.25 || //enchant & snow layer
			playerYPos == blockY - 0.1875 || //end port							
			playerYPos == blockY - 0.125 || //chest & snow layer & soul sand
			playerYPos == blockY - 0.0625 || //cactus
			playerYPos == blockY + 0.5); //fence
	}
	
	public boolean readyToJump() {
		if (helperBlock != null) {
			if (mc.getRenderViewEntity() != null) {
				Block block = helperBlock.getHelperBlock();
				IBlockState state = helperBlock.getHelperBlockState();
				double yPos = mc.getRenderViewEntity().getEntityBoundingBox().minY;
				int bY = helperBlock.getHelperBlockLocation().getY();
				double yCor = yPos - 1;
				if (testBlockJumpHeights(bY, yCor)) {
					if (jumpOffset == 0.0) {
						if (WorldHelper.isFenceBlock(state)) { jumpOffset = 0.135; }
						else if (WorldHelper.isPane(state)) { jumpOffset = 0.035; }
						else if (WorldHelper.isTrapDoor(state)) {
							if ((boolean) state.getProperties().values().toArray()[2]) { jumpOffset = -0.535; }
						} else if (WorldHelper.compareID(block, 139)) { jumpOffset = 0.335; } //cobblestone wall
						else { jumpOffset = 0.535; }
					}
					
					if (helperBlock.getHelperBlockID() == 144) { jumpOffset = 0.335; }
					if (helperBlock.getHelperBlockID() == 102) { jumpOffset = -0.135; }
			        
			        if (PlayerFacing.isXFacing()) { playerDist = mc.getRenderViewEntity().posX; } 
			        else { playerDist = mc.getRenderViewEntity().posZ; }
			        
			        if (helperBlock.isPositiveXZFacing) { jumpPos = helperBlock.getJumpPos() + jumpOffset; } 
			        else { jumpPos = helperBlock.getJumpPos() - jumpOffset; }
			        
			        //for double neo x-facing
			        //stand 0.17 blocks away
			        
			        // for 3 over 1.0625 up carpet
			        // 0.629 distance			        
			        // 1.1439613639
			        // 1.1863137861
			        // (0.0423524222 window)
			        
			        // for daylight 3 over 1.125 upt
			        // 0.629 distance
			        // x & z oriented - (10 precision)
			        // 1.1439781408
			        // 1.1863137861
			        // (0.0423356453 window) (slightly smaller window)
			        
			        // (0.0000167769 difference)

			        //13 precision is the most precise (14 and after does not seem to be handled very well)
			        
			        double val = 0;
			        
			        val = Math.abs(playerDist - jumpPos);
			        
			        if (jumpOffset == 0.535) {
			        	if (val >= 0.575 && val <= 0.620) {
			        		testDisplay = "PASS: " + val;
			        		displayColor = ChatType.GAME.getChatDisplayColor();
			        		//ChatDrawer.addChatToBeDrawnToScreen("PASS: " + val, 3, ChatDrawer.getCurrentResoultion().getScaledHeight() - 26, ChatType.GAME.getChatDisplayColor());
			        	} else if (val >= 0.86 && val <= 0.93) {
			        		testDisplay = "PASS: " + val;
			        		displayColor = ChatType.GAME.getChatDisplayColor();
			        		//ChatDrawer.addChatToBeDrawnToScreen("PASS: " + val, 3, ChatDrawer.getCurrentResoultion().getScaledHeight() - 26, ChatType.GAME.getChatDisplayColor());
			        	} else if (val >= 1.07 && val <= 1.22) {
			        		testDisplay = "PASS: " + val;
			        		displayColor = ChatType.GAME.getChatDisplayColor();
			        		//ChatDrawer.addChatToBeDrawnToScreen("PASS: " + val, 3, ChatDrawer.getCurrentResoultion().getScaledHeight() - 26, ChatType.GAME.getChatDisplayColor());
			        	} else if (val >= 1.33 && val <= 1.52) {
			        		testDisplay = "PASS: " + val;
			        		displayColor = ChatType.GAME.getChatDisplayColor();
			        		//ChatDrawer.addChatToBeDrawnToScreen("PASS: " + val, 3, ChatDrawer.getCurrentResoultion().getScaledHeight() - 26, ChatType.GAME.getChatDisplayColor());
			        	} else if (val >= 1.60 && val <= 1.80) {
			        		testDisplay = "PASS: " + val;
			        		displayColor = ChatType.GAME.getChatDisplayColor();
			        		//ChatDrawer.addChatToBeDrawnToScreen("PASS: " + val, 3, ChatDrawer.getCurrentResoultion().getScaledHeight() - 26, ChatType.GAME.getChatDisplayColor());
			        	} else if (val >= 1.88 && val <= 2.08) {
			        		testDisplay = "PASS: " + val;
			        		displayColor = ChatType.GAME.getChatDisplayColor();
			        		//ChatDrawer.addChatToBeDrawnToScreen("PASS: " + val, 3, ChatDrawer.getCurrentResoultion().getScaledHeight() - 26, ChatType.GAME.getChatDisplayColor());
			        	} else if (val >= 2.18 && val <= 2.30) {
			        		testDisplay = "PASS: " + val;
			        		displayColor = ChatType.GAME.getChatDisplayColor();
			        		//ChatDrawer.addChatToBeDrawnToScreen("PASS: " + val, 3, ChatDrawer.getCurrentResoultion().getScaledHeight() - 26, ChatType.GAME.getChatDisplayColor());
			        	} else if (val > 2.30 && val < 6) {
			        		testDisplay = "UNKNOWN: " + val;
			        		displayColor = 0xffffff;
			        		//ChatDrawer.addChatToBeDrawnToScreen("UNKNOWN: " + val, 3, ChatDrawer.getCurrentResoultion().getScaledHeight() - 26, ChatType.NONE.getChatDisplayColor());
			        	} else if (val >= 6) {
			        		testDisplay = "PASS: " + val;
			        		displayColor = ChatType.GAME.getChatDisplayColor();
			        		//ChatDrawer.addChatToBeDrawnToScreen("PASS: " + val, 3, ChatDrawer.getCurrentResoultion().getScaledHeight() - 26, ChatType.GAME.getChatDisplayColor());
			        	} else {
			        		testDisplay = "FAIL: " + val;
			        		displayColor = ChatType.STAFF.getChatDisplayColor();
			        		//ChatDrawer.addChatToBeDrawnToScreen("FAIL: " + val, 3, ChatDrawer.getCurrentResoultion().getScaledHeight() - 26, ChatType.STAFF.getChatDisplayColor());
			        	}
			        } else if (jumpOffset == 0.629) {
			        	if (val >= 1.1439781408 && val <= 1.1863137861) {
			        		testDisplay = "PASS: " + val;
			        		displayColor = ChatType.GAME.getChatDisplayColor();
			        		//ChatDrawer.addChatToBeDrawnToScreen("PASS: " + val, 3, ChatDrawer.getCurrentResoultion().getScaledHeight() - 26, ChatType.GAME.getChatDisplayColor());
			        	} else {
			        		testDisplay = "FAIL: " + val;
			        		displayColor = ChatType.STAFF.getChatDisplayColor();
			        		//ChatDrawer.addChatToBeDrawnToScreen("FAIL: " + val, 3, ChatDrawer.getCurrentResoultion().getScaledHeight() - 26, ChatType.STAFF.getChatDisplayColor());
			        	}
			        }
			        
			        //System.out.println(testDisplay);
			        
				    if (val <= 0.2478625 && val >= 0.0001) { return true; }
				}
			}			
		}
		return false;
	}
	
	public synchronized ParkourAI setHelperBlock(HelperBlock blockIn) { helperBlock = blockIn; return this; }
	public BackgroundChecker getBackgroundChecker() { return checker; }
	public HelperBlock getHelperBlock() { return helperBlock; }
	public boolean isConstantlySprinting() { return constantSprint; }
	public boolean isEdgeJumping() { return edgeJump; }
	public void setConstantlySprinting(boolean value) { constantSprint = value; }
	public void setEdgeJumping(boolean value) { edgeJump = value; }
}
