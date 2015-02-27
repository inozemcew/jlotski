import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Stack;
import java.util.Vector;
import java.util.function.Consumer;

/**
 * Representation of a game level
 * Created by ainozemtsev on 13.11.14.
 */

public class Level {
    private String name = "";
    private Vector<String> data = new Vector<>();
    private final Vector<Piece> pieces = new Vector<>();
    private final Dimension size = new Dimension(0,0);
    private Piece draggingFigure = null;

    private final Stack<MoveRecord> moves = new Stack<>();
    private MoveRecord moveRecord = null;

    public Level() {
    }

    public Level(String name, Vector<String> data) {
        this.name = name;
        this.data = data;
    }

    public void forAllPieces(Consumer<Piece> pieceConsumer) {
        this.pieces.forEach(pieceConsumer::accept);
    }

    public final String getName() {
        return this.name;
    }

    public boolean startDrag(int x, int y) {
        Optional<Piece> f = pieces.stream()
                .filter(s -> s instanceof Figure)
                .filter(s -> s.isInsidePiece(x, y))
                .findFirst();
        draggingFigure = f.orElse(null);
        if (draggingFigure != null) {
            draggingFigure.setDragPoint(x, y);
            moveRecord = draggingFigure.newMoveRecord();
        }
        return f.isPresent();
    }

    public Point doDrag(int dx, int dy) {
        if (draggingFigure == null)
            return null;
        draggingFigure.move(dx, dy, this.pieces);
        return draggingFigure.getDragPoint();
    }

    public boolean doSnap(int dx, int dy){
        if (draggingFigure == null || draggingFigure.isAligned()) {
            this.endDrag();
            return true;
        }
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
        for (Piece p:this.pieces) {
            if (p instanceof MainFigure) main = p;
            if (p instanceof Target) target = p;
            if ((main != null)&& (target != null))
                return main.isCoincided(target, 0, 0);
        }
        return false;
    }

    public void endDrag() {
        if (draggingFigure == null) return;
        MoveRecord move = draggingFigure.newMoveRecord();
        if (!moveRecord.equals(move)) {
            if (!moves.empty() && moves.peek().equals(move))
                moves.pop();
            else
                moves.push(moveRecord);
        }
        draggingFigure = null;
    }

    public int getMovesCount() {
        return moves.size();
    }

    public boolean undo() {
        if (!moves.empty()) {
            MoveRecord move = moves.pop();
            move.piece.setXY(move.x, move.y);
            return true;
        }
        return false;
    }

    public Level getCopy() {
        Level newLevel = new Level(name, data);
        newLevel.setSize(size);
        newLevel.createPieces();
        return newLevel;
    }

    public Dimension getLevelSize() {
        return new Dimension(size.width * Cell.CELLSIZE, size.height * Cell.CELLSIZE);
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
        size.setSize(x,y);
        createPieces();
        return true;
    }

    public void paint(Graphics g) {
        this.pieces.forEach(p -> p.paint(g));
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

    private void createFigures(char f, char t, PieceCreator<? extends Piece> F) {
        for (char c = f; c <= t; c++) {
            Piece fig = F.create();
            fig.setLevel(this);
            if (fig.loadCells(data, c)) pieces.add(fig);
        }
    }
}

