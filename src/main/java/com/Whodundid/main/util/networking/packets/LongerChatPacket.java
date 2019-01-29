package com.Whodundid.main.util.networking.packets;

import com.Whodundid.main.util.miscUtil.ChatBuilder;
import com.Whodundid.main.util.networking.PacketBase;
import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;
import net.minecraft.entity.player.EntityPlayer;

//Last edited: Oct 20, 2018
//First Added: Oct 20, 2018
//Author: Hunter Bragg

public class LongerChatPacket extends PacketBase<LongerChatPacket> {

	private String message;

    public LongerChatPacket() {}
    public LongerChatPacket(String messageIn) {
        if (messageIn.length() > 100) { messageIn = messageIn.substring(0, 100); }
        message = messageIn;
    }
	
	@Override
	public void fromBytes(ByteBuf buf) {
		System.out.println("recieved: " + buf.readableBytes());
		byte[] byteString = new byte[buf.readableBytes()];
		for (int i = 0; i < buf.readableBytes(); i++) {
			byteString[i] = buf.readByte();
		}
		message = new String(byteString, StandardCharsets.UTF_8);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBytes(message.getBytes());
		System.out.println(message.getBytes().length);
	}

	@Override
	public void handleClientSide(LongerChatPacket message, EntityPlayer player) {
	}

	@Override
	public void handleServerSide(LongerChatPacket message, EntityPlayer player) {
		player.addChatMessage(ChatBuilder.of(message.message).build());
	}
}
