package paint;

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
}
