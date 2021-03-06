import paint.CellDrawPainter;
import paint.CellPainter;
import paint.Corners;

import java.awt.*;

/**
 * Created by ainozemtsev on 12.11.14.
 * Cell class as element of game pieces
 */



abstract class Cell {
    private static final CellPainter cellPainter = new CellDrawPainter();
    public static int CELLSIZE = 32;

    private int x,y;            //block coordinates
    private final Piece parent;
    final protected Corners corners = new Corners();


    public Cell(Piece parent, int dx, int dy) {
        this.parent = parent;
        this.x = dx;
        this.y = dy;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    private Point getAbsCoord(int offsetX, int offsetY) {
        return new Point(offsetX + this.x * CELLSIZE, offsetY + this.y * CELLSIZE);
    }

    private Point getAbsCoord(Point offset) {
        return getAbsCoord(offset.x, offset.y);
    }

    public void move(int dx, int dy){
        this.x += dx;
        this.y += dy;
    }

    protected CellPainter getCellPainter(){
        return Cell.cellPainter;
    }

    public void paint(int x, int y, Graphics g){
        Point p = getAbsCoord(x, y);
        doPaint(p.x, p.y, CELLSIZE, CELLSIZE, g);
    }

    protected void doPaint(int x, int y, int w, int h, Graphics g){
        getCellPainter().setContext(g, x, y, w, h);
        getCellPainter().drawAll(this.corners);
    }

    public boolean isInsideCell(int x, int y) {
        return ((x >= this.x*CELLSIZE)
                && (x < (this.x+1)*CELLSIZE)
                && (y >= this.y*CELLSIZE)
                && (y < (this.y+1)*CELLSIZE));
    }

    public boolean isCellInside(int offsetX, int offsetY, Rectangle rectangle) {
        return rectangle.contains(getAbsCoord(offsetX, offsetY)) &&
                rectangle.contains(getAbsCoord(offsetX + CELLSIZE - 1, offsetY + CELLSIZE - 1));
    }

    /**
     * Checks if this cell is overlapped by another one
     * @param offset offset of this cell within board
     * @param anotherOffset offset of another cell within board
     * @param another reference of another cell
     * @return true if cells are overlapped
     */
    public boolean isOverlapped(Point offset, Point anotherOffset, Cell another) {
        Point coord = getAbsCoord(offset);
        Point anotherCoord = another.getAbsCoord(anotherOffset);
        if ((coord.x >= anotherCoord.x + CELLSIZE) || (coord.x <= anotherCoord.x - CELLSIZE))
            return false;
        if ((coord.y >= anotherCoord.y + CELLSIZE) || (coord.y <= anotherCoord.y - CELLSIZE))
            return false;
        return true;
    }

}
