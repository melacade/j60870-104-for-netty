package com.melody.j60870.datapack.encode;

import com.melody.j60870.datapack.Frame;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
/**
 * @author melody
 */
public class HeaderEncoderImpl implements  IEncoder {
	
	@Override
	public ByteBuf formBuf(Frame frame) {
		ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer();
		byteBuf.writeByte(frame.getStartByte());
		byteBuf.writeByte(frame.getLen());
		byteBuf.writeByte(frame.getControlOne());
		byteBuf.writeByte(frame.getControlTwo());
		byteBuf.writeByte(frame.getControlThree());
		byteBuf.writeByte(frame.getControlFour());
		return byteBuf;
	}
	
}
