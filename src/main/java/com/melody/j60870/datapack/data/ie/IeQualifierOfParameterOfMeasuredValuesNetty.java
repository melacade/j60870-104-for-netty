package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a qualifier of parameter of measured values (QPM) information element.
 */
public class IeQualifierOfParameterOfMeasuredValuesNetty extends InformationNettyElement {

    private final int kindOfParameter;
    private final boolean change;
    private final boolean notInOperation;

    public IeQualifierOfParameterOfMeasuredValuesNetty(int kindOfParameter, boolean change, boolean notInOperation) {
        this.kindOfParameter = kindOfParameter;
        this.change = change;
        this.notInOperation = notInOperation;
    }

    IeQualifierOfParameterOfMeasuredValuesNetty(ByteBuf is) throws IOException {
        int b1 = (is.readByte() & 0xff);
        kindOfParameter = b1 & 0x3f;
        change = ((b1 & 0x40) == 0x40);
        notInOperation = ((b1 & 0x80) == 0x80);
    }
    
    @Override
    public int encode(ByteBuf buffer, int i) {
        buffer.writerIndex(i);
        byte temp  = (byte) kindOfParameter;
        if (change) {
            temp |= 0x40;
        }
        if (notInOperation) {
            temp |= 0x80;
        }
        buffer.writeByte(temp);
        return 1;
    }

    public int getKindOfParameter() {
        return kindOfParameter;
    }

    public boolean isChange() {
        return change;
    }

    public boolean isNotInOperation() {
        return notInOperation;
    }

    @Override
    public String toString() {
        return "Qualifier of parameter of measured values, kind of parameter: " + kindOfParameter + ", change: "
                + change + ", not in operation: " + notInOperation;
    }
}
