package com.Whodundid.main.util.networking;

import com.Whodundid.main.MainMod;
import com.Whodundid.main.util.networking.packets.LongerChatPacket;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

//Last edited: Oct 20, 2018
//First Added: Oct 20, 2018
//Author: Hunter Bragg

public class NetworkHandler {
	
	private static SimpleNetworkWrapper instance;
	
	public NetworkHandler() {
		instance = NetworkRegistry.INSTANCE.newSimpleChannel(MainMod.NAME);
		
		instance.registerMessage(LongerChatPacket.class, LongerChatPacket.class, 0, Side.SERVER);
	}
	
	public static SimpleNetworkWrapper toSever(IMessage message) { instance.sendToServer(message); return instance; }
	
	public SimpleNetworkWrapper getInstance() { return instance; }
}
