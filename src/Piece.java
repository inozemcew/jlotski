import java.awt.*;
import java.util.Vector;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by ainozemtsev on 12.11.14.
 * Abstract base class for game pieces
 */
abstract class Piece {
    private int x=0, y=0;
    private final Vector<Cell> cells;
    private Level level;
    private Point dragPoint;

    public Piece() {
        this.level = null;
        cells = new Vector<>();
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public MoveRecord getMoveRecord() {
        return new MoveRecord(this.x, this.y, this);
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public boolean loadCells(final Vector<String> data,char c){
        boolean f = false;
        int x,y = 0;
        for(String s:data){
            if (s.contains(new StringBuffer().append(c))) {
                f = true;
                for(x=0; x<s.length();x++){
                    if (s.charAt(x) == c) this.addCell(x, y);
                }
            }
            y++;
        }
        normalize();
        cells.forEach(this::findCorners);
        return f;
    }

    abstract protected Cell newCell(int x, int y);

    void addCell(int x, int y){
        Cell cell = newCell(x, y);
        cells.add(cell);
    }

    private void normalize() {
        int x = 99999;
        int y = 99999;
        for (Cell cell:cells) {
            if (cell.getX()<x) x = cell.getX();
            if (cell.getY()<y) y = cell.getY();
        }
        for (Cell cell:cells) {
            cell.move(-x,-y);
        }
        this.x += x*Cell.CELLSIZE;
        this.y += y*Cell.CELLSIZE;
    }

    public void changeCellSize(int newCellSize) {
        int x = this.x * newCellSize / Cell.CELLSIZE;
        int y = this.y * newCellSize / Cell.CELLSIZE;
        setXY(x, y);
    }

    private void findCorners(Cell cell){
        //noinspection Convert2streamapi
        for(Cell c:cells) {
            if (c != cell) {
                if (c.getY() == cell.getY()) {
                    if (c.getX() == cell.getX() - 1) cell.corners.setW();
                    if (c.getX() == cell.getX() + 1) cell.corners.setE();
                }
                if (c.getY() == cell.getY() - 1) {
                    if (c.getX() == cell.getX() - 1) cell.corners.setNW();
                    if (c.getX() == cell.getX()) cell.corners.setN();
                    if (c.getX() == cell.getX() + 1) cell.corners.setNE();
                }
                if (c.getY() == cell.getY() + 1) {
                    if (c.getX() == cell.getX() - 1) cell.corners.setSW();
                    if (c.getX() == cell.getX()) cell.corners.setS();
                    if (c.getX() == cell.getX() + 1) cell.corners.setSE();
                }
            }
        }
    }

    protected boolean allowOverlap(Piece piece){
        return piece == this;
    }

    boolean isOverlapped(Piece another, int dx, int dy){
        Point offset = new Point(x + dx,y + dy);
        Point anotherOffset = new Point(another.x, another.y);
        for(Cell cell:cells){
            for (Cell anotherCell:another.cells){
                if (cell.isOverlapped(offset, anotherOffset, anotherCell))
                    return true;
            }
        }
        return false;
    }

    boolean isCoincided(Piece another, int dx, int dy){
        Point offset = new Point(x + dx,y + dy);
        Point anotherOffset = new Point(another.x, another.y);
        for(Cell cell:cells){
            boolean b = false;
            for (Cell anotherCell:another.cells){
                b = b || cell.isOverlapped(offset, anotherOffset, anotherCell);
            }
            if (!b) return false;
        }
        return true;
    }

    boolean isInsidePiece(int x, int y){
        for (Cell cell:cells){
            if (cell.isInsideCell(x - this.x, y - this.y)) return true;
        }
        return false;
    }

    boolean isPieceInside(int dx, int dy, Rectangle rectangle) {
        return cells
                .stream()
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
        for(Piece piece:pieces){
            if (allowOverlap(piece)) continue;
            if (isOverlapped(piece, dx, dy)) return false;
        }
        return true;
    }

    Point snapDirection() {
        return new Point(x % Cell.CELLSIZE < Cell.CELLSIZE / 2 ? -1 : 1
                       , y % Cell.CELLSIZE < Cell.CELLSIZE / 2 ? -1 : 1);
    }

    public Point getDragPoint() {
        return new Point(dragPoint);
    }

    public void setDragPoint(int x, int y) {
        setDragPoint(new Point(x, y));
    }

    public void setDragPoint(Point dragPoint) {
        this.dragPoint = dragPoint;
    }

    public void moveDragPoint(int dx, int dy) {
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
        for (Cell i: cells){
            i.paint(x, y, g);
        }
    }
}
