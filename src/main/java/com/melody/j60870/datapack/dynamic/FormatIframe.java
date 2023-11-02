package com.melody.j60870.datapack.dynamic;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author melody
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FormatIframe extends DynamicFrame {
	
	Date date;
	
	@Override
	public Date getDate() {
		return date;
	}
	
	@Override
	public void setDate(Date date) {
		this.date = date;
	}
	
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
