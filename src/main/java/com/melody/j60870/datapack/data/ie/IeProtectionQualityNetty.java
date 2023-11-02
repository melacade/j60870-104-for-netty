package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a quality descriptor for events of protection equipment (QDP) information element.
 */
public class IeProtectionQualityNetty extends IeAbstractNettyQuality {

    public IeProtectionQualityNetty(boolean elapsedTimeInvalid, boolean blocked, boolean substituted, boolean notTopical,
                                    boolean invalid) {
        super(blocked, substituted, notTopical, invalid);

        if (elapsedTimeInvalid) {
            value |= 0x08;
        }
    }

    IeProtectionQualityNetty(ByteBuf is) throws IOException {
        super(is);
    }

    public boolean isElapsedTimeInvalid() {
        return (value & 0x08) == 0x08;
    }

    @Override
    public String toString() {
        return "Protection Quality, elapsed time invalid: " + isElapsedTimeInvalid() + ", " + super.toString();
    }
}