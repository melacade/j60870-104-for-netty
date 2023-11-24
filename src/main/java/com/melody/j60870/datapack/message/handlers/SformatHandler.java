package com.melody.j60870.datapack.message.handlers;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.init.ConnectionHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import static com.melody.j60870.datapack.data.APduNetty.ApciType.S_FORMAT;

/**
 * @author melody
 */
public class SformatHandler extends com.melody.j60870.datapack.message.MessageHandler {
	
	@Override
	protected void register() {
		com.melody.j60870.datapack.message.MainHandler.register(S_FORMAT, this);
	}
	
	@Override
	public APduNetty toServer(APduNetty netty, ChannelHandlerContext ctx) {
		return null;
	}
	
	@Override
	public APduNetty toClient(APduNetty netty, ChannelHandlerContext ctx) {
		ChannelHandler init = ctx.pipeline().get("Init");
		ConnectionHandler init1 = (ConnectionHandler) init;
		try {
			init1.handleReceiveSequenceNumber(netty.getReceiveSeqNumber());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
}
