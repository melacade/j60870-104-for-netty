package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a qualifier of set-point command (QOS) information element.
 */
public class IeQualifierOfSetPointCommandNetty extends InformationNettyElement {

    private final int ql;
    private final boolean select;

    public IeQualifierOfSetPointCommandNetty(int ql, boolean select) {
        this.ql = ql;
        this.select = select;
    }

    IeQualifierOfSetPointCommandNetty(ByteBuf is) throws IOException {
        int b1 = (is.readByte() & 0xff);
        ql = b1 & 0x7f;
        select = ((b1 & 0x80) == 0x80);
    }
    
    @Override
    public int encode(ByteBuf buffer, int i) {
//        buffer.writerIndex(i);
        if (select) {
            buffer.writeByte(ql | 0x80);
        } else {
            buffer.writeByte(ql);
        }
        return 1;
    }

    public int getQl() {
        return ql;
    }

    public boolean isSelect() {
        return select;
    }

    @Override
    public String toString() {
        return "Qualifier of set point command, QL: " + ql + ", select: " + select;
    }
}
