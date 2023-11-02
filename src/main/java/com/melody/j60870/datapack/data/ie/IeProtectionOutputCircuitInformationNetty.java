package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents an output circuit information of protection equipment (OCI) information element.
 */
public class IeProtectionOutputCircuitInformationNetty extends InformationNettyElement {

    private int value;

    public IeProtectionOutputCircuitInformationNetty(boolean generalCommand, boolean commandToL1, boolean commandToL2,
                                                     boolean commandToL3) {

        value = 0;

        if (generalCommand) {
            value |= 0x01;
        }
        if (commandToL1) {
            value |= 0x02;
        }
        if (commandToL2) {
            value |= 0x04;
        }
        if (commandToL3) {
            value |= 0x08;
        }

    }

    IeProtectionOutputCircuitInformationNetty(ByteBuf is) throws IOException {
        value = (is.readByte() & 0xff);
    }

    @Override
    public int encode(ByteBuf buffer, int i) {
        buffer.writeByte(value);
        return 1;
    }

    public boolean isGeneralCommand() {
        return (value & 0x01) == 0x01;
    }

    public boolean isCommandToL1() {
        return (value & 0x02) == 0x02;
    }

    public boolean isCommandToL2() {
        return (value & 0x04) == 0x04;
    }

    public boolean isCommandToL3() {
        return (value & 0x08) == 0x08;
    }

    @Override
    public String toString() {
        return "Protection output circuit information, general command: " + isGeneralCommand() + ", command to L1: "
                + isCommandToL1() + ", command to L2: " + isCommandToL2() + ", command to L3: " + isCommandToL3();
    }

}