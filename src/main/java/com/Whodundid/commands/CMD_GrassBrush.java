package com.Whodundid.commands;

import java.util.ArrayList;
import java.util.List;

import com.Whodundid.main.global.GlobalSettings;
import com.Whodundid.scripts.builtInScripts.Script_GrassBrush;
import com.Whodundid.scripts.scriptBase.Script;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

//Last edited: 9-14-18
//First Added: 11-27-17
//Author: Hunter Bragg

public class CMD_GrassBrush extends CommandBase {

	@Override public String getCommandName() { return "grassbrush"; }
	@Override public int getRequiredPermissionLevel() { return 0; }
	@Override public String getCommandUsage(ICommandSender sender) { return null; }
	
	@Override
	public List<String> getCommandAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("grass");
		aliases.add("grabr");
		return aliases;
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		//Script.tryStartScript(new Script_GrassBrush(), args, true);		
	}
}