package com.Whodundid.mainMenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

//Last edited: 9-24-18
//First Added: 9-10-18
//Author: Hunter Bragg

public class TestGuiMainMenu extends GuiMainMenu {
	
	private static final Random RANDOM = new Random();
	GuiButton testBtn;
	String splashText = "misingno";
	
	@Override
	public void initGui() {
		super.initGui();
		
		generateSplashList();
		
		Iterator<GuiButton> it = buttonList.iterator();
		while (it.hasNext()) {
			GuiButton b = it.next();
			if (b.id == 14) {
				it.remove();
				break;
			}
		}
		buttonList.add(testBtn = new GuiButton(523, width / 2 + 2, height / 4 + 96, 98, 20, "testytest"));
    }
	
	private void generateSplashList() {
		try {
			ArrayList<String> list = new ArrayList();
			list.add("hello");
			list.add("bagels!");
			list.add("this is a splash msg! wow!");
	        splashText = list.get(RANDOM.nextInt(list.size()));
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawGradientRect(0, 0, width, height, -2130706433, 16777215);
        drawGradientRect(0, 0, width, height, 0, Integer.MIN_VALUE);
		
		for (int q = 0; q < this.buttonList.size(); q++) {
            buttonList.get(q).drawButton(mc, mouseX, mouseY);
        }
        for (int v = 0; v < this.labelList.size(); v++) {
            labelList.get(v).drawLabel(mc, mouseX, mouseY);
        }
        
        drawSplash();
    }
	
	private void drawSplash() {
		GlStateManager.pushMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate(width / 2 + 90, 70.0F, 0.0F);
        GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
        float f = 1.8F - MathHelper.abs(MathHelper.sin(mc.getSystemTime() % 1000L / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
        f = f * 100.0F / (fontRendererObj.getStringWidth(splashText) + 32);
        GlStateManager.scale(f, f, f);
        this.drawCenteredString(fontRendererObj, splashText, 0, -8, -256);
        GlStateManager.popMatrix();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1) {
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		}
	}
	
	@Override
	public void onGuiClosed() {
		CustomMainMenu.mainMenuOpen = false;
    }
}
