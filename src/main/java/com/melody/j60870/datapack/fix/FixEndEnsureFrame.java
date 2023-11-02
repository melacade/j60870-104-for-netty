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
public class FixEndEnsureFrame extends FixFrame{
	
	/**
	 * 控制域1
	 */
	final byte controlOne = 0x23;
	
	@Override
	public void log() {
		log.info("结束确认报文");
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
