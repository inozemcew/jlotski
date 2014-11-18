import java.awt.*;
import java.util.HashSet;
import java.util.Vector;

/**
 * Created by ainozemtsev on 12.11.14.
 * Abstract base class for game pieces
 */
abstract class Piece {
    private int x=0, y=0;
    private Vector<Cell> cells = new Vector<>();
    private Level level;

    public Piece() {
        this.level = null;
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
        for (Cell cell:cells){
            cell.setNeighbours(findNeighbours(cell));
        }
        return f;
    }

    abstract protected Cell newCell(int x, int y);

    public void addCell(int x, int y){
        Cell cell = newCell(x, y);
        cells.add(cell);
    }

    private HashSet<Dirs> findNeighbours(Cell cell){
        HashSet<Dirs> d = new HashSet<>();
        for(Cell c:cells) {
            if (c != cell) {
                if (c.getY() == cell.getY()) {
                    if (c.getX() == cell.getX() - 1) d.add(Dirs.W);
                    if (c.getX() == cell.getX() + 1) d.add(Dirs.E);
                }
                if (c.getY() == cell.getY() - 1) {
                    if (c.getX() == cell.getX() - 1) d.add(Dirs.NW);
                    if (c.getX() == cell.getX()) d.add(Dirs.N);
                    if (c.getX() == cell.getX() + 1) d.add(Dirs.NE);
                }
                if (c.getY() == cell.getY() + 1) {
                    if (c.getX() == cell.getX() - 1) d.add(Dirs.SW);
                    if (c.getX() == cell.getX()) d.add(Dirs.S);
                    if (c.getX() == cell.getX() + 1) d.add(Dirs.SE);
                }
            }
        }
        return d;
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

    boolean canMove(int dx, int dy, Vector<Piece> pieces) {
        if (!isPieceInside(dx, dy, new Rectangle(level.getSize())))
            return false;
        for(Piece piece:pieces){
            if (allowOverlap(piece)) continue;
            if (isOverlapped(piece, dx, dy)) return false;
        }
        return true;
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

    Point snapDirection() {
        return new Point(x%Cell.CELLSIZE< Cell.CELLSIZE/2?-1:1,y%Cell.CELLSIZE< Cell.CELLSIZE/2?-1:1);
    }

    void move(Point d, Vector<Piece> pieces){
        move(d.x, d.y, pieces);
    }

    void move(int dx, int dy, Vector<Piece> pieces){
        if (canMove(dx, 0, pieces))
            x += dx;
        if (canMove(0,dy, pieces))
            y += dy;
    }

    boolean isInsidePiece(int x, int y){
        for (Cell cell:cells){
            if (cell.isInsideCell(x - this.x, y - this.y)) return true;
        }
        return false;
    }

    boolean isPieceInside(int dx, int dy, Rectangle rectangle) {
        boolean b = cells.stream().allMatch(c -> c.isCellInside(this.x + dx, this.y + dy, rectangle));
        return b;
    }

    void paint(Graphics g){
        for (Cell i: cells){
            i.paint(x, y, g);
        }
    }
}
