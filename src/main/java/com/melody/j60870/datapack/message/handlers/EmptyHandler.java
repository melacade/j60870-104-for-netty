package com.melody.j60870.datapack.message.handlers;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.message.MainHandler;
import com.melody.j60870.datapack.message.MessageHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author melody
 */
public class EmptyHandler extends MessageHandler {
	
	
	@Override
	protected void register() {
		MainHandler.register(APduNetty.ApciType.STARTDT_CON, this);
		MainHandler.register(APduNetty.ApciType.TESTFR_CON, this);
	}
	
	@Override
	public APduNetty toServer(APduNetty netty, ChannelHandlerContext ctx) {
		return null;
	}
	
	@Override
	public APduNetty toClient(APduNetty netty, ChannelHandlerContext ctx) {
		return null;
	}
	
}
