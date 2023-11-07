package com.melody.j60870.datapack.listener;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.data.ASduTypeNetty;
import com.melody.j60870.datapack.listener.listeners.EmptyIframeListener;
import io.netty.channel.ChannelHandlerContext;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author melody
 */
public interface IframeListener {
	
	Map<ASduTypeNetty,IframeListener> mem = new EnumMap<>(ASduTypeNetty.class);
	void on(APduNetty aPduNetty, ChannelHandlerContext ctx);
	void register();
	default void doOn(APduNetty aPduNetty, ChannelHandlerContext ctx) {
		if (aPduNetty == null) {
			return;
		}
		if (aPduNetty.getASdu() == null) {
			return;
		}
		IframeListener iframeListener = get(aPduNetty.getASdu().getTypeIdentification());
		iframeListener.on(aPduNetty, ctx);
	}
	default IframeListener get(ASduTypeNetty aPduNetty) {
		if (aPduNetty == null) {
			return new EmptyIframeListener();
		}
		return mem.getOrDefault(aPduNetty, new EmptyIframeListener());
	}
}
