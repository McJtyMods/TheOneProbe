package mcjty.theoneprobe.apiimpl.elements;

import mcjty.theoneprobe.rendering.RenderHelper;

public class ElementProgress implements Element {

    private final int current;
    private final int max;

    public ElementProgress(int current, int max) {
        this.current = current;
        this.max = max;
    }

    @Override
    public void render(Cursor cursor) {
        int x = cursor.getX();
        int y = cursor.getY();
        RenderHelper.drawThickBeveledBox(x, y, x + 100, y + 10, 1, 0xffffffff, 0xffffffff, 0xff000000);
        RenderHelper.drawThickBeveledBox(x+31, y+1, x + 100 - 1, y + 10 - 1, 1, 0xffff0000, 0xffff0000, 0xffff0000);
        cursor.addX(100);
        cursor.updateMaxY(10);
    }
}
