package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a fixed test bit pattern (FBP) information element.
 */
public class IeFixedTestBitPattern extends InformationNettyElement {

    public IeFixedTestBitPattern() {
    }

    IeFixedTestBitPattern(ByteBuf is) throws IOException {
        if ((is.readByte() & 0xff) != 0x55 || (is.readByte() & 0xff) != 0xaa) {
            throw new IOException("Incorrect bit pattern in Fixed Test Bit Pattern.");
        }
    }

    @Override
    public int encode(ByteBuf buffer, int i) {
//        buffer.writerIndex(i);
        buffer.writeByte(0x55);
        buffer.writeByte((byte) 0xaa);
        return 2;
    }

    @Override
    public String toString() {
        return "Fixed test bit pattern";
    }
}
