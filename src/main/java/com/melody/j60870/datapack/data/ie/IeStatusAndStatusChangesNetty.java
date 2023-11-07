package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a status and status change detection (SCD) information element.
 */
public class IeStatusAndStatusChangesNetty extends InformationNettyElement {

    private final int value;

    /**
     * Creates a SCD (status and status change detection) information element.
     * 
     * @param value
     *            the bits of value represent the status and status changed bits. Bit1 (the least significant bit) of
     *            value represents the first status changed detection bit. Bit17 of value represents the first status
     *            bit.
     */
    public IeStatusAndStatusChangesNetty(int value) {

        this.value = value;
    }

    IeStatusAndStatusChangesNetty(ByteBuf is) throws IOException {
        value = is.readInt();
    }

    @Override
    public int encode(ByteBuf buffer, int i) {
//        buffer.writerIndex(i);
        buffer.writeIntLE(value);
        return 4;
    }

    public int getValue() {
        return value;
    }

    /**
     * Returns true if the status at the given position is ON(1) and false otherwise.
     * 
     * @param position
     *            the position in the status bitstring. Range: 1-16. Status 1 is bit 17 and status 16 is bit 32 of the
     *            value returned by <code>getValue()</code>.
     * @return true if the status at the given position is ON(1) and false otherwise.
     */
    public boolean getStatus(int position) {
        if (position < 1 || position > 16) {
            throw new IllegalArgumentException("Position out of bound. Should be between 1 and 16.");
        }
        return (((value >> (position - 17)) & 0x01) == 0x01);
    }

    /**
     * Returns true if the status at the given position has changed and false otherwise.
     * 
     * @param position
     *            the position in the status changed bitstring. Range: 1-16. Status changed 1 is bit 1 and status 16 is
     *            bit 16 of the value returned by <code>getValue()</code>.
     * @return true if the status at the given position has changed and false otherwise.
     */
    public boolean hasStatusChanged(int position) {
        if (position < 1 || position > 16) {
            throw new IllegalArgumentException("Position out of bound. Should be between 1 and 16.");
        }
        return (((value >> (position - 1)) & 0x01) == 0x01);
    }

    @Override
    public String toString() {
        StringBuilder sb1 = new StringBuilder();
        sb1.append(Integer.toHexString(value >>> 16));
        while (sb1.length() < 4) {
            sb1.insert(0, '0'); // pad with leading zero if needed
        }

        StringBuilder sb2 = new StringBuilder();
        sb2.append(Integer.toHexString(value & 0xffff));
        while (sb2.length() < 4) {
            sb2.insert(0, '0'); // pad with leading zero if needed
        }

        return "Status and status changes (first bit = LSB), states: " + sb1.toString() + ", state changes: "
                + sb2.toString();
    }

}