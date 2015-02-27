package paint;

import java.awt.*;

/**
 * Created by ainozemtsev on 20.11.14.
 */

public abstract class AbstractCellPainter implements CellPainter {
    protected Graphics g;
    protected int x, y, w, h, xw, yh;

    @Override
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
    abstract protected void drawNW(CornerType type);
    abstract protected void drawNE(CornerType type);
    abstract protected void drawSW(CornerType type);
    abstract protected void drawSE(CornerType type);

    @Override
    public void drawAll(Corners corners) {
        drawBG();
        drawNW(corners.nw);
        drawNE(corners.ne);
        drawSW(corners.sw);
        drawSE(corners.se);
        drawFG();
    }

}
