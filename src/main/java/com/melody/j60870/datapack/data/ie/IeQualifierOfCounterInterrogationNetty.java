package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a qualifier of counter interrogation (QCC) information element.
 */
public class IeQualifierOfCounterInterrogationNetty extends InformationNettyElement {

    private final int request;
    private final int freeze;

    public IeQualifierOfCounterInterrogationNetty(int request, int freeze) {
        this.request = request;
        this.freeze = freeze;
    }

    IeQualifierOfCounterInterrogationNetty(ByteBuf is) throws IOException {
        int b1 = (is.readByte() & 0xff);
        request = b1 & 0x3f;
        freeze = (b1 >> 6) & 0x03;
    }
    
    @Override
    public int encode(ByteBuf buffer, int i) {
//        buffer.writerIndex(i);
        buffer.writeByte((byte) (request | (freeze << 6)));
        return 1;
    }

    public int getRequest() {
        return request;
    }

    public int getFreeze() {
        return freeze;
    }

    @Override
    public String toString() {
        return "Qualifier of counter interrogation, request: " + request + ", freeze: " + freeze;
    }
}