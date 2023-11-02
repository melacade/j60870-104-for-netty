package com.melody.j60870.datapack.cdec;

import com.melody.j60870.datapack.Frame;
import com.melody.j60870.except.NotValidFrameException;
import io.netty.buffer.ByteBuf;

/**
 * @author melody
 */
public interface IDecoder {
	
	default Frame decode(ByteBuf data) throws NotValidFrameException {
		throw new NotValidFrameException("", "没有实现parse方法");
	}
	default Frame decode(ByteBuf data, Frame context) throws NotValidFrameException {
		throw new NotValidFrameException("", "没有实现parse方法");
	}
	
}
