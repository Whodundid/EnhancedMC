package com.Whodundid.worldEditor.EditorScripts;

import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.playerUtil.PlayerTraits;
import com.Whodundid.main.util.storageUtil.Vector3D;
import com.Whodundid.scripts.ScriptManager;
import com.Whodundid.scripts.scriptBase.Script;
import com.Whodundid.worldEditor.Editor;

public class EditorScript_Paste extends Script {
	
	static ScriptManager scriptMan = (ScriptManager) RegisteredSubMods.getMod(SubModType.SCRIPTS);
	boolean hasArg = false;
	static volatile boolean running = false;

	public void startScript(String[] args) {
		if (!running) {
			if (args != null) { hasArg = args[0].equals("-a"); }
			if (Editor.isEditorOpen() && Editor.getCopyPosition() != null) {
				Vector3D currentPosition = new Vector3D(PlayerTraits.getPlayerLocation());
				double distX = Editor.getPos1().getX() - Editor.getCopyPosition().getX();
				double distY = Editor.getPos1().getY() - Editor.getCopyPosition().getY();
				double distZ = Editor.getPos1().getZ() - Editor.getCopyPosition().getZ();
				
				Thread runner = new Thread() {
					@Override
					public void run() {
						running = true;
						int i = 0;
						refreshTime =  System.currentTimeMillis();
						while (i < 3 && !scriptMan.getGlobalKill()) {
							if (System.currentTimeMillis() - refreshTime >= 100) {
								switch (i) {
								case 0: mc.thePlayer.sendChatMessage("/tp " + (Editor.getPlayerCopyPosition().getX() + distX) + " " +
										   (Editor.getPlayerCopyPosition().getY() + distY) + " " +
										   (Editor.getPlayerCopyPosition().getZ() + distZ));
										break;
								case 1: mc.thePlayer.sendChatMessage((hasArg) ? "//paste -a" : "//paste"); break;
								case 2: mc.thePlayer.sendChatMessage("/tp " + currentPosition.getX() + " " + currentPosition.getY() + " " + currentPosition.getZ()); break;
								}
								i++;
								refreshTime = System.currentTimeMillis();
							}
						}
						running = false;
					}
				};
				runner.start();
			}
		} else {
			
		}
	}
}
