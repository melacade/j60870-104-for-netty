package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.text.MessageFormat;

public class IeValueWithTransientStateNetty extends InformationNettyElement {

    private final int value;
    private final boolean transientState;

    /**
     * Creates a VTI (value with transient state indication) information element.
     * 
     * @param value
     *            value between -64 and 63
     * @param transientState
     *            true if in transient state
     */
    public IeValueWithTransientStateNetty(int value, boolean transientState) {

        if (value < -64 || value > 63) {
            throw new IllegalArgumentException("Value has to be in the range -64..63");
        }

        this.value = value;
        this.transientState = transientState;

    }

    IeValueWithTransientStateNetty(ByteBuf is) throws IOException {
        int b1 = (is.readByte() & 0xff);

        transientState = ((b1 & 0x80) == 0x80);

        if ((b1 & 0x40) == 0x40) {
            value = b1 | 0xffffff80;
        }
        else {
            value = b1 & 0x3f;
        }

    }

    @Override
    public int encode(ByteBuf buffer, int i) {
//        buffer.writerIndex(i);
        if (transientState) {
            buffer.writeByte((byte) (value | 0x80));
        }
        else {
            buffer.writeByte((byte) (value & 0x7f));
        }

        return 1;

    }

    public int getValue() {
        return value;
    }

    public boolean getTransientState() {
        return transientState;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Value with transient state, value: {0}, transient state: {1}.", getValue(),
                getTransientState());
    }
}