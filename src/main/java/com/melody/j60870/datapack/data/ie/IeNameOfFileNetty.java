package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a name of file (NOF) information element.
 */
public class IeNameOfFileNetty extends InformationNettyElement {

    private final int value;

    public IeNameOfFileNetty(int value) {
        this.value = value;
    }

    static IeNameOfFileNetty decode(ByteBuf is) throws IOException {
        int value = is.readUnsignedByte() | (is.readUnsignedByte() << 8);
        return new IeNameOfFileNetty(value);
    }
    
    @Override
    public int encode(ByteBuf buffer, int i) {
//        buffer.writerIndex(i);
        buffer.writeByte((byte) value);
        buffer.writeByte((byte) (value >> 8));
        return 2;
        
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Name of file: " + value;
    }
}
