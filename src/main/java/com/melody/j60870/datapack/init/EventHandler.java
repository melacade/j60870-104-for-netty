package com.melody.j60870.datapack.init;

import com.melody.j60870.datapack.config.ConnectionNettySettings;
import com.melody.j60870.datapack.event.CancelDisconnectEvent;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

/**
 * @author melody
 */
public class EventHandler extends ChannelDuplexHandler {
	
	private ConnectionNettySettings settings;
	private ScheduledFuture disconnect;
	
	public EventHandler(ConnectionNettySettings settings) {
		this.settings = settings;
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof CancelDisconnectEvent) {
			CancelDisconnectEvent cancelDisconnectEvent = (CancelDisconnectEvent) evt;
			if (cancelDisconnectEvent.isStart()) {
				if (disconnect == null) {
					disconnect = ctx.channel().eventLoop().schedule(() -> {
						if (!disconnect.isCancelled()) {
							ctx.close();
						}
					}, settings.getMaxUnconfirmedIPdusReceived(), TimeUnit.SECONDS);
				}
			} else {
				if (disconnect != null) {
					disconnect.cancel(true);
					disconnect = null;
				}
			}
		} else {
			ctx.fireUserEventTriggered(evt);
		}
	}
	
}
