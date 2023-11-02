package com.melody.j60870.datapack.cdec.decoder;

import com.melody.j60870.datapack.Frame;
import com.melody.j60870.datapack.cdec.IDecoder;
import com.melody.j60870.datapack.cdec.IEntityGenerator;
import com.melody.j60870.datapack.cdec.generator.GeneratorImpl;
import com.melody.j60870.datapack.dynamic.FormatIframe;
import com.melody.j60870.datapack.dynamic.FormatSframe;
import com.melody.j60870.except.NotValidFrameException;
import io.netty.buffer.ByteBuf;


/**
 * @author melody
 */
public class HeaderDecoderImpl implements IDecoder {
	
	IEntityGenerator generator = new GeneratorImpl();
	SframeDecoderImpl sframeDecoder = new SframeDecoderImpl();
	IframeDecoderImpl iframeDecoder = new IframeDecoderImpl();
	@Override
	public Frame decode(ByteBuf data) throws NotValidFrameException {
		byte startByte = data.readByte();
		int len = data.readUnsignedByte();
//		int oneAndTwo = data.readIntLE();
		data.markReaderIndex();
		byte controlOne = data.readByte();
		byte controlTwo = data.readByte();
//		int threeAndFour = data.readIntLE();
		byte controlThree = data.readByte();
		byte controlFour = data.readByte();
		Frame frame = generator.generate(controlOne,len);
		frame.setStartByte(startByte);
		frame.setLen(len);
		frame.setControlOne(controlOne);
		frame.setControlTwo(controlTwo);
		frame.setControlThree(controlThree);
		frame.setControlFour(controlFour);
		if (frame instanceof FormatSframe) {
			ByteBuf byteBuf = data.resetReaderIndex();
			sframeDecoder.decode(byteBuf, frame);
		} else if (frame instanceof FormatIframe) {
			data.resetReaderIndex();
			iframeDecoder.decode(data,frame);
		}
		return frame;
	}
	
}
