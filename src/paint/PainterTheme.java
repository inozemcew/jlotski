package paint;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Created by ainozemtsev on 23.02.15.
 */
public enum PainterTheme {

    Draw("Simple draw"),
    Image("Textured");

    String themeName;

    PainterTheme(String themeName){
        this.themeName = themeName;
    }

    public String getThemeName() {
        return themeName;
    }

    private static ArrayList<CellPainterCollection> painters = new ArrayList<>();

    public static void registerPainterCollection(CellPainterCollection collection) {
        if (!PainterTheme.painters.contains(collection))
            PainterTheme.painters.add(collection);
    }

    public static void forEachPainterCollections(Consumer<CellPainterCollection> action){
        painters.forEach(action);
    }
}
