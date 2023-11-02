package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class IeDoublePointWithQualityNetty extends IeAbstractNettyQuality {

    public enum DoublePointInformation {
        INDETERMINATE_OR_INTERMEDIATE,
        OFF,
        ON,
        INDETERMINATE;
    }

    public IeDoublePointWithQualityNetty(DoublePointInformation dpi, boolean blocked, boolean substituted,
                                         boolean notTopical, boolean invalid) {
        super(blocked, substituted, notTopical, invalid);

        switch (dpi) {
        case INDETERMINATE_OR_INTERMEDIATE:
            break;
        case OFF:
            value |= 0x01;
            break;
        case ON:
            value |= 0x02;
            break;
        case INDETERMINATE:
            value |= 0x03;
            break;
        }
    }

    IeDoublePointWithQualityNetty(ByteBuf is) throws IOException {
        super(is);
    }

    public DoublePointInformation getDoublePointInformation() {
        switch (value & 0x03) {
        case 0:
            return DoublePointInformation.INDETERMINATE_OR_INTERMEDIATE;
        case 1:
            return DoublePointInformation.OFF;
        case 2:
            return DoublePointInformation.ON;
        default:
            return DoublePointInformation.INDETERMINATE;
        }
    }

    @Override
    public String toString() {
        return "Double Point: " + getDoublePointInformation() + ", " + super.toString();
    }
}