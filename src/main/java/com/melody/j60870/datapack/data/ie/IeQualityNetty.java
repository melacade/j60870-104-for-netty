package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a quality descriptor (QDS) information element.
 */
public class IeQualityNetty extends IeAbstractNettyQuality {

    public IeQualityNetty(boolean overflow, boolean blocked, boolean substituted, boolean notTopical, boolean invalid) {
        super(blocked, substituted, notTopical, invalid);

        if (overflow) {
            value |= 0x01;
        }
    }

    IeQualityNetty(ByteBuf is) throws IOException {
        super(is);
    }

    public boolean isOverflow() {
        return (value & 0x01) == 0x01;
    }

    @Override
    public String toString() {
        return "Quality, overflow: " + isOverflow() + ", " + super.toString();
    }
}