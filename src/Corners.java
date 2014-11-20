/**
 * <p>Helper class for correct drawing of cell corners
 * has four fields correspondents to each corner of piece cell Northwest, Northeast, etc..</p>
 *
 * <p>Inner enum 'Type' describes possible presence of edges: only Horizontal, only Vertical, Both, None
 * and a special type Inner that means the corner is inner part of L-shaped junction</p>
 *
 * @author <p> Created by ainozemtsev on 19.11.14.</p>
 */
public class Corners {
    enum Type {
        None, Horizontal, Vertical, Both, Inner;

        Type setNS(boolean d){
            if (this == Type.Both)  return Type.Vertical;
            if (this == Type.Horizontal) {
                if (d) return
                        Type.None;
                else
                    return Type.Inner;
            }
            return this;
        }

        Type setEW(boolean d){
            if (this == Type.Both)  return Type.Horizontal;
            if (this == Type.Vertical) {
                if (d) return Type.None; else return Type.Inner;
            }
            return this;
        }

        Type setC(){
            if (this == Inner) return None;
            return this;
        }
    }

    /** Corner types for correspondent quadrant */
    Type nw = Type.Both;
    Type ne = Type.Both;
    Type sw = Type.Both;
    Type se = Type.Both;

    // Flags for correct detection of inner corner
    private boolean nwf = false, nef = false, swf = false, sef = false;

    /** setXX methods  */
    public void setN() {
        nw = nw.setNS(nwf);
        ne = ne.setNS(nef);
    }

    public void setS() {
        sw = sw.setNS(swf);
        se = se.setNS(sef);
    }

    public void setE() {
        ne = ne.setEW(nef);
        se = se.setEW(sef);
    }

    public void setW() {
        nw = nw.setEW(nwf);
        sw = sw.setEW(swf);
    }

    public void setNW() {
        nwf = true;
        nw = nw.setC();
    }

    public void setNE() {
        nef = true;
        ne = ne.setC();
    }

    public void setSW() {
        swf = true;
        sw = sw.setC();
    }

    public void setSE() {
        sef = true;
        se = se.setC();
    }

}
