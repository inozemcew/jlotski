import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * Board component displays game field and handles motion events
 * Created by ainozemtsev on 11.11.14.
 */
public class Board extends JComponent implements MouseInputListener, ActionListener {
    private final Vector<Level> levels = new Vector<>();
    Level currentLevel = null;
    private int currentLevelNumber;
    private final String levelsFileName = "/boards.kts";
    private Point oldDragPos;
    private Point oldDirection;
    private final Timer timer = new Timer(5,this);
    private ActionListener moveListener = null;
    private boolean locked = true;

    public Board() {
        addMouseListener(this);
        addMouseMotionListener(this);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                componentResize();
            }
        });
        this.setPreferredSize(new Dimension(300,400));
        loadLevels();
    }

    public int getLevelsCount(){
        return this.levels.size();
    }

    public int getCurrentLevelNumber() {
        return this.currentLevelNumber;
    }

    public List<String> getLevelNames() {
        return this.levels.stream()
                .map(Level::getName)
                .collect(Collectors.toList());
    }

    public void setLevel(int index){
        if (index >= 0 && index <= getLevelsCount()) {
            this.currentLevel = this.levels.elementAt(index).getCopy();
            this.currentLevelNumber = index;
            Dimension p = this.currentLevel.getLevelSize();
            setMinimumSize(p);
            setPreferredSize(p);
            setLock(index<1);
        } else this.currentLevel = null;
    }

    public void setLock(boolean lock) {
        this.locked = lock;
    }

    @Override
    public void mouseClicked(MouseEvent e) {    }
    public void setMoveListener(ActionListener listener) {
        this.moveListener = listener;
    }

    @Override
    public void mouseExited(MouseEvent e) {    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (this.locked || this.timer.isRunning())
            return;
        this.oldDragPos = e.getPoint();
        if (this.currentLevel.startDrag(e.getX(), e.getY())) {
            repaint();
        }
    }

    @Override
    public synchronized void mouseDragged(MouseEvent e) {
        if (this.locked || this.timer.isRunning()) return;
        int dx = e.getX() - this.oldDragPos.x;
        int dy = e.getY() - this.oldDragPos.y;
        Point p = this.currentLevel.doDrag(dx, dy);
        if (p != null) {
            this.oldDragPos = p;
            repaint();
        }
        this.oldDirection = new Point(dx,dy);
    }

    @Override
    public void mouseEntered(MouseEvent e) {    }

    @Override
    public void mouseMoved(MouseEvent e) {    }

    @Override
    public synchronized void mouseReleased(MouseEvent e) {
        if (this.locked || this.timer.isRunning()) return;
        int dx = e.getX() - this.oldDragPos.x;
        int dy = e.getY() - this.oldDragPos.y;
        dx = (dx == 0) ? this.oldDirection.x : dx;
        dy = (dy == 0) ? this.oldDirection.y : dy;
        if (this.currentLevel.doSnap(dx, dy)) {
            moveListenerNotify();
            checkLevelComplete();
            this.oldDragPos = e.getPoint();
            repaint();
        } else {
            this.oldDragPos = new Point(dx,dy);
            this.timer.start();
        }
    }

    @Override // onTimer method
    public synchronized void actionPerformed(ActionEvent actionEvent) {
        if (this.currentLevel.doSnap(this.oldDragPos.x, this.oldDragPos.y)) {
            this.timer.stop();
            moveListenerNotify();
            checkLevelComplete();
        }
        repaint();
    }

    private  void moveListenerNotify() {
        if (this.moveListener != null)
            this.moveListener.actionPerformed(new ActionEvent(this,
                            ActionEvent.ACTION_LAST + 1,
                            Integer.toString(this.currentLevel.getMovesCount()))
            );
    }

    private void componentResize(){
        Dimension d = getSize();
        Dimension l = this.currentLevel.getLevelSize();
        int newCellSize = Integer.min(Cell.CELLSIZE * d.width / l.width, Cell.CELLSIZE * d.height / l.height);
        setCellSize(newCellSize);
        repaint();
    }

    public void setCellSize(int newCellSize) {
        if (newCellSize > 3 && newCellSize != Cell.CELLSIZE) {
            this.currentLevel.forAllPieces(p -> p.changeCellSize(newCellSize));
            Cell.CELLSIZE = newCellSize;
        }
    }

    public void updateBounds() {
        Dimension levelSize = this.currentLevel.getLevelSize();
        this.setPreferredSize(levelSize);
        this.setSize(levelSize);
    }

    private void checkLevelComplete() {
        if (this.currentLevel.isGoal()) {
            JOptionPane.showMessageDialog(this, KlotskiForm.langBundle.getString("level.complete"));
            setLock(true);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawRect(0,0,getWidth()-1,getHeight()-1);
        if (this.currentLevel != null) this.currentLevel.paint(g);
    }

    private void loadLevels() {
        InputStream s = getClass().getResourceAsStream(this.levelsFileName);
        if (s != null) {
            doLoadLevels(new InputStreamReader(s));
            return;
        }
        System.err.println("No levels found in jar file. Searching around...");
        String f = this.levelsFileName;
            try {
                doLoadLevels(new FileReader(f));
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "No levels file found: " + f);
            }
    }

    private void doLoadLevels(InputStreamReader f) {
        BufferedReader r = new BufferedReader(f);
        while (true) {
            Level l = new Level();
            if (!l.loadLevel(r)) break;
            this.levels.add(l);
        }
        setLevel(0);
    }
}


