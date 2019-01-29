package com.Whodundid.commands;

import com.Whodundid.enhancedChat.EnhancedChat;
import com.Whodundid.enhancedChat.chatUtil.ChatType;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.miscUtil.ChatBuilder;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

//Last edited: Dec 31, 2018
//First Added: Apr 17, 2017
//Author: Hunter Bragg

public class CMD_ShowTopChatType extends CommandBase{

	EnhancedChat chat;
	
	@Override public String getCommandName() { return "st"; }
	@Override public int getRequiredPermissionLevel() { return 0; }
	@Override public String getCommandUsage(ICommandSender sender) { return "Sets the chat veiwed in the upper right hand corner of the screen."; }

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		chat = (EnhancedChat) RegisteredSubMods.getMod(SubModType.ENHANCEDCHAT);
		if (chat.isEnabled()) {
			if (sender instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) sender;
				if (args.length > 0) {
					String type = args[0].toLowerCase();
					if (type.equals("g") || type.equals("guild")) {
						//chat.getChatOrganizer().setUpperList(ChatType.GUILD);
						player.addChatMessage(ChatBuilder.of("Guild").setColor(EnumChatFormatting.DARK_GREEN).append(" chat selected").setColor(EnumChatFormatting.WHITE).build());
					} else if (type.equals("p") || type.equals("party")) {
						//chat.getChatOrganizer().setUpperList(ChatType.PARTY);
						player.addChatMessage(ChatBuilder.of("Party").setColor(EnumChatFormatting.BLUE).append(" chat selected").setColor(EnumChatFormatting.WHITE).build());
					} else if (type.equals("pr") || type.equals("private") || type.equals("f") || type.equals("fr") || type.equals("friend")) {
						//chat.getChatOrganizer().setUpperList(ChatType.PRIVATE);
						player.addChatMessage(ChatBuilder.of("Private").setColor(EnumChatFormatting.LIGHT_PURPLE).append(" chat selected").setColor(EnumChatFormatting.WHITE).build());
					} else if (type.equals("s") || type.equals("st") || type.equals("staff")) {
						//chat.getChatOrganizer().setUpperList(ChatType.STAFF);
						player.addChatMessage(ChatBuilder.of("Staff").setColor(EnumChatFormatting.RED).append(" chat selected").setColor(EnumChatFormatting.WHITE).build());
					} else if (type.equals("l") || type.equals("lob") || type.equals("lobby")) {
						//chat.getChatOrganizer().setUpperList(ChatType.LOBBY);
						player.addChatMessage(ChatBuilder.of("Lobby").setColor(EnumChatFormatting.GREEN).append(" chat selected").setColor(EnumChatFormatting.WHITE).build());
					} else if (type.equals("ga") || type.equals("game")) {
						//chat.getChatOrganizer().setUpperList(ChatType.GAME);
						player.addChatMessage(ChatBuilder.of("Game").setColor(EnumChatFormatting.GOLD).append(" chat selected").setColor(EnumChatFormatting.WHITE).build());
					} else if (type.equals("a") || type.equals("all")) {
						player.addChatMessage(ChatBuilder.of("Cannot select all for upper").setColor(EnumChatFormatting.RED).build());
					} else if (type.equals("n") || type.equals("none") || type.equals("c") || type.equals("clr") || type.equals("clear")) {
						//chat.getChatOrganizer().setUpperList(ChatType.NONE);
						player.addChatMessage(ChatBuilder.of("Upper chat disabled").setColor(EnumChatFormatting.GRAY).build());
					} else
						player.addChatMessage(ChatBuilder.of("Unrecognized chat type").setColor(EnumChatFormatting.RED).build());
					if (args.length > 1) {
						String debugArg = args[1].toLowerCase();
						if (debugArg.equals("-d")) {
							//chat.getChatOrganizer().setDebug(true);
							player.addChatMessage(ChatBuilder.of("Chat debug mode enabled.").setColor(EnumChatFormatting.RED).build());
						}
					} else {
						//chat.getChatOrganizer().setDebug(false);
					}
				} else
					player.addChatMessage(ChatBuilder.of("use: g, p, pr, s, l or ga").setColor(EnumChatFormatting.RED).build());
			}
		}		
	}	
}