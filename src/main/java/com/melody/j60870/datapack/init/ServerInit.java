package com.melody.j60870.datapack.init;

import com.melody.j60870.datapack.config.ConnectionNettySettings;
import com.melody.j60870.datapack.decode.ApduDecoder;
import com.melody.j60870.datapack.decode.FrameCat;
import com.melody.j60870.datapack.encode.ApduEncoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author melody
 */
@Slf4j
public class ServerInit extends ChannelInitializer<SocketChannel> {
	
	private final Map<ChannelId,SocketChannel> map;
	ConnectionNettySettings settings;
	
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("id:{},的socket断开连接", ctx.channel().id());
		map.remove(ctx.channel().id());
		super.channelInactive(ctx);
	}
	
	public ServerInit(ConnectionNettySettings settings, Map<ChannelId,SocketChannel> map) {
		this.settings = settings;
		this.map = map;
	}
//	@Override
//	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//		if (evt instanceof IdleStateEvent) {
//			IdleStateEvent evt1 = (IdleStateEvent) evt;
//			if (evt1.equals(IdleStateEvent.READER_IDLE_STATE_EVENT)) {
//				ChannelHandler init = ctx.pipeline().get("Init");
//				ServerHandler serverInit = (ServerHandler) init;
//				if (System.currentTimeMillis() - serverInit.getLastTime() > settings.getMaxIdleTime()) {
//					log.error("在时间{}内无消息，关闭连接",settings.getMaxIdleTime());
//					ctx.close();
//				}
//			}
//		}
//		super.userEventTriggered(ctx, evt);
//	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.config().setTcpNoDelay(true);
		ch.pipeline()
				.addLast(new FrameCat())
				.addLast(new ApduEncoder(settings))
				.addLast(new ApduDecoder(settings))
				.addLast(new IdleStateHandler(settings.maxIdleTime, settings.maxIdleTime, settings.maxIdleTime))
				.addLast("Init", new ServerHandler(settings));
		map.put(ch.id(), ch);
	}
	
	
	
}
