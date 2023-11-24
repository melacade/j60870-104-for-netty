package com.melody.j60870.datapack.init;

import com.melody.j60870.datapack.config.ConnectionNettySettings;
import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.data.ASduNetty;
import com.melody.j60870.datapack.data.ASduTypeNetty;
import com.melody.j60870.datapack.data.CauseOfTransmission;
import com.melody.j60870.datapack.data.ie.*;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

/**
 * @author melody
 */
public class ClientHandler extends ConnectionHandler {
	
	private long lastTime = System.currentTimeMillis();
	
	public ClientHandler(ConnectionNettySettings settings) {
		super(settings);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof APduNetty) {
			lastTime = System.currentTimeMillis();
			mainHandler.toServer(((APduNetty) msg), ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}
	
	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		// 发送总招 对时等
		super.channelWritabilityChanged(ctx);
	}
	
	
	public void sendSyncClockCommand(ChannelHandlerContext ctx, int commonAddr, IeTime56Netty time) throws IOException {
		InformationNettyObject io = new InformationNettyObject(0, new InformationNettyElement[][]{{time}});
		InformationNettyObject[] ios = new InformationNettyObject[]{io};
		this.send(new ASduNetty(ASduTypeNetty.C_CS_NA_1, false, CauseOfTransmission.ACTIVATION, false, false, 0, commonAddr, ios), ctx);
	}
	
	public void sendSyncClockCommand(ChannelHandlerContext ctx, int commonAddr) throws IOException {
		this.sendSyncClockCommand(ctx, commonAddr, new IeTime56Netty(System.currentTimeMillis()));
	}
	
	public void sendInitInterrogationCommand(ChannelHandlerContext ctx, int commonAddr, IeQualifierOfInterrogationNetty qualifier) throws IOException {
		this.sendInterrogationCommand(ctx, commonAddr, CauseOfTransmission.ACTIVATION, qualifier);
	}
	
	public void sendInterrogationCommand(ChannelHandlerContext ctx, int commonAddr, CauseOfTransmission causeOfTransmission, IeQualifierOfInterrogationNetty qualifier) throws IOException {
		ASduNetty aSduNetty = new ASduNetty(ASduTypeNetty.C_IC_NA_1, false, causeOfTransmission, false, false, 0, commonAddr, new InformationNettyObject[]{new InformationNettyObject(0, new InformationNettyElement[][]{{qualifier}})});
		this.send(aSduNetty, ctx);
	}
	
	public void sendCounterInterrogation(ChannelHandlerContext ctx,int commonAddress, CauseOfTransmission cot, IeQualifierOfCounterInterrogationNetty qualifier) throws IOException {
		ASduNetty aSduNetty = new ASduNetty(ASduTypeNetty.C_CI_NA_1, false, cot, false, false, 0, commonAddress, new InformationNettyObject[]{new InformationNettyObject(0, new InformationNettyElement[][]{{qualifier}})});
		this.send(aSduNetty,ctx);
	}
	
	
}
