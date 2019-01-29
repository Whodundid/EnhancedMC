package com.Whodundid.commands;

import java.util.ArrayList;
import java.util.List;
import com.Whodundid.scripts.builtInScripts.Script_BiomeCopy;
import com.Whodundid.scripts.scriptBase.Script;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.GlobalSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

//Last edited: 9-14-18
//First Added: 8-12-17
//Author: Hunter Bragg

public class CMD_biomeCopy extends CommandBase {
	
	@Override public String getCommandName() { return "biomecopy"; }
	@Override public int getRequiredPermissionLevel() { return 0; }
	@Override public String getCommandUsage(ICommandSender sender) { return null; }
	
	@Override
	public List<String> getCommandAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("biomec");
		aliases.add("bioc");
		return aliases;
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		//Script.tryStartScript(new Script_BiomeCopy(), args, true);	
	}
}
