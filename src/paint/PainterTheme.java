package paint;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Created by ainozemtsev on 23.02.15.
 */
public enum PainterTheme {

    Draw("Simple draw"),
    Image("Textured");

    final String themeName;
    private static final ArrayList<CellPainterCollection> painters = new ArrayList<>();

    PainterTheme(String themeName){
        this.themeName = themeName;
    }

    public String getThemeName() {
        return this.themeName;
    }


    public static void registerPainterCollection(CellPainterCollection collection) {
        if (!PainterTheme.painters.contains(collection))
            PainterTheme.painters.add(collection);
    }

    public static void forEachPainterCollections(Consumer<CellPainterCollection> action){
        painters.forEach(action);
    }
}
