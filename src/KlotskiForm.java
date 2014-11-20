import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ainozemtsev on 10.11.14.
 */
public class KlotskiForm {
    private JPanel panel1;
    private JButton exitButton;
    private JPanel BoardPanel;
    private JSpinner levelSpinner;
    private JButton goButton;
    private JPanel statusPanel;
    private JLabel statusLabel;

    private Board board;
    private static JFrame frame;

    public KlotskiForm() {
        this.board = new Board();
        this.BoardPanel.add(this.board);
        levelSpinner.setModel(new SpinnerNumberModel(1,1,board.getLevelsCount()-1,1));
        exitButton.addActionListener(e -> System.exit(0));
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = ((Number)levelSpinner.getValue()).intValue();
                board.setLevel(index);
                statusLabel.setText(board.currentLevel.getName());
                frame.pack();
                frame.repaint();
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            System.err.println("No system specific look&fell. Using default.");
        }
        frame = new JFrame("Klotski");
        KlotskiForm form = new KlotskiForm();
        frame.setContentPane(form.panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
