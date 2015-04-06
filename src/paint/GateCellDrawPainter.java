package paint;

import java.awt.*;

/**
 * Created by ainozemtsev on 27.03.15.
 */
public class GateCellDrawPainter extends AbstractCellPainter {
    @Override
    protected void drawFG() {

    }

    @Override
    protected void drawBG() {
        g.setColor(Color.blue);
        int w = this.w / 3;
        int h = this.h / 3;
        g.draw3DRect(x+w, y+h, w, h, true);
        g.draw3DRect(x+w + 1, y+h + 1, w - 2, h - 2, false);
    }

    @Override
    protected void drawNW(CornerType type) {
        g.drawLine(x, y+h2, x+w2, y );
        if (type != CornerType.Both)
            g.drawLine(x + w2, y + h2, x, y);
        else
            g.drawLine(x + w2, y + h2, x + w2 / 2, y + h2 / 2);
    }

    @Override
    protected void drawNE(CornerType type) {
        g.drawLine(xw, y+h2, x+w2, y );
        if (type != CornerType.Both)
            g.drawLine(x + w2, y + h2, xw, y);
        else
            g.drawLine(x+w2,y+h2, x+3*w2/2, y+h2/2);
    }

    @Override
    protected void drawSW(CornerType type) {
        g.drawLine(x, y+h2, x+w2, yh );
        if (type != CornerType.Both)
            g.drawLine(x + w2, y + h2, x, yh);
        else
            g.drawLine(x + w2, y + h2, x + w2 / 2, y + 3*h2 / 2);
    }

    @Override
    protected void drawSE(CornerType type) {
        g.drawLine(xw, y+h2, x+w2, yh );
        if (type != CornerType.Both)
            g.drawLine(x + w2, y + h2, xw, yh);
        else
            g.drawLine(x+w2,y+h2, x+3*w2/2, y+3*h2/2);
    }
}
