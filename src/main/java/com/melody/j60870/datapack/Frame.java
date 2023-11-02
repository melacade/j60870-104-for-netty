package com.melody.j60870.datapack;


import java.util.Date;

public interface Frame {
	
	
	void log();
	
	default Date getDate(){
		throw new UnsupportedOperationException();};
	byte getStartByte();
	
	void setStartByte(byte startByte);
	
	byte getLen();
	
	void setLen(int len);
	
	byte getControlOne();
	
	void setControlOne(byte controlOne);
	
	byte getControlTwo();
	
	void setControlTwo(byte controlTwo);
	
	byte getControlThree();
	
	void setControlThree(byte controlThree);
	
	byte getControlFour();
	
	void setControlFour(byte controlFour);
	
	void setDate(Date date);
	
}
