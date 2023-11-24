package com.melody.j60870.datapack.message.handlers;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.init.ClientHandler;
import com.melody.j60870.datapack.message.MessageHandler;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

/**
 * @author melody
 */
public class StartDtConHandler extends MessageHandler {
	
	@Override
	protected void register() {
		com.melody.j60870.datapack.message.MainHandler.register(APduNetty.ApciType.STARTDT_CON, this);
	}
	
	@Override
	public APduNetty toServer(APduNetty netty, ChannelHandlerContext ctx) {
		ClientHandler init = (ClientHandler) ctx.pipeline().get("Init");
		try {
			init.sendSyncClockCommand(ctx, 0);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return netty;
	}
	
	@Override
	public APduNetty toClient(APduNetty netty, ChannelHandlerContext ctx) {
		return null;
	}
	
}
