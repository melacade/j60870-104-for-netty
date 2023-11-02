package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a start events of protection equipment (SPE) information element.
 */
public class IeProtectionStartEventNetty extends InformationNettyElement {

    private int value;

    public IeProtectionStartEventNetty(boolean generalStart, boolean startOperationL1, boolean startOperationL2,
                                       boolean startOperationL3, boolean startOperationIe, boolean startReverseOperation) {

        value = 0;

        if (generalStart) {
            value |= 0x01;
        }
        if (startOperationL1) {
            value |= 0x02;
        }
        if (startOperationL2) {
            value |= 0x04;
        }
        if (startOperationL3) {
            value |= 0x08;
        }
        if (startOperationIe) {
            value |= 0x10;
        }
        if (startReverseOperation) {
            value |= 0x20;
        }
    }

    IeProtectionStartEventNetty(ByteBuf is) throws IOException {
        value = (is.readByte() & 0xff);
    }
    
    @Override
    public int encode(ByteBuf buffer, int i) {
        buffer.writeByte((byte) value);
        return 1;
    }

    public boolean isGeneralStart() {
        return (value & 0x01) == 0x01;
    }

    public boolean isStartOperationL1() {
        return (value & 0x02) == 0x02;
    }

    public boolean isStartOperationL2() {
        return (value & 0x04) == 0x04;
    }

    public boolean isStartOperationL3() {
        return (value & 0x08) == 0x08;
    }

    public boolean isStartOperationIe() {
        return (value & 0x10) == 0x10;
    }

    public boolean isStartReverseOperation() {
        return (value & 0x20) == 0x20;
    }

    @Override
    public String toString() {
        return "Protection start event, general start of operation: " + isGeneralStart() + ", start of operation L1: "
                + isStartOperationL1() + ", start of operation L2: " + isStartOperationL2()
                + ", start of operation L3: " + isStartOperationL3() + ", start of operation IE(earth current): "
                + isStartOperationIe() + ", start of operation in reverse direction: " + isStartReverseOperation();
    }

}