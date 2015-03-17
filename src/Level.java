import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    private final Stack<MoveRecord> recordStack = new Stack<>();
    //private MoveRecord moveRecord = null;

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
            //moveRecord = draggingFigure.newMoveRecord();
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
        Set<Piece> unalignedPieces = this.pieces.stream().filter(x -> !x.isAligned()).collect(Collectors.toSet());
        if (unalignedPieces.isEmpty()) {
            this.updateRecord();
            draggingFigure = null;
            return true;
        }
        if (draggingFigure != null && !draggingFigure.isAligned()) {
            unalignedPieces.removeAll(draggingFigure.snap(dx, dy, this.pieces));
        }
        while (!unalignedPieces.isEmpty()) {
            Piece p = unalignedPieces.stream().findFirst().get();
            unalignedPieces.removeAll(p.snap(0, 0, this.pieces));
        }
    return false;
    }

    public boolean isGoal() {
        Piece main = null,target = null;
        for (Piece p : this.pieces) {
            if (p instanceof MainFigure) main = p;
            if (p instanceof Target) target = p;
            if ((main != null)&& (target != null))
                return main.isCoincided(target, 0, 0);
        }
        return false;
    }

    public void updateRecord() {
        if (draggingFigure == null) return;
        List<MoveRecord> moves = this.pieces.stream()
                .map(Piece::collectMoveRecord)
                .filter(p -> p != null)
                .collect(Collectors.toList());
        if (!moves.isEmpty()) {
            if (!recordStack.empty() && moves.size() == 1
                    && recordStack.peek().equals(moves.get(0).piece.getNewMoveRecord())) {
                recordStack.pop();
            } else {
                moves.get(0).setDragged();
                moves.forEach(recordStack::push);
            }
        }
    }

    public int getMovesCount() {
        return recordStack.size();
    }

    public boolean undo() {
        while (!recordStack.empty()) {
            MoveRecord move = recordStack.pop();
            move.piece.setXY(move.x, move.y);
            if (move.dragged)
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

