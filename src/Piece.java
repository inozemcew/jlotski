import java.awt.*;
import java.util.Vector;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by ainozemtsev on 12.11.14.
 * Abstract base class for game pieces
 */
abstract class Piece {
    private final Vector<Cell> cells;
    private int x=0, y=0;
    private Level level;
    private Point dragPoint;

    public Piece() {
        this.level = null;
        this.cells = new Vector<>();
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public MoveRecord newMoveRecord() {
        return new MoveRecord(this.x, this.y, this);
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public boolean loadCells(final Vector<String> data, char c){
        int x,y = 0;
        StringBuffer cc = new StringBuffer().append(c);
        for(String s:data){
            if (s.contains(cc)) {
                for(x=0; x<s.length();x++){
                    if (s.charAt(x) == c) this.addCell(x, y);
                }
            }
            y++;
        }
        if (this.cells.isEmpty()) return false;
        this.normalize();
        this.cells.forEach(this::findCorners);
        return true;
    }

    abstract protected Cell newCell(int x, int y);

    void addCell(int x, int y){
        this.cells.add(newCell(x, y));
    }

    private void normalize() {
        final int x = this.cells.stream().mapToInt(Cell::getX).min().getAsInt();
        final int y = this.cells.stream().mapToInt(Cell::getY).min().getAsInt();
        this.cells.forEach(c -> c.move(-x, -y));
        this.setXY(this.x + x*Cell.CELLSIZE, this.y + y*Cell.CELLSIZE);
    }

    public void changeCellSize(int newCellSize) {
        int x = this.x * newCellSize / Cell.CELLSIZE;
        int y = this.y * newCellSize / Cell.CELLSIZE;
        setXY(x, y);
    }

    private void findCorners(Cell cell){
        for(Cell c:this.cells) {
            if (c != cell) {
                switch (c.getY() - cell.getY()) {
                    case 0 :
                        switch (c.getX() - cell.getX()) {
                            case -1 : cell.corners.setW(); break;
                            case 1  : cell.corners.setE(); break;
                        } break;
                    case -1 :
                        switch (c.getX() - cell.getX()) {
                            case -1 : cell.corners.setNW(); break;
                            case 0  : cell.corners.setN();  break;
                            case 1  : cell.corners.setNE(); break;
                        } break;
                    case 1 :
                        switch (c.getX() - cell.getX()) {
                            case -1 : cell.corners.setSW(); break;
                            case 0  : cell.corners.setS();  break;
                            case 1  : cell.corners.setSE(); break;
                        } break;

                }
            }
        }
    }

    protected boolean notAllowOverlap(Piece that){
        return that != this;
    }

    boolean isOverlapped(Piece another, int dx, int dy){
        Point offset = new Point(x + dx,y + dy);
        Point anotherOffset = new Point(another.x, another.y);
        return this.cells.stream()
                .map(cell -> another.cells.stream()
                        .anyMatch(anotherCell -> cell.isOverlapped(offset, anotherOffset, anotherCell)))
                .anyMatch(x -> x);
    }

    boolean isCoincided(Piece another, int dx, int dy){
        Point offset = new Point(x + dx,y + dy);
        Point anotherOffset = new Point(another.x, another.y);
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
        return (x % Cell.CELLSIZE == 0);
    }

    boolean isYAligned() {
        return (y %Cell.CELLSIZE == 0);
    }

    boolean canMove(int dx, int dy, Vector<Piece> pieces) {
        if (!isPieceInside(dx, dy, new Rectangle(level.getLevelSize())))
            return false;
        return pieces.stream()
                .filter(this::notAllowOverlap)
                .noneMatch(another -> this.isOverlapped(another, dx, dy));
    }

    Point snapDirection() {
        return new Point(x % Cell.CELLSIZE < Cell.CELLSIZE / 2 ? -1 : 1
                       , y % Cell.CELLSIZE < Cell.CELLSIZE / 2 ? -1 : 1);
    }

    public Point getDragPoint() {
        return new Point(dragPoint);
    }

    void setDragPoint(Point dragPoint) {
        this.dragPoint = dragPoint;
    }

    public void setDragPoint(int x, int y) {
        setDragPoint(new Point(x, y));
    }

    void moveDragPoint(int dx, int dy) {
        dragPoint.translate(dx, dy);
    }

    @SuppressWarnings("UnusedDeclaration")
    void move(Point d, Vector<Piece> pieces){
        move(d.x, d.y, pieces);
    }

    boolean move(int dx, int dy, Vector<Piece> pieces) {
        class Helper {
            final int c = Cell.CELLSIZE;
            int least(int dz, int z){
                if (z%c == 0) {
                    if (dz > 0) return min(c, dz);
                    else return max(-c, dz);
                } else {
                    int zz = Math.abs(z);
                    if (dz > 0) return min(c - (zz % c), dz);
                    else return max(-zz % c, dz);
                }
            }
        }
        Helper h=new Helper();
        boolean moved;
        do {
            moved = false;
            int d = h.least(dx, this.x);
            if ((d != 0) && canMove(d, 0, pieces)) {
                x += d;
                dx -= d;
                moveDragPoint(d, 0);
                moved = true;
            }
            d = h.least(dy, this.y);
            if ((d != 0) && canMove(0, d, pieces)) {
                y += d;
                dy -= d;
                moveDragPoint(0, d);
                moved = true;
            }
            if ((dx == 0) && (dy == 0)) break;
        } while (moved);
        return moved;
    }

    void paint(Graphics g){
        this.cells.forEach(cell -> cell.paint(this.x, this.y, g));
    }
}
