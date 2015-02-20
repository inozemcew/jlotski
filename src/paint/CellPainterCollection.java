package paint;

import java.util.HashMap;


/**
 * Created by AInozemtsev on 20.02.15.
 */
public class CellPainterCollection {
    private static HashMap<String,AbstractCellPainter> painters = new HashMap<>();

    public void addPainter(String name,AbstractCellPainter painter) {
        painters.putIfAbsent(name, painter);
    }
    public int getPaintersCount() {
        return painters.size();
    }

    public static String[] getPaintersNames() {
        return painters.keySet().toArray(new String[0]);
    }

    public static AbstractCellPainter getPainter(String name) {
        return painters.get(name);
    }

}
