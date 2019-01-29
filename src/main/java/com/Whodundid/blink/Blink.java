package com.Whodundid.blink;

import org.lwjgl.input.Keyboard;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.global.subMod.SubModType;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//Last edited: 10-16-18
//First Added: 4-19-18
//Author: Hunter Bragg

public class Blink extends SubMod {
	
	static Minecraft mc = MainMod.getMC();
	protected BlinkDrawer drawer;
	public int blinkCount = 3;
	public final long blinkRefreshCoolDown = 3000;
	public boolean refreshing = false;
	public long refreshProgress = 0;
	private boolean blinking = false;
	private long blinkProgress = 0;
	private double previousX;
	private double previousZ;
	
	public Blink() {
		super(SubModType.BLINK);
		drawer = new BlinkDrawer(this);
	}
	
	@Override
	public void eventClientTick(TickEvent.ClientTickEvent e) {
		if (isEnabled()) {
			if (blinkCount < 3) {
				if (!refreshing) {
					refreshing = true;
					refreshProgress = System.currentTimeMillis();
				} else {
					if (System.currentTimeMillis() - refreshProgress >= blinkRefreshCoolDown) {
						blinkCount++;
						refreshing = false;
					}
				}
			}
			if (blinking) {
				if (System.currentTimeMillis() - blinkProgress >= 25) {
					mc.thePlayer.motionX = previousX;
					mc.thePlayer.motionZ = previousZ;					
					previousX = 0;
					previousZ = 0;
					blinking = false;
				}
			}
		}		
	}
	
	@Override
	public void eventPostOverlayRenderTick(RenderGameOverlayEvent.Post e) {
		if (isEnabled()) { drawer.drawBlinkCharges(); }
	}
	
	@Override
	public void eventMouse(MouseEvent e) {
		if (isEnabled()) {
			if (e.button == 1 && e.buttonstate) {
				if (mc.thePlayer.getHeldItem() == null) {
					if (blinkCount > 0) {
						blinkCount--;
						
						blinking = true;
						blinkProgress = System.currentTimeMillis();
						
						previousX = mc.thePlayer.motionX;
						previousZ = mc.thePlayer.motionZ;
						
						//System.out.println(mc.thePlayer.getLookVec());
						
						if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())) {
							
						} else if (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) {
							
						} else if (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
							
						} else {
							mc.thePlayer.motionX = mc.thePlayer.getLookVec().xCoord * 9.5;
							mc.thePlayer.motionZ = mc.thePlayer.getLookVec().zCoord * 9.5;
						}
						
						if (blinkCount == 2) {
							//mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation(MainMod.MODID, "blink.blinkSound1"), 1.0F));
						} else if (blinkCount == 1) {
							//mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation(MainMod.MODID, "blink.blinkSound2"), 1.0F));
						} else {
							//mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation(MainMod.MODID, "blink.blinkSound3"), 1.0F));
						}		
					}
				}
			}
		}
	}
}
