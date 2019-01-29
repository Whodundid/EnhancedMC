package com.Whodundid.autoGM3;

import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.playerUtil.PlayerTraits;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//Last edited: 10-16-18
//First Added: 4-18-18
//Author: Hunter Bragg

public class AutoGM3 extends SubMod {

	public static Minecraft mc = MainMod.getMC();
	public boolean CommandDelayBuffer = false;
	public long CommandDelay = 0;
	public long recheckTime = 1000;
	
	public AutoGM3() {
		super(SubModType.AUTOGM3);
	}
	
	@Override
	public void eventClientTick(TickEvent.ClientTickEvent e) {
		if (isEnabled()) {
			if (PlayerTraits.isInBlock() && !PlayerTraits.isSpectator()) {
				if (!CommandDelayBuffer) {
					CommandDelayBuffer = true;
					CommandDelay = System.currentTimeMillis();
					mc.thePlayer.sendChatMessage("/gm 3");
					mc.ingameGUI.getChatGUI().addToSentMessages("/gm 1");
				} else {
					if (System.currentTimeMillis() - CommandDelay >= recheckTime) { CommandDelayBuffer = false; }
				}
			}
		}
	}
}
