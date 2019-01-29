package com.Whodundid.debug;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import com.Whodundid.worldEditor.Editor;
import com.Whodundid.worldEditor.Editor2D.Editor2D;
import com.Whodundid.worldEditor.EditorUtil.EditorLoadingScreen;
import java.lang.reflect.Field;
import com.Whodundid.clearVisuals.ClearVisuals;
import com.Whodundid.enhancedChat.EnhancedChat;
import com.Whodundid.enhancedChat.chatUtil.ChatType;
import com.Whodundid.enhancedChat.chatUtil.ChatUtil;
import com.Whodundid.enhancedChat.externalChatWindows.ChatWindowFrame;
import com.Whodundid.enhancedChat.externalChatWindows.ChatWindows;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.keySaveLoad.KeySaver;
import com.Whodundid.main.EnhancedInGameGui;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiLabel;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiLinkConfirmationDialogueBox;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.main.util.networking.NetworkHandler;
import com.Whodundid.main.util.networking.packets.LongerChatPacket;
import com.Whodundid.main.util.playerUtil.PlayerFacing;
import com.Whodundid.main.util.playerUtil.PlayerTraits;
import com.Whodundid.main.util.storageUtil.EArrayList;
import com.Whodundid.main.util.storageUtil.Vector3D;
import com.Whodundid.mainMenu.CustomInGameMenu;
import com.Whodundid.mainMenu.TestGuiMainMenu;
import com.Whodundid.miniMap.MiniMap;
import com.Whodundid.parkourHelper.HelperBlock;
import com.Whodundid.parkourHelper.ParkourAI;
import com.Whodundid.pingDrawer.Ping;
import com.Whodundid.pingDrawer.PingGui;
import com.Whodundid.scripts.builtInScripts.Script_EmptyScript;
import com.Whodundid.scripts.builtInScripts.Script_NeoJumps;
import com.Whodundid.scripts.builtInScripts.Script_StackUp;
import com.Whodundid.scripts.scriptBase.Script;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

//Last edited: 12-12-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class DebugFunctions {
	
	static Minecraft mc = MainMod.getMC();
	
	public static void runDebugFunction(IDebugCommand function) { runDebugFunction(function.getDebugCommandID()); }
	public static void runDebugFunction(int functionID) {
		try {
			switch (functionID) {
			case 0: debug_0(); break;
			case 1: debug_1(); break;
			case 2: debug_2(); break;
			case 3: debug_3(); break;
			}
		} catch (Throwable e) { e.printStackTrace(); }
	}
	
	private static void debug_0() throws Throwable {
		EnhancedChat chatMod = (EnhancedChat) RegisteredSubMods.getMod(SubModType.ENHANCEDCHAT);
		chatMod.getChatOrganizer().printOutContents();
		//ConfigMaker.addSpawnPoint();
		//EnhancedInGameGui guiMain = MainMod.getInGameGui();
		//for (IEnhancedGuiObject o : guiMain.getObjects()) {
		//	guiMain.removeObject(o);
		//}
		
		//EGuiLabel label = new EGuiLabel(guiMain, 50, 50, "The quick brown fox jumped over the incredibly large boulder at an impressive 32.04 kph.");
		//label.setDrawCentered(true).enableShadow(false).enableWordWrap(true, 190);
		//guiMain.addObject(label);
		//guiMain.addObject(new EGuiLinkConfirmationDialogueBox(guiMain, "https://www.google.com"));
		
		//ClearVisuals visuals = (ClearVisuals) RegisteredSubMods.getMod(SubModType.CLEARVISUALS);
		//visuals.getConfig().loadConfig();
		//DisplayMode m = new DisplayMode(1918, 1013);
		//Display.setDisplayMode(m);
		//Display.setLocation(-2, 0);
		//mc.displayGuiScreen(new TestGuiMainMenu());
		//MiniMap map = (MiniMap) RegisteredSubMods.getMod(SubModType.MINIMAP);
		//map.zoomMiniMap(1);
		//System.out.println(mc.thePlayer.motionX + " " + mc.thePlayer.motionZ);
		//mc.thePlayer.motionX = 0;
		//mc.thePlayer.motionZ = 0;
		//mc.thePlayer.motionY = 0;
		//mc.thePlayer.motionX = mc.thePlayer.getLookVec().xCoord * 0.65;
		//mc.thePlayer.motionZ = mc.thePlayer.getLookVec().zCoord * 0.65;
		//mc.thePlayer.motionY = mc.thePlayer.getLookVec().yCoord * 0.15;
		//Mouse.setCursorPosition(50, Mouse.getY());
		//System.out.println("eh");
		/*ParkourAI parkour = (ParkourAI) RegisteredSubMods.getMod(SubModType.PARKOUR);
		if (PlayerFacing.getFacingBlockPos() != null) {
			if (parkour.getHelperBlock() == null || !(PlayerFacing.getFacingBlockPos().equals(parkour.getHelperBlock().getHelperBlockLocation()))) {
				parkour.setHelperBlock(new HelperBlock(PlayerFacing.getFacingBlockPos()));
			}
		}
		parkour.setHelperBlock(null);
		*/
		//mc.displayGuiScreen(new ExperimentGui());
		//mc.displayGuiScreen(new CustomInGameMenu());
		//EArrayList<String> cat = new EArrayList<String>("baccon", "bigBoi");
		//cat.add("baccon", "bigBoi");
		//System.out.println(cat);
		//Ping mod = (Ping) RegisteredSubMods.getMod(SubModType.PING);
		//mod.getConfig().saveConfig(mod.getConfig().getConfigNames().get(0));
		//mod.getConfig().loadConfig(mod.getConfig().getConfigNames().get(0));
		//ChatWindows mod = (ChatWindows) RegisteredSubMods.getMod(SubModType.CHATWINDOWS);
		//mod.openChatWindow(ChatType.ALL);
		//mc.displayGuiScreen(new PingGui());
	}
	
	private static void debug_1() throws Throwable {
		/*for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
			System.out.print(i + ": ");
			if (mc.thePlayer.inventory.getStackInSlot(i) != null) {
				System.out.println(mc.thePlayer.inventory.getStackInSlot(i).getDisplayName());
			} else {
				System.out.println("Nothing");
			}
			
		}*/
		try {
			DisplayMode m = new DisplayMode(1918, 1013);
			Display.setDisplayMode(m);
			Display.setLocation(-2, 0);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private static void debug_2() throws Throwable {
		Object[] info = mc.getNetHandler().getPlayerInfoMap().toArray();
		for (Object o : info) {
			if (o instanceof NetworkPlayerInfo) {
				NetworkPlayerInfo i = ((NetworkPlayerInfo) o);
				System.out.println(i.getGameProfile().getName() + " " + i.getResponseTime());
			}
		}
	}
	
	/** Used for editor things mainly */
	private static void debug_3() throws Throwable {
		Editor.reset();
		//if (Editor.getCamPos() != null) {
		//	double chunkStartX = (Math.floor(Editor.getCamPos().x / 16) * 16);
		//	double chunkStartZ = (Math.floor(Editor.getCamPos().z / 16) * 16);
		//	System.out.println(chunkStartX + " " + chunkStartZ);
		//}
	}
}
