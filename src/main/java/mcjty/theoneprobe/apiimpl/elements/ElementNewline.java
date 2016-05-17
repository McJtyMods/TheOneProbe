package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;

public class ElementNewline implements Element {

    @Override
    public void render(Cursor cursor) {
        cursor.setX(cursor.getLeftmargin());
        cursor.setY(cursor.getMaxy());
    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }
}
