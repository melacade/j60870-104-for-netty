package com.melody.j60870.datapack.init;

import com.melody.j60870.datapack.config.ConnectionNettySettings;
import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.data.ASduNetty;
import com.melody.j60870.datapack.data.ASduTypeNetty;
import com.melody.j60870.datapack.data.CauseOfTransmission;
import com.melody.j60870.datapack.data.ie.IeCauseOfInitializationNetty;
import com.melody.j60870.datapack.data.ie.InformationNettyObject;
import com.melody.j60870.datapack.listener.IframeListener;
import com.melody.j60870.datapack.listener.MainListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author melody
 */
@Slf4j
public class ConnectionHandler extends ChannelInboundHandlerAdapter {
	
	ConnectionNettySettings settings;
	AtomicInteger count = new AtomicInteger(0);
	protected com.melody.j60870.datapack.message.MainHandler mainHandler = new com.melody.j60870.datapack.message.MainHandler();
	
	private int receiveSequenceNumber;
	private int acknowledgedReceiveSequenceNumber;
	private int acknowledgedSendSequenceNumber;
	private int sendSequenceNumber;
	private IframeListener iframeListener = new MainListener();
	
	public ConnectionHandler(ConnectionNettySettings settings) {
		this.settings = settings;
	}
	
	
	
	public synchronized void sendConfirmation(ASduNetty aSdu, ChannelHandlerContext ctx) throws IOException {
		CauseOfTransmission cot = aSdu.getCauseOfTransmission();
		if (cot == CauseOfTransmission.ACTIVATION) {
			cot = CauseOfTransmission.ACTIVATION_CON;
		} else if (cot == CauseOfTransmission.DEACTIVATION) {
			cot = CauseOfTransmission.DEACTIVATION_CON;
		}
		this.send(new ASduNetty(aSdu.getTypeIdentification(), aSdu.isSequenceOfElements(), cot, aSdu.isTestFrame(), aSdu.isNegativeConfirm(), aSdu.getOriginatorAddress(), aSdu.getCommonAddress(), aSdu.getInformationObjects()), ctx);
	}
	
	public void sendEndConfirmation(ASduNetty aSdu, ChannelHandlerContext ctx) throws IOException {
		CauseOfTransmission cot = aSdu.getCauseOfTransmission();
		if (cot == CauseOfTransmission.ACTIVATION) {
			cot = CauseOfTransmission.ACTIVATION_TERMINATION;
		}
		this.send(new ASduNetty(aSdu.getTypeIdentification(), aSdu.isSequenceOfElements(), cot, aSdu.isTestFrame(), aSdu.isNegativeConfirm(), aSdu.getOriginatorAddress(), aSdu.getCommonAddress(), aSdu.getInformationObjects()), ctx);
	}
	
	/**
	 * 发送操作未知地址的错误
	 *
	 * @param aSdu
	 * @param ctx
	 */
	public void sendUnknownIoa(ASduNetty aSdu, ChannelHandlerContext ctx) throws IOException {
		
		this.send(new ASduNetty(aSdu.getTypeIdentification(), aSdu.isSequenceOfElements(), CauseOfTransmission.UNKNOWN_INFORMATION_OBJECT_ADDRESS, aSdu.isTestFrame(), aSdu.isNegativeConfirm(), aSdu.getOriginatorAddress(), aSdu.getCommonAddress(), aSdu.getInformationObjects()), ctx);
	}
	
	
	/**
	 * 发送操作未知的传输错误
	 *
	 * @param aSdu
	 * @param ctx
	 */
	public void sendUnknownCot(ASduNetty aSdu, ChannelHandlerContext ctx) throws IOException {
		this.send(new ASduNetty(aSdu.getTypeIdentification(), aSdu.isSequenceOfElements(), CauseOfTransmission.UNKNOWN_CAUSE_OF_TRANSMISSION, aSdu.isTestFrame(), aSdu.isNegativeConfirm(), aSdu.getOriginatorAddress(), aSdu.getCommonAddress(), aSdu.getInformationObjects()), ctx);
	}
	
	/**
	 * 发送操作未知点位类型的传输错误
	 *
	 * @param aSdu
	 * @param ctx
	 */
	public void sendUnknownTid(ASduNetty aSdu, ChannelHandlerContext ctx) throws IOException {
		
		this.send(new ASduNetty(aSdu.getTypeIdentification(), aSdu.isSequenceOfElements(), CauseOfTransmission.UNKNOWN_TYPE_ID, aSdu.isTestFrame(), aSdu.isNegativeConfirm(), aSdu.getOriginatorAddress(), aSdu.getCommonAddress(), aSdu.getInformationObjects()), ctx);
	}
	
	/**
	 * 发送操作未知公共地址的错误
	 *
	 * @param aSdu
	 * @param ctx
	 */
	public void sendUnknownCoa(ASduNetty aSdu, ChannelHandlerContext ctx) throws IOException {
		
		this.send(new ASduNetty(aSdu.getTypeIdentification(), aSdu.isSequenceOfElements(), CauseOfTransmission.UNKNOWN_COMMON_ADDRESS_OF_ASDU, aSdu.isTestFrame(), aSdu.isNegativeConfirm(), aSdu.getOriginatorAddress(), aSdu.getCommonAddress(), aSdu.getInformationObjects()), ctx);
	}
	
	/**
	 * 发送操作未知地址的错误
	 *
	 * @param aSdu
	 * @param ctx
	 */
	public void sendDataUpdateInfo(ASduNetty aSdu, ChannelHandlerContext ctx) throws IOException {
		
		this.send(new ASduNetty(aSdu.getTypeIdentification(), aSdu.isSequenceOfElements(), CauseOfTransmission.SPONTANEOUS, aSdu.isTestFrame(), aSdu.isNegativeConfirm(), aSdu.getOriginatorAddress(), aSdu.getCommonAddress(), aSdu.getInformationObjects()), ctx);
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
		
		//		if (numUnconfirmedIPdusReceived >= settings.getMaxUnconfirmedIPdusReceived()) {
		sendSFormatPdu(ctx);
		//		}
		
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
		ConnectionHandler.this.notifyAll();
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
	
	
	
	public void sendConfirmStart(ChannelHandlerContext ctx) throws IOException {
		// 发送确认开始报文
		APduNetty aPduNetty = new APduNetty(sendSequenceNumber, sendSequenceNumber, APduNetty.ApciType.STARTDT_CON, null);
		ctx.channel().writeAndFlush(aPduNetty);
		
		// 接下来做一些初始化工作
		// 发送初始化完毕报文
		ASduNetty init = new ASduNetty(ASduTypeNetty.M_EI_NA_1, false, CauseOfTransmission.INITIALIZED, false, false, 0, 65535, new InformationNettyObject(1, new IeCauseOfInitializationNetty(0, false)));
		this.send(init, ctx);
		
	}
	
	public void sendStart(ChannelHandlerContext ctx) {
		// 发送确认开始报文
		APduNetty aPduNetty = new APduNetty(sendSequenceNumber, sendSequenceNumber, APduNetty.ApciType.STARTDT_ACT, null);
		ctx.channel().writeAndFlush(aPduNetty);
		
	}
}
