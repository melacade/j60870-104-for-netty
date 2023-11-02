package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a qualifier of parameter activation (QPA) information element.
 */
public class IeQualifierOfParameterActivationNetty extends InformationNettyElement {

    private final int value;

    public IeQualifierOfParameterActivationNetty(int value) {
        this.value = value;
    }

    static IeQualifierOfParameterActivationNetty decode(ByteBuf is) throws IOException {
        return new IeQualifierOfParameterActivationNetty(is.readUnsignedByte());
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
        return "Qualifier of parameter activation: " + value;
    }
}
