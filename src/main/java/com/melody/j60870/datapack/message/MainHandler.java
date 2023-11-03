package com.melody.j60870.datapack.message;

import com.melody.j60870.datapack.data.APduNetty;
import com.melody.j60870.datapack.message.handlers.EmptyHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author melody
 */
@Slf4j
public class MainHandler extends MessageHandler {
	
	private static final Map<APduNetty.ApciType,MessageHandler> mem = new EnumMap<>(APduNetty.ApciType.class);
	
	public static void register(APduNetty.ApciType type, MessageHandler messageHandler) {
		mem.put(type, messageHandler);
	}
	
	static {
		// 自动化注册
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = new Resource[0];
		try {
			
			// todo 这个路径可以弄成配置文件
			resources = resolver.getResources(("classpath*:" + MainHandler.class.getPackage() + ".handlers.*-class").replace('.', '/').replace("package ", "").replace('-','.'));
		} catch (IOException e) {
		}
		SimpleMetadataReaderFactory simpleMetadataReaderFactory = new SimpleMetadataReaderFactory();
		for (Resource resource : resources) {
			try {
				String className = simpleMetadataReaderFactory.getMetadataReader(resource).getClassMetadata().getClassName();
				
				Object o = Class.forName(className).newInstance();
				if (o instanceof MessageHandler) {
					((MessageHandler) o).register();
					log.info("加载:{}", className);
				}
			} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException ignored) {
			}
		}
		log.info("注册了{}个MessageHandler", mem.size());
	}
	
	@Override
	protected void register() {
	}
	
	@Override
	public APduNetty toServer(APduNetty netty, ChannelHandlerContext ctx) {
		return get(netty).toServer(netty, ctx);
	}
	
	@Override
	public APduNetty toClient(APduNetty netty, ChannelHandlerContext ctx) {
		return get(netty).toClient(netty, ctx);
	}
	
	static MessageHandler get(APduNetty type) {
		if (type == null) {
			return new EmptyHandler();
		}
		return mem.getOrDefault(type.getApciType(), new EmptyHandler());
	}
	
}
