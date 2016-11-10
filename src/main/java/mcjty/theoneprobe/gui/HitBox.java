package mcjty.theoneprobe.gui;

class HitBox {
    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;
    private final Runnable runnable;

    public HitBox(int x1, int y1, int x2, int y2, Runnable runnable) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.runnable = runnable;
    }

    public boolean isHit(int xx, int yy) {
        return xx >= x1 && xx < x2 && yy >= y1 && yy < y2;
    }

    public void call() {
        runnable.run();
    }
}
