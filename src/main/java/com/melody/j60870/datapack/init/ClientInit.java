package com.melody.j60870.datapack.init;

import com.melody.j60870.datapack.config.ConnectionNettySettings;
import com.melody.j60870.datapack.config.ConnectionSettings;
import com.melody.j60870.datapack.decode.ApduDecoder;
import com.melody.j60870.datapack.decode.FrameCat;
import com.melody.j60870.datapack.encode.ApduEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author melody
 */
public class ClientInit extends ChannelInitializer<SocketChannel> {
	
	ConnectionNettySettings settings;
	
	public ClientInit(ConnectionSettings settings) {
		this.settings = new ConnectionNettySettings(settings);
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.config().setTcpNoDelay(true);
		ch.pipeline().addLast(new FrameCat())
				.addLast(new ApduEncoder(settings))
				.addLast(new ApduDecoder(settings))
				.addLast(new IdleStateHandler(settings.maxIdleTime, settings.maxIdleTime, settings.maxIdleTime))
				.addLast("state",new StateHandler())
				.addLast("Init", new ClientHandler(settings));
	}
	
	
	
}
