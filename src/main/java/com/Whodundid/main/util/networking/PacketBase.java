package com.Whodundid.main.util.networking;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

//Last edited: Oct 20, 2018
//First Added: Oct 20, 2018
//Author: Hunter Bragg

public abstract class PacketBase<REQ extends IMessage> implements IMessage, IMessageHandler<REQ, REQ> {
	
	@Override
	public REQ onMessage(REQ message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) { handleServerSide(message, ctx.getServerHandler().playerEntity); }
		else { handleClientSide(message, null); }
		return null;
	}
	
	public abstract void handleClientSide(REQ message, EntityPlayer player);
	public abstract void handleServerSide(REQ message, EntityPlayer player);
}
