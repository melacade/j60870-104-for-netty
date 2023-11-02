package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a qualifier of interrogation (QOI) information element.
 */
public class IeQualifierOfInterrogationNetty extends InformationNettyElement {

    private final int value;

    public IeQualifierOfInterrogationNetty(int value) {
        this.value = value;
    }

    IeQualifierOfInterrogationNetty(ByteBuf is) throws IOException {
        value = (is.readByte() & 0xff);
    }
    
    @Override
    public int encode(ByteBuf buffer, int i) {
        buffer.writerIndex(i);
        buffer.writeByte((byte) value);
        return 1;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Qualifier of interrogation: " + value;
    }
}
