import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by ainozemtsev on 12.11.14.
 * Abstract base class for game pieces
 */
abstract class Piece {
    private final ArrayList<Cell> cells;
    private int x=0, y=0;
    private Level level;
    private Point dragPoint = new Point();
    private MoveRecord moveRecord = null;

    public Piece() {
        this.level = null;
        this.cells = new ArrayList<>();
    }

    public Point getXY() {
        return new Point(this.x, this.y);
    }

    public Point getXY(int dx, int dy) {
        return new Point(this.x + dx, this.y + dy);
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void newMoveRecord() {
        if (this.moveRecord == null)
            this.moveRecord = getNewMoveRecord();
    }

    public MoveRecord collectMoveRecord() {
        MoveRecord result = this.moveRecord;
        this.moveRecord = null;
        if (result == null || result.equals(getNewMoveRecord()))
            return null;
        else
            return result;
    }

    public final MoveRecord getNewMoveRecord() {
        return new MoveRecord(this.x, this.y, this);
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    abstract protected Cell newCell(int x, int y);

    public boolean loadCells(List<StringBuilder> data, char c){
        int x,y = 0;
        boolean loading = false;
        String cc = Character.toString(c);
        for(StringBuilder s:data){
            if (s.indexOf(cc)>=0) {
                loading = true;
                for(x=0; x<s.length();x++){
                    if (s.charAt(x) == c) {
                        this.cells.add(newCell(x, y));
                        s.setCharAt(x,' ');
                    }
                }
            } else if (loading) break;
            y++;
        }
        if (this.cells.isEmpty()) return false;
        this.normalize();
        this.cells.forEach(this::findCorners);
        return true;
    }

    private void normalize() {
        final int x = this.cells.stream().mapToInt(Cell::getX).min().getAsInt();
        final int y = this.cells.stream().mapToInt(Cell::getY).min().getAsInt();
        this.cells.forEach(c -> c.move(-x, -y));
        this.setXY(this.x + x * Cell.CELLSIZE, this.y + y * Cell.CELLSIZE);
    }

    public void changeCellSize(int newCellSize) {
        int x = this.x * newCellSize / Cell.CELLSIZE;
        int y = this.y * newCellSize / Cell.CELLSIZE;
        setXY(x, y);
    }

    private void findCorners(Cell cell){
        this.cells.stream()
                .filter(c -> c != cell)
                .forEach(c -> cell.corners.setIfNear(c.getX() - cell.getX(), c.getY() - cell.getY()) );
    }

    protected boolean cannotOverlap(Piece that){
        return that != this;
    }

    protected boolean canPush(Piece that) {
        return false;
    }

    boolean isOverlapped(Piece another, int dx, int dy){
        Point offset = this.getXY(dx, dy);
        Point anotherOffset = another.getXY();
        return this.cells.stream()
                .map(cell -> another.cells.stream()
                        .anyMatch(anotherCell -> cell.isOverlapped(offset, anotherOffset, anotherCell)))
                .anyMatch(x -> x);
    }

    boolean isCoincided(Piece another, int dx, int dy){
        Point offset = this.getXY(dx, dy);
        Point anotherOffset = another.getXY();
        return this.cells.stream()
                .map(cell -> another.cells.stream()
                        .anyMatch(anotherCell -> cell.isOverlapped(offset, anotherOffset, anotherCell)))
                .allMatch(x -> x);
    }

    boolean isInsidePiece(int x, int y){
        return this.cells.stream()
                .anyMatch(cell -> cell.isInsideCell(x - this.x, y - this.y));
    }

    boolean isPieceInside(int dx, int dy, Rectangle rectangle) {
        return this.cells.stream()
                .allMatch(c -> c.isCellInside(this.x + dx, this.y + dy, rectangle));
    }

    boolean isAligned() {
        return isXAligned() && isYAligned();
    }

    boolean isXAligned() {
        return (this.x % Cell.CELLSIZE == 0);
    }

    boolean isYAligned() {
        return (this.y %Cell.CELLSIZE == 0);
    }

    /**
     * Checks collisions with other pieces and returns collection of pieces that should be moved also
     * or empty list if such move is impossible
     * @param dx - shift distance along x coordinate for movement
     * @param dy - shift distance along x coordinate for movement
     * @param pieces - collection of pieces that will be checked for possible collision
     * @return - collection of pieces that would be pushed by this piece.
     *           It contains at least this piece if move is possible.
     *
     */
    Collection<Piece> findPushedPieces(int dx, int dy, Collection<Piece> pieces) {
        Collection<Piece> result = new HashSet<>();
        if (!isPieceInside(dx, dy, new Rectangle(this.level.getLevelSize())))
            return result;
        List<Piece> ps = new ArrayList<>(pieces);
        ps.remove(this);
        result.add(this);
        for (Piece another : pieces) {
            if (!result.contains(another)
                    && this.isOverlapped(another, dx, dy)
                    && this.cannotOverlap(another)) {
                if (this.canPush(another)) {
                    Collection<Piece> rest = another.findPushedPieces(dx, dy, ps);
                    if (!rest.isEmpty()) {
                        ps.removeAll(rest);
                        result.addAll(rest);
                        continue;
                    }
                }
                result.clear();
                return result;
            }
        }
        return result;
    }

    @SuppressWarnings("UnusedDeclaration")
    boolean canMove(int dx, int dy, Collection<Piece> pieces) {
        return !findPushedPieces(dx, dy, pieces).isEmpty();
    }

    public Point getDragPoint() {
        return new Point(this.dragPoint);
    }

    void setDragPoint(Point dragPoint) {
        this.dragPoint = dragPoint;
    }

    public void setDragPoint(int x, int y) {
        setDragPoint(new Point(x, y));
    }

    void moveDragPoint(int dx, int dy) {
        this.dragPoint.translate(dx, dy);
    }

    @SuppressWarnings("UnusedDeclaration")
    void move(Point d, Collection<Piece> pieces){
        move(d.x, d.y, pieces);
    }

    Collection<Piece> move(int dx, int dy, Collection<Piece> pieces) {

        class Helper {
            final int c = Cell.CELLSIZE;
            int least(int dz, int z){
                if (z% this.c == 0) {
                    if (dz > 0) return min(this.c, dz);
                    else return max(-this.c, dz);
                } else {
                    int zz = Math.abs(z);
                    if (dz > 0) return min(this.c - (zz % this.c), dz);
                    else return max(-zz % this.c, dz);
                }
            }
        }

        Helper h=new Helper();
        boolean moved;
        Set<Piece> pieceSet = new HashSet<>();
        Collection<Piece> ps;
        do {
            //System.err.println(x + ", " + y + ", " + dx + ", " + dy);
            moved = false;
            int xd = h.least(dx, this.x);
            if (xd != 0) {
                ps = this.findPushedPieces(xd, 0, pieces);
                if (!ps.isEmpty()) {
                    ps.forEach(p -> {
                        p.newMoveRecord();
                        p.setXY(p.x+xd, p.y);
                    });
                    dx -= xd;
                    moveDragPoint(xd, 0);
                    moved = true;
                    pieceSet.addAll(ps);
                }
            }
            int yd = h.least(dy, this.y);
            if (yd != 0) {
                ps = findPushedPieces(0, yd, pieces);
                if (!ps.isEmpty()) {
                    ps.forEach(p -> {
                        p.newMoveRecord();
                        p.setXY(p.x, p.y+yd);
                    });
                    dy -= yd;
                    moveDragPoint(0, yd);
                    moved = true;
                    pieceSet.addAll(ps);
                }
            }
            if ((dx == 0) && (dy == 0)) break;
        } while (moved);
        return pieceSet;
    }

    Point snapDirection(int dx, int dy) {
        int sx = 0, sy = 0;
        int c = Cell.CELLSIZE / 2;

        if (!this.isXAligned()) {
            if (dx != 0)
                sx = (dx > 0) ? 1 : -1;
            else
                sx = this.x % Cell.CELLSIZE < c ? -1 : 1;
        }

        if (!this.isYAligned()) {
            if (dy != 0)
                sy = (dy > 0) ? 1 : -1;
            else
                sy = this.y % Cell.CELLSIZE < c ? -1 : 1;
        }

        return new Point(sx, sy);
    }

    Collection<Piece> snap(int dx, int dy, Collection<Piece> pieces) {
        Point p = this.snapDirection(dx, dy);
        return this.move(p.x, p.y, pieces);
    }

    void paint(Graphics g){
        this.cells.forEach(cell -> cell.paint(this.x, this.y, g));
    }
}
