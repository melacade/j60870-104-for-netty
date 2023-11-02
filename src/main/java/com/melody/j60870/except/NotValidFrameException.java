package com.melody.j60870.except;

public class NotValidFrameException extends Exception{
	
	public NotValidFrameException(String field,Object value) {
		super("非法的 "+field+":"+String.valueOf(value));
	}
}
