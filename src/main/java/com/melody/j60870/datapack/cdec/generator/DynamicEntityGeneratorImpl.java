package com.melody.j60870.datapack.cdec.generator;

import com.melody.j60870.datapack.Frame;
import com.melody.j60870.datapack.cdec.IEntityGenerator;
import com.melody.j60870.datapack.dynamic.FormatIframe;
import com.melody.j60870.datapack.dynamic.FormatSframe;

/**
 * @author melody
 */
public class DynamicEntityGeneratorImpl implements IEntityGenerator {
	
	@Override
	public Frame generate(byte controlOne, int len) {
		if ((controlOne & 0x01) == 1 && len == 4) {
			return new FormatSframe();
		} else {
			return new FormatIframe();
		}
		
	}
	
}
