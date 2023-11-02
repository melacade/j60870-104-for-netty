package com.melody.j60870.datapack.fix;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author melody
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class FixStartLinkEnsureFrame extends FixFrame {
	
	/**
	 * 控制域1
	 */
	final byte controlOne = 0x0B;
	
	@Override
	public void log() {
		log.info("开始确认报文");
	}
	
	@Override
	public Date getDate() {
		return null;
	}
	
	@Override
	public void setLen(int len) {
	
	}
	
	@Override
	public void setControlOne(byte controlOne) {
	}
	
	@Override
	public void setDate(Date date) {
	
	}
	
}
