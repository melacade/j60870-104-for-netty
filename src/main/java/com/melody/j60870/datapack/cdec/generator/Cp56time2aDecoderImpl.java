package com.melody.j60870.datapack.cdec.generator;

import com.melody.j60870.datapack.Frame;
import com.melody.j60870.datapack.cdec.IDecoder;
import com.melody.j60870.datapack.dynamic.FloatFrame;
import com.melody.j60870.except.NotValidFrameException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.openmuc.j60870.internal.HexUtils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author melody
 */
@Slf4j
public class Cp56time2aDecoderImpl implements IDecoder {
	
	public Frame decode(ByteBuf data, Frame f) throws NotValidFrameException {
		if (data.readableBytes() < 7) {
			throw new NotValidFrameException("没有足够长的数据用来解析Cp56time2a", data.readUnsignedByte());
		}
		int ms = data.readUnsignedShortLE();
		short i = data.readUnsignedByte();
		boolean iv = ((i >> 7) & 1) == 0;
		if (!iv) {
			log.info("iv无效{}", iv);
		}
		int res1 = ((i >> 6) & 1);
		int min = i & 0b00111111;
		i = data.readUnsignedByte();
		boolean su = ((i >> 7) & 1) == 1;
		log.info("夏令时: {}", su);
		int res2 = ((i & 0b1100000) >> 5);
		int hour = i & 0b00011111;
		i = data.readUnsignedByte();
		int dayOfWeek = (0b11100000 & i) >> 5;
		int dayOfMonth = (0b11111 & i);
		i = data.readUnsignedByte();
		int res3 = ((0b11110000 & i) >> 4);
		int months = (0b1111 & i);
		i = data.readUnsignedByte();
		int res4 = (0b10000000 & i) >> 7;
		int year = (0b01111111 & i);
		Calendar instance = Calendar.getInstance();
		int y = instance.get(Calendar.YEAR)/1000*1000;
		instance.set(year + y, months - 1, dayOfMonth, hour, min, ms / 1000);
		instance.set(Calendar.MILLISECOND, ms - ms / 1000 * 1000);
		instance.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		f.setDate(iv ? instance.getTime() : null);
		return f;
	}
	
	public static void main(String[] args) throws NotValidFrameException {
		long l = System.currentTimeMillis();
		String s = "A7232E0FB80217";
		byte[] bytes = HexUtils.hexToBytes(s);
		ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer().writeBytes(bytes);
		System.out.println(System.currentTimeMillis() - l);
		l = System.currentTimeMillis();
		
		new Cp56time2aDecoderImpl().decode(byteBuf,new FloatFrame());
		System.out.println(l-System.currentTimeMillis());
	}
	
}
