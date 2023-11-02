package com.melody.j60870.datapack.cdec.decoder;

import com.melody.j60870.datapack.Frame;
import com.melody.j60870.datapack.cdec.IDecoder;
import com.melody.j60870.datapack.dynamic.FloatFrame;
import com.melody.j60870.except.NotValidFrameException;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * @author melody
 */
public class Ieee754DecoderImpl implements IDecoder {
	
	public final int ADDR_LEN;
	
	Ieee754DecoderImpl(int addrLen) {
		ADDR_LEN = addrLen;
	}
	
	
	@Override
	public Frame decode(ByteBuf data, Frame context) throws NotValidFrameException {
		if (context instanceof FloatFrame) {
			FloatFrame context1 = (FloatFrame) context;
			if (ADDR_LEN == 1) {
				context1.getAddress().add((long) data.readUnsignedByte());
			} else if (ADDR_LEN == 2) {
				context1.getAddress().add(((long) data.readUnsignedShort()));
			}
			float v = data.readFloat();
			List<Float> data1 = context1.getData();
			if (data1 == null) {
				context1.setData(new ArrayList<>());
				data1 = context1.getData();
			}
			data1.add(v);
		}
		return context;
	}
}
