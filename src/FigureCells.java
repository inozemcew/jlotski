import paint.*;

import java.awt.*;

class FigureCell extends Cell{
    static final CellPainterCollection painterCollection = new CellPainterCollection();
    static {
        painterCollection.addPainter(PainterTheme.Draw,new CellDrawPainter());
        painterCollection.addPainter(PainterTheme.Image, new CellImgPainter("/img/green2.png"));
        PainterTheme.registerPainterCollection(painterCollection);
    }

    @Override
    protected CellPainter getCellPainter() {
        return FigureCell.painterCollection;
    }

    public FigureCell(Piece parent, int dx, int dy) {
        super(parent, dx, dy);
    }
}

class MainFigureCell extends FigureCell {
    static final CellPainterCollection painterCollection = new CellPainterCollection();
    static {
        painterCollection.addPainter(PainterTheme.Draw,new CellDrawPainter(Color.red));
        painterCollection.addPainter(PainterTheme.Image, new CellImgPainter("/img/red2.png"));
        PainterTheme.registerPainterCollection(painterCollection);
    }

    @Override
    public CellPainter getCellPainter(){
        return MainFigureCell.painterCollection;
    }

    public MainFigureCell(Piece parent, int dx, int dy) {
        super(parent, dx, dy);
    }

    @Override
    protected void doPaint(int x, int y, int w, int h, Graphics g) {
        super.doPaint(x, y, w, h, g);
        g.setColor(new Color(255,128,128));
        g.draw3DRect(x + w / 3, y + h / 3, w / 3, h / 3, false);
    }
}


class WallCell extends Cell{
    static final CellPainterCollection painterCollection = new CellPainterCollection();
    static {
        painterCollection.addPainter(PainterTheme.Draw, new CellDrawPainter(Color.cyan));
        painterCollection.addPainter(PainterTheme.Image, new CellImgPainter("/img/blue2.png"));
        PainterTheme.registerPainterCollection(painterCollection);
    }

    @Override
    protected CellPainter getCellPainter() {
        return WallCell.painterCollection;
    }

    public WallCell(Piece parent, int dx, int dy) {
        super(parent, dx, dy);
    }

    @Override
    protected void doPaint(int x, int y, int w, int h, Graphics g) {
        super.doPaint(x, y, w, h, g);
    }
}


class GateCell extends Cell{
    static final CellPainterCollection painterCollection = new CellPainterCollection();
    static {
        painterCollection.addPainter(PainterTheme.Draw, new GateCellDrawPainter());
        painterCollection.addPainter(PainterTheme.Image, new CellImgPainter("/img/dark2.png"));
        PainterTheme.registerPainterCollection(painterCollection);
    }

    @Override
    protected CellPainter getCellPainter() {
        return GateCell.painterCollection;
    }

    public GateCell(Piece parent, int dx, int dy) {
        super(parent, dx, dy);
    }

//    @Override
//    protected void doPaint(int x, int y, int w2, int h2, Graphics g) {
//        g.setColor(Color.blue);
//        g.draw3DRect(x + w2 / 3, y + h2 / 3, w2 / 3, h2 / 3, true);
//        g.draw3DRect(x + w2/3+1, y + h2/3+1, w2/3-2, h2/3-2 , false);
//    }
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

