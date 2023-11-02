package com.melody.j60870.datapack.data;

import lombok.Data;

/**
 * @author melody
 */
@Data
public class SinglePointData {
	byte addr;
	byte value;
	@lombok.Setter()
	ShortTime shortTime;
}
