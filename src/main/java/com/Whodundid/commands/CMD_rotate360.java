package com.Whodundid.commands;

import java.util.ArrayList;
import java.util.List;
import com.Whodundid.scripts.builtInScripts.Script_Rotate360;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

//Last edited: 10-14-18
//First Added: 10-9-17
//Author: Hunter Bragg

public class CMD_rotate360 extends CommandBase {

	@Override public String getCommandName() { return "rotate360"; }
	@Override public int getRequiredPermissionLevel() { return 0; }
	@Override public String getCommandUsage(ICommandSender sender) { return null; }
	
	@Override
	public List<String> getCommandAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("r360");
		return aliases;
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		new Script_Rotate360().startScript(null);
	}
}