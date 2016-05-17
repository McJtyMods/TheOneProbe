package mcjty.theoneprobe.apiimpl.elements;

public class Cursor {
    private int x;
    private int y;

    public Cursor clone() {
        return new Cursor(x, y);
    }

    public Cursor(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addX(int dx) {
        x += dx;
    }

    public void addY(int dy) {
        y += dy;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
