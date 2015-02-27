package paint;

import java.awt.*;
import java.util.HashMap;


/**
 * Created by AInozemtsev on 20.02.15.
 */
public class CellPainterCollection implements CellPainter {
    private final HashMap<PainterTheme, AbstractCellPainter> painters = new HashMap<>();
    private AbstractCellPainter currentPainter = null;

    public void addPainter(PainterTheme theme, AbstractCellPainter painter) {
        painters.putIfAbsent(theme, painter);
        if (currentPainter == null)
            currentPainter = painter;
    }

    public int getPaintersCount() {
        return painters.size();
    }

    public PainterTheme[] getPaintersNames() {
        return painters.keySet().toArray(new PainterTheme[0]);
    }

    public void selectPainter(PainterTheme theme) {
        if (painters.containsKey(theme))
            currentPainter = painters.get(theme);
    }

    public AbstractCellPainter getCurrentPainter() {
        return currentPainter;
    }

    @Override
    public void setContext(Graphics g, int x, int y, int w, int h) {
        currentPainter.setContext(g, x, y, w, h);
    }

    @Override
    public void drawAll(Corners corners) {
        currentPainter.drawAll(corners);
    }
}
