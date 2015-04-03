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
@SuppressWarnings("UnqualifiedFieldAccess")
public class Corners {

    /** Corner types for correspondent quadrant */
    CornerType nw = CornerType.Both;
    CornerType ne = CornerType.Both;
    CornerType sw = CornerType.Both;
    CornerType se = CornerType.Both;

    // Flags for correct detection of inner corner
    private boolean nwf = false, nef = false, swf = false, sef = false;

    public void setIfNear(int cx, int cy) {
        switch (cy) {
            case 0 :
                switch (cx) {
                    case -1 : setW(); break;
                    case 1  : setE(); break;
                } break;
            case -1 :
                switch (cx) {
                    case -1 : setNW(); break;
                    case 0  : setN();  break;
                    case 1  : setNE(); break;
                } break;
            case 1 :
                switch (cx) {
                    case -1 : setSW(); break;
                    case 0  : setS();  break;
                    case 1  : setSE(); break;
                } break;
        }
    }


    /** setXX methods. Call them for indicate presence of neighbour cell
     * and correspondent corner type will be corrected */
    private void setN() {
        nw = nw.setNS(nwf);
        ne = ne.setNS(nef);
    }

    private void setS() {
        sw = sw.setNS(swf);
        se = se.setNS(sef);
    }

    private void setE() {
        ne = ne.setEW(nef);
        se = se.setEW(sef);
    }

    private void setW() {
        nw = nw.setEW(nwf);
        sw = sw.setEW(swf);
    }

    private void setNW() {
        nwf = true;
        nw = nw.setC();
    }

    private void setNE() {
        nef = true;
        ne = ne.setC();
    }

    private void setSW() {
        swf = true;
        sw = sw.setC();
    }

    private void setSE() {
        sef = true;
        se = se.setC();
    }

}
