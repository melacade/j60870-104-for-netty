package com.melody.j60870.datapack.cdec;

import com.melody.j60870.datapack.Frame;
import com.melody.j60870.except.NotValidFrameException;
/**
 * @author melody
 */
public interface IEntityGenerator {
	Frame generate(byte controlOne, int len) throws NotValidFrameException;
}
