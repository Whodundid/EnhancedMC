package com.Whodundid.commands;

import com.Whodundid.scripts.builtInScripts.Script_SchemPreviewer;
import com.Whodundid.scripts.scriptBase.Script;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

//Last edited: 9-14-18
//First Added: 6-5-18
//Author: Hunter Bragg

public class CMD_StartScriptVisualizer extends CommandBase {

	@Override public String getCommandName() { return "pkv"; }
	@Override public int getRequiredPermissionLevel() { return 0; }

	@Override public String getCommandUsage(ICommandSender sender) {
		return "specify: base schem name ie 'pk_1_mp1', number of frames, return frame where the animation starts to double back on existing frames.";
	}

	@Override public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayer) {
			if (args.length > 0 && args.length < 4) {
				//Script.tryStartScript(new Script_SchemPreviewer(), args, true);
			} else {
				getCommandUsage(sender);
			}
		}		
	}
}
