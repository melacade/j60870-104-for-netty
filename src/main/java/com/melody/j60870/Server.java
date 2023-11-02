package com.melody.j60870;

import com.melody.j60870.datapack.config.ConnectionNettySettings;
import com.melody.j60870.datapack.config.ConnectionSettings;
import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.init.ServerInit;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.openmuc.j60870.ServerEventListener;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The server is used to start listening for IEC 60870-5-104 client connections.
 */
@Slf4j
public class Server {
	
	private Map<ChannelId,SocketChannel> clients = new ConcurrentHashMap<>();
	private final int port;
	private final InetAddress bindAddr;
	private final int backlog;
	private final ServerSocketFactory serverSocketFactory;
	private final int maxConnections;
	private final List<String> allowedClientIps;
	private ServerBootstrap server;
	private final ConnectionNettySettings settings;
	
	private ExecutorService exec;
	
	private Server(Builder builder) {
		port = builder.port;
		bindAddr = builder.bindAddr;
		backlog = builder.backlog;
		serverSocketFactory = builder.serverSocketFactory;
		maxConnections = builder.maxConnections;
		allowedClientIps = builder.allowedClientIps;
		settings = new ConnectionNettySettings(builder.settings);
	}
	
	AtomicInteger count = new AtomicInteger(0);
	
	public static Builder builder() {
		return new Builder();
	}
	
	/**
	 * Starts a new thread that listens on the configured port. This method is non-blocking.
	 * e ServerConnectionListener that will be notified when remote clients are connecting or the server
	 * stopped listening.
	 *
	 * @throws IOException if any kind of error occurs while creating the server socket.
	 */
	public void start() throws IOException, InterruptedException {
		NioEventLoopGroup boos = new NioEventLoopGroup();
		NioEventLoopGroup worker = new NioEventLoopGroup();
		try {
			server = new ServerBootstrap().channel(NioServerSocketChannel.class).group(boos, worker);
			server.childHandler(new ServerInit(settings, clients));
			ChannelFuture sync = server.bind(port).sync();
			log.info("正在监听端口{}", port);
			sync.channel().closeFuture().sync();
		} finally {
			boos.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
	
	public void send(APduNetty data) {
		log.info("发送数据：{}", data);
		for (Map.Entry<ChannelId,SocketChannel> next : clients.entrySet()) {
			ChannelId key = next.getKey();
			SocketChannel value = next.getValue();
			value.writeAndFlush(data);
			log.info("客户端{}", key);
		}
	}
	
	/**
	 * Stop listening for new connections. Existing connections are not touched.
	 */
	public void stop() {
		//        if (serverThread == null) {
		//            return;
		//        }
		//
		//        serverThread.stopServer();
		//
		//        if (this.settings.useSharedThreadPool()) {
		//            ConnectionSettings.decrementConnectionsCounter();
		//        }
		//        else {
		//            this.exec.shutdown();
		//        }
		//
		//        serverThread = null;
		
		
	}
	
	/**
	 * The server builder which builds a 60870 server instance.
	 *
	 * @see Server#builder()
	 */
	public static class Builder {
		
		private int port = 2404;
		private InetAddress bindAddr = null;
		private int backlog = 0;
		private ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();
		private List<String> allowedClientIps = null;
		
		private int maxConnections = 100;
		private ConnectionSettings settings = new ConnectionSettings();
		
		private Builder() {
		}
		
		/**
		 * Sets the TCP port that the server will listen on. IEC 60870-5-104 usually uses port 2404.
		 *
		 * @param port the port
		 * @return this builder
		 */
		public Builder setPort(int port) {
			this.port = port;
			return this;
		}
		
		/**
		 * Sets the backlog that is passed to the java.net.ServerSocket.
		 *
		 * @param backlog the backlog
		 * @return this builder
		 */
		public Builder setBacklog(int backlog) {
			this.backlog = backlog;
			return this;
		}
		
		/**
		 * Sets the IP address to bind to. It is passed to java.net.ServerSocket
		 *
		 * @param bindAddr the IP address to bind to
		 * @return this builder
		 */
		public Builder setBindAddr(InetAddress bindAddr) {
			this.bindAddr = bindAddr;
			return this;
		}
		
		/**
		 * Sets the ServerSocketFactory to be used to create the ServerSocket. Default is
		 * ServerSocketFactory.getDefault().
		 *
		 * @param socketFactory the ServerSocketFactory to be used to create the ServerSocket
		 * @return this builder
		 */
		public Builder setSocketFactory(ServerSocketFactory socketFactory) {
			this.serverSocketFactory = socketFactory;
			return this;
		}
		
		/**
		 * Set the maximum number of client connections that are allowed in parallel.
		 *
		 * @param maxConnections the number of connections allowed (default is 100) @ return this builder
		 * @return this builder
		 */
		public Builder setMaxConnections(int maxConnections) {
			if (maxConnections <= 0) {
				throw new IllegalArgumentException("maxConnections is out of bound");
			}
			this.maxConnections = maxConnections;
			return this;
		}
		
		/**
		 * Set the IPs from which clients may connect. Pass {@code null} to allow all clients. By default all clients
		 * are allowed to connect.
		 *
		 * @param allowedClientIps the allowed client IPs
		 * @return this builder
		 */
		public Builder setAllowedClients(List<String> allowedClientIps) {
			this.allowedClientIps = allowedClientIps;
			return this;
		}
		
		/**
		 * To start/activate the server call {@link Server#(ServerEventListener)} on the returned server.
		 */
		public Server build() {
			return new Server(this);
		}
		
		
	}
	
}