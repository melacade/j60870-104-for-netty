package com.melody.j60870.datapack.listener.listeners;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.data.ie.IeDoubleCommandNetty;
import com.melody.j60870.datapack.data.ie.InformationNettyObject;
import com.melody.j60870.datapack.init.ServerHandler;
import com.melody.j60870.datapack.listener.IframeListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.melody.j60870.datapack.data.ASduTypeNetty.C_DC_NA_1;

/**
 * @author melody
 */
@Slf4j
public class DoubleCommandListener implements IframeListener {
	
	@Override
	public void on(APduNetty aPduNetty, ChannelHandlerContext ctx) {
		if (log.isDebugEnabled()) {
			InformationNettyObject[] informationObjects = aPduNetty.getASdu().getInformationObjects();
			InformationNettyObject informationObject = informationObjects[0];
			IeDoubleCommandNetty informationObject1 = (IeDoubleCommandNetty) (informationObject.getInformationElements()[0][0]);
			log.debug("双点遥控命令遥控命令触发:选择 {}, 直行{}, 执行/撤销{}", informationObject1.isSelect(), !informationObject1.isSelect(), aPduNetty.getASdu().getCauseOfTransmission());
		}
		// 发送确认报文
		ServerHandler init = (ServerHandler) ctx.pipeline().get("Init");
		try {
			init.sendConfirmation(aPduNetty.getASdu(), ctx);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// todo 执行 收到遥控命令后进行控制的一些操作比如Redis数据更新
		
		// todo 如何注册一个可操作类去操作
	}
	
	@Override
	public void register() {
		mem.put(C_DC_NA_1, this);
	}
	
}
