import paint.CellImgPainter;
import paint.CellPainterCollection;
import paint.PainterTheme;

import java.awt.*;

/**
 * Implementation of Piece subclasses for various game objects
 * Created by ainozemtsev on 20.11.14.
 */

class FigureCell extends Cell{
    static CellPainterCollection painterCollection = new CellPainterCollection();
    static {
        painterCollection.addPainter(PainterTheme.Image, new CellImgPainter("/img/green1.png"));
    }

    public FigureCell(Piece parent, int dx, int dy) {
        super(parent, dx, dy);
    }

    @Override
    protected CellPainterCollection getPainterCollection() {
        return painterCollection;
    }
}

class MainFigureCell extends FigureCell {
    static CellPainterCollection painterCollection = new CellPainterCollection();
    static {
        painterCollection.addPainter(PainterTheme.Image, new CellImgPainter("/img/red1.png"));
    }

    public MainFigureCell(Piece parent, int dx, int dy) {
        super(parent, dx, dy);
        color = Color.red;
    }

    @Override
    protected CellPainterCollection getPainterCollection(){
        return painterCollection;
    }

    @Override
    protected void doPaint(int x, int y, int w, int h, Graphics g) {
        super.doPaint(x, y, w, h, g);
        g.setColor(new Color(255,128,128));
        g.draw3DRect(x + w / 3, y + h / 3, w / 3, h / 3, false);
    }
}


class WallCell extends Cell{
    static CellPainterCollection painterCollection = new CellPainterCollection();
    static {
        painterCollection.addPainter(PainterTheme.Image, new CellImgPainter("/img/blue1.png"));
    }

    @Override
    protected CellPainterCollection getPainterCollection() {
        return painterCollection;
    }

    public WallCell(Piece parent, int dx, int dy) {
        super(parent, dx, dy);
    }

    @Override
    protected void doPaint(int x, int y, int w, int h, Graphics g) {
        super.doPaint(x, y, w, h, g,Color.cyan);
    }
}


class GateCell extends Cell{
    public GateCell(Piece parent, int dx, int dy) {
        super(parent, dx, dy);
    }

    @Override
    protected void doPaint(int x, int y, int w, int h, Graphics g) {
        g.setColor(Color.blue);
        g.draw3DRect(x + w / 3, y + h / 3, w / 3, h / 3, true);
        g.draw3DRect(x + w/3+1, y + h/3+1, w/3-2, h/3-2 , false);
    }
}


class TargetCell extends Cell{
    public TargetCell(Piece parent, int dx, int dy) {
        super(parent, dx, dy);
    }

    @Override
    protected void doPaint(int x, int y, int w, int h, Graphics g) {
        g.setColor(Color.red);
        g.fill3DRect(x + w/3, y + h/3, w/3, h/3, true);
    }

}


class Figure extends Piece{
    @Override
    protected Cell newCell(int x, int y) {
        return new FigureCell(this,x,y);
    }

    @Override
    protected boolean allowOverlap(Piece piece) {
        return super.allowOverlap(piece) || (piece instanceof Target);
    }
}


class MainFigure extends Figure{
    @Override
    protected Cell newCell(int x, int y) {
        return new MainFigureCell(this,x,y);
    }

    @Override
    protected boolean allowOverlap(Piece piece) {
        return super.allowOverlap(piece) || (piece instanceof Gate) || (piece instanceof Target);
    }
}


class Wall extends Piece{
    @Override
    protected Cell newCell(int x, int y) {
        return new WallCell(this,x,y);
    }
}


class Gate extends Piece{
    @Override
    protected Cell newCell(int x, int y) {
        return new GateCell(this,x,y);
    }
}


class Target extends Piece{
    @Override
    protected Cell newCell(int x, int y) {
        return new TargetCell(this, x, y);
    }
}


