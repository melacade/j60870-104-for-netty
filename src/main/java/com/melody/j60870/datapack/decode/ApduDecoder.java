package com.melody.j60870.datapack.decode;

import com.melody.j60870.datapack.config.ConnectionNettySettings;
import com.melody.j60870.datapack.data.APduNetty;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author melody
 */
@Slf4j
public class ApduDecoder extends ByteToMessageDecoder {
	ConnectionNettySettings settings;
	public ApduDecoder(ConnectionNettySettings settings) {
		this.settings = settings;
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//		byte aByte = in.getByte(0);
//		if (aByte != 0x68) {
//			log.error("错误的报文起始");
//			ctx.close();
//			return;
//		}
		APduNetty decode = APduNetty.decode(in, settings);
		out.add(decode);
	}
	
}
