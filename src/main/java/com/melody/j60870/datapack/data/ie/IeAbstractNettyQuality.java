package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.text.MessageFormat;

abstract class IeAbstractNettyQuality extends InformationNettyElement {

    protected int value;

    public IeAbstractNettyQuality(boolean blocked, boolean substituted, boolean notTopical, boolean invalid) {

        value = 0;

        if (blocked) {
            value |= 0x10;
        }
        if (substituted) {
            value |= 0x20;
        }
        if (notTopical) {
            value |= 0x40;
        }
        if (invalid) {
            value |= 0x80;
        }

    }

    IeAbstractNettyQuality(ByteBuf is) throws IOException {
        value = is.readUnsignedByte();
    }
    
    @Override
    public int encode(ByteBuf buffer, int i) {
        buffer.writeByte((byte) value);
        return 1;
    }

    public boolean isBlocked() {
        return hasBitSet(0x10);
    }

    public boolean isSubstituted() {
        return hasBitSet(0x20);
    }

    public boolean isNotTopical() {
        return hasBitSet(0x40);
    }

    public boolean isInvalid() {
        return hasBitSet(0x80);
    }

    private boolean hasBitSet(int mask) {
        return (value & mask) == mask;
    }

    @Override
    public String toString() {
        return MessageFormat.format("blocked: {0}, substituted: {1}, not topical: {2}, invalid: {3}", isBlocked(),
                isSubstituted(), isNotTopical(), isInvalid());
    }
}