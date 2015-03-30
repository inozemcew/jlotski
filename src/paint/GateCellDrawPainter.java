package paint;

import java.awt.*;

/**
 * Created by ainozemtsev on 27.03.15.
 */
public class GateCellDrawPainter extends AbstractCellPainter{
    @Override
    protected void drawFG() {

    }

    @Override
    protected void drawBG() {
        g.setColor(Color.blue);
        int w = this.w2 *2/3;
        int h = this.h2 *2/3;
        g.draw3DRect(x+w ,  y+h,   w , h , true);
        g.draw3DRect(x+w+1, y+h+1, w-2, h-2 , false);
    }

    @Override
    protected void drawNW(CornerType type) {

    }

    @Override
    protected void drawNE(CornerType type) {

    }

    @Override
    protected void drawSW(CornerType type) {

    }

    @Override
    protected void drawSE(CornerType type) {

    }
}
