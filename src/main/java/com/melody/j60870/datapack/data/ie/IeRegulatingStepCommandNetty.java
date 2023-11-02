package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a regulating step command (RCO) information element.
 */
public class IeRegulatingStepCommandNetty extends IeAbstractQualifierOfCommandNetty {

    public enum StepCommandState {
        NOT_PERMITTED_A(0),
        NEXT_STEP_LOWER(1),
        NEXT_STEP_HIGHER(2),
        NOT_PERMITTED_B(3);

        private final int id;

        private static final Map<Integer, StepCommandState> idMap = new HashMap<>();

        static {
            for (StepCommandState enumInstance : StepCommandState.values()) {
                if (idMap.put(enumInstance.getId(), enumInstance) != null) {
                    throw new IllegalArgumentException("duplicate ID: " + enumInstance.getId());
                }
            }
        }

        private StepCommandState(int id) {
            this.id = id;
        }

        /**
         * Returns the ID of this StepCommandState.
         * 
         * @return the ID
         */
        public int getId() {
            return id;
        }

        /**
         * Returns the StepCommandState that corresponds to the given ID. Returns <code>null</code> if no
         * StepCommandState with the given ID exists.
         * 
         * @param id
         *            the ID
         * @return the StepCommandState that corresponds to the given ID
         */
        public static StepCommandState getInstance(int id) {
            return idMap.get(id);
        }

    }

    /**
     * Create a Regulating Step Command Information Element.
     * 
     * @param commandState
     *            the command state
     * @param qualifier
     *            the qualifier
     * @param select
     *            true if select, false if execute
     */
    public IeRegulatingStepCommandNetty(StepCommandState commandState, int qualifier, boolean select) {
        super(qualifier, select);

        value |= commandState.getId();
    }

    IeRegulatingStepCommandNetty(ByteBuf is) throws IOException {
        super(is);
    }

    public StepCommandState getCommandState() {
        return StepCommandState.getInstance(value & 0x03);
    }

    @Override
    public String toString() {
        return "Regulating step command state: " + getCommandState() + ", " + super.toString();
    }

}