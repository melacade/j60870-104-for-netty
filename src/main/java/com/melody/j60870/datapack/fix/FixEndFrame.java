package com.melody.j60870.datapack.fix;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class FixEndFrame extends FixFrame{
	
	/**
	 * 控制域1
	 */
	final byte controlOne = 0x13;
	
	@Override
	public void log() {
		log.info("结束报文");
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
