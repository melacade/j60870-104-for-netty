package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * @author melody
 */
public class IeSinglePointWithQualityNetty extends IeAbstractNettyQuality {
	
	public IeSinglePointWithQualityNetty(boolean on, boolean blocked, boolean substituted, boolean notTopical, boolean invalid) {
		super(blocked, substituted, notTopical, invalid);
		if (on) {
			value |= 0x01;
		}
	}
	
	IeSinglePointWithQualityNetty(ByteBuf is) throws IOException {
		super(is);
	}
	
	public boolean isOn() {
		return (value & 0x01) == 0x01;
	}
	
	@Override
	public String toString() {
		return "Single Point, is on: " + isOn() + ", " + super.toString();
	}
	
}
