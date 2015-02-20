package paint;

/**
 * Created by AInozemtsev on 20.02.15.
 */
public enum CornerType {
    None, Horizontal, Vertical, Both, Inner;

    CornerType setNS(boolean d) {
        if (this == CornerType.Both) return CornerType.Vertical;
        if (this == CornerType.Horizontal) {
            if (d) return
                    CornerType.None;
            else
                return CornerType.Inner;
        }
        return this;
    }

    CornerType setEW(boolean d) {
        if (this == CornerType.Both) return CornerType.Horizontal;
        if (this == CornerType.Vertical) {
            if (d) return CornerType.None;
            else return CornerType.Inner;
        }
        return this;
    }

    CornerType setC() {
        if (this == Inner) return None;
        return this;
    }
}

