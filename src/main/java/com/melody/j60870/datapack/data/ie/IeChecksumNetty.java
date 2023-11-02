package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a checksum (CHS) information element.
 */
public class IeChecksumNetty extends InformationNettyElement {

    private final int value;

    public IeChecksumNetty(int value) {
        this.value = value;
    }

    static IeChecksumNetty decode(ByteBuf is) throws IOException {
        return new IeChecksumNetty(is.readUnsignedByte());
    }
    
    @Override
    public int encode(ByteBuf buffer, int i) {
        buffer.writerIndex(i).writeByte((byte) value);
        return 1;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Checksum: " + value;
    }
}
