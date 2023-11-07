package com.melody.j60870.datapack.listener.listeners;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.data.ASduNetty;
import com.melody.j60870.datapack.data.ASduTypeNetty;
import com.melody.j60870.datapack.data.CauseOfTransmission;
import com.melody.j60870.datapack.init.ServerHandler;
import com.melody.j60870.datapack.listener.IframeListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import static com.melody.j60870.datapack.data.ASduTypeNetty.C_CI_NA_1;

/**
 * @author melody
 */
public class CounterInterrogationListener implements IframeListener {
	
	@Override
	public void on(APduNetty aPduNetty, ChannelHandlerContext ctx) {
		ChannelHandler init = ctx.pipeline().get("Init");
		ServerHandler serverHandler = (ServerHandler) init;
		
		try {
			serverHandler.sendConfirmation(aPduNetty.getASdu(), ctx);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			ASduNetty aSduNetty = new ASduNetty(ASduTypeNetty.M_IT_NA_1, false, CauseOfTransmission.ACTIVATION_CON, false, false, 0, 65535);
			serverHandler.send(aSduNetty,ctx);
			serverHandler.sendEndConfirmation(aPduNetty.getASdu(), ctx);
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void register() {
		mem.put(C_CI_NA_1, this);
	}
	
}
