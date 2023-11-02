package com.melody.j60870.datapack.cdec.decoder;

import com.melody.j60870.datapack.Frame;
import com.melody.j60870.datapack.cdec.IDecoder;
import com.melody.j60870.except.NotValidFrameException;
import io.netty.buffer.ByteBuf;

/**
 * @author melody
 */
public class FormatIdecoderImpl implements IDecoder {
	
	@Override
	public Frame decode(ByteBuf data, Frame frame) throws NotValidFrameException {
		int sendNum = data.readUnsignedShortLE();
		int receiveNum = data.readUnsignedShortLE();
		int leftLen = frame.getLen() - 4;
		if (leftLen < 0) {
			throw new NotValidFrameException("不合法的len", leftLen);
		}
		int typeCode = data.readUnsignedByte();
		//todo
		return null;
	}
	
}
