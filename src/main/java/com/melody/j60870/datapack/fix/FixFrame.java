package com.melody.j60870.datapack.fix;

import com.melody.j60870.datapack.Frame;
import lombok.Data;
import lombok.Getter;
/**
 * @author melody
 */
@Getter
@Data
public abstract class FixFrame implements Frame {
	/**
	 * 启动字符
	 */
	final byte startByte = 0x68;
	
	/**
	 * 长度
	 */
	final byte len = 0x04;

	/**
	 * 控制域2
	 */
	final byte controlTwo = 0x00;
	/**
	 * 控制域3
	 */
	final byte controlThree = 0x00;
	/**
	 * 控制域4
	 */
	final byte controlFour = 0x00;
	
	
	public void setStartByte(byte startByte){
	}
	
	
	public void setLen(byte len){
	}
	
	
	
	
	public void setControlTwo(byte controlTwo){
	}
	
	
	public void setControlThree(byte controlThree){
	}
	
	
	public void setControlFour(byte controlFour){
	}
	
}
