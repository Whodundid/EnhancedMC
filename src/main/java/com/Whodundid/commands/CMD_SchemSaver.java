package com.Whodundid.commands;

import com.Whodundid.scripts.builtInScripts.Script_SchemSaverCommand;
import com.Whodundid.scripts.scriptBase.Script;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

//Last edited: 9-14-18
//First Added: 6-5-18
//Author: Hunter Bragg

public class CMD_SchemSaver extends CommandBase {

	@Override public String getCommandName() { return "pks"; }
	@Override public int getRequiredPermissionLevel() { return 0; }

	@Override public String getCommandUsage(ICommandSender sender) {
		return "bundles //copy and //schem save 'name' into one command";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayer) {
			if (args.length > 0 && args.length < 2) {
				//Script.tryStartScript(new Script_SchemSaverCommand(), args, true);
			} else {
				getCommandUsage(sender);
			}
		}
	}
}
