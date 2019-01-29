package com.Whodundid.enhancedChat.gameChat;

import com.Whodundid.enhancedChat.EnhancedChat;
import com.Whodundid.enhancedChat.chatUtil.ChatType;
import com.Whodundid.enhancedChat.chatUtil.TimedChatLine;
import com.Whodundid.main.EnhancedInGameGui;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class ModdedGuiNewChat extends GuiNewChat {
	
	protected static Minecraft mc;
	protected static final Logger logger = LogManager.getLogger();
	protected EnhancedChat mod = (EnhancedChat) RegisteredSubMods.getMod(SubModType.ENHANCEDCHAT);
	public int historyLength = 1000;
	public final List<String> sentMessages = Lists.<String>newArrayList();
	public final List<TimedChatLine> totalLines = Lists.<TimedChatLine>newArrayList();
	public final List<TimedChatLine> chatHistory = Lists.<TimedChatLine>newArrayList();
	public List<TimedChatLine> currentChat = new ArrayList();
	protected ChatType currentChatType = ChatType.ALL;
	public int scrollPos;
	public boolean isScrolled;
	
	public ModdedGuiNewChat(Minecraft mcIn) {
		super(mcIn);
		mc = mcIn;
	}

	public void drawChat(int updateCounterIn) {
		if (mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN && currentChat != null) {
			int i = getLineCount();
			boolean flag = false;
			int j = 0;
			int k = currentChat.size();
			float f = mc.gameSettings.chatOpacity * 0.9F + 0.1F;
			if (k > 0) {
				flag = getChatOpen();
				float f1 = getChatScale();
				int l = MathHelper.ceiling_float_int(getChatWidth() / f1);
				GlStateManager.pushMatrix();
				GlStateManager.translate(2.0F, 20.0F, 0.0F);
				GlStateManager.scale(f1, f1, 1.0F);
				for (int i1 = 0; i1 + scrollPos < currentChat.size() && i1 < i; ++i1) {
					//System.out.println(i1 + scrollPos);
					ChatLine chatline = currentChat.get(i1 + scrollPos);
					if (chatline != null) {
						int j1 = updateCounterIn - chatline.getUpdatedCounter();
						if (j1 < 200 || flag) {
							double d0 = j1 / 200.0D;
							d0 = 1.0D - d0;
							d0 = d0 * 10.0D;
							d0 = MathHelper.clamp_double(d0, 0.0D, 1.0D);
							d0 = d0 * d0;
							int l1 = (int) (255.0D * d0);
							if (flag) {
								l1 = 255;
							}
							l1 = (int) (l1 * f);
							++j;
							if (l1 > 3) {
								int i2 = 0;
								int j2 = -i1 * 9;
								drawRect(i2, j2 - 9, i2 + l + 4, j2, l1 / 2 << 24);
								String s = chatline.getChatComponent().getFormattedText();
								GlStateManager.enableBlend();
								mc.fontRendererObj.drawStringWithShadow(s, i2, j2 - 8, 16777215 + (l1 << 24));
								GlStateManager.disableAlpha();
								GlStateManager.disableBlend();
							}
						}
					}
				}
				if (flag) {
					int k2 = mc.fontRendererObj.FONT_HEIGHT;
					GlStateManager.translate(-3.0F, 0.0F, 0.0F);
					int l2 = k * k2 + k;
					int i3 = j * k2 + j;
					int j3 = scrollPos * i3 / k;
					int k1 = i3 * i3 / l2;
					if (l2 != i3) {
						int k3 = j3 > 0 ? 170 : 96;
						int l3 = isScrolled ? 13382451 : 3355562;
						drawRect(0, -j3, 2, -j3 - k1, l3 + (k3 << 24));
						drawRect(2, -j3, 1, -j3 - k1, 13421772 + (k3 << 24));
					}
				}
				GlStateManager.popMatrix();
			}
		}
	}

	public void clearChatMessages() {
		chatHistory.clear();
		totalLines.clear();
		sentMessages.clear();
		currentChat.clear();
	}
	
	public void printChatMessageWithOptionalDeletion(IChatComponent chatComponent, int chatLineId) {
		setChatLine(chatComponent, chatLineId, mc.ingameGUI.getUpdateCounter(), false);
		logger.info("[CHAT] " + chatComponent.getUnformattedText());
	}

	private void setChatLine(IChatComponent chatComponent, int chatLineId, int updateCounter, boolean deletion) {
		if (chatLineId != 0) { deleteChatLine(chatLineId); }
		int i = MathHelper.floor_float(getChatWidth() / getChatScale());
		List<IChatComponent> list = GuiUtilRenderComponents.func_178908_a(chatComponent, i, mc.fontRendererObj, false, false);
		boolean flag = getChatOpen();
		for (IChatComponent ichatcomponent : list) {
			if (flag && scrollPos > 0) {
				isScrolled = true;
				scroll(1);
			}
			TimedChatLine l = new TimedChatLine(updateCounter, ichatcomponent, chatLineId);
			chatHistory.add(0, l);
			if (currentChatType.equals(ChatType.ALL)) { currentChat.add(0, l); }
		}
		while (chatHistory.size() > historyLength) { chatHistory.remove(chatHistory.size() - 1); }
		if (!deletion) {
			totalLines.add(0, new TimedChatLine(updateCounter, chatComponent, chatLineId));
			while (totalLines.size() > historyLength) { totalLines.remove(totalLines.size() - 1); }
		}
		EnhancedInGameGui hudInstance = MainMod.getInGameGui();
		//hudInstance.sendChatToCorrectWindow(ChatType.ALL, new TimedChatLine(updateCounter, chatComponent, chatLineId));
		mod.getChatOrganizer().readChat(new TimedChatLine(updateCounter, chatComponent, chatLineId));
	}

	public void refreshChat() {
		chatHistory.clear();
		resetScroll();
		for (int i = totalLines.size() - 1; i >= 0; --i) {
			ChatLine chatline = totalLines.get(i);
			setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
		}
	}
	
	public void addToSentMessages(String msgIn) {
		if (sentMessages.isEmpty() || !sentMessages.get(sentMessages.size() - 1).equals(msgIn)) { sentMessages.add(msgIn); }
	}
	
	public void resetScroll() {
		scrollPos = 0;
		isScrolled = false;
	}
	
	public void scroll(int p_146229_1_) {
		scrollPos += p_146229_1_;
		int i = chatHistory.size();
		if (scrollPos > i - getLineCount()) { scrollPos = i - getLineCount(); }
		if (scrollPos <= 0) {
			scrollPos = 0;
			isScrolled = false;
		}
	}
	
	public IChatComponent getChatComponent(int p_146236_1_, int p_146236_2_) {
		if (!getChatOpen()) { return null; }
		ScaledResolution scaledresolution = new ScaledResolution(mc);
		int i = scaledresolution.getScaleFactor();
		float f = getChatScale();
		int j = p_146236_1_ / i - 3;
		int k = p_146236_2_ / i - 27;
		j = MathHelper.floor_float(j / f);
		k = MathHelper.floor_float(k / f);
		if (j >= 0 && k >= 0) {
			int l = Math.min(getLineCount(), chatHistory.size());
			if (j <= MathHelper.floor_float(getChatWidth() / getChatScale()) && k < mc.fontRendererObj.FONT_HEIGHT * l + l) {
				int i1 = k / mc.fontRendererObj.FONT_HEIGHT + scrollPos;
				if (i1 >= 0 && i1 < chatHistory.size()) {
					ChatLine chatline = chatHistory.get(i1);
					int j1 = 0;
					for (IChatComponent ichatcomponent : chatline.getChatComponent()) {
						if (ichatcomponent instanceof ChatComponentText) {
							j1 += mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText) ichatcomponent).getChatComponentText_TextValue(), false));
							if (j1 > j) { return ichatcomponent; }
						}
					}
				}
			}
		}
		return null;
	}
	
	public boolean getChatOpen() { return mc.currentScreen instanceof GuiChat; }
	
	public void deleteChatLine(int lineIdIn) {
		if (mc.currentScreen instanceof ModdedChat) {
			ModdedChat chat = ((ModdedChat) mc.currentScreen);
			chat.getChatWindowInstance().deleteChatLine(lineIdIn);
		}
		Iterator<TimedChatLine> iterator = chatHistory.iterator();
		while (iterator.hasNext()) {
			ChatLine chatline = iterator.next();
			if (chatline.getChatLineID() == lineIdIn) {
				iterator.remove();
			}
		}
		iterator = totalLines.iterator();
		while (iterator.hasNext()) {
			ChatLine chatline1 = iterator.next();
			if (chatline1.getChatLineID() == lineIdIn) {
				iterator.remove();
				break;
			}
		}
	}
	
	public void setSelectedChat(ChatType typeIn) {
		/*
		ArrayList<String> list;
		if (typeIn.equals(ChatType.ALL)) {
			currentChat.clear();
			for (TimedChatLine l : chatHistory) { currentChat.add(l); }
		} else if (!typeIn.equals(ChatType.NONE)) {
			list = mod.getChatOrganizer().getChat(typeIn);
			currentChat.clear();
			for (int i = list.size() - 1; i >= 0; i--) {
				ChatComponentText line = new ChatComponentText(list.get(i));
				currentChat.add(new TimedChatLine(mc.ingameGUI.getUpdateCounter(), line, i));
			}
		} else { currentChat.clear(); }
		while (currentChat.size() > historyLength) { currentChat.remove(chatHistory.size() - 1); }
		resetScroll();
		currentChatType = typeIn;
		*/
	}
	
	public void updateChatContents(ChatType typeIn, String newLine) {
		if (typeIn.equals(currentChatType)) {
			ChatComponentText line = new ChatComponentText(newLine);
			currentChat.add(0, new TimedChatLine(mc.ingameGUI.getUpdateCounter(), line, currentChat.size() - 1));
		}
	}

	public static int calculateChatboxWidth(float p_146233_0_) {
		int i = 320;
		int j = 40;
		return MathHelper.floor_float(p_146233_0_ * (i - j) + j);
	}

	public static int calculateChatboxHeight(float p_146243_0_) {
		int i = 180;
		int j = 20;
		return MathHelper.floor_float(p_146243_0_ * (i - j) + j);
	}
	
	public void printChatMessage(IChatComponent chatComponent) { printChatMessageWithOptionalDeletion(chatComponent, 0); }
	public int getChatWidth() { return calculateChatboxWidth(mc.gameSettings.chatWidth); }
	public int getChatHeight() { return calculateChatboxHeight(getChatOpen() ? mc.gameSettings.chatHeightFocused : mc.gameSettings.chatHeightUnfocused); }
	public float getChatScale() { return mc.gameSettings.chatScale; }
	public int getLineCount() { return getChatHeight() / 9; }
	public List<String> getSentMessages() { return sentMessages; }
}