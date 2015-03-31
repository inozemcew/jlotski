package paint;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

/**
 * <p>paint.CellImgPainter a descendant of paint.AbstractCellPainter paints cell of piece using an external image.</p>
 * <p>It uses an image divided by 6 parts each direction as such</p>
 * <p style="font:mono">
 *     122221<br>
 *     344446<br>
 *     345546<br>
 *     345546<br>
 *     344446<br>
 *     177771</p>
 * <p>Where
 * <br>"1" - means corner image
 * <br>"2" - upper edge
 * <br>"3" - left edge
 * <br>"4" - inner image
 * <br>"5" - inner corner
 * <br>"6" - right edge
 * <br>"7" - lower edge
 *
 * Created by aleksey on 20.11.14.
 */


public class CellImgPainter extends AbstractCellPainter {
    private BufferedImage image = null;
    private int sx,sy;
    private HashMap<CornerType,BufferedImage> nwImages, neImages, swImages, seImages;

    static int[][] nw = {{0, 0}, {4, 2}, {2, 4}, {2, 0}, {2, 2}};
    static int[][] ne = {{1, 0}, {1, 2}, {3, 4}, {3, 0}, {3, 2}};
    static int[][] sw = {{0, 1}, {4, 3}, {2, 1}, {2, 5}, {2, 3}};
    static int[][] se = {{1, 1}, {1, 3}, {3, 1}, {3, 5}, {3, 3}};

    public CellImgPainter(String fileName) {
        try {
            this.image = ImageIO.read(getClass().getResource(fileName));
            this.sx = this.image.getWidth() / 6;
            this.sy = this.image.getHeight() /6;
        } catch (IOException e) {
            System.err.printf("No image file %s found", fileName);
            System.exit(1);
        }
        int l = CornerType.values().length;
        this.nwImages = new HashMap<>(l);
        this.neImages = new HashMap<>(l);
        this.swImages = new HashMap<>(l);
        this.seImages = new HashMap<>(l);
        for (CornerType i : CornerType.values()){
            this.nwImages.put(i, cutFromImage(i, nw));
            this.neImages.put(i, cutFromImage(i, ne));
            this.swImages.put(i, cutFromImage(i, sw));
            this.seImages.put(i, cutFromImage(i, se));
        }
    }

    private void drawCorner(int x, int y, int w, int h, CornerType type, HashMap<CornerType,BufferedImage> img) {
        this.g.drawImage(img.get(type), x, y, w, h, null);
    }

    private BufferedImage cutFromImage(CornerType type, int [][] array) {
        int px=array[type.ordinal()][0];
        int py=array[type.ordinal()][1];
        return this.image.getSubimage(
                px * this.sx, py * this.sy, this.sx, this.sy);
    }

    @Override
    protected void drawFG() {

    }

    @Override
    protected void drawBG() {

    }

    @Override
    protected void drawNW(CornerType type) {
        drawCorner(this.x, this.y, this.w1, this.h1, type, this.nwImages);
    }

    @Override
    protected void drawNE(CornerType type) {
        drawCorner(this.x + this.w1, this.y, this.w2, this.h1, type, this.neImages);
    }

    @Override
    protected void drawSW(CornerType type) {
        drawCorner(this.x, this.y + this.h1, this.w1, this.h2, type, this.swImages);
    }

    @Override
    protected void drawSE(CornerType type) {
        drawCorner(this.x + this.w1, this.y + this.h1, this.w2, this.h2, type, this.seImages);
    }
}
