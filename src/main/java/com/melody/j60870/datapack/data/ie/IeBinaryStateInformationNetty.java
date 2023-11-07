package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;
import org.openmuc.j60870.internal.HexUtils;

import java.io.IOException;

/**
 * Represents a binary state information (BSI) information element.
 */
public class IeBinaryStateInformationNetty extends InformationNettyElement {
	
	private final int value;
	
	/**
	 * Creates a BSI (binary state information) information element from an integer value.
	 *
	 * @param value the bits of value represent the 32 binary states of this element. When encoded in a message, the MSB
	 *              of <code>value</code> is transmitted first and the LSB of <code>value</code> is transmitted last.
	 */
	public IeBinaryStateInformationNetty(int value) {
		this.value = value;
	}
	
	/**
	 * Creates a BSI (binary state information) information element from a byte array.
	 *
	 * @param value the bits of value represent the 32 binary states of this element. When encoded in a message, the MSB
	 *              of the first byte is transmitted first and the LSB of fourth byte is transmitted last.
	 */
	public IeBinaryStateInformationNetty(byte[] value) {
		if (value == null || value.length != 4) {
			throw new IllegalArgumentException("value needs to be of length 4");
		}
		this.value = (value[0] << 24) | ((value[1] & 0xff) << 16) | ((value[2] & 0xff) << 8) | (value[3] & 0xff);
	}
	
	IeBinaryStateInformationNetty(ByteBuf is) throws IOException {
		value = is.readInt();
	}
	
	@Override
	public int encode(ByteBuf buffer, int i) {
//		buffer.writerIndex(i);
		buffer.writeIntLE(value);
		return 4;
	}
	
	/**
	 * Returns the 32 binary states of this element as an integer. When encoded in a message, the MSB of the return
	 * value is transmitted first and the LSB of the return value is transmitted last.
	 *
	 * @return the 32 binary states of this element.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Returns the 32 binary states of this element as a byte array. When encoded in a message, the MSB of the first
	 * byte is transmitted first and the LSB of the fourth byte is transmitted last.
	 *
	 * @return the 32 binary states of this element.
	 */
	public byte[] getValueAsByteArray() {
		return new byte[]{(byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) (value)};
	}
	
	/**
	 * Returns true if the bit at the given position is 1 and false otherwise.
	 *
	 * @param position the position in the bit string. Range: 1-32. Position 1 represents the last bit in the encoded message
	 *                 and is the least significant bit (LSB) of the value returned by <code>getValue()</code>. Position 32
	 *                 represents the first bit in the encoded message and is the most significant bit (MSB) of the value
	 *                 returned by <code>getValue()</code>.
	 * @return true if the bit at the given position is 1 and false otherwise.
	 */
	public boolean getBinaryState(int position) {
		if (position < 1 || position > 32) {
			throw new IllegalArgumentException("Position out of bound. Should be between 1 and 32.");
		}
		return (((value >> (position - 1)) & 0x01) == 0x01);
	}
	
	@Override
	public String toString() {
		return "BinaryStateInformation (32 bits as hex): " + HexUtils.bytesToHex(
				new byte[]{(byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) (value)});
	}
	
}