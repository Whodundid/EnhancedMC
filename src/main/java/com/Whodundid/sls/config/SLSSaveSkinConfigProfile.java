package com.Whodundid.sls.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.sls.SkinSwitcher;
import com.Whodundid.sls.util.LayerTypes;
import com.Whodundid.sls.util.PartStats;

public class SLSSaveSkinConfigProfile {
	
	static SkinSwitcher sls = (SkinSwitcher) RegisteredSubMods.getMod(SubModType.SLS);
	
	public static void updateProfile(int profileNum) {
		try {
			File config = new File("EnhancedMC/SkinLayerSwitcher/");
			if (!config.exists()) { config.mkdirs(); }
			PrintWriter saver = new PrintWriter("EnhancedMC/SkinLayerSwitcher/SLS_Profile" + profileNum + ".cfg", "UTF-8");
			saver.println("** Skin Layer Switcher Profile Config **");
			saver.println("** NOTE: Only change these values if you know what you are doing!");
			saver.println("** Wrong values could potentially crash MC!");
			saver.println("** --------------------------");
			saver.println("VERSION: " + sls.version);
			saver.println("PROFILE: " + profileNum);
			saver.println("profileSaveStates: " + sls.savePartStates);
			saver.println("** --------------------------");
			saver.println("globalSwitching: " + sls.getGlobalSwitchingStatus());
			saver.println("globalBlinking: " + sls.getGlobalBlinkingStatus());
			saver.println("globalBlinkingFlipped: " + sls.getGlobalBlinkFlipped());
			saver.println("**");
			saver.println("globalSwitchSpeed: " + sls.getGlobalSwitchingSpeed());
			saver.println("globalBlinkDelay: " + sls.getGlobalBlinkingSpeed());
			saver.println("globalBlinkDuration: " + sls.getGlobalBlinkDuration());
			saver.println("**");
			saver.println("hat: " + PartStats.getPartEnabled(LayerTypes.H));
			saver.println("jacket: " + PartStats.getPartEnabled(LayerTypes.J));
			saver.println("cape: " + PartStats.getPartEnabled(LayerTypes.CA));
			saver.println("leftArm: " + PartStats.getPartEnabled(LayerTypes.LA));
			saver.println("rightArm: " + PartStats.getPartEnabled(LayerTypes.RA));
			saver.println("leftLeg: " + PartStats.getPartEnabled(LayerTypes.LL));
			saver.println("rightLeg: " + PartStats.getPartEnabled(LayerTypes.RL));
			saver.println("**");
			saver.println("hatState: " + PartStats.getPartState(LayerTypes.H));
			saver.println("jacketState: " + PartStats.getPartState(LayerTypes.J));
			saver.println("capeState: " + PartStats.getPartState(LayerTypes.CA));
			saver.println("leftArmState: " + PartStats.getPartState(LayerTypes.LA));
			saver.println("rightArmState: " + PartStats.getPartState(LayerTypes.RA));
			saver.println("leftLegState: " + PartStats.getPartState(LayerTypes.LL));
			saver.println("rightLegState: " + PartStats.getPartState(LayerTypes.RL));
			saver.println("**");
			saver.println("hatMode: " + PartStats.getPartMode(LayerTypes.H));
			saver.println("jacketMode: " + PartStats.getPartMode(LayerTypes.J));
			saver.println("capeMode: " + PartStats.getPartMode(LayerTypes.CA));
			saver.println("leftArmMode: " + PartStats.getPartMode(LayerTypes.LA));
			saver.println("rightArmMode: " + PartStats.getPartMode(LayerTypes.RA));
			saver.println("leftLegMode: " + PartStats.getPartMode(LayerTypes.LL));
			saver.println("rightLegMode: " + PartStats.getPartMode(LayerTypes.RL));
			saver.println("**");
			saver.println("hatSwitchSpeed: " + PartStats.getPartSwitchSpeed(LayerTypes.H));
			saver.println("jacketSwitchSpeed: " + PartStats.getPartSwitchSpeed(LayerTypes.J));
			saver.println("capeSwitchSpeed: " + PartStats.getPartSwitchSpeed(LayerTypes.CA));
			saver.println("leftArmSwitchSpeed: " + PartStats.getPartSwitchSpeed(LayerTypes.LA));
			saver.println("rightArmSwitchSpeed: " + PartStats.getPartSwitchSpeed(LayerTypes.RA));
			saver.println("leftLegSwitchSpeed: " + PartStats.getPartSwitchSpeed(LayerTypes.LL));
			saver.println("rightLegSwitchSpeed: " + PartStats.getPartSwitchSpeed(LayerTypes.RL));
			saver.println("**");
			saver.println("hatBlinkDelay: " + PartStats.getPartBlinkSpeed(LayerTypes.H));
			saver.println("jacketBlinkDelay: " + PartStats.getPartBlinkSpeed(LayerTypes.J));
			saver.println("capeBlinkDelay: " + PartStats.getPartBlinkSpeed(LayerTypes.CA));
			saver.println("leftArmBlinkDelay: " + PartStats.getPartBlinkSpeed(LayerTypes.LA));
			saver.println("rightArmBlinkDelay: " + PartStats.getPartBlinkSpeed(LayerTypes.RA));
			saver.println("leftLegBlinkDelay: " + PartStats.getPartBlinkSpeed(LayerTypes.LL));
			saver.println("rightLegBlinkDelay: " + PartStats.getPartBlinkSpeed(LayerTypes.RL));
			saver.println("**");
			saver.println("hatBlinkDuration: " + PartStats.getPartBlinkDuration(LayerTypes.H));
			saver.println("jacketBlinkDuration: " + PartStats.getPartBlinkDuration(LayerTypes.J));
			saver.println("capeBlinkDuration: " + PartStats.getPartBlinkDuration(LayerTypes.CA));
			saver.println("leftArmBlinkDuration: " + PartStats.getPartBlinkDuration(LayerTypes.LA));
			saver.println("rightArmBlinkDuration: " + PartStats.getPartBlinkDuration(LayerTypes.RA));
			saver.println("leftLegBlinkDuration: " + PartStats.getPartBlinkDuration(LayerTypes.LL));
			saver.println("rightLegBlinkDuration: " + PartStats.getPartBlinkDuration(LayerTypes.RL));
			saver.println("**");
			saver.println("hatOffset: " + PartStats.getPartOffset(LayerTypes.H));
			saver.println("jacketOffset: " + PartStats.getPartOffset(LayerTypes.J));
			saver.println("capeOffset: " + PartStats.getPartOffset(LayerTypes.CA));
			saver.println("leftArmOffset: " + PartStats.getPartOffset(LayerTypes.LA));
			saver.println("rightArmOffset: " + PartStats.getPartOffset(LayerTypes.RA));
			saver.println("leftLegOffset: " + PartStats.getPartOffset(LayerTypes.LL));
			saver.println("rightLegOffset: " + PartStats.getPartOffset(LayerTypes.RL));
			saver.println("**");
			saver.println("hatFlipped: " + PartStats.isPartStateFlipped(LayerTypes.H));
			saver.println("jacketFlipped: " + PartStats.isPartStateFlipped(LayerTypes.J));
			saver.println("capeFlipped: " + PartStats.isPartStateFlipped(LayerTypes.CA));
			saver.println("leftArmFlipped: " + PartStats.isPartStateFlipped(LayerTypes.LA));
			saver.println("rightArmFlipped: " + PartStats.isPartStateFlipped(LayerTypes.RA));
			saver.println("leftLegFlipped: " + PartStats.isPartStateFlipped(LayerTypes.LL));
			saver.println("rightLegFlipped: " + PartStats.isPartStateFlipped(LayerTypes.RL));
			saver.print("END");
			saver.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) { e.printStackTrace(); }
	}
}