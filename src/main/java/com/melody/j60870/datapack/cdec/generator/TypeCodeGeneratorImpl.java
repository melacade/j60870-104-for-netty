package com.melody.j60870.datapack.cdec.generator;

import com.melody.j60870.datapack.Frame;
import com.melody.j60870.datapack.cdec.IEntityGenerator;
import com.melody.j60870.except.NotValidFrameException;

/**
 * @author melody
 */
public class TypeCodeGeneratorImpl implements IEntityGenerator {
	
	@Override
	public Frame generate(byte controlOne,int len) throws NotValidFrameException {
		int typeCode = Byte.toUnsignedInt(controlOne);
		switch (typeCode) {
			case 0x01:
				// TODO: 2023/9/20 单点遥信
		}
		return null;
	}
	
}
