package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;

public interface Element {
    void render(Cursor cursor);

    void toBytes(ByteBuf buf);

    ElementType getType();
}
