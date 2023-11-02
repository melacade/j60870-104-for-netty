package com.melody.j60870.datapack.message.handlers;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.init.ServerHandler;
import com.melody.j60870.datapack.message.MainHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import static com.melody.j60870.datapack.data.APduNetty.ApciType.I_FORMAT;

/**
 * @author melody
 */
public class IframeHandler extends com.melody.j60870.datapack.message.MessageHandler {
	
	@Override
	protected void register() {
		MainHandler.register(I_FORMAT,this);
	}
	
	@Override
	public APduNetty toServer(APduNetty netty, ChannelHandlerContext ctx) {
		return null;
	}
	
	@Override
	public APduNetty toClient(APduNetty netty, ChannelHandlerContext ctx) {
		ChannelHandler init = ctx.pipeline().get("Init");
		ServerHandler s = (ServerHandler) init;
		try {
			s.handleIFrame(netty, ctx);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return netty;
	}
	
}