package com.melody.j60870.datapack.init;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.data.CauseOfTransmission;
import com.melody.j60870.datapack.init.state.ClientState;
import com.melody.j60870.datapack.init.state.ServerState;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author melody
 */
public class StateHandler extends ChannelInboundHandlerAdapter {
	
	private boolean isInit;
	private ClientState clientState;
	private ServerState serverState;
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof APduNetty) {
			APduNetty aPduNetty = (APduNetty) msg;
			if (aPduNetty.getApciType() == APduNetty.ApciType.STARTDT_ACT) {
				clientState = ClientState.START_DT;
			}
			if (aPduNetty.getApciType() == APduNetty.ApciType.STARTDT_CON) {
				serverState = ServerState.START_CON;
			}
			if (aPduNetty.getASdu() != null) {
				switch (aPduNetty.getASdu().getTypeIdentification()) {
					case C_CS_NA_1://对时状态
						if (aPduNetty.getASdu().getCauseOfTransmission() == CauseOfTransmission.ACTIVATION) {
							clientState = ClientState.SYNC_CLOCK;
						} else if (aPduNetty.getASdu().getCauseOfTransmission() == CauseOfTransmission.ACTIVATION_CON) {
							serverState = ServerState.SYNC_CLOCK_CON;
						}
						updateState(aPduNetty,ClientState.SYNC_CLOCK,ServerState.SYNC_CLOCK_CON,null);
						break;
					case C_IC_NA_1://总招状态
						updateState(aPduNetty, ClientState.INTEGRATION, ServerState.INTERROGATION_CON, ServerState.INTERROGATION_TERMINATE);
					case C_CI_NA_1:
						updateState(aPduNetty, ClientState.COUNTER_INTERROGATION, ServerState.COUNTER_INTERROGATION_CON, ServerState.COUNTER_INTERROGATION_TERMINATE);
				}
			}
			ctx.fireChannelRead(msg);
		}
		
	}
	
	private void updateState(APduNetty aPduNetty, ClientState counterIntegration, ServerState counterIntegrationCon, ServerState counterIntegrationTerminate) {
		if (aPduNetty.getASdu().getCauseOfTransmission() == CauseOfTransmission.ACTIVATION) {
			clientState = counterIntegration;
		} else if (aPduNetty.getASdu().getCauseOfTransmission() == CauseOfTransmission.ACTIVATION_CON) {
			serverState = counterIntegrationCon;
		} else if (aPduNetty.getASdu().getCauseOfTransmission() == CauseOfTransmission.ACTIVATION_TERMINATION) {
			serverState = counterIntegrationTerminate;
		}
	}
	
}