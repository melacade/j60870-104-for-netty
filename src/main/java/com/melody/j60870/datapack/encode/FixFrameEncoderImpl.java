package com.melody.j60870.datapack.encode;

import com.melody.j60870.datapack.Frame;
import io.netty.buffer.ByteBuf;
/**
 * @author melody
 */
public class FixFrameEncoderImpl extends HeaderEncoderImpl{
	@Override
	public ByteBuf formBuf(Frame frame) {
		return super.formBuf(frame);
	}
	
}
