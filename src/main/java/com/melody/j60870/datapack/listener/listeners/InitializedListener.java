package com.melody.j60870.datapack.listener.listeners;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.data.ASduTypeNetty;
import com.melody.j60870.datapack.data.CauseOfTransmission;
import com.melody.j60870.datapack.data.ie.IeQualifierOfInterrogationNetty;
import com.melody.j60870.datapack.init.ClientHandler;
import com.melody.j60870.datapack.listener.IframeListener;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

/**
 * @author melody
 */
public class InitializedListener implements IframeListener {
	
	@Override
	public void on(APduNetty aPduNetty, ChannelHandlerContext ctx) {
		// 客户端在收到初始化结束后发送总招命令
		ClientHandler init = (ClientHandler) ctx.pipeline().get("Init");
		try {
			init.sendInterrogationCommand(ctx,0, CauseOfTransmission.ACTIVATION,new IeQualifierOfInterrogationNetty(15));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void register() {
		mem.put(ASduTypeNetty.M_EI_NA_1, this);
	}
	
}
