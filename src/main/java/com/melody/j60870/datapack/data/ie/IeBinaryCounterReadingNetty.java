package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Represents a binary counter reading (BCR) information element.
 */
public class IeBinaryCounterReadingNetty extends InformationNettyElement {

    private final int counterReading;
    private final int sequenceNumber;

    private final Set<Flag> flags;

    public enum Flag {
        CARRY(0x20),
        COUNTER_ADJUSTED(0x40),
        INVALID(0x80);

        private int mask;

        private Flag(int mask) {
            this.mask = mask;
        }

        private static Set<Flag> flagsFor(byte b) {
            EnumSet<Flag> s = EnumSet.allOf(Flag.class);

            Iterator<Flag> iter = s.iterator();

            while (iter.hasNext()) {
                int mask2 = iter.next().mask;
                if ((mask2 & b) != mask2) {
                    iter.remove();
                }
            }

            return s;
        }

    }

    public IeBinaryCounterReadingNetty(int counterReading, int sequenceNumber, Set<Flag> flags) {
        this.counterReading = counterReading;
        this.sequenceNumber = sequenceNumber;
        this.flags = flags;
    }

    public IeBinaryCounterReadingNetty(int counterReading, int sequenceNumber) {
        this(counterReading, sequenceNumber, EnumSet.noneOf(Flag.class));
    }

    public IeBinaryCounterReadingNetty(int counterReading, int sequenceNumber, Flag firstFlag, Flag... flag) {
        this(counterReading, sequenceNumber, EnumSet.of(firstFlag, flag));
    }

    static IeBinaryCounterReadingNetty decode(ByteBuf is) throws IOException {

        int counterReading = is.readInt();

        byte b0 = is.readByte();

        int sequenceNumber = b0 & 0x1f;

        Set<Flag> flags = Flag.flagsFor(b0);
        return new IeBinaryCounterReadingNetty(counterReading, sequenceNumber, flags);

    }

    @Override
    public int encode(ByteBuf buffer, int i) {
        buffer.writerIndex(i);
        buffer.writeIntLE(seq());
        return 4;

    }

    private byte seq() {
        byte v = (byte) sequenceNumber;
        for (Flag flag : flags) {
            v |= flag.mask;
        }

        return v;
    }

    public int getCounterReading() {
        return counterReading;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public Set<Flag> getFlags() {
        return flags;
    }

    @Override
    public String toString() {
        return "Binary counter reading: " + counterReading + ", seq num: " + sequenceNumber + ", flags: " + flags;
    }
}
