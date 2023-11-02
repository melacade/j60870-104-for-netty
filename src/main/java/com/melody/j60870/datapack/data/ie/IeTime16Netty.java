package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Calendar;

/**
 * Represents a two octet binary time (CP16Time2a) information element.
 */
public class IeTime16Netty extends InformationNettyElement {
	
	private final byte[] value = new byte[2];
	
	public IeTime16Netty(long timestamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		
		int ms = calendar.get(Calendar.MILLISECOND) + 1000 * calendar.get(Calendar.SECOND);
		
		value[0] = (byte) ms;
		value[1] = (byte) (ms >> 8);
	}
	
	public IeTime16Netty(int timeInMs) {
		
		int ms = timeInMs % 60000;
		value[0] = (byte) ms;
		value[1] = (byte) (ms >> 8);
	}
	
	IeTime16Netty(ByteBuf is) throws IOException {
		is.readBytes(value);
	}
	
	@Override
	public int encode(ByteBuf buffer, int i) {
		buffer.writeBytes(value);
		return 2;
	}
	
	public int getTimeInMs() {
		return (value[0] & 0xff) + ((value[1] & 0xff) << 8);
	}
	
	@Override
	public String toString() {
		return "Time16, time in ms: " + getTimeInMs();
	}
	
}