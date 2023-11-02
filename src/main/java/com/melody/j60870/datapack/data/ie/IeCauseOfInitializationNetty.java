package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a cause of initialization (COI) information element.
 */
public class IeCauseOfInitializationNetty extends InformationNettyElement {
	
	private final int value;
	private final boolean initAfterParameterChange;
	
	/**
	 * Creates a COI (cause of initialization) information element.
	 *
	 * @param value                    value between 0 and 127
	 * @param initAfterParameterChange true if initialization after change of local parameters and false if initialization with unchanged
	 *                                 local parameters
	 */
	public IeCauseOfInitializationNetty(int value, boolean initAfterParameterChange) {
		
		if (value < 0 || value > 127) {
			throw new IllegalArgumentException("Value has to be in the range 0..127");
		}
		
		this.value = value;
		this.initAfterParameterChange = initAfterParameterChange;
		
	}
	
	IeCauseOfInitializationNetty(ByteBuf is) throws IOException {
		int b1 = (is.readByte() & 0xff);
		
		initAfterParameterChange = ((b1 & 0x80) == 0x80);
		
		value = b1 & 0x7f;
		
	}
	
	@Override
	public int encode(ByteBuf buffer, int i) {
		
		if (initAfterParameterChange) {
			buffer.writeByte((byte) (value | 0x80));
		} else {
			buffer.writeByte((byte) value);
		}
		
		return 1;
		
	}
	
	public int getValue() {
		return value;
	}
	
	public boolean isInitAfterParameterChange() {
		return initAfterParameterChange;
	}
	
	@Override
	public String toString() {
		return "Cause of initialization: " + value + ", init after parameter change: " + initAfterParameterChange;
	}
	
}