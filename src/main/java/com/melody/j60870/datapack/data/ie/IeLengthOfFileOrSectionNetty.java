package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a length of file or section (LOF) information element.
 */
public class IeLengthOfFileOrSectionNetty extends InformationNettyElement {

    private final int value;

    public IeLengthOfFileOrSectionNetty(int value) {
        this.value = value;
    }

    static IeLengthOfFileOrSectionNetty decode(ByteBuf is) throws IOException {
        int value = 0;
        for (int i = 0; i < 3; ++i) {
            value |= is.readUnsignedByte() << (i * 8);
        }
        return new IeLengthOfFileOrSectionNetty(value);
    }

    @Override
    public int encode(ByteBuf buffer, int i) {
        buffer.writerIndex(i);
        buffer.writeByte((byte) value);
        buffer.writeByte((byte) (value >> 8));
        buffer.writeByte((byte) (value >> 16));
        return 3;

    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Length of file or section: " + value;
    }
}
