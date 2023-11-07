package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents the segment of a file as transferred by ASDUs of type F_SG_NA_1 (125).
 */
public class IeFileSegmentNetty extends InformationNettyElement {

    private final byte[] segment;
    private final int offset;
    private final int length;

    public IeFileSegmentNetty(byte[] segment, int offset, int length) {
        this.segment = segment;
        this.offset = offset;
        this.length = length;
    }

    IeFileSegmentNetty(ByteBuf is) throws IOException {

        length = (is.readByte() & 0xff);
        segment = new byte[length];
        is.readBytes(segment);
        offset = 0;
    }

    @Override
   public int encode(ByteBuf buffer, int i) {
//        buffer.writerIndex(i);
        buffer.writeByte((byte) length);
        buffer.writeBytes(segment);
        return length + 1;

    }

    public byte[] getSegment() {
        return segment;
    }

    @Override
    public String toString() {
        return "File segment of length: " + length;
    }
}
