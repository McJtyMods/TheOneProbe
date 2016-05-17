package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;

public interface Element {
    void render(Cursor cursor);

    int getWidth();

    int getHeight();

    void toBytes(ByteBuf buf);

    ElementType getType();
}
