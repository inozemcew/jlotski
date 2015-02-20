package paint;

import java.awt.*;

public class CellDrawPainter extends AbstractCellPainter {
    private int b;
    private Color bgColor;
    private Color frameColor = Color.green;

    public CellDrawPainter() {
        this(2);
    }

    public CellDrawPainter(int b) {
        this.b = b;
    }

    protected void drawFG(){

    }

    protected void drawBG(){
        g.fillRect(x, y, xw-x, yh-y);
    }

    protected void drawNW(CornerType type) {
        switch (type) {
            case Horizontal:
                H(x, x + w, y, y + b, 0, 0, false);
                break;
            case Vertical:
                V(x, x + b, y, y + h, 0, 0, false);
                break;
            case Both:
                H(x, x + w, y, y + b, 1, 0, false);
                V(x, x + b, y, y + h, 1, 0, false);
                break;
            case Inner:
                H(x, x, y, y + b, 0, 1, false);
                V(x, x + b, y, y, 0, 1, false);
                break;
            case None:
        }
    }

    protected void drawNE(CornerType type) {
        switch (type) {
            case Horizontal:
                H(xw-w, xw, y, y+b, 0, 0,false);
                break;
            case Vertical:
                V(xw-b, xw, y, y+h, 0, 0);
                break;
            case Both:
                H(xw-w, xw, y, y+b, 0, -1,false);
                V(xw-b, xw, y+b , y+h, -1, 0);
                break;
            case Inner:
                V(xw-b, xw, y, y+b, 0, -1);
                H(xw, xw, y, y+b, -1, 0, false);
                break;
            case None:
        }
    }

    protected void drawSW(CornerType type) {
        switch (type) {
            case Horizontal:
                H(x, x + w, yh - b, yh, 0, 0);
                break;
            case Vertical:
                V(x, x + b, yh-h, yh, 0, 0, false);
                break;
            case Both:
                H(x+b, x + w, yh - b, yh, -1, 0);
                V(x, x + b, yh-h, yh, 0, -1, false);
                break;
            case Inner:
                H(x, x + b, yh - b, yh, 0, -1);
                V(x, x + b, yh, yh, -1, 0, false);
                break;
            case None:
        }
    }

    protected void drawSE(CornerType type) {
        switch (type) {
            case Horizontal:
                H(xw-w, xw, yh - b, yh, 0, 0);
                break;
            case Vertical:
                V(xw-b, xw, yh - h, yh, 0, 0);
                break;
            case Both:
                H(xw-h, xw-b, yh - b, yh, 0, 1);
                V(xw-b, xw, yh-h, yh-b, 0, 1);
                break;
            case Inner:
                H(xw-b, xw, yh - b, yh, 1, 0);
                V(xw-b, xw, yh-b, yh, 1, 0);
                break;
            case None:
        }
    }
    private void plot(int x,int y){ g.drawLine(x,y,x,y); }
    private void V(int xs, int xe, int ys, int ye, int dys, int dye) { V(xs, xe, ys, ye, dys, dye, true); }
    private void V(int xs, int xe, int ys, int ye, int dys, int dye, boolean s){
        for (int x = xs; x <= xe ; x++) {
            if (s || x==xs || x==xe) g.drawLine(x,ys,x,ye); else {
                if (dys != 0) plot(x,ys);
                if (dye != 0) plot(x,ye);
            }
            ys += dys;
            ye += dye;
        }
    }
    private void H(int xs, int xe, int ys, int ye, int dxs, int dxe){H( xs, xe, ys, ye, dxs, dxe, true);}
    private void H(int xs, int xe, int ys, int ye, int dxs, int dxe, boolean s){
        for (int y = ys; y <= ye ; y++) {
            if (s || y == ys || y == ye) g.drawLine(xs,y,xe,y); else {
                if (dxs != 0) plot(xs, y);
                if (dxe != 0) plot(xe, y);
            }
            xs += dxs;
            xe += dxe;
        }
    }
}
