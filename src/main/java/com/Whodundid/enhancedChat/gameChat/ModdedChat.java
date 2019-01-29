package com.Whodundid.enhancedChat.gameChat;

import com.Whodundid.enhancedChat.EnhancedChat;
import com.Whodundid.enhancedChat.chatUtil.ChatType;
import com.Whodundid.enhancedChat.chatUtil.ChatUtil;
import com.Whodundid.main.EnhancedInGameGui;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiTextField;
import com.Whodundid.main.util.miscUtil.EFontRenderer;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.ClientCommandHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

//Last edited: Jan 10, 2019
//First Added: Nov 3, 2018
//Author: Hunter Bragg

/**This has changed. Yet to update.
 * 
 * ModdedChat must extend GuiChat in order for tab completions to properly work.
 * While not technically an EnhancedGui, ModdedChat incorporates most of the
 * EnhancedGui framework so that it is still compatible with IEnhancedGuiObjects.
 * There are some noteworthy differences to be aware of when working with
 * ModdedChat's EnhancedGui architecture however.
 * 1. There is no header.
 * 2. There is no past gui history traversal system
 * 3. ModdedChats cannot have a custom position set on init
 */
public class ModdedChat extends GuiChat {
	
	public static final Logger logger = LogManager.getLogger();
    protected EnhancedChat mod = (EnhancedChat) RegisteredSubMods.getMod(SubModType.ENHANCEDCHAT);
    public ModdedChat instance;
    private long lastMouseEvent, timeSinceLastClick;
	public int wPos = width / 2, hPos = height / 2;
	public int startX, startY, endX, endY;
	public int gWidth = 200, gHeight = 256;
	public int mX, mY;
	public int lastMouseButton = -1;
	public int lastScrollChange = 0;
	public boolean mouseClicked = false;
	public boolean leftClick = false;
	public boolean rightClick = false;
	public boolean middleClick = false;
	public EFontRenderer fontRenderer;
	public RenderItem itemRenderer;
    ChatType current = ChatType.NONE;
    public String defaultInputFieldText = "";
    protected ModdedGuiNewChat chatHistory;
    public String[] latestAutoComplete = null;
    public List<String> foundPlayerNames = Lists.<String>newArrayList();
    public boolean playerNamesFound;
    public boolean waitingOnAutocomplete;
    public int autocompleteIndex;
    public String historyBuffer = "";
    public int sentHistoryCursor = -1;
    EnhancedInGameGui hudInstance = MainMod.getInGameGui();
    
    public ModdedChat() { this(""); }
    public ModdedChat(String defaultInput) { defaultInputFieldText = defaultInput; instance = this; }
    
