package mcjty.theoneprobe.apiimpl.elements;

public class ElementNewline implements Element {

    @Override
    public void render(Cursor cursor) {
        cursor.setX(cursor.getLeftmargin());
        cursor.setY(cursor.getMaxy());
    }
}
