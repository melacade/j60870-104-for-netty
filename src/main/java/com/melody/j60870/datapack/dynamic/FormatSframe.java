package com.melody.j60870.datapack.dynamic;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author melody
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FormatSframe extends DynamicFrame {
	
	int acceptNum;
	
	@Override
	public void log() {
	
	}
	
	@Override
	public byte getStartByte() {
		return 0;
	}
	
	@Override
	public void setStartByte(byte startByte) {
	
	}
	
	@Override
	public byte getLen() {
		return 0;
	}
	
	@Override
	public void setLen(int len) {
	
	}
	
	@Override
	public byte getControlOne() {
		return 0;
	}
	
	@Override
	public void setControlOne(byte controlOne) {
	
	}
	
	@Override
	public byte getControlTwo() {
		return 0;
	}
	
	@Override
	public void setControlTwo(byte controlTwo) {
	
	}
	
	@Override
	public byte getControlThree() {
		return 0;
	}
	
	@Override
	public void setControlThree(byte controlThree) {
	
	}
	
	@Override
	public byte getControlFour() {
		return 0;
	}
	
	@Override
	public void setControlFour(byte controlFour) {
	
	}
	
}
