package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a double command (DCO) information element.
 */
public class IeDoubleCommandNetty extends IeAbstractQualifierOfCommandNetty {

    public enum DoubleCommandState {
        NOT_PERMITTED_A(0),
        OFF(1),
        ON(2),
        NOT_PERMITTED_B(3);

        private final int id;

        private static final Map<Integer, DoubleCommandState> idMap = new HashMap<>();

        static {
            for (DoubleCommandState enumInstance : DoubleCommandState.values()) {
                if (idMap.put(enumInstance.getId(), enumInstance) != null) {
                    throw new IllegalArgumentException("duplicate ID: " + enumInstance.getId());
                }
            }
        }

        private DoubleCommandState(int id) {
            this.id = id;
        }

        /**
         * Returns the ID of this DoubleCommandState.
         * 
         * @return the ID
         */
        public int getId() {
            return id;
        }

        /**
         * Returns the DoubleCommandState that corresponds to the given ID. Returns <code>null</code> if no
         * DoubleCommandState with the given ID exists.
         * 
         * @param id
         *            the ID
         * @return the DoubleCommandState that corresponds to the given ID
         */
        public static DoubleCommandState getInstance(int id) {
            return idMap.get(id);
        }

    }

    /**
     * Create the Double Command Information Element.
     * 
     * @param commandState
     *            the command state
     * @param qualifier
     *            the qualifier
     * @param select
     *            true if select, false if execute
     */
    public IeDoubleCommandNetty(DoubleCommandState commandState, int qualifier, boolean select) {
        super(qualifier, select);

        value |= commandState.getId();
    }

    IeDoubleCommandNetty(ByteBuf is) throws IOException {
        super(is);
    }

    public DoubleCommandState getCommandState() {
        return DoubleCommandState.getInstance(value & 0x03);
    }

    @Override
    public String toString() {
        return "Double Command state: " + getCommandState() + ", " + super.toString();
    }

}