package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;

public class ElementOffset implements Element {

    private final int dx;
    private final int dy;

    public ElementOffset(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public ElementOffset(ByteBuf buf) {
        dx = buf.readInt();
        dy = buf.readInt();
    }

    @Override
    public void render(Cursor cursor) {
        cursor.setX(cursor.getX() + dx);
        cursor.setY(cursor.getY() + dy);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dx);
        buf.writeInt(dy);
    }

    @Override
    public ElementType getType() {
        return ElementType.OFFSET;
    }
}
