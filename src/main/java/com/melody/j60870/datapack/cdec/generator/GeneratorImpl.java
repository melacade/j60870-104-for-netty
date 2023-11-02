package com.melody.j60870.datapack.cdec.generator;

import com.melody.j60870.datapack.Frame;
import com.melody.j60870.datapack.cdec.IEntityGenerator;
import com.melody.j60870.except.NotValidFrameException;

/**
 * @author melody
 */
public class GeneratorImpl implements IEntityGenerator {
	
	private final static byte FIX_LEN_FRAME = 0x4;
	private final FixEntityGeneratorImpl fixEntityGenerator = new FixEntityGeneratorImpl();
	private final DynamicEntityGeneratorImpl dynamicEntityGenerator = new DynamicEntityGeneratorImpl();
	
	@Override
	public Frame generate(byte controlOne, int len) throws NotValidFrameException {
		if ((controlOne & 0x01) == 0 || (controlOne & 0x03) == 1) {
			return dynamicEntityGenerator.generate(controlOne,len);
		} else {
			return fixEntityGenerator.generate(controlOne);
		}
	}
	
}
