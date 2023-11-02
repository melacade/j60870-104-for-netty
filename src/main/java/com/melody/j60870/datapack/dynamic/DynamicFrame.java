package com.melody.j60870.datapack.dynamic;

import com.melody.j60870.datapack.Frame;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author melody
 */
@Data

public abstract class DynamicFrame<T> implements Frame {
	
	Date date;
	
	/**
	 * 类型标志符
	 */
	byte transformType;
	/**
	 * 数据是否连续
	 */
	boolean coiled;
	/**
	 * 数据数量
	 */
	byte dataNum;
	/**
	 * 传送原因高位
	 */
	byte transformReasonHigh;
	/**
	 * 传送原因低位
	 */
	byte transformReasonLow;
	/**
	 * 传送地址高位
	 */
	byte commonLocationHigh;
	/**
	 * 传送地址低位
	 */
	byte commonLocationLow;
	/**
	 * 数据
	 */
	List<T> data;
	
	List<Long> address = new ArrayList<>();
	
}
