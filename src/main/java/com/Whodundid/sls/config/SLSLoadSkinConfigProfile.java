package com.Whodundid.sls.config;

import java.io.File;
import java.util.Scanner;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.sls.SkinSwitcher;
import com.Whodundid.sls.util.GlobalModes;
import com.Whodundid.sls.util.LayerTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class SLSLoadSkinConfigProfile {
	
	static SkinSwitcher sls = (SkinSwitcher) RegisteredSubMods.getMod(SubModType.SLS);
	static boolean isEnd = false;
	static String configLine = "";
	static String command = "";
	static float versionNum;
	static String saveStates = "";
	
	static String globalSwitchingState = "", globalBlinkState = "", globalBlinkFlipState = "";
	static String hFlipped = "", jFlipped = "", caFlipped = "", laFlipped = "", raFlipped = "", llFlipped = "", rlFlipped = "";
	static String hatEnabledState = "", jacketEnabledState = "", capeEnabledState = "", laEnabledState = "";
	static String raEnabledState = "", llEnabledState = "", rlEnabledState = "";
	static String hState = "", jState = "", caState = "", laState = "", raState = "", llState = "", rlState = "";
	static String hMode = "", jMode = "", caMode = "", laMode = "", raMode = "", llMode = "", rlMode = "";
	static boolean globalSwitching = false, globalBlinking = false, globalBlinkFlip = false, states = false;
	static boolean hST = false, jST = false, caST = false, laST = false, raST = false, llST = false, rlST = false;
	static boolean h = false, j = false, ca = false, la = false, ra = false, ll = false, rl = false;
	static boolean hf = false, jf = false, caf = false, laf = false, raf = false, llf = false, rlf = false;
	static int globalSwitchSpeed, globalBlinkDelay, globalBlinkDuration;
	static int hS, jS, caS, laS, raS, llS, rlS;
	static int hbS, jbS, cabS, labS, rabS, llbS, rlbS;
	static int hbD, jbD, cabD, labD, rabD, llbD, rlbD;
	static int hO, jO, caO, laO, raO, llO, rlO;

	public static void load(String profile) {
		if (profile != null) {
			File profConfig;
			try {
				profConfig = new File("EnhancedMC/SkinLayerSwitcher/SLS_Profile" + profile + ".cfg");
				if (profConfig.exists()) {
					isEnd = false;
					resetValues();
					try (Scanner fileReader = new Scanner(profConfig)) {
						while (!isEnd) {
							configLine = fileReader.nextLine();
							Scanner line = new Scanner(configLine);
							command = line.next();
							switch (command) {
							case "**": break; //ignore line
							case "VERSION:": versionNum = Float.valueOf(line.next()); break;
							case "profileSaveStates:": saveStates = line.next(); break;
							
							case "globalSwitching:": globalSwitchingState = line.next(); break;
							case "globalBlinking:": globalBlinkState = line.next(); break;
							case "globalBlinkFlipped:": globalBlinkFlipState = line.next(); break;
							case "globalSwitchSpeed:": globalSwitchSpeed = Integer.valueOf(line.next()); break;
							case "globalBlinkDelay:": globalBlinkDelay = Integer.valueOf(line.next()); break;
							case "globalBlinkDuration:": globalBlinkDuration = Integer.valueOf(line.next()); break;
							
							case "hat:": hatEnabledState = line.next(); break;
							case "jacket:": jacketEnabledState = line.next(); break;
							case "cape:": capeEnabledState = line.next(); break;
							case "leftArm:": laEnabledState = line.next(); break;
							case "rightArm:": raEnabledState = line.next(); break;
							case "leftLeg:": llEnabledState = line.next(); break;
							case "rightLeg:": rlEnabledState = line.next(); break;
							
							case "hatState:": hState = line.next(); break;
							case "jacketState:": jState = line.next(); break;
							case "capeState:": caState = line.next(); break;
							case "leftArmState:": laState = line.next(); break;
							case "rightArmState:": raState = line.next(); break;
							case "leftLegState:": llState = line.next(); break;
							case "rightLegState:": rlState = line.next(); break;
							
							case "hatMode:": hMode = line.next(); break;
							case "jacketMode:": jMode = line.next(); break;
							case "capeMode:": caMode = line.next(); break;
							case "leftArmMode:": laMode = line.next(); break;
							case "rightArmMode:": raMode = line.next(); break;
							case "leftLegMode:": llMode = line.next(); break;
							case "rightLegMode:": rlMode = line.next(); break;
							
							case "hatSwitchSpeed:": hS = Integer.valueOf(line.next()); break;
							case "jacketSwitchSpeed:": jS = Integer.valueOf(line.next()); break;
							case "capeSwitchSpeed:": caS = Integer.valueOf(line.next()); break;
							case "leftArmSwitchSpeed:": laS = Integer.valueOf(line.next()); break;
							case "rightArmSwitchSpeed:": raS = Integer.valueOf(line.next()); break;
							case "leftLegSwitchSpeed:": llS = Integer.valueOf(line.next()); break;
							case "rightLegSwitchSpeed:": rlS = Integer.valueOf(line.next()); break;
							
							case "hatBlinkDelay:": hbS = Integer.valueOf(line.next()); break;
							case "jacketBlinkDelay:": jbS = Integer.valueOf(line.next()); break;
							case "capeBlinkDelay:": cabS = Integer.valueOf(line.next()); break;
							case "leftArmBlinkDelay:": labS = Integer.valueOf(line.next()); break;
							case "rightArmBlinkDelay:": rabS = Integer.valueOf(line.next()); break;
							case "leftLegBlinkDelay:": llbS = Integer.valueOf(line.next()); break;
							case "rightLegBlinkDelay:": rlbS = Integer.valueOf(line.next()); break;
							
							case "hatBlinkDuration:": hbD = Integer.valueOf(line.next()); break;
							case "jacketBlinkDuration:": jbD = Integer.valueOf(line.next()); break;
							case "capeBlinkDuration:": cabD = Integer.valueOf(line.next()); break;
							case "leftArmBlinkDuration:": labD = Integer.valueOf(line.next()); break;
							case "rightArmBlinkDuration:": rabD = Integer.valueOf(line.next()); break;
							case "leftLegBlinkDuration:": llbD = Integer.valueOf(line.next()); break;
							case "rightLegBlinkDuration:": rlbD = Integer.valueOf(line.next()); break;
							
							case "hatOffset:": hO = Integer.valueOf(line.next()); break;
							case "jacketOffset:": jO = Integer.valueOf(line.next()); break;
							case "capeOffset:": caO = Integer.valueOf(line.next()); break;
							case "leftArmOffset:": laO = Integer.valueOf(line.next()); break;
							case "rightArmOffset:": raO = Integer.valueOf(line.next()); break;
							case "leftLegOffset:": llO = Integer.valueOf(line.next()); break;
							case "rightLegOffset:": rlO = Integer.valueOf(line.next()); break;
							
							case "hatFlipped:": hFlipped = line.next(); break;
							case "jacketFlipped:": jFlipped = line.next(); break;
							case "capeFlipped:": caFlipped = line.next(); break;
							case "leftArmFlipped:": laFlipped = line.next(); break;
							case "rightArmFlipped:": raFlipped = line.next(); break;
							case "leftLegFlipped:": llFlipped = line.next(); break;
							case "rightLegFlipped:": rlFlipped = line.next(); break;
								
							case "END": isEnd = true;
							default: break;
							}
							line.close();
						}
						fileReader.close();
						correctValues();
						updateValues();
					} catch (Exception e) { System.out.println("File corrupted.. Attempting to create new config."); createNewConfig(profile); }
				} else { createNewConfig(profile); }
			} catch (Exception e) { System.out.println("UNEXPECTED ERROR"); e.printStackTrace(); }
		}
	}
	
	public static void createNewConfig(String profile) {
		System.out.println("Creating new SLS profile " + profile + " config.");
		SLSSaveSkinConfigProfile.updateProfile(Integer.valueOf(profile));
		switch (sls.resetMode) {
		case SW: globalSwitching = true; break;
		case BL: globalBlinking = true; break;
		default: globalSwitching = false; globalBlinking = false; break;
		}
		globalBlinkFlip = false;
		states = true;
		globalSwitchSpeed = sls.defaultSwitchSpeed;
		globalBlinkDelay = sls.defaultBlinkDelay;
		globalBlinkDuration = sls.defaultBlinkDuration;
		h = true; hf = false;
		j = true; jf = false;
		ca = true; caf = false;
		la = true; laf = false;
		ra = true; raf = false;
		ll = true; llf = false;
		rl = true; rlf = false;
		int s = sls.defaultSwitchSpeed;
		int b = sls.defaultBlinkDelay;
		int d = sls.defaultBlinkDuration;
		hFlipped = ""; jFlipped = ""; caFlipped = ""; laFlipped = ""; raFlipped = ""; llFlipped = ""; rlFlipped = "";
		hatEnabledState = ""; jacketEnabledState = ""; capeEnabledState = ""; laEnabledState = "";
		raEnabledState = ""; llEnabledState = ""; rlEnabledState = "";
		hState = ""; jState = ""; caState = ""; laState = ""; raState = ""; llState = ""; rlState = "";
		hMode = "none"; jMode = "none"; caMode = "none"; laMode = "none"; 
		raMode = "none"; llMode = "none"; rlMode = "none";
		hS = s; jS = s; caS = s; laS = s; raS = s; llS = s; rlS = s;
		hbS = b; jbS = b; cabS = b; labS = b; rabS = b; llbS = b; rlbS = b;
		hbD = d; jbD = d; cabD = d; labD = d; rabD = d; llbD = d; rlbD = d;
		hO = 0; jO = 0; caO = 0; laO = 0; raO = 0; llO = 0; rlO = 0;
		versionNum = Float.valueOf(sls.version);
		correctValues();
		updateValues();
	}
	
	private static void resetValues() {
		globalSwitchingState = "";
		globalBlinkState = "";
		globalBlinkFlipState = "";
		hatEnabledState = ""; hFlipped = ""; hState = "";
		jacketEnabledState = ""; jFlipped = ""; jState = "";
		capeEnabledState = ""; caFlipped = ""; caState = "";
		laEnabledState = ""; laState = ""; laFlipped = "";
		raEnabledState = ""; raState = ""; raFlipped = "";
		llEnabledState = ""; llState = ""; llFlipped = "";
		rlEnabledState = ""; rlState = ""; rlFlipped = "";
		globalSwitching = false;
		globalBlinking = false;
		globalBlinkFlip = false;
		states = false;
		h = false; hf = false; hST = false;
		j = false; jf = false; jST = false;
		ca = false; caf = false; caST = false;
		la = false; laf = false; laST = false;
		ra = false; raf = false; raST = false;
		ll = false; llf = false; llST = false;
		rl = false; rlf = false; rlST = false;
		globalSwitchSpeed = 0;
		globalBlinkDelay = 0;
		globalBlinkDuration = 0;
		hS = 0; jS = 0; caS = 0; laS = 0; raS = 0; llS = 0; rlS = 0;
		hbS = 0; jbS = 0; cabS = 0; labS = 0; rabS = 0; llbS = 0; rlbS = 0;
		hbD = 0; jbD = 0; cabD = 0; labD = 0; rabD = 0; llbD = 0; rlbD = 0;
		hO = 0; jO = 0; caO = 0; laO = 0; raO = 0; llO = 0; rlO = 0;
		versionNum = Float.valueOf(sls.version);
	}
	
	private static void correctValues() {
		if (globalSwitchingState.equalsIgnoreCase("true")) { globalSwitching = true; }
		if (globalBlinkState.equalsIgnoreCase("true")) { globalBlinking = true; }
		if (globalBlinkFlipState.equalsIgnoreCase("true")) { globalBlinkFlip = true; }
		if (hatEnabledState.equalsIgnoreCase("true")) { h = true; }
		if (jacketEnabledState.equalsIgnoreCase("true")) { j = true; }
		if (capeEnabledState.equalsIgnoreCase("true")) { ca = true; }
		if (laEnabledState.equalsIgnoreCase("true")) { la = true; }
		if (raEnabledState.equalsIgnoreCase("true")) { ra = true; }
		if (llEnabledState.equalsIgnoreCase("true")) { ll = true; }
		if (rlEnabledState.equalsIgnoreCase("true")) { rl = true; }
		if (hState.equalsIgnoreCase("true")) { hST = true; }
		if (jState.equalsIgnoreCase("true")) { jST = true; }
		if (caState.equalsIgnoreCase("true")) { caST = true; }
		if (laState.equalsIgnoreCase("true")) { laST = true; }
		if (raState.equalsIgnoreCase("true")) { raST = true; }
		if (llState.equalsIgnoreCase("true")) { llST = true; }
		if (rlState.equalsIgnoreCase("true")) { rlST = true; }
		if (hFlipped.equalsIgnoreCase("true")) { hf = true; }
		if (jFlipped.equalsIgnoreCase("true")) { jf = true; }
		if (caFlipped.equalsIgnoreCase("true")) { caf = true; }
		if (laFlipped.equalsIgnoreCase("true")) { laf = true; }
		if (raFlipped.equalsIgnoreCase("true")) { raf = true; }
		if (llFlipped.equalsIgnoreCase("true")) { llf = true; }
		if (rlFlipped.equalsIgnoreCase("true")) { rlf = true; }
		if (saveStates.equalsIgnoreCase("true")) { states = true; }
	}
	
	private static void updateValues() {
		if (versionNum < Float.valueOf(sls.version)) {
			System.out.println("WARNING! Using older configs in skin switcher could cause unpredicatble events!");
		}
		if (versionNum > Float.valueOf(sls.version)) {
			System.out.println("WARNING! Future profile config version detected! Unpredicatble results may occur!");
		}
		
		if (states) { sls.savePartStates = true; }
		else { sls.savePartStates = false; }
		
		if (globalSwitching && globalBlinking) { globalBlinking = false; }
		if (globalSwitching) { 
			sls.setGlobalSwitch(true);
			sls.currentMode = GlobalModes.SW;
		}
		else if (globalBlinking) {
			sls.setGlobalSwitch(false);
			sls.currentMode = GlobalModes.BL;
		} else {
			sls.setGlobalSwitch(false);
			sls.setGlobalBlink(false);
			sls.currentMode = GlobalModes.IN;
		}
		if (globalBlinking) { sls.setGlobalBlink(true); }
		else { sls.setGlobalBlink(false); }
		
		if (globalBlinkFlip) { sls.toggleGlobalBlinkFlipped(); }
		
		if (globalSwitchSpeed < sls.MinRate) { sls.setGlobalSwitchSpeed(sls.MinRate); }
		else if (globalSwitchSpeed > sls.MaxRate) { sls.setGlobalSwitchSpeed(sls.MaxRate); }
		else { sls.setGlobalSwitchSpeed(globalSwitchSpeed); }
		if (globalBlinkDelay < sls.MinRate) { sls.setGlobalBlinkDelay(sls.MinRate); }
		else if (globalBlinkDelay > 100000) { sls.setGlobalBlinkDelay(sls.MaxRate); }
		else { sls.setGlobalBlinkDelay(globalBlinkDelay); }
		if (globalBlinkDuration < 200) { sls.setGlobalBlinkDuration(200); }
		else if (globalBlinkDuration > 100000) { sls.setGlobalBlinkDuration(100000); }
		else { sls.setGlobalBlinkDuration(globalBlinkDuration); }
		
		if (hMode.equals("switching")) { sls.getPart(LayerTypes.H).setSwitching(); }
		else if (hMode.equals("blinking")) { sls.getPart(LayerTypes.H).setBlinking(); }
		else { sls.getPart(LayerTypes.H).setNoMode(); }
		if (jMode.equals("switching")) { sls.getPart(LayerTypes.J).setSwitching(); }
		else if (jMode.equals("blinking")) { sls.getPart(LayerTypes.J).setBlinking(); }
		else { sls.getPart(LayerTypes.J).setNoMode(); }
		if (caMode.equals("switching")) { sls.getPart(LayerTypes.CA).setSwitching(); }
		else if (caMode.equals("blinking")) { sls.getPart(LayerTypes.CA).setBlinking(); }
		else { sls.getPart(LayerTypes.CA).setNoMode(); }
		if (laMode.equals("switching")) { sls.getPart(LayerTypes.LA).setSwitching(); }
		else if (laMode.equals("blinking")) { sls.getPart(LayerTypes.LA).setBlinking(); }
		else { sls.getPart(LayerTypes.LA).setNoMode(); }
		if (raMode.equals("switching")) { sls.getPart(LayerTypes.RA).setSwitching(); }
		else if (raMode.equals("blinking")) { sls.getPart(LayerTypes.RA).setBlinking(); }
		else { sls.getPart(LayerTypes.RA).setNoMode(); }
		if (llMode.equals("switching")) { sls.getPart(LayerTypes.LL).setSwitching(); }
		else if (llMode.equals("blinking")) { sls.getPart(LayerTypes.LL).setBlinking(); }
		else { sls.getPart(LayerTypes.LL).setNoMode(); }
		if (rlMode.equals("switching")) { sls.getPart(LayerTypes.RL).setSwitching(); }
		else if (rlMode.equals("blinking")) { sls.getPart(LayerTypes.RL).setBlinking(); }
		else { sls.getPart(LayerTypes.RL).setNoMode(); }
	
		if (h) { sls.getPart(LayerTypes.H).setEnabled(true); }
		if (j) { sls.getPart(LayerTypes.J).setEnabled(true); }
		if (ca) { sls.getPart(LayerTypes.CA).setEnabled(true); }
		if (la) { sls.getPart(LayerTypes.LA).setEnabled(true); }
		if (ra) { sls.getPart(LayerTypes.RA).setEnabled(true); }
		if (ll) { sls.getPart(LayerTypes.LL).setEnabled(true); }
		if (rl) { sls.getPart(LayerTypes.RL).setEnabled(true); }
		
		GameSettings s = Minecraft.getMinecraft().gameSettings;
		
		if (sls.savePartStates) {
			if (hST) {
				sls.getPart(LayerTypes.H).togglePartStateTo(true);
				s.setModelPartEnabled(EnumPlayerModelParts.HAT, true);
			} else s.setModelPartEnabled(EnumPlayerModelParts.HAT, false);
			if (jST) {
				sls.getPart(LayerTypes.J).togglePartStateTo(true);
				s.setModelPartEnabled(EnumPlayerModelParts.JACKET, true);
			} else s.setModelPartEnabled(EnumPlayerModelParts.JACKET, false);
			if (caST) {
				sls.getPart(LayerTypes.CA).togglePartStateTo(true);
				s.setModelPartEnabled(EnumPlayerModelParts.CAPE, true);
			} else s.setModelPartEnabled(EnumPlayerModelParts.CAPE, false);
			if (laST) {
				sls.getPart(LayerTypes.LA).togglePartStateTo(true);
				s.setModelPartEnabled(EnumPlayerModelParts.LEFT_SLEEVE, true);
			} else s.setModelPartEnabled(EnumPlayerModelParts.LEFT_SLEEVE, false);
			if (raST) {
				sls.getPart(LayerTypes.RA).togglePartStateTo(true);
				s.setModelPartEnabled(EnumPlayerModelParts.RIGHT_SLEEVE, true);
			} else s.setModelPartEnabled(EnumPlayerModelParts.RIGHT_SLEEVE, false);
			if (llST) {
				sls.getPart(LayerTypes.LL).togglePartStateTo(true);
				s.setModelPartEnabled(EnumPlayerModelParts.LEFT_PANTS_LEG, true);
			} else s.setModelPartEnabled(EnumPlayerModelParts.LEFT_PANTS_LEG, false);
			if (rlST) {
				sls.getPart(LayerTypes.RL).togglePartStateTo(true);
				s.setModelPartEnabled(EnumPlayerModelParts.RIGHT_PANTS_LEG, true);
			} else s.setModelPartEnabled(EnumPlayerModelParts.RIGHT_PANTS_LEG, false);
		} else {
			sls.getPart(LayerTypes.H).togglePartStateTo(false);
			sls.getPart(LayerTypes.J).togglePartStateTo(false);
			sls.getPart(LayerTypes.CA).togglePartStateTo(false);
			sls.getPart(LayerTypes.LA).togglePartStateTo(false);
			sls.getPart(LayerTypes.RA).togglePartStateTo(false);
			sls.getPart(LayerTypes.LL).togglePartStateTo(false);
			sls.getPart(LayerTypes.RL).togglePartStateTo(false);
			s.setModelPartEnabled(EnumPlayerModelParts.HAT, false);
			s.setModelPartEnabled(EnumPlayerModelParts.JACKET, false);
			s.setModelPartEnabled(EnumPlayerModelParts.CAPE, false);
			s.setModelPartEnabled(EnumPlayerModelParts.LEFT_SLEEVE, false);
			s.setModelPartEnabled(EnumPlayerModelParts.RIGHT_SLEEVE, false);
			s.setModelPartEnabled(EnumPlayerModelParts.LEFT_PANTS_LEG, false);
			s.setModelPartEnabled(EnumPlayerModelParts.RIGHT_PANTS_LEG, false);
		}
		
		if (hf) {
			sls.getPart(LayerTypes.H).setStateFlipped(true);
			sls.getPart(LayerTypes.H).togglePartStateTo(true);
			s.setModelPartEnabled(EnumPlayerModelParts.HAT, true);
			System.out.println(sls.getPart(LayerTypes.H).getState());
		} if (jf) {
			sls.getPart(LayerTypes.J).setStateFlipped(true);
			sls.getPart(LayerTypes.J).togglePartStateTo(!jST);
			s.setModelPartEnabled(EnumPlayerModelParts.JACKET, !jST);;
		} if (caf) {
			sls.getPart(LayerTypes.CA).setStateFlipped(true);
			sls.getPart(LayerTypes.CA).togglePartStateTo(!caST);
			s.setModelPartEnabled(EnumPlayerModelParts.CAPE, !caST);
		} if (laf) {
			sls.getPart(LayerTypes.LA).setStateFlipped(true);
			sls.getPart(LayerTypes.LA).togglePartStateTo(!laST);
			s.setModelPartEnabled(EnumPlayerModelParts.LEFT_SLEEVE, !laST);
		} if (raf) {
			sls.getPart(LayerTypes.RA).setStateFlipped(true);
			sls.getPart(LayerTypes.RA).togglePartStateTo(!raST);
			s.setModelPartEnabled(EnumPlayerModelParts.RIGHT_SLEEVE, !raST);
		} if (llf) {
			sls.getPart(LayerTypes.LL).setStateFlipped(true);
			sls.getPart(LayerTypes.LL).togglePartStateTo(!llST);
			s.setModelPartEnabled(EnumPlayerModelParts.LEFT_PANTS_LEG, !llST);
		} if (rlf) {
			sls.getPart(LayerTypes.RL).setStateFlipped(true);
			sls.getPart(LayerTypes.RL).togglePartStateTo(!rlST);
			s.setModelPartEnabled(EnumPlayerModelParts.RIGHT_PANTS_LEG, !rlST);
		}

		if (hS < sls.MinRate) { sls.getPart(LayerTypes.H).setSwitchSpeed(sls.MinRate); }
		else if (hS > sls.MaxRate) { sls.getPart(LayerTypes.H).setSwitchSpeed(sls.MaxRate); }
		else { sls.getPart(LayerTypes.H).setSwitchSpeed(hS); }
		if (jS < sls.MinRate) { sls.getPart(LayerTypes.J).setSwitchSpeed(sls.MinRate); }
		else if (jS > sls.MaxRate) { sls.getPart(LayerTypes.J).setSwitchSpeed(sls.MaxRate); }
		else { sls.getPart(LayerTypes.J).setSwitchSpeed(jS); }
		if (caS < sls.MinRate) { sls.getPart(LayerTypes.CA).setSwitchSpeed(sls.MinRate); }
		else if (caS > sls.MaxRate) { sls.getPart(LayerTypes.CA).setSwitchSpeed(sls.MaxRate); }
		else { sls.getPart(LayerTypes.CA).setSwitchSpeed(caS); }
		if (laS < sls.MinRate) { sls.getPart(LayerTypes.LA).setSwitchSpeed(sls.MinRate); }
		else if (laS > sls.MaxRate) { sls.getPart(LayerTypes.LA).setSwitchSpeed(sls.MaxRate); }
		else { sls.getPart(LayerTypes.LA).setSwitchSpeed(laS); }
		if (raS < sls.MinRate) { sls.getPart(LayerTypes.RA).setSwitchSpeed(sls.MinRate); }
		else if (raS > sls.MaxRate) { sls.getPart(LayerTypes.RA).setSwitchSpeed(sls.MaxRate); }
		else { sls.getPart(LayerTypes.RA).setSwitchSpeed(raS); }
		if (llS < sls.MinRate) { sls.getPart(LayerTypes.LL).setSwitchSpeed(sls.MinRate); }
		else if (llS > sls.MaxRate) { sls.getPart(LayerTypes.LL).setSwitchSpeed(sls.MaxRate); }
		else { sls.getPart(LayerTypes.LL).setSwitchSpeed(llS); }
		if (rlS < sls.MinRate) { sls.getPart(LayerTypes.RL).setSwitchSpeed(sls.MinRate); }
		else if (rlS > sls.MaxRate) { sls.getPart(LayerTypes.RL).setSwitchSpeed(sls.MaxRate); }
		else { sls.getPart(LayerTypes.RL).setSwitchSpeed(rlS); }
		
		if (hbS < sls.MinRate) { sls.getPart(LayerTypes.H).setBlinkSpeed(sls.MinRate); }
		else if (hbS > sls.MaxRate) { sls.getPart(LayerTypes.H).setBlinkSpeed(sls.MaxRate); }
		else { sls.getPart(LayerTypes.H).setBlinkSpeed(hbS); }
		if (jbS < sls.MinRate) { sls.getPart(LayerTypes.J).setBlinkSpeed(sls.MinRate); }
		else if (jbS > sls.MaxRate) { sls.getPart(LayerTypes.J).setBlinkSpeed(sls.MaxRate); }
		else { sls.getPart(LayerTypes.J).setBlinkSpeed(jbS); }
		if (cabS < sls.MinRate) { sls.getPart(LayerTypes.CA).setBlinkSpeed(sls.MinRate); }
		else if (cabS > sls.MaxRate) { sls.getPart(LayerTypes.CA).setBlinkSpeed(sls.MaxRate); }
		else { sls.getPart(LayerTypes.CA).setBlinkSpeed(cabS); }
		if (labS < sls.MinRate) { sls.getPart(LayerTypes.LA).setBlinkSpeed(sls.MinRate); }
		else if (labS > sls.MaxRate) { sls.getPart(LayerTypes.LA).setBlinkSpeed(sls.MaxRate); }
		else { sls.getPart(LayerTypes.LA).setBlinkSpeed(labS); }
		if (rabS < sls.MinRate) { sls.getPart(LayerTypes.RA).setBlinkSpeed(sls.MinRate); }
		else if (rabS > sls.MaxRate) { sls.getPart(LayerTypes.RA).setBlinkSpeed(sls.MaxRate); }
		else { sls.getPart(LayerTypes.RA).setBlinkSpeed(rabS); }
		if (llbS < sls.MinRate) { sls.getPart(LayerTypes.LL).setBlinkSpeed(sls.MinRate); }
		else if (llbS > sls.MaxRate) { sls.getPart(LayerTypes.LL).setBlinkSpeed(sls.MaxRate); }
		else { sls.getPart(LayerTypes.LL).setBlinkSpeed(llbS); }
		if (rlbS < sls.MinRate) { sls.getPart(LayerTypes.RL).setBlinkSpeed(sls.MinRate); }
		else if (rlbS > sls.MaxRate) { sls.getPart(LayerTypes.RL).setBlinkSpeed(sls.MaxRate); }
		else { sls.getPart(LayerTypes.RL).setBlinkSpeed(rlbS); }
		
		if (hbD < sls.MinRate) { sls.getPart(LayerTypes.H).setBlinkDuration(sls.MinBlinkDurRate); }
		else if (hbD > sls.MaxRate) { sls.getPart(LayerTypes.H).setBlinkDuration(sls.MaxRate); }
		else { sls.getPart(LayerTypes.H).setBlinkDuration(hbS); }
		if (jbD < sls.MinRate) { sls.getPart(LayerTypes.J).setBlinkDuration(sls.MinBlinkDurRate); }
		else if (jbD > sls.MaxRate) { sls.getPart(LayerTypes.J).setBlinkDuration(sls.MaxRate); }
		else { sls.getPart(LayerTypes.J).setBlinkDuration(jbS); }
		if (cabD < sls.MinRate) { sls.getPart(LayerTypes.CA).setBlinkDuration(sls.MinBlinkDurRate); }
		else if (cabD > sls.MaxRate) { sls.getPart(LayerTypes.CA).setBlinkDuration(sls.MaxRate); }
		else { sls.getPart(LayerTypes.CA).setBlinkDuration(cabS); }
		if (labD < sls.MinRate) { sls.getPart(LayerTypes.LA).setBlinkDuration(sls.MinBlinkDurRate); }
		else if (labD > sls.MaxRate) { sls.getPart(LayerTypes.LA).setBlinkDuration(sls.MaxRate); }
		else { sls.getPart(LayerTypes.LA).setBlinkDuration(labS); }
		if (rabD < sls.MinRate) { sls.getPart(LayerTypes.RA).setBlinkDuration(sls.MinBlinkDurRate); }
		else if (rabD > sls.MaxRate) { sls.getPart(LayerTypes.RA).setBlinkDuration(sls.MaxRate); }
		else { sls.getPart(LayerTypes.RA).setBlinkDuration(rabS); }
		if (llbD < sls.MinRate) { sls.getPart(LayerTypes.LL).setBlinkDuration(sls.MinBlinkDurRate); }
		else if (llbD > sls.MaxRate) { sls.getPart(LayerTypes.LL).setBlinkDuration(sls.MaxRate); }
		else { sls.getPart(LayerTypes.LL).setBlinkDuration(llbS); }
		if (rlbD < sls.MinRate) { sls.getPart(LayerTypes.RL).setBlinkDuration(sls.MinBlinkDurRate); }
		else if (rlbD > sls.MaxRate) { sls.getPart(LayerTypes.RL).setBlinkDuration(sls.MaxRate); }
		else { sls.getPart(LayerTypes.RL).setBlinkDuration(rlbS); }
		
		if (hO < 0) { sls.getPart(LayerTypes.H).setOffset(0); }
		else if (hO > sls.MaxRate) { sls.getPart(LayerTypes.H).setOffset(sls.MaxRate); }
		else { sls.getPart(LayerTypes.H).setOffset(hO); }
		if (jO < 0) { sls.getPart(LayerTypes.J).setOffset(0); }
		else if (jO > sls.MaxRate) { sls.getPart(LayerTypes.J).setOffset(sls.MaxRate); }
		else { sls.getPart(LayerTypes.J).setOffset(jO); }
		if (caO < 0) { sls.getPart(LayerTypes.CA).setOffset(0); }
		else if (caO > sls.MaxRate) { sls.getPart(LayerTypes.CA).setOffset(sls.MaxRate); }
		else { sls.getPart(LayerTypes.CA).setOffset(caO); }
		if (laO < 0) { sls.getPart(LayerTypes.LA).setOffset(0); }
		else if (laO > sls.MaxRate) { sls.getPart(LayerTypes.LA).setOffset(sls.MaxRate); }
		else { sls.getPart(LayerTypes.LA).setOffset(laO); }
		if (raO < 0) { sls.getPart(LayerTypes.RA).setOffset(0); }
		else if (raO > sls.MaxRate) { sls.getPart(LayerTypes.RA).setOffset(sls.MaxRate); }
		else { sls.getPart(LayerTypes.RA).setOffset(raO); }
		if (llO < 0) { sls.getPart(LayerTypes.LL).setOffset(0); }
		else if (llO > sls.MaxRate) { sls.getPart(LayerTypes.LL).setOffset(sls.MaxRate); }
		else { sls.getPart(LayerTypes.LL).setOffset(llO); }
		if (rlO < 0) { sls.getPart(LayerTypes.RL).setOffset(0); }
		else if (rlO > sls.MaxRate) { sls.getPart(LayerTypes.RL).setOffset(sls.MaxRate); }
		else { sls.getPart(LayerTypes.RL).setOffset(rlO); }
	}
}