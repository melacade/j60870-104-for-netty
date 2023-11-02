package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a select and call qualifier (SCQ) information element.
 */
public class IeSelectAndCallQualifierNetty extends InformationNettyElement {

    private final int action;
    private final int notice;

    public IeSelectAndCallQualifierNetty(int action, int notice) {
        this.action = action;
        this.notice = notice;
    }

    static IeSelectAndCallQualifierNetty decode(ByteBuf is) throws IOException {
        int b1 = is.readUnsignedByte();

        int action = b1 & 0x0f;
        int notice = (b1 >> 4) & 0x0f;
        return new IeSelectAndCallQualifierNetty(action, notice);
    }
    
    @Override
    public int encode(ByteBuf buffer, int i) {
        buffer.writeByte((byte) (action | (notice << 4)));
        return 1;
    }

    public int getRequest() {
        return action;
    }

    public int getFreeze() {
        return notice;
    }

    @Override
    public String toString() {
        return "Select and call qualifier, action: " + action + ", notice: " + notice;
    }
}
