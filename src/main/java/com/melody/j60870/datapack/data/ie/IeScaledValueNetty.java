package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a scaled value (SVA) information element.
 */
public class IeScaledValueNetty extends IeNormalizedValueNetty {

    /**
     * Scaled value is a 16 bit integer (short) in the range from -32768 to 32767
     * 
     * @param value
     *            value in the range -32768 to 32767
     */
    public IeScaledValueNetty(int value) {
        super(value);
    }

    IeScaledValueNetty(ByteBuf is) throws IOException {
        super(is);
    }

    @Override
    public String toString() {
        return "Scaled value: " + value;
    }

}