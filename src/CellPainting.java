import java.awt.*;

/**
 * Created by ainozemtsev on 18.11.14.
 */

public interface CellPainting {
    static enum Corner {None, Horizontal, Vertical, Both, Inner }
    void setContext(Graphics g, int x, int y, int l);
    void drawBG();
    void drawFG();
    void drawNW(Corner type);
    void drawNE(Corner type);
    void drawSW(Corner type);
    void drawSE(Corner type);
}
