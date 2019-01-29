package com.Whodundid.debug;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import com.Whodundid.enhancedChat.chatUtil.ChatType;
import com.Whodundid.enhancedChat.externalChatWindows.ChatWindows;
import com.Whodundid.enhancedChat.gameChat.InGameChatWindow;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiDialogueBox;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiDialogueBox.DialogueBoxTypes;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiLabel;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiLinkConfirmationDialogueBox;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiScrollList;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiSlider;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiTextArea;
import com.Whodundid.main.util.enhancedGui.guiObjects.ERightClickMenu;
import com.Whodundid.main.util.storageUtil.EDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

//Last edited: 10-14-18
//First Added: 9-5-18
//Author: Hunter Bragg

public class ExperimentGui extends EnhancedGui {
	
	static Minecraft mc = MainMod.getMC();
	ArrayList<Integer> values = new ArrayList();
	EGuiButton test1, test2, test3, test4;
	public boolean bool1, bool2, bool3, bool4;
	public int color = 0;
	EGuiLabel label;
	EGuiTextArea textArea;
	EGuiDialogueBox dialogueBox;
	EGuiScrollList scrollList;
	EGuiSlider slider;
	ERightClickMenu rcm;
	InGameChatWindow chatWindow;
	
	public ExperimentGui() { super(); }
	public ExperimentGui(EnhancedGui oldGui) { super(oldGui); }
	public ExperimentGui(int posX, int posY) { super(posX, posY); }
	public ExperimentGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		setFocusLockObject(this);
		
		header.setDisplayStringColor(0x000000);
		
		//enableHeader(false);
		//EScreenLocationSelector selector = new EScreenLocationSelector(this, wPos - 300, hPos - 200, 100);
		//EGuiHeader header = new EGuiHeader(this);
		//addObject(test1 = new EGuiButton(this, wPos + 32, hPos + 100, 60, 20, "Lobby"));
		//addObject(selector);
		//addObject(header);
		//buttonList.add(new GuiButton(0, wPos + 70, hPos + 70, 200, 20, "no"));
		
		
		//EGuiTextArea test = new EGuiTextArea(dialogueBox, dBoxDim.startX + 2, dBoxDim.startY + 2, 150, 150);
		//dialogueBox.addObject(test.setZLevel(1));
		
		//label = new EGuiLabel(this, wPos, hPos + 60, "The quick brown fox jumped over the incredibly large boulder at an impressive 32.04 kph.");
		//label.setDrawCentered(true).enableShadow(false).enableWordWrap(true, 190);
		//addObject(label);
		
		//dialogueBox = new EGuiDialogueBox(this, 50, 50, 200, 100, DialogueBoxTypes.ok);
		//dialogueBox.setDisplayString("Notice");
		//dialogueBox.setMessage("Press ok!");
		//addObject(dialogueBox);
		
		textArea = new EGuiTextArea(this, startX + 3, startY + 3, width - 6, 150, true).setDrawLineNumbers(true);
		addObject(textArea);
		
		//https://hypixel.net/my2018/?5c2a973544f4e2a67393289c
		
		//addObject(new EGuiLinkConfirmationDialogueBox(this, "https://www.google.com"));
		
		//chatWindow = new InGameChatWindow(this);
		//addObject(chatWindow);
		clearFocusLockObject();
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		drawDefaultBackground();
		
		//System.out.println(this.getObjectUnderMouse());
		/*
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        //GlStateManager.disableTexture2D();
		Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(wPos + 150, hPos - 150, -1.0D).tex(1.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(wPos - 250, hPos - 150, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(wPos - 150, hPos + 150, 0.0D).tex(0.0D, 1.0D).color(255, 255, 255, 255).endVertex();
        //worldrenderer.pos(wPos + 150, hPos + 150, 0.0D).tex(1.0D, 1.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(wPos + 250, hPos + 150, 0.0D).tex(1.0D, 1.0D).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        //GlStateManager.enableTexture2D();
        //GlStateManager.shadeModel(7424);
        //GlStateManager.enableAlpha();
        //GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        */
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		color = Color.HSBtoRGB(System.currentTimeMillis() % 10000L / 10000.0f, 0.8f, 1f);
		if (label != null) { label.setDisplayStringColor(-color + 0xff222222); }
		header.setMainColor(color);
	}
	
	@Override
	public void mouseClicked(int mX, int mY, int button) throws IOException {
		super.mouseClicked(mX, mY, button);
	}
	
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		super.mouseReleased(mX, mY, button);
	}
	
	@Override
	public void mouseScrolled(int change) {
		super.mouseScrolled(change);
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(test1)) {
			//ChatWindowFrame f = c.getChatWindow(ChatType.LOBBY);
			//if (f != null) { f.setVisible(true); }
			ChatWindows.openChatWindow(ChatType.LOBBY);
		}
	}
	
	public void testMethod1() {
		
	}
	
	public void testMethodWithArgs(String[] args) {
		
	}
	
	public int testMethod2() {
		return 0;
	}
	
	public boolean testMethodBoolean(String[] args) {
		return false;
	}
 }
