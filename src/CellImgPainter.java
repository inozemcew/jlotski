import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by aleksey on 20.11.14.
 */
public class CellImgPainter extends AbstractCellPainter {
    private BufferedImage image = null;

    public CellImgPainter(String fileName) {
        try {
            image = ImageIO.read(new File(fileName));
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
        switch (type) {
            case Both: g.drawImage(image,x,y,x+w,y+h,0,0,15,15,null); break;
            case Horizontal: g.drawImage(image,x,y,x+w,y+h,32,0,47,16,null); break;
            case Vertical: g.drawImage(image,x,y,x+w,y+h,0,32,15,47,null); break;
            case None: g.drawImage(image,x,y,x+w,y+h,64,64,79,79,null); break;
            case Inner: g.drawImage(image,x,y,x+w,y+h,48,48,63,63,null); break;
        }
    }

    @Override
    protected void drawNE(Corners.Type type) {
        switch (type) {
            case Both: g.drawImage(image,x+w,y,xw,y+h,80,0,95,15,null); break;
            case Horizontal: g.drawImage(image,x+w,y,xw,y+h,16,0,31,15,null); break;
            case Vertical: g.drawImage(image,x+w,y,xw,y+h,80,32,95,47,null); break;
            case None: g.drawImage(image,x+w,y,xw,y+h,16,32,31,47,null); break;
            case Inner: g.drawImage(image,x+w,y,xw,y+h,32,48,47,63,null); break;
        }

    }

    @Override
    protected void drawSW(Corners.Type type) {
        switch (type) {
            case Both: g.drawImage(image,x,y+h,x+w,yh,0,80,15,95,null); break;
            case Horizontal: g.drawImage(image,x,y+h,x+w,yh,32,80,47,95,null); break;
            case Vertical: g.drawImage(image,x,y+h,x+w,yh,0,16,15,31,null); break;
            case None: g.drawImage(image,x,y+h,x+w,yh,32,16,47,31,null); break;
            case Inner: g.drawImage(image,x,y+h,x+w,yh,32,16,47,31,null); break;
        }
    }

    @Override
    protected void drawSE(Corners.Type type) {
        switch (type) {
            case Both: g.drawImage(image,x+w,y+h,xw,yh,80,80,95,95,null); break;
            case Horizontal: g.drawImage(image,x+w,y+h,xw,yh,16,80,31,95,null); break;
            case Vertical: g.drawImage(image,x+w,y+h,xw,yh,80,16,95,31,null); break;
            case None: g.drawImage(image,x+w,y+h,xw,yh,16,16,31,31,null); break;
            case Inner: g.drawImage(image,x+w,y+h,xw,yh,16,16,31,31,null); break;
        }
    }
}
