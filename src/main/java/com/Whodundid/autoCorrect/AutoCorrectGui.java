package com.Whodundid.autoCorrect;

import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjectUtil.TextAreaLine;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiTextArea;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiTextField;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.miscUtil.Resources;

//Last edited: Oct 22, 2018
//First Added: Oct 14, 2018
//Author: Hunter Bragg

public class AutoCorrectGui extends EnhancedGui {

	AutoCorrectManager man = (AutoCorrectManager) RegisteredSubMods.getMod(SubModType.AUTOCORRECT);
	EGuiTextField commandName;
	EGuiTextArea commandList, aliasList;
	EGuiButton newCommand, deleteCommand, reloadCommands, saveCommands, edit;
	AutoCorrectCommand selectedCommand = null;
	
	public AutoCorrectGui() { super(); }
	public AutoCorrectGui(EnhancedGui oldGui) { super(oldGui); }
	public AutoCorrectGui(int posX, int posY) { super(posX, posY); }
	public AutoCorrectGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	
	@Override
	public void initGui() {
		centerGuiWithDimensions(400, 256);
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		try {
			newCommand = new EGuiButton(this, wPos + 10, hPos + 98, 90, 22, "Create New");
			deleteCommand = new EGuiButton(this, wPos - 190, hPos + 98, 70, 22, "Delete");
			reloadCommands = new EGuiButton(this, wPos - 200, hPos + 133, 110, 22, "Reload All Commands");
			saveCommands = new EGuiButton(this, wPos - 80, hPos + 133, 110, 22, "Save All Commands");
			
			commandName = new EGuiTextField(this, wPos + 10, hPos - 105, 180, 20);
			
			commandList = new EGuiTextArea(this, wPos - 190, hPos - 106, 180, 196, false).setDrawLineNumbers(true);
			aliasList = new EGuiTextArea(this, wPos + 10, hPos - 62, 180, 152, false).setDrawLineNumbers(true);
			
			addObject(newCommand, deleteCommand, reloadCommands, saveCommands);
			addObject(commandName, commandList, aliasList);
			
			for (AutoCorrectCommand c : man.getCommandList()) { commandList.addTextLine(c.getBaseCommand(), 0x00ff00, c); }
			
			if (commandList.getTextDocument().size() > 0) {
				TextAreaLine l = commandList.getTextLineWithLineNumber(1);
				if (l != null) {
					l.requestFocus();
					buildLists((AutoCorrectCommand) commandList.getTextLineWithLineNumber(1).getStoredObj());
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		drawDefaultBackground();
		//mc.renderEngine.bindTexture(Resources.guiBase);
		//drawTexturedModalRect(wPos - gWidth, hPos - gHeight / 2, 0, 0, gWidth, gHeight);
		//drawTexturedModalRect(wPos, hPos - gHeight / 2, 56, 0, gWidth, gHeight);
		//drawTexturedModalRect(wPos - 1, hPos - gHeight / 2, 0, 0, 2, gHeight);
		drawCenteredString("Registered AutoCorrect Commands", wPos - 100, hPos - 120, 0xFFFFFF);
		drawString("Command Name:", wPos + 10, hPos - 120, 0xFFFFFF);
		drawString("Aliases", wPos + 10, hPos - 77, 0xFFFFFF);
		
		if (commandList.getCurrentLine() != null && commandList.getCurrentLine().getStoredObj() != null) {
			if (!commandList.getCurrentLine().getStoredObj().equals(selectedCommand)) {
				selectedCommand = (AutoCorrectCommand) commandList.getCurrentLine().getStoredObj();
				buildLists(selectedCommand);
			}
		}
	}
	
	private void buildLists(AutoCorrectCommand cmdIn) {
		if (cmdIn != null && commandName != null && aliasList != null) {
			commandName.setText(cmdIn.getBaseCommand());
			aliasList.clear();
			for (String s : cmdIn.getAliases()) { aliasList.addTextLine(s).setLineNumberColor(0xffaa00); }
			aliasList.setDocumentVerticalPos(0);
		}
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(deleteCommand)) {
			if (commandList.getCurrentLine() != null) {
				AutoCorrectCommand cmd = (AutoCorrectCommand)commandList.getCurrentLine().getStoredObj();
				if (cmd != null) {
					man.unloadCommandFromRegistry(cmd);
					man.removeCommand(cmd);
					commandList.clear();
					aliasList.clear();
					for (AutoCorrectCommand c : man.getCommandList()) { commandList.addTextLine(c.getBaseCommand(), 0x00ff00, c); }
					commandList.setDocumentVerticalPos(0);
					commandName.setText("");
					if (commandList.getTextDocument().size() > 0) {
						commandList.getTextLineWithLineNumber(0).requestFocus();
						buildLists((AutoCorrectCommand)commandList.getTextLineWithLineNumber(0).getStoredObj());
					}
					man.saver.saveCommands();
				}
			}
		}
		if (object.equals(reloadCommands)) {
			man.unloadAllCommandsFromRegistry();
			man.commands.clear();
			commandList.clear();
			aliasList.clear();
			man.loader.loadCommands();
			man.registerCommands();
			for (AutoCorrectCommand c : man.getCommandList()) { commandList.addTextLine(c.getBaseCommand(), 0x00ff00, c); }
			commandList.setDocumentVerticalPos(0);
			commandName.setText("");
			if (commandList.getTextDocument().size() > 0) { buildLists((AutoCorrectCommand)commandList.getTextLineWithLineNumber(0).getStoredObj()); }
		}
		if (object.equals(saveCommands)) { man.saver.saveCommands(); }
	}
}
