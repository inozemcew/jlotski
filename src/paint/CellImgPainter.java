package paint;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

    public CellImgPainter(String fileName) {
        try {
            image = ImageIO.read(getClass().getResource(fileName));
            sx = image.getWidth() / 6;
            sy = image.getHeight() /6;
        } catch (IOException e) {
            System.err.printf("No image file %s found", fileName);
            System.exit(1);
        }
    }

    private void drawCorner(int x, int y, CornerType type, int [][] array) {
        int px=0;
        int py=0;
        switch (type) {
            case Both:          { px = array[0][0]; py = array[0][1]; } break;
            case Horizontal:    { px = array[1][0]; py = array[1][1]; } break;
            case Vertical:      { px = array[2][0]; py = array[2][1]; } break;
            case None:          { px = array[3][0]; py = array[3][1]; } break;
            case Inner:         { px = array[4][0]; py = array[4][1]; } break;
        }
        g.drawImage(image, x, y, x+w, y+h, px*sx, py*sy, (px+1)*sx-1,(py+1)*sy-1,null);
    }

    @Override
    protected void drawFG() {

    }

    @Override
    protected void drawBG() {

    }

    @Override
    protected void drawNW(CornerType type) {
        int array[][] = {{0, 0}, {2, 0}, {0, 2}, {4, 4}, {3, 3}};
        drawCorner(x, y, type, array);
    }

    @Override
    protected void drawNE(CornerType type) {
        int array[][] = {{5, 0}, {1, 0}, {5, 4}, {1, 2}, {2, 3}};
        drawCorner(x + w, y, type, array);
    }

    @Override
    protected void drawSW(CornerType type) {
        int array[][] = {{0, 5}, {2, 5}, {0, 1}, {1, 4}, {3, 2}};
        drawCorner(x, y + h, type, array);
    }

    @Override
    protected void drawSE(CornerType type) {
        int array[][] = {{5, 5}, {1, 5}, {5, 1}, {1, 1}, {2, 2}};
        drawCorner(x + w, y + h, type, array);
    }
}
