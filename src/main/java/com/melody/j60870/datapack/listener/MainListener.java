package com.melody.j60870.datapack.listener;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.listener.listeners.*;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;

/**
 * @author melody
 */
@Slf4j
public class MainListener implements IframeListener{
	static {
		new CounterInterrogationListener().register();
		new DoubleCommandListener().register();
		new EmptyIframeListener().register();
		new InitializedListener().register();
		new InterrogationListener().register();
		new SingleCommandListener().register();
		new SyncClockListener().register();

	}
	@Override
	public void on(APduNetty aPduNetty, ChannelHandlerContext ctx) {
	}
	
	@Override
	public void register() {
	}
	
}
