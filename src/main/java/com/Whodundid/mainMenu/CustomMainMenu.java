package com.Whodundid.mainMenu;

import com.Whodundid.main.MainMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;

public class CustomMainMenu {
	
	static Minecraft mc = MainMod.getMC();
	public static boolean mainMenuOpen = false;
	
	public static void guiInit() {
		if (!mainMenuOpen) {
			mainMenuOpen = true;
			mc.displayGuiScreen(new TestGuiMainMenu());
		} else if (!(mc.currentScreen instanceof GuiMainMenu)) { mainMenuOpen = false; }
	}
	
	public static boolean isMainMenuOpen() { return mainMenuOpen; }
}
