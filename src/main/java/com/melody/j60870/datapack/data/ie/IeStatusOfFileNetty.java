package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a status of file (SOF) information element.
 */
public class IeStatusOfFileNetty extends InformationNettyElement {

    private final int status;
    private final Set<Flag> flags;

    public enum Flag {
        LAST_FILE_OF_DIRECTORY(0x20),
        NAME_DEFINES_DIRECTORY(0x40),
        TRANSFER_IS_ACTIVE(0x80);

        private int mask;

        private Flag(int mask) {
            this.mask = mask;
        }

        private static Set<Flag> flagsFor(int b) {
            HashSet<Flag> res = new HashSet<>();
            for (Flag v : values()) {
                if ((v.mask & b) != v.mask) {
                    continue;
                }
                res.add(v);
            }
            return res;
        }

    }

    public IeStatusOfFileNetty(int status, Flag... flags) {
        this(status, new HashSet<>(Arrays.asList(flags)));
    }

    public IeStatusOfFileNetty(int status, Set<Flag> flags) {
        this.status = status;
        this.flags = flags;
    }

    static IeStatusOfFileNetty decode(ByteBuf is) throws IOException {
        int b1 = is.readUnsignedByte();
        int status = b1 & 0x1f;

        Set<Flag> flags = Flag.flagsFor(b1);

        return new IeStatusOfFileNetty(status, flags);
    }

    @Override
    public int encode(ByteBuf buffer, int i) {
//        buffer.writerIndex(i);
        byte temp = (byte) status;
        for (Flag f : flags) {
            temp |= (byte) f.mask;
        }
        buffer.writeByte(temp);
        return 1;
    }

    public int getStatus() {
        return status;
    }

    public Set<Flag> getFlags() {
        return flags;
    }

    @Override
    public String toString() {
        return "Status of file: " + status + ", last file of directory: " + flags.contains(Flag.LAST_FILE_OF_DIRECTORY)
                + ", name defines directory: " + flags.contains(Flag.NAME_DEFINES_DIRECTORY) + ", transfer is active: "
                + flags.contains(Flag.TRANSFER_IS_ACTIVE);
    }
}
