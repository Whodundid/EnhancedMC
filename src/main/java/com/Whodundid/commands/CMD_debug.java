package com.Whodundid.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.client.ClientCommandHandler;

//Last edited: 9-14-18
//First Added: 1-7-18
//Author: Hunter Bragg

public class CMD_debug extends CommandBase {

	@Override public String getCommandName() { return "debug"; }
	@Override public int getRequiredPermissionLevel() { return 0; }
	@Override public String getCommandUsage(ICommandSender sender) { return ""; }
	
	@Override
	public List<String> getCommandAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("deb");
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0) {
			//String first = args[0];
			Iterator it = ClientCommandHandler.instance.getCommands().keySet().iterator();
			while (it.hasNext()) {
				System.out.println(it.next());
				/*try {
					Object obj = it.next();
					ICommand command = (ICommand) obj;
					System.out.println(command.getCommandAliases().toString());
				} catch (Exception e) {
					e.printStackTrace();
				}*/
			}
		}
		//GlobalOptions.loadConfig();
	}
}