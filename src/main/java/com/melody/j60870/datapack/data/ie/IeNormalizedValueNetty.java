package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a normalized value (NVA) information element.
 */
public class IeNormalizedValueNetty extends InformationNettyElement {

    final int value;

    /**
     * Normalized value is a value in the range from -1 to (1-1/(2^15)). The normalized value is encoded as a 16 bit
     * integer ranging from -32768 to 32767. In order to get the normalized value the integer value is divided by 32768.
     * Use this constructor to initialize the value exactly using the integer value in the range from -32768 to 32767.
     * 
     * @param value
     *            non-normalized value in the range -32768 to 32767
     */
    public IeNormalizedValueNetty(int value) {
        if (value < -32768 || value > 32767) {
            throw new IllegalArgumentException("Value has to be in the range -32768..32767");
        }
        this.value = value;
    }

    /**
     * Normalized value is a value in the range from -1 to (1-1/(2^15)). Use this constructor to initialize the value
     * using a double value ranging from -1 to (1-1/(2^15)).
     * 
     * @param value
     *            normalized value in the range -1 to (1-1/(2^15))
     */
    public IeNormalizedValueNetty(double value) {
        this.value = (int) (value * 32768.0);
        if (this.value < -32768 || this.value > 32767) {
            throw new IllegalArgumentException(
                    "The value multiplied by 32768 has to be an integer in the range -32768..32767, but it is: "
                            + this.value);
        }
    }

    IeNormalizedValueNetty(ByteBuf is) throws IOException {
        value = (is.readByte() & 0xff) | (is.readByte() << 8);
    }

    @Override
    public int encode(ByteBuf buffer, int i) {
//        buffer.writerIndex(i);
        buffer.writeShortLE(value);
        return 2;
    }

    /**
     * Get the value as a normalized double value ranging from -1 to (1-1/(2^15))
     * 
     * @return the value as a normalized double.
     */
    public double getNormalizedValue() {
        return ((double) value) / 32768;
    }

    /**
     * Get the value as a non-normalized integer value ranging from -32768..32767. In order to get the normalized value
     * the returned integer value has to be devided by 32768. The normalized value can also be retrieved using
     * {@link #getNormalizedValue()}
     * 
     * @return the value as a non-normalized integer value
     */
    public int getUnnormalizedValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Normalized value: " + ((double) value / 32768);
    }
}