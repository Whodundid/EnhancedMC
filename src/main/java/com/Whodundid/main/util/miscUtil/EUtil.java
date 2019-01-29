package com.Whodundid.main.util.miscUtil;

import com.Whodundid.main.MainMod;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.main.util.storageUtil.EArrayList;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.Util;
import org.lwjgl.input.Keyboard;

//Last edited: Jan 16, 2019
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public class EUtil {
	
	public static Minecraft mc = MainMod.getMC();
	private static FontRenderer fr = mc.fontRendererObj;
	
	public static int manipulateColor(int color, float factor) {
		int r, g, b;
		String code = String.valueOf(color);
		if (code.substring(0, 1).equals("0x")) {
			if (code.length() > 7) {
				r = Math.round(Integer.parseInt(code.substring(2, 3)) * factor);
				g = Math.round(Integer.parseInt(code.substring(4, 5)) * factor);
				b = Math.round(Integer.parseInt(code.substring(6, 7)) * factor);
				String returnCode = "0x" + String.valueOf(Math.min(r, 255) + String.valueOf(Math.min(g, 255) + String.valueOf(Math.min(b, 255))));
				return Integer.parseInt(returnCode);
			}
		} else {
			//TOO TIRED!
		}
		return 0x00000;
    }
	
	public static int getHexCode(Color colorIn) {
		return Integer.parseInt("0x" + Integer.toHexString(colorIn.getRGB()).substring(2));
	}
	
	public static Color getRGB(String colorIn) {
		return new Color(Integer.valueOf(colorIn.substring(2, 4), 16),
						 Integer.valueOf(colorIn.substring(4, 6), 16),
						 Integer.valueOf(colorIn.substring(6, 8), 16));
	}
	
	public static EArrayList<String> createWordWrapString(String stringIn, int widthMax) {
		EArrayList<String> lines = new EArrayList();
		try {
			if (!stringIn.isEmpty() && fr.getStringWidth(stringIn) > widthMax) {
				String restOfString = stringIn;
				while (fr.getStringWidth(restOfString) > widthMax) {
					int i = 0;
					int iPos = 0;
					char end = Character.MIN_VALUE;
					String buildString = "";
					while (!(fr.getStringWidth(buildString) >= widthMax) && i < restOfString.length() - 1) {
						buildString += restOfString.charAt(i);
						i++;
					}
					while (i > 0 && end != ' ') {
						iPos = i;
						end = restOfString.charAt(i--);
					}
					if (i <= 0) {
						lines.add(restOfString.substring(0, buildString.length() - 1));
						restOfString = restOfString.substring(buildString.length() - 1, restOfString.length());
					} else {
						lines.add(restOfString.substring(0, iPos));
						restOfString = restOfString.substring(iPos + 1, restOfString.length());
					}
				}
				lines.add(restOfString);
			} else { lines.add(stringIn); }
		} catch (Exception e) { e.printStackTrace(); }
		return lines;
	}
	
	public static String keysToString(int[] keysIn) {
		EArrayList<Integer> list = new EArrayList();
		for (int i : keysIn) { list.add(i); }
		return keysToString(list);
	}
	
	public static String keysToString(EArrayList<Integer> keysIn) {
		String newText = "";
		for (int i = 0; i < keysIn.size(); i++) {
			int keyCode = keysIn.get(i);
			boolean isCtrl = (Minecraft.isRunningOnMac && (keyCode == 219 || keyCode == 220)) || keyCode == 29 || keyCode == 157;
			boolean isShift = keyCode == 42 || keyCode == 54;
			boolean isAlt = keyCode == 56 || keyCode == 184;
			
			String altString = Util.getOSType() == Util.EnumOS.OSX ? "Options" : "Alt";
			
			if (isCtrl) { newText += (keysIn.size() > 1) ? "Ctrl + " : "Ctrl"; }
			if (isShift) { newText += (keysIn.size() > 1 && i != keysIn.size() - 1) ? "Shift + " : "Shift"; }
			if (isAlt) { newText += (keysIn.size() > 1 && i != keysIn.size() - 1) ? altString + " + " : altString; }
			
			if (!isCtrl && !isShift && !isAlt) {
				String keyName = Keyboard.getKeyName(keyCode);
				keyName = keyName.toLowerCase();
				if (keyName.length() > 0) { keyName = keyName.substring(0, 1).toUpperCase() + keyName.substring(1); }
				if (keyName.equals("Grave")) { keyName = "Tilde"; }
				newText += keyName;
			}
		}
		return newText;
	}
	
	public static void setObjectEnabled(boolean val, IEnhancedGuiObject... objs) { for (IEnhancedGuiObject o : objs) { o.setEnabled(val); } }
	public static void setObjectVisibility(boolean val, IEnhancedGuiObject... objs) {for (IEnhancedGuiObject o : objs) { o.setVisible(val); } }
	public static void setObjectPersistence(boolean val, IEnhancedGuiObject... objs) {for (IEnhancedGuiObject o : objs) { o.setPersistent(val); } }
}
