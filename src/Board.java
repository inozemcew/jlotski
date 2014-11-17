
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by ainozemtsev on 11.11.14.
 */
public class Board extends JComponent implements MouseInputListener, ActionListener {
    private Vector<Level> levels = new Vector<>();
    private Level currentLevel = null;
    private final String[] levelsFileName = {"/home/aleksey/Projects/java/klotski/out/production/klotski/boards.kts","boards.kts","d:\\Projects\\klotski.py\\src\\boards.kts"};
    private Point oldDragPos;
    private Point oldDirection;
    private Timer timer = new Timer(10,this);

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
        if (timer.isRunning())
            return;
        oldDragPos = e.getPoint();
        if (currentLevel.startDrag(e.getX(),e.getY())) {
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int dx = e.getX() - oldDragPos.x;
        int dy = e.getY() - oldDragPos.y;
        if (currentLevel.doDrag(dx, dy)) {
            oldDragPos = e.getPoint();
            repaint();
        }
        oldDirection = new Point(dx,dy);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int dx = e.getX() - oldDragPos.x;
        int dy = e.getY() - oldDragPos.y;
        dx = dx==0?oldDirection.x:dx;
        dy = dy==0?oldDirection.y:dy;
        if (currentLevel.snap(dx,dy)) {
            currentLevel.endDrag();
            oldDragPos = e.getPoint();
            repaint();
        } else {
            oldDragPos = new Point(dx,dy);
            timer.start();
        }
    }

    @Override // onTimer method
    public void actionPerformed(ActionEvent actionEvent) {
        if (currentLevel.snap(oldDragPos.x,oldDragPos.y)) {
            currentLevel.endDrag();
            timer.stop();
        }
        repaint();
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


