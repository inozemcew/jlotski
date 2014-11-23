import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * <p>CellImgPainter a descendant of AbstractCellPainter paints cell of piece using an external image.</p>
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
    private int x1,x2,x3; //todo: implement usage of different size images

    public CellImgPainter(String fileName) {
        try {
            image = ImageIO.read(getClass().getResource(fileName));
        } catch (IOException e) {
            System.err.printf("No image file %s found", fileName);
            System.exit(1);
        }
    }

    @Override
    protected void drawFG() {

    }

    @Override
    protected void drawBG() {

    }

    @Override
    protected void drawNW(Corners.Type type) {
        Point p = new Point();
        switch (type) {
            case Both: p.setLocation(0,0); break;
            case Horizontal: p.setLocation(32, 0); break;
            case Vertical: p.setLocation(0, 32); break;
            case None: p.setLocation(64, 64); break;
            case Inner: p.setLocation(48, 48); break;
        }
        g.drawImage(image,x,y,x+w,y+h,p.x,p.y,p.x+w,p.y+h,null);
    }

    @Override
    protected void drawNE(Corners.Type type) {
        Point p = new Point();
        switch (type) {
            case Both: p.setLocation(80, 0); break;
            case Horizontal: p.setLocation(16, 0); break;
            case Vertical: p.setLocation(80, 32); break;
            case None: p.setLocation(16, 32); break;
            case Inner: p.setLocation(32,48); break;
        }
        g.drawImage(image,x+w,y,xw,y+h,p.x,p.y,p.x+w,p.y+h,null);
    }

    @Override
    protected void drawSW(Corners.Type type) {
        Point p = new Point();
        switch (type) {
            case Both: p.setLocation(0, 80); break;
            case Horizontal: p.setLocation(32, 80); break;
            case Vertical: p.setLocation(0, 16); break;
            case None: p.setLocation(16, 64); break;
            case Inner: p.setLocation(48, 32); break;
        }
        g.drawImage(image,x,y+h,x+w,yh,p.x,p.y,p.x+w,p.y+h,null);
    }

    @Override
    protected void drawSE(Corners.Type type) {
        Point p = new Point();
        switch (type) {
            case Both: p.setLocation(80,80); break;
            case Horizontal: p.setLocation(16,80); break;
            case Vertical: p.setLocation(80,16); break;
            case None: p.setLocation(16,16); break;
            case Inner: p.setLocation(32,32); break;
        }
        g.drawImage(image,x+w,y+h,xw,yh,p.x,p.y,p.x+w,p.y+h,null);
    }
}
