package com.melody.j60870.datapack.message;

import com.melody.j60870.datapack.data.APduNetty;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author melody
 */
public abstract class MessageHandler {
	
	/**
	 * 实现以注册对应的104报文类型
	 */
	protected abstract void register();
	/**
	 * 实现以在客户端调用
	 *
	 * @param netty 报文
	 * @param ctx channel对象
	 * @return 报文
	 */
	public abstract APduNetty toServer(APduNetty netty, ChannelHandlerContext ctx);
	/**
	 * 实现以在服务器调用
	 * @param netty 报文
	 * @param ctx channel对象
	 * @return 报文
	 */
	public abstract APduNetty toClient(APduNetty netty, ChannelHandlerContext ctx);
}
