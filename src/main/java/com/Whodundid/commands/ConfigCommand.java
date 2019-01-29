package com.Whodundid.commands;

import com.Whodundid.debug.ConfigMaker;
import com.Whodundid.debug.HypixelGameType;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

//Last edited: Jan 23, 2019
//First Added: Jan 23, 2019
//Author: Hunter Bragg

public class ConfigCommand extends CommandBase {
	
	@Override public String getCommandName() { return "hconfig"; }
	@Override public int getRequiredPermissionLevel() { return 0; }
	@Override public String getCommandUsage(ICommandSender sender) { return ""; }
	//@Override public List<String> getCommandAliases() { return null; }

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		try {
			if (args.length > 0) {
				String first = args[0];
				switch (first) {
				case "create": ConfigMaker.createNewConfigMaker(HypixelGameType.valueOf(args[1])); System.out.println("created"); break;
				case "destroy": ConfigMaker.destroyConfigMaker(); System.out.println("destroyed"); break;
				case "team": ConfigMaker.setTeamName(args[1]); System.out.println("team set: " + args[1]); break;
				case "init": ConfigMaker.setInitial(true); System.out.println("spawn point init"); break;
				case "norm": ConfigMaker.setInitial(false); System.out.println("spawn point norm"); break;
				case "players": ConfigMaker.setPlayerCount(Integer.parseInt(args[1])); System.out.println("players set: " + Integer.parseInt(args[1])); break;
				case "mapname": ConfigMaker.setMapName(args[1]); System.out.println("map name set: " + args[1]); break;
				case "save": ConfigMaker.createQuakeConfig(); break;
				}
			}
		} catch (Exception e) { System.out.println("failure"); e.printStackTrace(); }
	}
}
