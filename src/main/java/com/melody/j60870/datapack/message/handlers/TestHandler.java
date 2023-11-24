package com.melody.j60870.datapack.message.handlers;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.event.CancelDisconnectEvent;
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
		MainHandler.register(APduNetty.ApciType.TESTFR_CON, this);
	}
	
	@Override
	public APduNetty toServer(APduNetty netty, ChannelHandlerContext ctx) {
		return getaPduNetty(netty, ctx);
	}
	
	@Override
	public APduNetty toClient(APduNetty netty, ChannelHandlerContext ctx) {
		return getaPduNetty(netty, ctx);
	}
	
	private APduNetty getaPduNetty(APduNetty netty, ChannelHandlerContext ctx) {
		if (netty.getApciType() == TESTFR_ACT) {
			ctx.channel().writeAndFlush(new APduNetty(0, 0, APduNetty.ApciType.TESTFR_CON, null));
		} else {
			ctx.channel().writeAndFlush(new APduNetty(0, 0, APduNetty.ApciType.TESTFR_ACT, null));
			ctx.pipeline().fireUserEventTriggered(new CancelDisconnectEvent());
		}
		return netty;
	}
	
}
