package com.melody.j60870.datapack.encode;

import com.melody.j60870.datapack.config.ConnectionNettySettings;
import com.melody.j60870.datapack.data.APduNetty;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author melody
 */
@Slf4j
public class ApduEncoder extends MessageToByteEncoder<APduNetty> {
	
	private ConnectionNettySettings settings;
	
	public ApduEncoder(ConnectionNettySettings settings) {
		super();
		this.settings = settings;
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, APduNetty msg, ByteBuf out) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("encoding msg \n{}",msg);
		}
		msg.encode(out, settings);
	}
	
}
