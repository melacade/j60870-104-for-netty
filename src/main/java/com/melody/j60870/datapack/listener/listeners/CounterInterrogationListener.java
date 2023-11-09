package com.melody.j60870.datapack.listener.listeners;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.data.ASduNetty;
import com.melody.j60870.datapack.data.ASduTypeNetty;
import com.melody.j60870.datapack.data.CauseOfTransmission;
import com.melody.j60870.datapack.data.ie.IeSinglePointWithQualityNetty;
import com.melody.j60870.datapack.data.ie.InformationNettyObject;
import com.melody.j60870.datapack.init.ServerHandler;
import com.melody.j60870.datapack.listener.IframeListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.melody.j60870.datapack.data.ASduTypeNetty.C_CI_NA_1;

/**
 * @author melody
 */
@Slf4j
public class CounterInterrogationListener implements IframeListener {
	
	@Override
	public void on(APduNetty aPduNetty, ChannelHandlerContext ctx) {
		log.info("触发电镀总招");
		ChannelHandler init = ctx.pipeline().get("Init");
		ServerHandler serverHandler = (ServerHandler) init;
		
		try {
			//			// 确认电镀总招
			serverHandler.sendConfirmation(aPduNetty.getASdu(), ctx);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		ASduNetty aSduNetty = new ASduNetty(ASduTypeNetty.M_SP_NA_1, false, CauseOfTransmission.ACTIVATION_CON, false, false, 0, 65535,new InformationNettyObject(0,new IeSinglePointWithQualityNetty(false,false,false,false,false)));
		try {
			serverHandler.send(aSduNetty, ctx);
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
