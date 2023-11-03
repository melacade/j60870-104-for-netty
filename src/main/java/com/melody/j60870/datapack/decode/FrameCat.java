package com.melody.j60870.datapack.decode;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author melody
 */
public class FrameCat extends LengthFieldBasedFrameDecoder {
	
	public FrameCat() {
		super(255, 1, 1, 0, 0, true);
	}
	
}
