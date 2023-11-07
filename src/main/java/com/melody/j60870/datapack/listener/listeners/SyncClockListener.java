package com.melody.j60870.datapack.listener.listeners;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.data.ASduNetty;
import com.melody.j60870.datapack.data.ASduTypeNetty;
import com.melody.j60870.datapack.data.CauseOfTransmission;
import com.melody.j60870.datapack.data.ie.IeTime56Netty;
import com.melody.j60870.datapack.data.ie.InformationNettyElement;
import com.melody.j60870.datapack.data.ie.InformationNettyObject;
import com.melody.j60870.datapack.init.ServerHandler;
import com.melody.j60870.datapack.listener.IframeListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

/**
 * @author melody
 */
public class SyncClockListener implements IframeListener {
	
	@Override
	public void on(APduNetty aPduNetty, ChannelHandlerContext ctx) {
		ChannelHandler init = ctx.pipeline().get("Init");
		ServerHandler serverHandler = (ServerHandler) init;
		try {
			serverHandler.send(new ASduNetty(ASduTypeNetty.C_CS_NA_1, false, CauseOfTransmission.ACTIVATION_CON, false, false, 0, 65535,
					new InformationNettyObject(0, new InformationNettyElement[][]{{
							new IeTime56Netty(System.currentTimeMillis())
					}})
			), ctx);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void register() {
		mem.put(ASduTypeNetty.C_CS_NA_1, this);
	}
	
}
