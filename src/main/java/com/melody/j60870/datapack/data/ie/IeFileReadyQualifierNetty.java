package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a file ready qualifier (FRQ) information element.
 */
public class IeFileReadyQualifierNetty extends InformationNettyElement {

    private final int value;
    private final boolean negativeConfirm;

    public IeFileReadyQualifierNetty(int value, boolean negativeConfirm) {
        this.value = value;
        this.negativeConfirm = negativeConfirm;
    }

    public static InformationNettyElement decode(ByteBuf is) throws IOException {
        int b1 = is.readUnsignedByte();
        int value = b1 & 0x7f;
        boolean negativeConfirm = ((b1 & 0x80) == 0x80);

        return new IeFileReadyQualifierNetty(value, negativeConfirm);
    }

    @Override
    public int encode(ByteBuf buffer, int i) {
        buffer.writerIndex(i);
        
        byte temp = (byte) value;
        if (negativeConfirm) {
            temp |= 0x80;
        }
        buffer.writeByte(temp);
        return 1;
    }

    public int getValue() {
        return value;
    }

    public boolean isNegativeConfirm() {
        return negativeConfirm;
    }

    @Override
    public String toString() {
        return "File ready qualifier: " + value + ", negative confirm: " + negativeConfirm;
    }

}
