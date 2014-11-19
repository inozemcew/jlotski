import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Vector;

/**
 * Created by ainozemtsev on 13.11.14.
 */

public class Level {
    private Vector<String> data = new Vector<>();
    private String name = "";
    private Vector<Piece> pieces = new Vector<>();
    private Dimension size = new Dimension(0,0);
    private Piece draggingFigure = null;

    public Level() {
    }

    public Level(String name, Vector<String> data) {
        this.name = name;
        this.data = data;
    }

    final String getName() {
        return this.name;
    }

    public boolean startDrag(int x, int y) {
        Optional<Piece> f = pieces.stream()
                .filter(s -> s instanceof Figure)
                .filter(s -> s.isInsidePiece(x, y))
                .findFirst();
        if (f.isPresent()) {
            draggingFigure = f.get();
            return true;
        }
        return false;
    }

    public boolean doDrag(int dx, int dy) {
        if (draggingFigure == null) return false;
        draggingFigure.move(dx, dy, this.pieces);
        return true;
    }

    public boolean snap(int dx, int dy){
        if (draggingFigure == null || draggingFigure.isAligned())
            return true;
        Point point = draggingFigure.snapDirection();
        int sx = dx>0? 1:dx<0?-1:point.x;
        if (draggingFigure.isXAligned())
            sx = 0;
        int sy = dy>0? 1:dy<0?-1:point.y;
        if (draggingFigure.isYAligned())
            sy = 0;
        draggingFigure.move(sx, sy, this.pieces);
        return false;
    }

    public void endDrag() {
        draggingFigure = null;
    }

    public Level getCopy() {
        Level newLevel = new Level(name, data);
        newLevel.setSize(size);
        newLevel.createPieces();
        return newLevel;
    }

    public Dimension getSize() {
        return new Dimension(size);
    }

    public void setSize(Dimension size) {
        this.size.setSize(size);
    }

    public boolean loadLevel(BufferedReader r) {
        String s;
        int x=0;
        int y=0;
        try {
            do {
                s = r.readLine();
                if (s == null) throw new IOException();
            } while (!s.startsWith("<"));
            this.name = s.substring(1,s.length()-1);
            s = r.readLine().trim();
            while (s.startsWith("@") && s.endsWith("@")){
                this.data.add(s.substring(1,s.length()-1));
                int xx = s.length()-2;
                if (xx > x) x = xx;
                y++;
                s = r.readLine().trim();
            }
        } catch (IOException e) {
            this.data.clear();
            return false;
        }
        size.setSize(x*Cell.CELLSIZE,y*Cell.CELLSIZE);
        createPieces();
        return true;
    }

    public void paint(Graphics g) {
        for(Piece f: pieces) {
            f.paint(g);
        }
    }

    interface PieceCreator<P extends Piece>{
        public P create();
    }

    private void createPieces() {
        //done initialize walls etc
        createFigures('a','z',Figure::new);
        createFigures('A','Z',Figure::new);
        createFigures('0','9',Figure::new);
        createFigures('*','*',MainFigure::new);
        createFigures('#','#',Wall::new);
        createFigures('-','-',Gate::new);
        createFigures('.','.',Target::new);
    }

    private void createFigures(char f, char t, PieceCreator F) {
        for (char c = f; c <= t; c++) {
            Piece fig = F.create();
            fig.setLevel(this);
            if (fig.loadCells(data, c)) pieces.add(fig);
        }
    }
}

class FigureCell extends Cell{
    static { painter = new CellPainter(); }

    public FigureCell(Piece parent, int dx, int dy) {
        super(parent, dx, dy);


    }
}

class MainFigureCell extends FigureCell {
    public MainFigureCell(Piece parent, int dx, int dy) {
        super(parent, dx, dy);
        color = Color.red;
    }

    @Override
    protected void doPaint(int x, int y, int w, int h, Graphics g) {
        super.doPaint(x, y, w, h, g);
        g.setColor(new Color(255,128,128));
        g.draw3DRect(x + w / 3, y + h / 3, w / 3, h / 3, false);
    }
}


class WallCell extends Cell{
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


