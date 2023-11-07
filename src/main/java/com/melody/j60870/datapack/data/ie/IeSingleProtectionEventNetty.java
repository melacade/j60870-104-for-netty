package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a single event of protection equipment (SEP) information element.
 */
public class IeSingleProtectionEventNetty extends InformationNettyElement {

    private int value;

    public enum EventState {
        INDETERMINATE,
        OFF,
        ON;
    }

    public IeSingleProtectionEventNetty(EventState eventState, boolean elapsedTimeInvalid, boolean blocked,
                                        boolean substituted, boolean notTopical, boolean eventInvalid) {

        value = 0;

        switch (eventState) {
        case OFF:
            value |= 0x01;
            break;
        case ON:
            value |= 0x02;
            break;
        default:
            break;
        }

        if (elapsedTimeInvalid) {
            value |= 0x08;
        }
        if (blocked) {
            value |= 0x10;
        }
        if (substituted) {
            value |= 0x20;
        }
        if (notTopical) {
            value |= 0x40;
        }
        if (eventInvalid) {
            value |= 0x80;
        }
    }

    IeSingleProtectionEventNetty(ByteBuf is) throws IOException {
        value = (is.readByte() & 0xff);
    }

    @Override
    public int encode(ByteBuf buffer, int i) {
//        buffer.writerIndex(i);
        buffer.writeByte((byte) value);
        return 1;
    }

    public EventState getEventState() {
        switch (value & 0x03) {
        case 1:
            return EventState.OFF;
        case 2:
            return EventState.ON;
        default:
            return EventState.INDETERMINATE;
        }
    }

    public boolean isElapsedTimeInvalid() {
        return (value & 0x08) == 0x08;
    }

    public boolean isBlocked() {
        return (value & 0x10) == 0x10;
    }

    public boolean isSubstituted() {
        return (value & 0x20) == 0x20;
    }

    public boolean isNotTopical() {
        return (value & 0x40) == 0x40;
    }

    public boolean isEventInvalid() {
        return (value & 0x80) == 0x80;
    }

    @Override
    public String toString() {
        return "Single protection event, elapsed time invalid: " + isElapsedTimeInvalid() + ", blocked: " + isBlocked()
                + ", substituted: " + isSubstituted() + ", not topical: " + isNotTopical() + ", event invalid: "
                + isEventInvalid();
    }

}