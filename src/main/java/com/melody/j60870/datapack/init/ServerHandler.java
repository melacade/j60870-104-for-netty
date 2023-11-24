package com.melody.j60870.datapack.init;

import com.melody.j60870.datapack.config.ConnectionNettySettings;
import com.melody.j60870.datapack.data.APduNetty;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author melody
 */
@Slf4j
public class ServerHandler  extends ConnectionHandler {
	private Map<ChannelId,SocketChannel> map;
	public ServerHandler(ConnectionNettySettings settings, Map<ChannelId,SocketChannel> map) {
		super(settings);
		this.map = map;
	}
	
	public ServerHandler(ConnectionNettySettings settings) {
		super(settings);
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		this.map.remove(ctx.channel().id());
		ctx.fireChannelInactive();
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent evt1 = (IdleStateEvent) evt;
			if (evt1.equals(IdleStateEvent.READER_IDLE_STATE_EVENT)) {
				log.info("长时间无报文发送test报文");
				ctx.channel().writeAndFlush(new APduNetty(0, 0, APduNetty.ApciType.TESTFR_ACT, null));
			}
		} else {
			ctx.fireUserEventTriggered(evt);
		}
	}
	@Override
	public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof APduNetty) {
			super.mainHandler.toClient(((APduNetty) msg), ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
		
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.channel().close();
	}
	
}
