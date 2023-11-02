package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a last section or segment qualifier (LSQ) information element.
 */
public class IeLastSectionOrSegmentQualifierNetty extends InformationNettyElement {

    private final int value;

    public IeLastSectionOrSegmentQualifierNetty(int value) {
        this.value = value;
    }

    static IeLastSectionOrSegmentQualifierNetty decode(ByteBuf is) throws IOException {
        return new IeLastSectionOrSegmentQualifierNetty(is.readUnsignedByte());
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
        return "Last section or segment qualifier: " + value;
    }
}
