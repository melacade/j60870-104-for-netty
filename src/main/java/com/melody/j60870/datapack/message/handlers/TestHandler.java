package com.melody.j60870.datapack.message.handlers;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.message.MainHandler;
import io.netty.channel.ChannelHandlerContext;

import static com.melody.j60870.datapack.data.APduNetty.ApciType.TESTFR_ACT;

/**
 * @author melody
 */
public class TestHandler extends com.melody.j60870.datapack.message.MessageHandler {
	
	@Override
	protected void register() {
		MainHandler.register(TESTFR_ACT, this);
	}
	
	@Override
	public APduNetty toServer(APduNetty netty, ChannelHandlerContext ctx) {
		return null;
	}
	
	@Override
	public APduNetty toClient(APduNetty netty, ChannelHandlerContext ctx) {
		ctx.writeAndFlush(new APduNetty(0, 0, APduNetty.ApciType.TESTFR_CON, null));
		return netty;
	}
	
}
