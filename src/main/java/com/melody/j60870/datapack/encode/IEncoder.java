package com.melody.j60870.datapack.encode;

import com.melody.j60870.datapack.Frame;
import io.netty.buffer.ByteBuf;
/**
 * @author melody
 */
public interface IEncoder {
	ByteBuf formBuf(Frame frame);
}