    //-----------------
    //GuiChat Overrides
    //-----------------
    
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
        sentHistoryCursor = mc.ingameGUI.getChatGUI().getSentMessages().size();
        if (mc.ingameGUI.getChatGUI() instanceof ModdedGuiNewChat) { chatHistory = (ModdedGuiNewChat) mc.ingameGUI.getChatGUI(); }
        if (mod.getEnableChatWindows()) { hudInstance.addChatWindow(defaultInputFieldText); }
        else { mc.displayGuiScreen(new GuiChat()); }
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn, float ticks) {
		mX = mXIn; mY = mYIn;
		if (!checkForChatWindow()) { closeGui(); }
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		for (GuiButton b : buttonList) { b.drawButton(this.mc, mX, mY); }
		for (GuiLabel l : labelList) { l.drawLabel(this.mc, mX, mY); }
		GlStateManager.popMatrix();
	}
	
	//updateScreen
	@Override
	public void updateScreen() {
		hudInstance.updateScreen();
	}
    
	//basic inputs
	@Override
	protected void mouseClicked(int mX, int mY, int button) throws IOException {
		hudInstance.mousePressed(mX, mY, button);
	}
	protected void mouseUnclicked(int mX, int mY, int button) {
		hudInstance.mouseReleased(mX, mY, button);
		super.mouseReleased(mX, mY, button);
	}
	@Override
	public void mouseClickMove(int mX, int mY, int button, long timeSinceLastClick) {
		hudInstance.mouseDragged(mX, mY, button, timeSinceLastClick);
		super.mouseClickMove(mX, mY, button, timeSinceLastClick);
	}
    @Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
        waitingOnAutocomplete = false;
        if (keyCode == 15) { autocompletePlayerNames(); }
        else { playerNamesFound = false; }

        if (keyCode == 1) { closeGui(); }
        else if (keyCode != 28 && keyCode != 156) {
            if (keyCode == 200) { getSentHistory(-1); }
            else if (keyCode == 208) { getSentHistory(1); }
            else if (keyCode == 201) { mc.ingameGUI.getChatGUI().scroll(mc.ingameGUI.getChatGUI().getLineCount() - 1); }
            else if (keyCode == 209) { mc.ingameGUI.getChatGUI().scroll(-mc.ingameGUI.getChatGUI().getLineCount() + 1); }
            //else if (!mainChatWindow.getEntryField().hasFocus()) { mainChatWindow.getEntryField().keyPressed(typedChar, keyCode); }
        } else {
        	/*if (!mainChatWindow.getEntryField().hasFocus()) {
        		if (!mainChatWindow.getEntryField().getText().isEmpty()) {
        			sendChatMessage(mainChatWindow.getEntryField().getText());
        			mainChatWindow.setText("", true);
        		}
        	}*/
            closeGui();
        }
    }
    
    //basic input handlers
	@Override
    public void handleMouseInput() throws IOException {
    	mX = (Mouse.getEventX() * width / mc.displayWidth);
        mY = (height - Mouse.getEventY() * height / mc.displayHeight - 1);
		int button = Mouse.getEventButton();
        
        if (Mouse.hasWheel()) {
        	lastScrollChange = Integer.signum(Mouse.getEventDWheel());
        	if (lastScrollChange != 0) { hudInstance.mouseScrolled(lastScrollChange); }
        }
        
        if (Mouse.getEventButtonState()) {
        	lastMouseEvent = Minecraft.getSystemTime();
        	lastMouseButton = button;
            mouseClicked(mX, mY, lastMouseButton);
        } else if (button != -1) {
        	lastMouseButton = -1;
        	mouseUnclicked(mX, mY, button);
        } else if (lastMouseButton != -1 && this.lastMouseEvent > 0L) {
        	timeSinceLastClick = Minecraft.getSystemTime() - lastMouseEvent;
            mouseClickMove(mX, mY, lastMouseButton, timeSinceLastClick);
        }
    }
    @Override
	public void handleKeyboardInput() throws IOException {
    	if (Keyboard.getEventKeyState()) {
			keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
			hudInstance.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		} else {
			hudInstance.keyReleased(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}
		mc.dispatchKeypresses();
	}
    
    @Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		this.mc = mc;
		itemRender = mc.getRenderItem();
		fontRenderer = MainMod.getFontRenderer();
		fontRendererObj = mc.fontRendererObj;
		this.width = width;
		this.height = height;
		if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Pre(this, this.buttonList))) {
			buttonList.clear();
			initGui();
		}
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post(this, this.buttonList));
	}
	
	//resize
	@Override
	public void onResize(Minecraft mcIn, int width, int height) {
		setWorldAndResolution(MainMod.getMC(), width, height);
	}
	
    //send chat
    @Override
	public void sendChatMessage(String msg) { sendChatMessage(msg, true); }
	@Override
    public void sendChatMessage(String msg, boolean addToChat) {
        if (addToChat) { mc.ingameGUI.getChatGUI().addToSentMessages(msg); }
        if (ClientCommandHandler.instance.executeCommand(mc.thePlayer, msg) != 0) { return; }
        ChatUtil.sendLongerChatMessage(msg);
    }
	
	@Override public boolean doesGuiPauseGame() { return false; }
	
	//-----------------
	//GuiChat Overrides
	//-----------------
    
    @Override
	protected void setText(String newChatText, boolean shouldOverwrite) {
    	if (hudInstance.hasAChatWindow()) {
    		EGuiTextField f = hudInstance.getFocusedChatWindow().getEntryField();
    		if (shouldOverwrite) { f.setText(newChatText); }
            else { f.writeText(newChatText); }
    	}
    }
    
    @Override
    public void autocompletePlayerNames() {
    	if (hudInstance.hasAChatWindow()) {
    		EGuiTextField f = hudInstance.getFocusedChatWindow().getEntryField();
    		if (playerNamesFound) {
                f.deleteFromCursor(f.func_146197_a(-1, f.getCursorPosition(), false) - f.getCursorPosition());
                if (autocompleteIndex >= foundPlayerNames.size()) { autocompleteIndex = 0; }
            } else {
            	int i = f.func_146197_a(-1, f.getCursorPosition(), false);
                foundPlayerNames.clear();
                autocompleteIndex = 0;
                String s = f.getText().substring(i).toLowerCase();
                String s1 = f.getText().substring(0, f.getCursorPosition());
                sendAutocompleteRequest(s1, s);

                if (foundPlayerNames.isEmpty()) { return; }
                playerNamesFound = true;
                f.deleteFromCursor(i - f.getCursorPosition());
            }

            if (foundPlayerNames.size() > 1) {
                StringBuilder stringbuilder = new StringBuilder();

                for (String s2 : foundPlayerNames) {
                    if (stringbuilder.length() > 0) { stringbuilder.append(", "); }
                    stringbuilder.append(s2);
                }
                mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText(stringbuilder.toString()), 1);
            }
            f.writeText(EnumChatFormatting.getTextWithoutFormattingCodes(foundPlayerNames.get(autocompleteIndex++)));
    	}
    }
    
    private void sendAutocompleteRequest(String p_146405_1_, String p_146405_2_) {
        if (p_146405_1_.length() >= 1) {
            ClientCommandHandler.instance.autoComplete(p_146405_1_, p_146405_2_);
            BlockPos blockpos = null;

            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) { blockpos = mc.objectMouseOver.getBlockPos(); }
            mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(p_146405_1_, blockpos));
            waitingOnAutocomplete = true;
        }
    }
    
    @Override
    public void onAutocompleteResponse(String[] response) {
        if (waitingOnAutocomplete) {
        	if (hudInstance.hasAChatWindow()) {
        		EGuiTextField f = hudInstance.getFocusedChatWindow().getEntryField();
                playerNamesFound = false;
                foundPlayerNames.clear();

                String[] complete = ClientCommandHandler.instance.latestAutoComplete;
                if (complete != null) { response = ObjectArrays.concat(complete, response, String.class); }

                for (String s : response) { if (s.length() > 0) { foundPlayerNames.add(s); } }

                String s1 = f.getText().substring(f.func_146197_a(-1, f.getCursorPosition(), false));
                String s2 = StringUtils.getCommonPrefix(response);
                s2 = EnumChatFormatting.getTextWithoutFormattingCodes(s2);

                if (s2.length() > 0 && !s1.equalsIgnoreCase(s2)) {
                    f.deleteFromCursor(f.func_146197_a(-1, f.getCursorPosition(), false) - f.getCursorPosition());
                    f.writeText(s2);
                }
                else if (foundPlayerNames.size() > 0) {
                    playerNamesFound = true;
                    autocompletePlayerNames();
                }
        	}
        }
    }
    
    @Override
    public void getSentHistory(int msgPos) {
    	if (hudInstance.hasAChatWindow()) {
    		EGuiTextField f = hudInstance.getFocusedChatWindow().getEntryField();
    		int i = sentHistoryCursor + msgPos;
            int j = mc.ingameGUI.getChatGUI().getSentMessages().size();
            i = MathHelper.clamp_int(i, 0, j);

            if (i != sentHistoryCursor) {
                if (i == j) {
                    sentHistoryCursor = j;
                    f.setText(historyBuffer);
                } else {
                    if (sentHistoryCursor == j) { historyBuffer = f.getText(); }
                    f.setText(mc.ingameGUI.getChatGUI().getSentMessages().get(i));
                    sentHistoryCursor = i;
                }
            }
    	}
    }
    
    @Override
	public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        mc.ingameGUI.getChatGUI().resetScroll();
    }
    
    public boolean checkForChatWindow() { return hudInstance.hasAChatWindow(); }
	public InGameChatWindow getChatWindowInstance() { if (checkForChatWindow()) { return hudInstance.getFocusedChatWindow(); } return null; }
	public void closeGui() { hudInstance.removeAllUnpinnedChatWindows(); mc.displayGuiScreen(null); mc.setIngameFocus(); }
	
	protected int drawString(String text, int x, int y, int color) { return fontRenderer.drawStringI(text, x, y, color); }
    protected int drawCenteredString(String text, int x, int y, int color) { return fontRenderer.drawStringI(text, x - fontRenderer.getStringWidth(text) / 2, y, color); }
	protected int drawStringWithShadow(String text, int x, int y, int color) { return fontRenderer.drawStringWithShadowI(text, x, y, color); }
	protected int drawCenteredStringWithShadow(String text, int x, int y, int color) { return fontRenderer.drawStringWithShadowI(text, x - fontRenderer.getStringWidth(text) / 2, y, color); }
	protected int drawString(String text, int x, int y, int color, float height) { return fontRenderer.drawStringIH(text, x, y, color, height); }
    protected int drawCenteredString(String text, int x, int y, int color, float height) { return fontRenderer.drawStringIH(text, x - fontRenderer.getStringWidth(text) / 2, y, color, height); }
	protected int drawStringWithShadow(String text, int x, int y, int color, float height) { return fontRenderer.drawStringWithShadowIH(text, x, y, color, height); }
	protected int drawCenteredStringWithShadow(String text, int x, int y, int color, float height) { return fontRenderer.drawStringWithShadowIH(text, x - fontRenderer.getStringWidth(text) / 2, y, color, height); }
}
