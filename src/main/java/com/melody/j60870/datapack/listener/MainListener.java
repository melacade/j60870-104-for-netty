package com.melody.j60870.datapack.listener;

import com.melody.j60870.datapack.data.APduNetty;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.io.IOException;

/**
 * @author melody
 */
@Slf4j
public class MainListener implements IframeListener{
	static {
		// 自动化注册
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = new Resource[0];
		try {
			
			// todo 这个路径可以弄成配置文件
			resources = resolver.getResources(("classpath*:" + MainListener.class.getPackage() + ".listeners.*-class").replace('.', '/').replace("package ", "").replace('-','.'));
		} catch (IOException e) {
		}
		SimpleMetadataReaderFactory simpleMetadataReaderFactory = new SimpleMetadataReaderFactory();
		for (Resource resource : resources) {
			try {
				String className = simpleMetadataReaderFactory.getMetadataReader(resource).getClassMetadata().getClassName();
				
				Object o = Class.forName(className).newInstance();
				if (o instanceof IframeListener) {
					((IframeListener) o).register();
					log.info("加载:{}", className);
				}
			} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException ignored) {
			}
		}
		log.info("注册了{}个IframeListener", mem.size());
	}
	@Override
	public void on(APduNetty aPduNetty, ChannelHandlerContext ctx) {
	}
	
	@Override
	public void register() {
	}
	
}
