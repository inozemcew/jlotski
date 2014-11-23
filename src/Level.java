import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Vector;

/**
 * Representation of a game level
 * Created by ainozemtsev on 13.11.14.
 */

public class Level {
    private String name = "";
    private Vector<String> data = new Vector<>();
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
            draggingFigure.setDragPoint(x, y);
            return true;
        }
        return false;
    }

    public Point doDrag(int dx, int dy) {
        if (draggingFigure == null)
            return null;
        draggingFigure.move(dx, dy, this.pieces);
        return draggingFigure.getDragPoint();
    }

    public boolean snap(int dx, int dy){
        if (draggingFigure == null || draggingFigure.isAligned())
            return true;
        Point point = draggingFigure.snapDirection();
        int sx = dx > 0? 1: dx < 0 ? -1 : point.x;
        if (draggingFigure.isXAligned())
            sx = 0;
        int sy = dy > 0 ? 1 : dy < 0 ? -1 : point.y;
        if (draggingFigure.isYAligned())
            sy = 0;
        draggingFigure.move(sx, sy, this.pieces);
        return false;
    }

    public boolean isGoal() {
        Piece main = null,target = null;
        for (Piece p:pieces) {
            if (p instanceof MainFigure) main = p;
            if (p instanceof Target) target =p;
            if ((main != null)&& (target != null))
                return main.isCoincided(target, 0, 0);
        }
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

