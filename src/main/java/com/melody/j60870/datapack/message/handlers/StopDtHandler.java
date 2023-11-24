package com.melody.j60870.datapack.message.handlers;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.init.ConnectionHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import static com.melody.j60870.datapack.data.APduNetty.ApciType.STOPDT_ACT;

/**
 * @author melody
 */
public class StopDtHandler extends com.melody.j60870.datapack.message.MessageHandler {
	
	@Override
	protected void register() {
		com.melody.j60870.datapack.message.MainHandler.register(STOPDT_ACT, this);
	}
	
	@Override
	public APduNetty toServer(APduNetty netty, ChannelHandlerContext ctx) {
		return null;
	}
	
	@Override
	public APduNetty toClient(APduNetty netty, ChannelHandlerContext ctx) {
		ChannelHandler init = ctx.pipeline().get("Init");
		ConnectionHandler s = (ConnectionHandler) init;
		try {
			s.sendSFormatIfUnconfirmedAPdu(ctx);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		ctx.channel().writeAndFlush(new APduNetty(0, 0, APduNetty.ApciType.STOPDT_CON, null));
		try {
			s.sendEndConfirmation(netty.getASdu(), ctx);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return netty;
	}
	
}
