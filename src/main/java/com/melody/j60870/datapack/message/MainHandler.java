package com.melody.j60870.datapack.message;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.message.handlers.*;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author melody
 */
@Slf4j
public class MainHandler extends MessageHandler {
	
	private static final Map<APduNetty.ApciType,MessageHandler> mem = new EnumMap<>(APduNetty.ApciType.class);
	
	public static void register(APduNetty.ApciType type, MessageHandler messageHandler) {
		mem.put(type, messageHandler);
	}

	static {
		new EmptyHandler().register();
		new IframeHandler().register();
		new SformatHandler().register();
		new StartDtConHandler().register();
		new StopDtHandler().register();
		new StopDtHandler().register();
		new TestHandler().register();
	}

	
	@Override
	public void register() {
	}
	
	@Override
	public APduNetty toServer(APduNetty netty, ChannelHandlerContext ctx) {
		return get(netty).toServer(netty, ctx);
	}
	
	@Override
	public APduNetty toClient(APduNetty netty, ChannelHandlerContext ctx) {
		return get(netty).toClient(netty, ctx);
	}
	
	static MessageHandler get(APduNetty type) {
		if (type == null) {
			return new EmptyHandler();
		}
		return mem.getOrDefault(type.getApciType(), new EmptyHandler());
	}
	
}
