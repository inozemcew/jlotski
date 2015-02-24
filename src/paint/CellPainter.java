package paint;

import java.awt.*;

/**
 * Created by ainozemtsev on 24.02.15.
 */
public interface CellPainter {
    void setContext(Graphics g, int x, int y, int w, int h);
    void drawAll(Corners corners);
}
