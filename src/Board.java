
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by ainozemtsev on 11.11.14.
 */
public class Board extends JComponent implements MouseInputListener{
    private Vector<Level> levels = new Vector<>();
    private Level currentLevel = null;
    private final String[] levelsFileName = {"/home/aleksey/Projects/java/klotski/out/production/klotski/boards.kts","boards.kts","d:\\Projects\\klotski.py\\src\\boards.kts"};
    private Point oldDragPos;

    public Board() {
        addMouseListener(this);
        addMouseMotionListener(this);
        this.setPreferredSize(new Dimension(300,400));
        loadLevels();
    }

    @Override
    public void mouseClicked(MouseEvent e) {    }

    @Override
    public void mousePressed(MouseEvent e) {
        oldDragPos = e.getPoint();
        if (currentLevel.startDrag(e.getX(),e.getY())) {
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (currentLevel.doDrag(e.getX()-oldDragPos.x, e.getY()-oldDragPos.y)) {
            oldDragPos = e.getPoint();
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (currentLevel.endDrag(e.getX()-oldDragPos.x, e.getY()-oldDragPos.y)) {
            oldDragPos = e.getPoint();
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {    }
    @Override
    public void mouseExited(MouseEvent e) {    }
    @Override
    public void mouseMoved(MouseEvent e) {    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawRect(0,0,getWidth()-1,getHeight()-1);
        if (currentLevel != null) currentLevel.paint(g);
    }

    public int getLevelsCount(){
        return levels.size();
    }

    public void setLevel(int index){
        if (index >= 0 && index <= getLevelsCount()) {
                currentLevel = levels.elementAt(index).getCopy();
                Dimension p = new Dimension(currentLevel.getSize().x,currentLevel.getSize().y);
                setMinimumSize(p);
                setPreferredSize(p);
        } else currentLevel = null;
    }

    void loadLevels() {
        InputStream s = getClass().getResourceAsStream("/boards.kts");
        if (s != null) {
            doLoadLevels(new InputStreamReader(s));
            return;
        }
        System.err.println("No levels found in jar file. Searching around...");
        for(String f:levelsFileName) {
            try {
                doLoadLevels(new FileReader(f));
                return;
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "No levels file found: " + levelsFileName);
            }
        }
    }

    private void doLoadLevels(InputStreamReader f) {
        BufferedReader r = new BufferedReader(f);
        while (true) {
            Level l = new Level();
            if (!l.loadLevel(r)) break;
            levels.add(l);
        }
        setLevel(0);
    }
}


