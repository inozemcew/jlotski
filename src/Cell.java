import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ainozemtsev on 12.11.14.
 */

enum Dirs {N,NE,E,SE,S,SW,W,NW}

abstract class Cell {
    public static final int CELLSIZE = 32;
    private int x,y; //block coordinates
    private Piece parent;
    private Set<Dirs> neighbours = new HashSet<Dirs>();
    protected Color color = Color.green;

    public void setNeighbours(Set<Dirs> neighbours) {
        this.neighbours = neighbours;
    }

    public Cell(Piece parent, int dx, int dy) {
        this.parent = parent;
        this.x = dx;
        this.y = dy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(int dx, int dy){
        this.x += dx;
        this.y += dy;
    }

    public Point getAbsCoord(int offsetX, int offsetY) {
        return new Point(offsetX + this.x * CELLSIZE, offsetY + this.y * CELLSIZE);
    }

    public Point getAbsCoord(Point offset) {
        return getAbsCoord(offset.x, offset.y);
    }

    public void paint(int x, int y, Graphics g){
        Point p = getAbsCoord(x, y);
        doPaint(p.x, p.y, CELLSIZE, CELLSIZE, g);
        doPaintFrame(p.x, p.y, CELLSIZE, CELLSIZE, g);
    }

    protected void doPaint(int x, int y, int w, int h, Graphics g){
        g.setColor(color);
        g.fillRect(x, y, w, h);
    }
    protected void doPaint(int x, int y, int w, int h, Graphics g,Color color){
        g.setColor(color);
        g.fillRect(x, y, w, h);
    }

    protected void doPaintFrame(int x, int y, int w, int h, Graphics g){
        class DrawT {
            void plot(int x,int y){ g.drawLine(x,y,x,y); }
            void V(int xs, int xe, int ys, int ye, int dys, int dye){V(xs, xe, ys, ye, dys, dye, true);}
            void V(int xs, int xe, int ys, int ye, int dys, int dye, boolean s){
                for (int x = xs; x <= xe ; x++) {
                    if (s || x==xs || x==xe) g.drawLine(x,ys,x,ye); else {
                        if (dys != 0) plot(x,ys);
                        if (dye != 0) plot(x,ye);
                    }
                    ys += dys;
                    ye += dye;
                }
            }
            void H(int xs, int xe, int ys, int ye, int dxs, int dxe){H( xs, xe, ys, ye, dxs, dxe, true);}
            void H(int xs, int xe, int ys, int ye, int dxs, int dxe, boolean s){
                for (int y = ys; y <= ye ; y++) {
                    if (s || y == ys || y == ye) g.drawLine(xs,y,xe,y); else {
                        if (dxs != 0) plot(xs, y);
                        if (dxe != 0) plot(xe, y);
                    }
                    xs += dxs;
                    xe += dxe;
                }
            }
        }
        g.setColor(Color.black);
        int b=2; int b2=b*2;
        int f=0,l=0, s=0,e=0, ds=0, de = 0;
        DrawT d = new DrawT();
        if (!neighbours.contains(Dirs.N)){
            f = y; l = y+b;
            if (neighbours.contains(Dirs.E)) {
                e = x+w; de = 0;
            } else {
                e = x+w; de = -1;
            }
            if (neighbours.contains(Dirs.W)) {
                s = x; ds = 0;
            } else {
                s = x; ds = 1;
            }

            d.H(s,e,f,l,ds,de,false);
        } else {
            if (neighbours.contains(Dirs.E) && !neighbours.contains(Dirs.NE))
                d.H(x+w,x+w,y,y+b,-1,0,false);
            if (neighbours.contains(Dirs.W) && !neighbours.contains(Dirs.NW))
                d.H(x,x,y,y+b,0,1,false);
        }
        if (!neighbours.contains(Dirs.S)){
            f = y+h-b; l = y+h;
            if (neighbours.contains(Dirs.E)) {
                e = x+w; de = 0;
            } else {
                e = x+w-b; de = 1;
            }
            if (neighbours.contains(Dirs.W)) {
                s = x; ds = 0;
            } else {
                s = x + b; ds = -1;
            }

            d.H(s, e, f, l, ds, de);
        } else {
            if (neighbours.contains(Dirs.E) && !neighbours.contains(Dirs.SE))
                d.H(x+w-b,x+w,y+h-b,y+h,1,0);
            if (neighbours.contains(Dirs.W) && !neighbours.contains(Dirs.SW))
                d.H(x,x+b,y+h-b,y+h,0,-1);
        }

        if (!neighbours.contains(Dirs.E)){
            f = x+w-b; l = x+w;
            if (neighbours.contains(Dirs.N)) {
                s = y; ds =0;
            } else {
                s = y + b; ds = -1;
            }
            if (neighbours.contains(Dirs.S)) {
                e = y+h; de = 0;
            } else {
                e = y+h-b; de = 1;
            }
            d.V(f,l,s,e,ds,de);
        } else {
            if (neighbours.contains(Dirs.N) && !neighbours.contains(Dirs.NE))
                d.V(x+w-b,x+w,y,y+b,0,-1);
            if (neighbours.contains(Dirs.S) && !neighbours.contains(Dirs.SE))
                d.V(x+w-b,x+w,y+h-b,y+h,1,0);
        }
        if (!neighbours.contains(Dirs.W)){
            f = x; l = x+b;
            if (neighbours.contains(Dirs.N)) {
                s = y; ds =0;
            } else {
                s = y; ds = 1;
            }
            if (neighbours.contains(Dirs.S)) {
                e = y+h; de = 0;
            } else {
                e = y+h; de = -1;
            }
            d.V(f,l,s,e,ds,de,false);
        } else {
            if (neighbours.contains(Dirs.N) && !neighbours.contains(Dirs.NW))
                d.V(x,x+b,y,y,0,1,false);
            if (neighbours.contains(Dirs.S) && !neighbours.contains(Dirs.SW))
                d.V(x,x+b,y+h,y+h,0,-1,false);
        }
    }

    public boolean isInside(int x, int y){
        if ((x >= this.x*CELLSIZE)
                && (x < (this.x+1)*CELLSIZE)
                && (y >= this.y*CELLSIZE)
                && (y < (this.y+1)*CELLSIZE)) {
            color = Color.orange;

            return true;
        }
        return false;
    }

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
