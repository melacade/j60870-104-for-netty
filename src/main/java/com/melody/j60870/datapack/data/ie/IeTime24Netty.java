package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Calendar;

/**
 * @author melody
 */
public class IeTime24Netty extends InformationNettyElement {
	private final byte[] value = new byte[3];
	
	public IeTime24Netty(long timestamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		
		int ms = calendar.get(Calendar.MILLISECOND) + 1000 * calendar.get(Calendar.SECOND);
		
		value[0] = (byte) ms;
		value[1] = (byte) (ms >> 8);
		value[2] = (byte) calendar.get(Calendar.MINUTE);
	}
	
	public IeTime24Netty(int timeInMs){
		
		int ms = timeInMs % 60000;
		value[0] = (byte) ms;
		value[1] = (byte) (ms >> 8);
		value[2] = (byte) (timeInMs / 60000);
	}
	
	IeTime24Netty(ByteBuf is) throws IOException {
		is.readBytes(value);
	}
	
	@Override
	public int encode(ByteBuf buffer, int i) {
//		buffer.writerIndex(i);
		buffer.writeBytes(value);
		return 3;
	}
	
	public int getTimeInMs() {
		return (value[0] & 0xff) + ((value[1] & 0xff) << 8) + value[2] * 60000;
	}
	
	@Override
	public String toString() {
		return "Time24, time in ms: " + getTimeInMs();
	}
}
