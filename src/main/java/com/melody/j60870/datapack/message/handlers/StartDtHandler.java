package com.melody.j60870.datapack.message.handlers;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.init.ServerHandler;
import com.melody.j60870.datapack.message.MainHandler;
import com.melody.j60870.datapack.message.MessageHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import static com.melody.j60870.datapack.data.APduNetty.ApciType.STARTDT_ACT;

/**
 * @author melody
 */
public class StartDtHandler extends MessageHandler {
	
	@Override
	protected void register() {
		MainHandler.register(STARTDT_ACT, this);
	}
	
	@Override
	public APduNetty toServer(APduNetty netty, ChannelHandlerContext ctx) {
		APduNetty aPduNetty = new APduNetty(0, 0, STARTDT_ACT, null);
		ctx.channel().writeAndFlush(aPduNetty);
		return aPduNetty;
	}
	
	@Override
	public APduNetty toClient(APduNetty netty, ChannelHandlerContext ctx) {

		
		ChannelHandler init = ctx.pipeline().get("Init");
		ServerHandler serverHandler = (ServerHandler) init;
		try {
			serverHandler.sendStart(ctx);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return netty;
	}
	
}
