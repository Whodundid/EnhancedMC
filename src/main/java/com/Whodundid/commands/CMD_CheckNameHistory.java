package com.Whodundid.commands;

import java.util.ArrayList;
import java.util.List;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.nameHistory.NameHistory;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

//Last edited: 10-16-18
//First Added: 11-16-17
//Author: Hunter Bragg

public class CMD_CheckNameHistory extends CommandBase {
	
	NameHistory names = (NameHistory) RegisteredSubMods.getMod(SubModType.NAMEHISTORY);
	
	@Override public String getCommandName() { return "names"; }
	@Override public int getRequiredPermissionLevel() { return 0; }
	@Override public String getCommandUsage(ICommandSender sender) { return "/names (s) <playername>"; }
	
	@Override
	public List<String> getCommandAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("nh");
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0 || args.length > 2) {
			getCommandUsage(sender);
		} else if (args.length > 1) {
			if (args[0].equals("s")) {
				names.startFetchingHistory(args[1], false, true);
			} else {
				getCommandUsage(sender);
			}
		} else {
			names.startFetchingHistory(args[0], false, false);
		}
	}
}