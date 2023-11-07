package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a short floating point number (R32-IEEE STD 754) information element.
 */
public class IeShortFloatNetty extends InformationNettyElement {

    private final float value;

    public IeShortFloatNetty(float value) {
        this.value = value;
    }

    IeShortFloatNetty(ByteBuf is) throws IOException {
        value = Float.intBitsToFloat((is.readByte() & 0xff) | ((is.readByte() & 0xff) << 8)
                | ((is.readByte() & 0xff) << 16) | ((is.readByte() & 0xff) << 24));
    }

    @Override
    public int encode(ByteBuf buffer, int i) {
//        buffer.writerIndex(i);
        int tempVal = Float.floatToIntBits(value);
        buffer.writeIntLE(tempVal);

        return 4;
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Short float value: " + value;
    }
}