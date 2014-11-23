import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Vector;

/**
 * Board component displays game field and handles motion events
 * Created by ainozemtsev on 11.11.14.
 */
public class Board extends JComponent implements MouseInputListener, ActionListener {
    private final Vector<Level> levels = new Vector<>();
    Level currentLevel = null;
    private final String[] levelsFileName = {"/home/aleksey/Projects/java/klotski/out/production/klotski/boards.kts","boards.kts","d:\\Projects\\klotski.py\\src\\boards.kts"};
    private Point oldDragPos;
    private Point oldDirection;
    private final Timer timer = new Timer(10,this);

    public Board() {
        addMouseListener(this);
        addMouseMotionListener(this);
        this.setPreferredSize(new Dimension(300,400));
        loadLevels();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (timer.isRunning()) return;
        oldDragPos = e.getPoint();
        if (currentLevel.startDrag(e.getX(),e.getY())) {
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (timer.isRunning()) return;
        int dx = e.getX() - oldDragPos.x;
        int dy = e.getY() - oldDragPos.y;
        Point p = currentLevel.doDrag(dx, dy);
        if (p != null) {
            oldDragPos = p;
            repaint();
        }
        oldDirection = new Point(dx,dy);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (timer.isRunning()) return;
        int dx = e.getX() - oldDragPos.x;
        int dy = e.getY() - oldDragPos.y;
        dx = dx == 0 ? oldDirection.x : dx;
        dy = dy == 0 ? oldDirection.y : dy;
        if (currentLevel.snap(dx,dy)) {
            currentLevel.endDrag();
            checkLevelComplete();
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
            checkLevelComplete();
            timer.stop();
        }
        repaint();
    }

    void checkLevelComplete(){
        if (currentLevel.isGoal())
            JOptionPane.showMessageDialog(this,"Level complete!");
    }

    @Override
    public void mouseClicked(MouseEvent e) {    }

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

    public Vector<String> getLevelNames() {
        Vector<String> names = new Vector<>();
        for(Level level:levels) {
            names.add(level.getName());
        }
        return names;
    }

    public void setLevel(int index){
        if (index >= 0 && index <= getLevelsCount()) {
            this.currentLevel = levels.elementAt(index).getCopy();
            Dimension p = this.currentLevel.getSize();
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
                JOptionPane.showMessageDialog(null, "No levels file found: " + f);
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


