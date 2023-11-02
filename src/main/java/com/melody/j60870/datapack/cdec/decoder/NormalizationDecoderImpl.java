package com.melody.j60870.datapack.cdec.decoder;

import com.melody.j60870.datapack.Frame;
import com.melody.j60870.datapack.cdec.IDecoder;
import com.melody.j60870.datapack.dynamic.NormalizationFrame;
import com.melody.j60870.except.NotValidFrameException;
import io.netty.buffer.ByteBuf;

/**
 * @author melody
 */
public class NormalizationDecoderImpl implements IDecoder {
	

	@Override
	public Frame decode(ByteBuf data, Frame context) throws NotValidFrameException {
		if (context instanceof NormalizationFrame) {
		
		
		}
		return context;
	}
	
}
