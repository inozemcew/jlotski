import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Vector;

/**
 * Created by ainozemtsev on 13.11.14.
 */

public class Level implements Cloneable {
    private Vector<String> data = new Vector<String>();
    private String name = "";
    private Vector<Piece> pieces = new Vector<Piece>();
    private Point size = new Point(0,0);
    private Piece draggingFigure = null;

    public Level() {
    }

    public Level(String name, Vector<String> data) {
        this.name = name;
        this.data = data;
    }

    public boolean startDrag(int x, int y) {
        Optional<Piece> f = pieces.stream()
                .filter(s -> s instanceof Figure)
                .filter(s -> s.isInside(x,y))
                .findFirst();
        if (f.isPresent()) {
            draggingFigure = f.get();
            return true;
        }
        return false;
    }

    public boolean doDrag(int dx, int dy) {
        if (draggingFigure == null) return false;
        draggingFigure.move(dx, dy, pieces);
        return true;
    }

    public boolean endDrag(int dx, int dy) {
        if (!doDrag(dx,dy)) return  false;

        draggingFigure = null;
        return true;
    }

    @Override
    public Level clone() throws CloneNotSupportedException {
        Level clone = (Level)super.clone();
        clone.draggingFigure = null;
        clone.pieces = (Vector<Piece>)pieces.clone();
        return clone;
    } //todo: check correct cloneabi1lity

    public Level getCopy() {
        Level newLevel = new Level(name, data);
        newLevel.setSize(size);
        newLevel.createPieces();
        return newLevel;
    }

    public Point getSize() {
        return new Point(size);
    }

    public void setSize(Point point) {
        size.setLocation(point);
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
        size.setLocation(x*Cell.CELLSIZE,y*Cell.CELLSIZE);
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
            if (fig.loadCells(data, c)) pieces.add(fig);
        }
    }
}

class FigureCell extends Cell{
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
    protected void doPaintFrame(int x, int y, int w, int h, Graphics g) {
        //super.doPaintFrame(x, y, w, h, g);
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

    @Override
    protected void doPaintFrame(int x, int y, int w, int h, Graphics g) {
        //super.doPaintFrame(x, y, w, h, g);
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


