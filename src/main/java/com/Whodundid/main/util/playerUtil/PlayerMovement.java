package com.Whodundid.main.util.playerUtil;

import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.parkourHelper.ParkourAI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class PlayerMovement {
	
	static Minecraft mc = Minecraft.getMinecraft();
	static ParkourAI parkour = (ParkourAI) RegisteredSubMods.getMod(SubModType.PARKOUR);
	
	public static void pressMovementKey(PlayerFacing.Direction dir) {
		switch (dir) {
		case N: KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true); break;
		case E:  KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true); break;
		case S: KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true); break;
		case W: KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true); break;
		default: break;		
		}
	}
	
	public static void unpressMovementKey(PlayerFacing.Direction dir) {
		switch (dir) {
		case N: KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false); break;
		case E: KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false); break;
		case S: KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false); break;
		case W: KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false); break;
		default: break;		
		}
	}
	
	public static void setSneaking() { setSneaking(true); }	
	public static void setSneaking(boolean state) { KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), state); }
	
	public static void setJumping() { setJumping(true); }	
	public static void setJumping(boolean state) { KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), state); }
	
	public static void setSprinting() { setSprinting(true); }	
	public static void setSprinting(boolean state) { parkour.setConstantlySprinting(state); }
	
	public static void stopMovement() {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
	}
	
	public static void instantlyStopAllMovement() {
		stopMovement();
		mc.thePlayer.motionX = 0;
		mc.thePlayer.motionY = 0;
		mc.thePlayer.motionZ = 0;
	}
}
