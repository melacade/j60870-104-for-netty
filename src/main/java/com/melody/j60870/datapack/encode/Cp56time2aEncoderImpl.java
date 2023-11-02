package com.melody.j60870.datapack.encode;

import com.melody.j60870.datapack.Frame;
import com.melody.j60870.datapack.cdec.generator.Cp56time2aDecoderImpl;
import com.melody.j60870.datapack.dynamic.FormatIframe;
import com.melody.j60870.except.NotValidFrameException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author melody
 */
public class Cp56time2aEncoderImpl {
	
	ByteBuf encode(Frame frame, ByteBuf buf) {
		Date date = frame.getDate();
		Calendar instance = Calendar.getInstance();
		instance.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		instance.setTime(date);
		buf.writeShortLE(instance.get(Calendar.SECOND) * 1000 + instance.get(Calendar.MILLISECOND));
		int min = instance.get(Calendar.MINUTE);
		// todo 先默认iv为有效
		buf.writeByte(min);
		int hour = instance.get(Calendar.HOUR_OF_DAY);
		// todo 先忽略夏令时
		buf.writeByte(hour);
		int weekDay = instance.get(Calendar.DAY_OF_WEEK) - 1;
		weekDay = weekDay == 0 ? 7 : weekDay;
		int day = instance.get(Calendar.DAY_OF_MONTH);
		int b = (weekDay << 4) | day;
		buf.writeByte(b);
		buf.writeByte(instance.get(Calendar.MONTH) + 1);
		buf.writeByte(instance.get(Calendar.YEAR) - 2000);
		return buf;
	}
	
	public static void main(String[] args) throws NotValidFrameException {
		Cp56time2aEncoderImpl cp56time2aEncoder = new Cp56time2aEncoderImpl();
		Cp56time2aDecoderImpl cp56time2aDecoder = new Cp56time2aDecoderImpl();
		FormatIframe formatIframe = new FormatIframe();
		formatIframe.setDate(Calendar.getInstance().getTime());
		ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
		cp56time2aEncoder.encode(formatIframe, buffer);
		FormatIframe formatIframe1 = new FormatIframe();
		cp56time2aDecoder.decode(buffer, formatIframe1);
		System.out.println(formatIframe1.equals(formatIframe));
		
	}
	
}
