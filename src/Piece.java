import java.awt.*;
import java.util.HashSet;
import java.util.Vector;

/**
 * Created by ainozemtsev on 12.11.14.
 */
abstract class Piece {
    private int x,y;
    private Vector<Cell> cells = new Vector<Cell>();

    public Piece() {
        this.x = 0;
        this.y = 0;
        this.cells = new Vector<>();
    }

    void setXY(int x, int y){
        this.x = x;
        this.y =y;
    }

    public boolean loadCells(final Vector<String> data,char c){
        boolean f = false;
        int x,y = 0;
        for(String s:data){
            if (s.contains(new StringBuffer(c))) {
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

    boolean hasSibling(int x, int y){
        for (Cell i:this.cells){
            if (i.getX() == x && i.getY() == y) return true;
        }
        return false;
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
        for(Piece piece:pieces){
            if (piece == this) continue;
            if (isOverlapped(piece, dx, dy)) return false;
        }
        return true;
    }

    boolean isAligned() {
        return (x % Cell.CELLSIZE == 0) && (y %Cell.CELLSIZE ==0);
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

    boolean isInside(int x, int y){
        for (Cell cell:cells){
            if (cell.isInside(x - this.x,y - this.y)) return true;
        }
        return false;
    }

    void paint(Graphics g){
        for (Cell i: cells){
            i.paint(x, y, g);
        }
    }
}
