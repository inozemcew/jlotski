package paint;

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

    /** Corner types for correspondent quadrant */
    CornerType nw = CornerType.Both;
    CornerType ne = CornerType.Both;
    CornerType sw = CornerType.Both;
    CornerType se = CornerType.Both;

    // Flags for correct detection of inner corner
    private boolean nwf = false, nef = false, swf = false, sef = false;

    /** setXX methods. Call them for indicate presence of neighbour cell
     * and correspondent corner type will be corrected */
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
