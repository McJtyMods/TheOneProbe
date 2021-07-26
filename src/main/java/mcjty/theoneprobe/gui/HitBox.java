package mcjty.theoneprobe.gui;

record HitBox(int x1, int y1, int x2, int y2, Runnable runnable) {

    public boolean isHit(int xx, int yy) {
        return xx >= x1 && xx < x2 && yy >= y1 && yy < y2;
    }

    public void call() {
        runnable.run();
    }
}
