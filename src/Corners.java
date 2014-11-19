/**
 * Created by ainozemtsev on 19.11.14.
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
    Type nw = Type.Both;
    Type ne = Type.Both;
    Type sw = Type.Both;
    Type se = Type.Both;
    private boolean nwf = false, nef = false, swf = false, sef = false;

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
