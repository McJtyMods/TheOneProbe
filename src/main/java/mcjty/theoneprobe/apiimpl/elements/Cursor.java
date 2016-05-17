package mcjty.theoneprobe.apiimpl.elements;

public class Cursor {
    private int leftmargin;
    private int x;
    private int y;
    private int maxy;

    public Cursor(int leftmargin, int x, int y, int maxy) {
        this.leftmargin = leftmargin;
        this.x = x;
        this.y = y;
        this.maxy = maxy;
    }

    public void addX(int dx) {
        x += dx;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void updateMaxY(int height) {
        if (y + height > maxy) {
            maxy = y + height;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getMaxy() {
        return maxy;
    }

    public int getLeftmargin() {
        return leftmargin;
    }
}
