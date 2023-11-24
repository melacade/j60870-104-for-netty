package com.melody.j60870;

import com.melody.j60870.datapack.config.ConnectionSettings;
import com.melody.j60870.datapack.data.CauseOfTransmission;
import com.melody.j60870.datapack.data.ie.IeQualifierOfCounterInterrogationNetty;
import com.melody.j60870.datapack.data.ie.IeQualifierOfInterrogationNetty;
import com.melody.j60870.datapack.init.ClientHandler;
import com.melody.j60870.datapack.init.ClientInit;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author melody
 */
@Slf4j
public class Client {
	
	private static final int DEFAULT_PORT = 2404;
	
	private SocketFactory socketFactory;
	private InetAddress address;
	private int port;
	private InetAddress localAddr;
	private int localPort;
	private ConnectionSettings settings;
	
	private ClientHandler clientHandler;
	private Channel channel;
	
	/**
	 * Creates a client connection builder that can be used to connect to the given address.
	 *
	 * @param address the address to connect to
	 */
	public Client(InetAddress address) {
		this.address = address;
		this.port = DEFAULT_PORT;
		
		this.localAddr = null;
		
		this.socketFactory = SocketFactory.getDefault();
	}
	
	public Client(String inetAddress) throws UnknownHostException {
		this(InetAddress.getByName(inetAddress));
	}
	
	public Client(Builder builder) {
		this.settings = builder.settings;
		this.address = builder.getBindAddr();
		this.port = builder.getPort();
	}
	
	/**
	 * Set the socket factory to used to create the socket for the connection. The default is
	 * {@link SocketFactory#getDefault()}. You could pass an {@link SSLSocketFactory} to enable SSL.
	 *
	 * @param socketFactory the socket factory
	 * @return this builder
	 */
	public Client setSocketFactory(SocketFactory socketFactory) {
		this.socketFactory = socketFactory;
		return this;
	}
	
	/**
	 * Sets the port to connect to. The default port is 2404.
	 *
	 * @param port the port to connect to.
	 * @return this builder
	 */
	public Client setPort(int port) {
		this.port = port;
		return this;
	}
	
	/**
	 * Sets the address to connect to.
	 *
	 * @param address the address to connect to.
	 * @return this builder
	 */
	public Client setAddress(InetAddress address) {
		this.address = address;
		return this;
	}
	
	/**
	 * Sets the local (client) address and port the socket will connect to.
	 *
	 * @param address the local address the socket is bound to, or null for any local address.
	 * @param port    the local port the socket is bound to or zero for a system selected free port.
	 * @return this builder
	 */
	public Client setLocalAddress(InetAddress address, int port) {
		this.localAddr = address;
		this.localPort = port;
		return this;
	}
	
	/**
	 * Sets connection time out t0, in milliseconds.<br>
	 * t0 (connectionTimeout) must be between 1000ms and 255000ms.
	 *
	 * @param time_t0 the timeout in milliseconds. Default is 20 s
	 * @return this builder
	 */
	public Client setConnectionTimeout(int time_t0) {
		if (time_t0 < 1000 || time_t0 > 255000) {
			throw new IllegalArgumentException(
					"invalid timeout: " + time_t0 + ", t0 (connectionTimeout) must be between 1000ms and 255000ms");
		}
		settings.setConnectionTimeout(time_t0);
		return this;
	}
	
	public void start() throws IOException {
		Bootstrap bootstrap = new Bootstrap();
		NioEventLoopGroup group = new NioEventLoopGroup(1);
		try {
			ChannelFuture sync = bootstrap.group(group)
					.channel(NioSocketChannel.class).handler(new ClientInit(settings))
					.connect(address, port).sync();
			log.info("连接成功！");
			this.channel = sync.channel();
			this.clientHandler = (ClientHandler) this.channel.pipeline().get("Init");
			sync.channel().closeFuture().sync();
			log.info("断开连接");
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			group.shutdownGracefully();
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	@Data
	public static class Builder {
		
		private ConnectionSettings settings = new ConnectionSettings();
		private InetAddress bindAddr = null;
		private int port = 2404;
		private int backlog = 0;
		private int maxConnections = 100;
		
		private Builder() {
		}
		
		public Builder ip(String ip) {
			try {
				this.bindAddr = InetAddress.getByName(ip);
			} catch (UnknownHostException e) {
				throw new RuntimeException(e);
			}
			return this;
		}
		
		public Builder port(int port) {
			this.port = port;
			return this;
		}
		
		public Client build(String ip) {
			try {
				this.bindAddr = InetAddress.getByName(ip);
			} catch (UnknownHostException e) {
				throw new RuntimeException(e);
			}
			return new Client(this);
		}
		
	}
	
	public void sendStartDt() {
		clientHandler.sendStart(channel.pipeline().firstContext());
	}
	
	public void sendSyncClockCommand(int commonAddr) throws IOException {
		clientHandler.sendSyncClockCommand(channel.pipeline().firstContext(), commonAddr);
	}
	
	/**
	 * 发送总招
	 */
	public void sendInterrogation(int commonAddr, CauseOfTransmission cot,IeQualifierOfInterrogationNetty qualifier) throws IOException {
		clientHandler.sendInterrogationCommand(channel.pipeline().firstContext(), commonAddr, cot,qualifier);
	}
	
	/**
	 * 发送电镀总招
	 */
	public void sendCounterInterrogation(int commonAddress, CauseOfTransmission cot, IeQualifierOfCounterInterrogationNetty qualifier) throws IOException {
		clientHandler.sendCounterInterrogation(channel.pipeline().firstContext(), commonAddress,cot,qualifier);
	}
	
}
