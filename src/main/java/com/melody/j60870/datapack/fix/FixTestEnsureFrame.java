package com.melody.j60870.datapack.fix;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author melody
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class FixTestEnsureFrame extends FixFrame{
	
	/**
	 * 控制域1
	 */
	final byte controlOne = (byte) 0x83;
	
	@Override
	public void log() {
		log.info("测试确认报文");
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
