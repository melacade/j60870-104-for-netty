package com.melody.j60870.datapack.listener.listeners;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.data.ASduNetty;
import com.melody.j60870.datapack.data.ASduTypeNetty;
import com.melody.j60870.datapack.data.CauseOfTransmission;
import com.melody.j60870.datapack.data.ie.IeSinglePointWithQualityNetty;
import com.melody.j60870.datapack.data.ie.InformationNettyElement;
import com.melody.j60870.datapack.data.ie.InformationNettyObject;
import com.melody.j60870.datapack.init.ServerHandler;
import com.melody.j60870.datapack.listener.IframeListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author melody
 */
@Slf4j
public class InterrogationListener implements IframeListener {
	
	@Override
	public void on(APduNetty aPduNetty, ChannelHandlerContext ctx) {
		log.info("触发总召");
		ChannelHandler init = ctx.pipeline().get("Init");
		ServerHandler mainHandler = (ServerHandler) init;
		try {
			mainHandler.sendConfirmation(aPduNetty.getASdu(), ctx);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		// 发送单点遥信
		ASduNetty aSduNetty = new ASduNetty(ASduTypeNetty.M_SP_NA_1, false, CauseOfTransmission.SPONTANEOUS, false,false,0,0,new InformationNettyObject(0, new InformationNettyElement[][]{
				{
						new IeSinglePointWithQualityNetty(false, false, false, false, false)
				}
		}));
		
		try {
			mainHandler.send(aSduNetty,ctx);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void register() {
		mem.put(ASduTypeNetty.C_IC_NA_1, this);
	}
	
}
