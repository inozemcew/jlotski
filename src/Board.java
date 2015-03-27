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
public class Board extends JComponent {
    private final Vector<Level> levels = new Vector<>();
    Level currentLevel = null;
    private int currentLevelNumber;
    private final String levelsFileName = "/boards.kts";
    private Listener listener = new Listener();
    private boolean locked = true;


    public Board() {
        addMouseListener(this.listener);
        addMouseMotionListener(this.listener);
        addComponentListener(this.listener);
        setPreferredSize(new Dimension(300, 400));
        loadLevels();
    }

    public int getLevelsCount(){
        return this.levels.size();
    }

    public int getCurrentLevelNumber() {
        return this.currentLevelNumber;
    }

    public List<String> getLevelNames() {
        return this.levels.stream().map(Level::getName).collect(Collectors.toList());
    }

    public void setLevel(int index){
        if (index >= 0 && index <= getLevelsCount()) {
            this.currentLevel = this.levels.elementAt(index).getCopy();
            this.currentLevelNumber = index;
            updateBounds();
            setLock(index<1);
        } else this.currentLevel = null;
    }

    public void setLock(boolean lock) {
        this.locked = lock;
    }

    public void setMoveListener(ActionListener listener) {
        this.listener.moveListener = listener;
    }

    public void setCellSize(int newCellSize) {
        if (newCellSize > 3 && newCellSize != Cell.CELLSIZE) {
            this.currentLevel.forAllPieces(p -> p.changeCellSize(newCellSize));
            Cell.CELLSIZE = newCellSize;
        }
    }

    public void updateBounds() {
        Dimension levelSize = this.currentLevel.getLevelSize();
        setMinimumSize(levelSize);
        setPreferredSize(levelSize);
        setSize(levelSize);
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

    class Listener implements MouseInputListener, ActionListener, ComponentListener {
        private ActionListener moveListener = null;
        private final Timer timer ;
        private Point oldDragPos = new Point(0,0);
        private Point oldDirection = new Point(0,0);

        public Listener() {
            this.timer = new Timer(5, this);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (Board.this.locked || this.timer.isRunning())
                return;
            this.oldDragPos = e.getPoint();
            if (Board.this.currentLevel.startDrag(e.getX(), e.getY())) {
                repaint();
            }
        }

        @Override
        public synchronized void mouseDragged(MouseEvent e) {
            if (Board.this.locked || this.timer.isRunning()) return;
            int dx = e.getX() - this.oldDragPos.x;
            int dy = e.getY() - this.oldDragPos.y;
            Point p = Board.this.currentLevel.doDrag(dx, dy);
            if (p != null) {
                this.oldDragPos = p;
                repaint();
            }
            this.oldDirection = new Point(dx, dy);
        }

        @Override
        public synchronized void mouseReleased(MouseEvent e) {
            if (Board.this.locked || this.timer.isRunning()) return;
            int dx = e.getX() - this.oldDragPos.x;
            int dy = e.getY() - this.oldDragPos.y;
            dx = (dx == 0) ? this.oldDirection.x : dx;
            dy = (dy == 0) ? this.oldDirection.y : dy;
            if (Board.this.currentLevel.doSnap(dx, dy)) {
                moveListenerNotify();
                checkLevelComplete();
                this.oldDragPos = e.getPoint();
                repaint();
            } else {
                this.oldDragPos = new Point(dx, dy);
                this.timer.start();
            }
        }

        @Override // onTimer method
        public synchronized void actionPerformed(ActionEvent actionEvent) {
            if (Board.this.currentLevel.doSnap(this.oldDragPos.x, this.oldDragPos.y)) {
                this.timer.stop();
                moveListenerNotify();
                checkLevelComplete();
            }
            repaint();
        }

        private void moveListenerNotify() {
            if (this.moveListener != null)
                this.moveListener.actionPerformed(new ActionEvent(this,
                                ActionEvent.ACTION_LAST + 1,
                                Integer.toString(Board.this.currentLevel.getMovesCount()))
                );
        }

        @Override
        public void componentResized(ComponentEvent e) {
            Dimension d = getSize();
            Dimension l = Board.this.currentLevel.getLevelSize();
            int newCellSize = Integer.min(Cell.CELLSIZE * d.width / l.width, Cell.CELLSIZE * d.height / l.height);
            setCellSize(newCellSize);
            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void componentMoved(ComponentEvent e) {

        }

        @Override
        public void componentShown(ComponentEvent e) {

        }

        @Override
        public void componentHidden(ComponentEvent e) {

        }
    }

}


