import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Java version of klotski game
 * Created by ainozemtsev on 10.11.14.
 */
public class KlotskiForm {
    private JPanel boardPanel;
    private JButton exitButton;
    private JSpinner levelSpinner;
    private JButton resetButton;
    private JPanel statusPanel;
    private JLabel statusLabel;
    private JButton backButton;
    private JLabel movesLabel;

    private Board board;
    private static JFrame frame;
    private JMenuBar menuBar;

    static ResourceBundle langBundle;

    public KlotskiForm() {
        this.board = new Board();
        this.boardPanel.add(this.board);
        levelSpinner.setModel(new SpinnerNumberModel(1, 1, board.getLevelsCount() - 1, 1));
        exitButton.addActionListener(e -> System.exit(0));
        resetButton.addActionListener(e -> setLevel(board.getCurrentLevelNumber()));
        this.board.setMoveListener(e -> movesLabel.setText(formatMovesMsg(e.getActionCommand())));
        backButton.addActionListener(e -> undo() );
    }

    private void undo() {
        board.currentLevel.undo();
        movesLabel.setText(formatMovesMsg(Integer.toString(board.currentLevel.getMovesCount())));
        frame.repaint();
    }

    private String formatMovesMsg(String count) {
        return MessageFormat.format(langBundle.getString("moves.made.0"),count);
    }

    private void setLevel(int index) {
        board.setLevel(index);
        levelSpinner.setValue(index);
        menuBar.getMenu(1).getItem(index-1).setSelected(true);
        statusLabel.setText(board.currentLevel.getName());
        movesLabel.setText("");
        frame.pack();
        frame.repaint();
    }

    private JMenuBar createMenu() {
        menuBar = new JMenuBar();
        JMenu menu = new JMenu(langBundle.getString("game"));
        menu.setMnemonic('G');
        JMenuItem menuItem = new JMenuItem(langBundle.getString("exit"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        menuItem.addActionListener(actionEvent -> System.exit(0));
        menu.add(menuItem);
        menuBar.add(menu);

        menu = new JMenu(langBundle.getString("levels"));
        menu.setMnemonic('L');
        ButtonGroup group = new ButtonGroup();
        int i = 0;
        for (String level:board.getLevelNames()) {
            if (i > 0) {
                menuItem = new JRadioButtonMenuItem(level);
                menuItem.addActionListener(event -> setLevel(Integer.parseInt(event.getActionCommand())));
                menuItem.setActionCommand(String.valueOf(i));
                group.add(menuItem);
                menu.add(menuItem);
            }
            i++;
        }
        menuBar.add(menu);

        menu = new JMenu("Settings");
        menu.setMnemonic('S');
        menuItem = new JMenuItem("Language");

        menu.add(menuItem);

        menuBar.add(menu);

        return menuBar;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            System.err.println("No system specific look&fell. Using default.");
        }
        langBundle = ResourceBundle.getBundle("i18n/i18n",new Locale("ru","RU"));
        frame = new JFrame("Klotski");
        KlotskiForm form = new KlotskiForm();
        frame.setContentPane(form.boardPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setJMenuBar(form.createMenu());
        frame.pack();
        try {
            URL icon = frame.getClass().getResource("/img/icon.png");
            if (icon == null) throw new IOException();
            frame.setIconImage(ImageIO.read(icon));
        } catch (IOException e) {
            System.err.println("No application icon found");
        }
        frame.setVisible(true);
    }

}
