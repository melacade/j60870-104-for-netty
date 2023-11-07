package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

abstract class IeAbstractQualifierOfCommandNetty extends InformationNettyElement {

    protected int value;

    IeAbstractQualifierOfCommandNetty(int qualifier, boolean select) {

        if (qualifier < 0 || qualifier > 31) {
            throw new IllegalArgumentException("Qualifier is out of bound: " + qualifier);
        }

        value = qualifier << 2;

        if (select) {
            value |= 0x80;
        }

    }

    IeAbstractQualifierOfCommandNetty(ByteBuf is) throws IOException {
        value = (is.readByte() & 0xff);
    }
    
    @Override
    public int encode(ByteBuf buffer, int i) {
//        buffer.writerIndex(i);
        buffer.writeByte(value);
        return 1;
    }

    /**
     * Returns true if the command selects and false if the command executes.
     * 
     * @return true if the command selects and false if the command executes.
     */
    public boolean isSelect() {
        return (value & 0x80) == 0x80;
    }

    public int getQualifier() {
        return (value >> 2) & 0x1f;
    }

    @Override
    public String toString() {
        return "selected: " + isSelect() + ", qualifier: " + getQualifier();
    }

}