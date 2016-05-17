package mcjty.theoneprobe.apiimpl.elements;

public class ElementOffset implements Element {

    private final int dx;
    private final int dy;

    public ElementOffset(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void render(Cursor cursor) {
        cursor.setX(cursor.getX() + dx);
        cursor.setY(cursor.getY() + dy);
    }
}
