package com.Whodundid.parkourHelper;

import com.Whodundid.enhancedChat.chatUtil.ChatDrawer;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.playerUtil.PlayerFacing;
import com.Whodundid.scripts.ScriptManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

//Last edited: 10-16-18
//First Added: 11-16-17
//Author: Hunter Bragg

public class BackgroundChecker implements Runnable {

	public Minecraft mc = MainMod.getMC();
	static ScriptManager scriptMan = (ScriptManager) RegisteredSubMods.getMod(SubModType.SCRIPTS);
	static ParkourAI parkour;
	public volatile boolean running = false;
	public boolean jumpRunning = false;
	double maxHeight = 0;
	double startPos = 0;
	double startRunPos = 0;
	boolean jumped = false;
	boolean landed = false;
	boolean done = false;
	
	public BackgroundChecker(ParkourAI parkourIn) {
		parkour = parkourIn;
	}
	
	public void kill() { running = false; }
	
	@Override
	public void run() {
		scriptMan = (ScriptManager) RegisteredSubMods.getMod(SubModType.SCRIPTS);
		if (!running && mc.getRenderViewEntity() != null && !scriptMan.getGlobalKill()) {
			running = true;
			jumped = false;
			landed = false;
			//long start = System.currentTimeMillis();
			while (running && !scriptMan.getGlobalKill()) {
				
				if (parkour.helperBlock == null) {
					ParkourUtil.recalculateHelperBlock();
				}
				
				/*if (System.currentTimeMillis() - start >= 25) {
					if (Keyboard.getEventKeyState()) {
						System.out.println(System.currentTimeMillis() + ", " + Keyboard.getEventKey() + " '" + Keyboard.getEventCharacter() + "'" + ", " + PlayerFacing.getDegreeFacingDir());
					}
					start = System.currentTimeMillis();
				}*/
				
				if (!done) {
					if (mc.thePlayer.moveForward != 0) {
						done = true;
					} else {
						startRunPos = mc.thePlayer.posX;
					}
				}
				
				
				if (!jumped) {
					if (!mc.thePlayer.onGround) {
						jumped = true;
					}
					if (PlayerFacing.isXFacing()) {
						startPos = mc.thePlayer.posX;
					} else {
						startPos = mc.thePlayer.posZ;
					}
					
				} else {
					if (!landed) {
						if (mc.thePlayer.onGround) {
							landed = true;
						}
						if (PlayerFacing.isXFacing()) {
							maxHeight = Math.abs(startPos - mc.thePlayer.posX);
						} else {
							maxHeight = Math.abs(startPos - mc.thePlayer.posZ);
						}
						
					}
				}
				
				if (parkour.readyToJump()) {
					if (!jumpRunning) {
						try {
							jumpRunning = true;
							KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
							Thread starter = new Thread() {
								@Override
								public void run() {
									long startTime = System.currentTimeMillis();
									while (jumpRunning && !scriptMan.getGlobalKill()) {
										if (System.currentTimeMillis() - startTime >= 250) {
											KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
											jumpRunning = false;
											break;
										}
									}
								}
							};
							starter.start();
						} catch (Exception q) {
							q.printStackTrace();
						}
					}
				}
			}
		} else {
			parkour.setEdgeJumping(false);
		}
		System.out.println("Killed");
		ChatDrawer.clearAddedChat();
		maxHeight = 0;
	}
}
