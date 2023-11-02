package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a test sequence Counter (TSC) information element.
 */
public class IeTestSequenceCounterNetty extends InformationNettyElement {

    private static final int LOWER_BOUND = 0;
    private static final int UPPER_BOUND = 65535; // 2^16 - 1

    private final int value;

    public IeTestSequenceCounterNetty(int value) {
        if (value < LOWER_BOUND || value > UPPER_BOUND) {
            throw new IllegalArgumentException("Value has to be in the range 0..65535");
        }

        this.value = value;
    }

   public  static IeTestSequenceCounterNetty decode(ByteBuf is) throws IOException {
        return new IeTestSequenceCounterNetty(is.readUnsignedByte() | (is.readUnsignedByte() << 8));
    }

    @Override
    public int encode(ByteBuf buffer, int i) {
        buffer.writerIndex(i);
        buffer.writeByte((byte) value);
        buffer.writeByte((byte) (value >> 8));
        return 2;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Test sequence counter: " + getValue();
    }

}
