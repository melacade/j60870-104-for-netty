package com.melody.j60870.datapack.listener.listeners;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.listener.IframeListener;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author melody
 */
public class EmptyIframeListener implements IframeListener {
	
	
	@Override
	public void on(APduNetty aPduNetty, ChannelHandlerContext ctx) {
	}
	
	@Override
	public void register() {
	}
	
	
}
