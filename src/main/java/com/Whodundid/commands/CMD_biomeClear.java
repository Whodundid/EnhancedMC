package com.Whodundid.commands;

import java.util.ArrayList;
import java.util.List;
import com.Whodundid.scripts.builtInScripts.Script_BiomeCopy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

//Last edited: 10-16-18
//First Added: 11-5-19
//Author: Hunter Bragg

public class CMD_biomeClear extends CommandBase {

	@Override public String getCommandName() { return "biomeclear"; }
	@Override public int getRequiredPermissionLevel() { return 0; }
	@Override public String getCommandUsage(ICommandSender sender) { return null; }
	
	@Override
	public List<String> getCommandAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("bclear");
		return aliases;
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		Script_BiomeCopy.clearSavedBiome();
	}
}
