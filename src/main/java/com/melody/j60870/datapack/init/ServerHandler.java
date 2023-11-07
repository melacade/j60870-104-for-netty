package com.melody.j60870.datapack.init;

import com.melody.j60870.datapack.config.ConnectionNettySettings;
import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.data.ASduNetty;
import com.melody.j60870.datapack.data.CauseOfTransmission;
import com.melody.j60870.datapack.listener.IframeListener;
import com.melody.j60870.datapack.listener.MainListener;
import com.melody.j60870.datapack.message.MainHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author melody
 */
@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {
	
	ConnectionNettySettings settings;
	@Getter
	private long lastTime = System.currentTimeMillis();
	AtomicInteger count = new AtomicInteger(0);
	private MainHandler mainHandler = new MainHandler();
	
	private int receiveSequenceNumber;
	private int acknowledgedReceiveSequenceNumber;
	private int acknowledgedSendSequenceNumber;
	private int sendSequenceNumber;
	private IframeListener iframeListener = new MainListener();
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent evt1 = (IdleStateEvent) evt;
			if (evt1.equals(IdleStateEvent.READER_IDLE_STATE_EVENT)) {
				log.info("长时间无报文发送test报文");
				ctx.channel().writeAndFlush(new APduNetty(0, 0, APduNetty.ApciType.TESTFR_ACT, null));
			}
		}
		super.userEventTriggered(ctx, evt);
	}
	
	public ServerHandler(ConnectionNettySettings settings) {
		this.settings = settings;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof APduNetty) {
			lastTime = System.currentTimeMillis();
			int i = count.incrementAndGet();
			mainHandler.toClient(((APduNetty) msg), ctx);
		}
		ctx.fireChannelRead(msg);
	}
	public void sendConfirmation(ASduNetty aSdu, ChannelHandlerContext ctx) throws IOException {
		CauseOfTransmission cot = aSdu.getCauseOfTransmission();
		if (cot == CauseOfTransmission.ACTIVATION) {
			cot = CauseOfTransmission.ACTIVATION_CON;
		} else if (cot == CauseOfTransmission.DEACTIVATION) {
			cot = CauseOfTransmission.DEACTIVATION_CON;
		}
		ctx.channel().writeAndFlush(new ASduNetty(aSdu.getTypeIdentification(), aSdu.isSequenceOfElements(), cot, aSdu.isTestFrame(), aSdu.isNegativeConfirm(), aSdu.getOriginatorAddress(), aSdu.getCommonAddress(), aSdu.getInformationObjects()));
	}
	
	public void sendEndConfirmation(ASduNetty aSdu, ChannelHandlerContext ctx) {
		CauseOfTransmission cot = aSdu.getCauseOfTransmission();
		if (cot == CauseOfTransmission.ACTIVATION) {
			cot = CauseOfTransmission.ACTIVATION_TERMINATION;
		}
		ctx.channel().writeAndFlush(new ASduNetty(aSdu.getTypeIdentification(), aSdu.isSequenceOfElements(), cot, aSdu.isTestFrame(), aSdu.isNegativeConfirm(), aSdu.getOriginatorAddress(), aSdu.getCommonAddress(), aSdu.getInformationObjects()));
	}
	public void handleIFrame(final APduNetty aPdu, ChannelHandlerContext ctx) throws IOException {
		
		updateReceiveSeqNum(aPdu.getSendSeqNumber(), ctx);
		
		handleReceiveSequenceNumber(aPdu.getReceiveSeqNumber());
		
		//		if (aSduListener != null) {
		//			serialExecutor.execute(new Runnable() {
		//				@Override
		//				public void run() {
		//					Thread.currentThread().setName("aSduListener");
		//					aSduListener.newASdu(a/ Pdu.getASdu());
		//				}
		//			});
		//		}
		if (iframeListener != null) {
			iframeListener.doOn(aPdu, ctx);
		}
		int numUnconfirmedIPdusReceived = sequenceNumberDiff(receiveSequenceNumber, acknowledgedReceiveSequenceNumber);
		
		if (numUnconfirmedIPdusReceived >= settings.getMaxUnconfirmedIPdusReceived()) {
			sendSFormatPdu(ctx);
		}
		
	}
	
	public void updateReceiveSeqNum(int sendSeqNumber, ChannelHandlerContext ctx) throws IOException {
		verifySeqNumber(sendSeqNumber);
		
		receiveSequenceNumber = (sendSeqNumber + 1) % (1 << 15); // 32768
		
		// check for receiveSequenceNumber overflow
		if (sendSeqNumber > receiveSequenceNumber) {
			sendSFormatPdu(ctx);
		}
	}
	
	private void verifySeqNumber(int sendSeqNumber) throws IOException {
		if (receiveSequenceNumber != sendSeqNumber) {
			String msg = MessageFormat.format("Got unexpected send sequence number: {0}, expected: {1}.", sendSeqNumber, receiveSequenceNumber);
			throw new IOException(msg);
		}
	}
	
	public void handleReceiveSequenceNumber(int receiveSeqNumber) throws IOException {
		if (acknowledgedSendSequenceNumber == receiveSeqNumber) {
			return;
		}
		
		int diff = sequenceNumberDiff(receiveSeqNumber, acknowledgedSendSequenceNumber);
		if (diff > sequenceNumberDiff(sendSequenceNumber, acknowledgedSendSequenceNumber)) {
			String msg = MessageFormat.format("Got unexpected receive sequence number: {0}, expected a number between: {1} and {2}.", receiveSeqNumber, acknowledgedSendSequenceNumber, sendSequenceNumber);
			throw new IOException(msg);
		}
		
		
		acknowledgedSendSequenceNumber = receiveSeqNumber;
		
		if (sendSequenceNumber != acknowledgedSendSequenceNumber) {
			if (sequenceNumberDiff(sendSequenceNumber, acknowledgedSendSequenceNumber) > settings.getMaxNumOfOutstandingIPdus()) {
				throw new IOException("Max number of outstanding IPdus is exceeded.");
			}
			
		}
		
	}
	
	private static int sequenceNumberDiff(int number, int ackNumber) {
		// would hold true: ackNumber <= number (without mod 2^15)
		return ackNumber > number ? ((1 << 15) - ackNumber) + number : number - ackNumber;
	}
	
	public void sendSFormatIfUnconfirmedAPdu(ChannelHandlerContext ctx) throws IOException {
		int diff;
		synchronized (this) {
			diff = sequenceNumberDiff(receiveSequenceNumber, acknowledgedReceiveSequenceNumber);
		}
		if (diff > 0) {
			sendSFormatPdu(ctx);
		}
	}
	
	public synchronized void send(ASduNetty aSdu, ChannelHandlerContext ctx) throws IOException {
		
		while (sequenceNumberDiff(sendSequenceNumber, acknowledgedSendSequenceNumber) >= settings.getMaxNumOfOutstandingIPdus()) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				throw new IOException(e);
			}
		}
		
		if (!ctx.channel().isActive()) {
			throw new IOException("connection closed");
		}
		
		acknowledgedReceiveSequenceNumber = receiveSequenceNumber;
		APduNetty requestAPdu = new APduNetty(sendSequenceNumber, receiveSequenceNumber, APduNetty.ApciType.I_FORMAT, aSdu);
		
		int oldSendSequenceNumber = sendSequenceNumber;
		sendSequenceNumber = (sendSequenceNumber + 1) % (1 << 15); // 32768 = 2^15
		
		// check for sendSequenceNumber overflow
		if (oldSendSequenceNumber > sendSequenceNumber) {
			sendSFormatPdu(ctx);
		}
		ctx.channel().writeAndFlush(requestAPdu);
	}
	
	private void sendSFormatPdu(ChannelHandlerContext ctx) throws IOException {
		
		APduNetty sf = new APduNetty(sendSequenceNumber, receiveSequenceNumber, APduNetty.ApciType.S_FORMAT, null);
		
		ctx.channel().writeAndFlush(sf);
		
		acknowledgedReceiveSequenceNumber = receiveSequenceNumber;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.channel().close();
	}
	
}
