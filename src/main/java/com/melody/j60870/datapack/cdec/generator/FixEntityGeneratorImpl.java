package com.melody.j60870.datapack.cdec.generator;

import com.melody.j60870.datapack.Frame;
import com.melody.j60870.datapack.cdec.IEntityGenerator;
import com.melody.j60870.datapack.dic.FixTable;
import com.melody.j60870.datapack.fix.*;
import com.melody.j60870.except.NotValidFrameException;
/**
 * @author melody
 */
public class FixEntityGeneratorImpl implements IEntityGenerator {
	
	
	@Override
	public Frame generate(byte controlOne,int len) throws NotValidFrameException {
		return generate(controlOne);
	}
	
	public Frame generate(byte controlOne) throws NotValidFrameException {
		switch (controlOne) {
			case FixTable.START_LINK:
				return new FixStartLinkFrame();
			case FixTable.START_LINK_ENSURE:
				return new FixStartLinkEnsureFrame();
			case FixTable.END:
				return new FixEndFrame();
			case FixTable.END_ENSURE:
				return new FixEndEnsureFrame();
			case FixTable.TEST:
				return new FixTestFrame();
			case FixTable.TEST_ENSURE:
				return new FixTestEnsureFrame();
			default:
				return new FixStartLinkFrame();
		}
	}
	
}
