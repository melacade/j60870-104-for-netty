package com.melody.j60870.datapack.cdec.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
/**
 * @author melody
 */
public class TestDecoder extends ByteToMessageDecoder {
	
	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
	
	}
	
}
