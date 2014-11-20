import java.awt.*;

/**
 * Created by ainozemtsev on 20.11.14.
 */
public abstract class AbstractCellPainter {
    protected Graphics g;
    protected int x, y, w, h, xw, yh;

    public void setContext(Graphics g, int x, int y, int w, int h) {
        this.g = g;
        this.x = x;
        this.y = y;
        this.xw = x+w;
        this.yh = y+h;
        this.w = (w+1)/2;
        this.h = (h+1)/2;
    }

    abstract protected void drawFG();
    abstract protected void drawBG();
    abstract protected void drawNW(Corners.Type type);
    abstract protected void drawNE(Corners.Type type);
    abstract protected void drawSW(Corners.Type type);
    abstract protected void drawSE(Corners.Type type);

}
