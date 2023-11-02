package com.melody.j60870.datapack.data.ie;

import io.netty.buffer.ByteBuf;

public abstract class InformationNettyElement {

    public abstract int encode(ByteBuf buffer, int i);

    @Override
    public abstract String toString();
}