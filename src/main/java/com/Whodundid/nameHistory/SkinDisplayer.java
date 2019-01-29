package com.Whodundid.nameHistory;

import com.Whodundid.hotkeys.subModSpecific.Global_HotKeys;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class SkinDisplayer extends GuiScreen {

	ResourceLocation skin;	
	Minecraft mc = Minecraft.getMinecraft();
	int wPos, hPos;
	GuiButton closeBtn;
	int gWidth = 200, gHeight = 256;
	
	public SkinDisplayer(ResourceLocation pic) {
		skin = pic;
	}
	
	@Override
	public void initGui() {
		buttonList.add(closeBtn = new GuiButton(0, wPos - 23, hPos - 121, 46, 20, "Close"));
	}
	
	@Override
	public void drawScreen(int x, int y, float ticks) {
		int guiX = (width - gWidth) / 2;
		int guiY = (height - gHeight) / 2;
		mc.renderEngine.bindTexture(skin);
		drawTexturedModalRect(guiX, guiY, 0, 0, gWidth, gHeight);
		super.drawScreen(x, y, ticks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.equals(closeBtn)) {
			this.mc.displayGuiScreen((GuiScreen)null);
            if (this.mc.currentScreen == null) {
            	this.mc.setIngameFocus();
            }
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1) {
        	this.mc.displayGuiScreen((GuiScreen)null);
            if (this.mc.currentScreen == null) {
            	this.mc.setIngameFocus();
            }
		}
	}
}
