package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a single command (SCO) information element.
 */
public class IeSingleCommandNetty extends IeAbstractQualifierOfCommandNetty {

    public IeSingleCommandNetty(boolean commandStateOn, int qualifier, boolean select) {
        super(qualifier, select);

        if (commandStateOn) {
            value |= 0x01;
        }
    }

    IeSingleCommandNetty(ByteBuf is) throws IOException {
        super(is);
    }

    public boolean isCommandStateOn() {
        return (value & 0x01) == 0x01;
    }

    @Override
    public String toString() {
        return "Single Command state on: " + isCommandStateOn() + ", " + super.toString();
    }

}