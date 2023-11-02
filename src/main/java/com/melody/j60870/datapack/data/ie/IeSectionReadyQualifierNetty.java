package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a section ready qualifier (SRQ) information element.
 */
public class IeSectionReadyQualifierNetty extends InformationNettyElement {

    private final int value;
    private final boolean sectionNotReady;

    public IeSectionReadyQualifierNetty(int value, boolean sectionNotReady) {
        this.value = value;
        this.sectionNotReady = sectionNotReady;
    }

    static IeSectionReadyQualifierNetty decode(ByteBuf is) throws IOException {
        int b1 = is.readUnsignedByte();
        int value = b1 & 0x7f;
        boolean sectionNotReady = ((b1 & 0x80) == 0x80);
        return new IeSectionReadyQualifierNetty(value, sectionNotReady);
    }

    @Override
    public int encode(ByteBuf buffer, int i) {
        buffer.writerIndex(i);
        
        byte temp = (byte) value;
        if (sectionNotReady) {
            temp |= 0x80;
        }
        buffer.writeByte(temp);
        return 1;
    }

    public int getValue() {
        return value;
    }

    public boolean isSectionNotReady() {
        return sectionNotReady;
    }

    @Override
    public String toString() {
        return "Section ready qualifier: " + value + ", section not ready: " + sectionNotReady;
    }
}
