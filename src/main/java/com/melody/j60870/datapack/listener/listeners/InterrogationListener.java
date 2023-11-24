package com.melody.j60870.datapack.listener.listeners;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.data.ASduNetty;
import com.melody.j60870.datapack.data.ASduTypeNetty;
import com.melody.j60870.datapack.data.CauseOfTransmission;
import com.melody.j60870.datapack.data.ie.IeSinglePointWithQualityNetty;
import com.melody.j60870.datapack.data.ie.InformationNettyObject;
import com.melody.j60870.datapack.init.ConnectionHandler;
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
		if (log.isDebugEnabled()) {
			log.debug("收到总招命令：{}",aPduNetty.getASdu());
		}
		ChannelHandler init = ctx.pipeline().get("Init");
		ConnectionHandler mainHandler = (ConnectionHandler) init;
		try {
			// 确认总招消息
			mainHandler.sendConfirmation(aPduNetty.getASdu(), ctx);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// 发送单点信息
		ASduNetty aSduNetty = new ASduNetty(ASduTypeNetty.M_SP_NA_1, false, CauseOfTransmission.SPONTANEOUS, false, false, 0, 0, new InformationNettyObject(0, new IeSinglePointWithQualityNetty(false,false,false,false,false)));
		
		try {
			mainHandler.send(aSduNetty, ctx);
			mainHandler.sendEndConfirmation(aPduNetty.getASdu(), ctx);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@Override
	public void register() {
		mem.put(ASduTypeNetty.C_IC_NA_1, this);
	}
	
}
