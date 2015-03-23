import paint.PainterTheme;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.function.Predicate;

/**
 * Java version of klotski game
 * Created by ainozemtsev on 10.11.14.
 */
public class KlotskiForm {
    private JPanel boardPanel;
    private JButton exitButton;
    private JButton resetButton;
    private JPanel statusPanel;
    private JLabel welcomeLabel;
    private JButton backButton;
    private JLabel movesLabel;
    private JLabel levelNameLabel;
    private JSlider slider1;

    private Board board;
    private static JFrame frame;
    private JMenuBar menuBar;

    static Languages langBundle;

    public KlotskiForm() {
        KlotskiForm.langBundle = new Languages();
        this.board = new Board();
        this.boardPanel.add(this.board);
        this.exitButton.addActionListener(e -> System.exit(0));
        this.resetButton.addActionListener(e -> setLevel(board.getCurrentLevelNumber()));
        this.board.setMoveListener(e -> movesLabel.setText(formatMovesMsg(e.getActionCommand())));
        this.backButton.addActionListener(e -> undo());
        slider1.addChangeListener(event -> changeCellSize());
    }

    private void undo() {
        board.currentLevel.undo();
        movesLabel.setText(formatMovesMsg(Integer.toString(board.currentLevel.getMovesCount())));
        frame.repaint();
    }

    private void changeCellSize() {
        board.setCellSize(slider1.getValue());
        board.updateBounds();
        frame.pack();
    }

    private String formatMovesMsg(String count) {
        return MessageFormat.format(langBundle.getString("moves.made.0"),count);
    }

    private void setLevel(int index) {
        board.setLevel(index);
        menuBar.getMenu(1).getItem(index-1).setSelected(true);
        welcomeLabel.setVisible(false);
        levelNameLabel.setVisible(true);
        levelNameLabel.setText(board.currentLevel.getName());
        movesLabel.setText("");
        frame.pack();
        frame.repaint();
    }

    private JMenuBar createMenu() {
        menuBar = new JMenuBar();
        JMenu menu = new JMenu(langBundle.getString("game"));
        menu.setName("game");
        menu.setMnemonic('G');

        JMenuItem menuItem = new JMenuItem(langBundle.getString("exit"));
        menuItem.setName("exit");
        menuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        menuItem.addActionListener(actionEvent -> System.exit(0));
        menu.add(menuItem);
        menuBar.add(menu);

        menu = new JMenu(langBundle.getString("levels"));
        menu.setName("levels");
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

        menu = new JMenu(langBundle.getString("settings"));
        menu.setName("settings");
        menu.setMnemonic('S');
        menuItem = new JMenu(langBundle.getString("language"));
        menuItem.setName("language");
        for (String s:langBundle.getLanguages()){
            JMenuItem subItem = new JMenuItem(langBundle.getString(s));
            subItem.setName(s);
            subItem.setActionCommand(s);
            subItem.addActionListener(event -> languageChanged(event.getActionCommand()));
            menuItem.add(subItem);
        }
        menu.add(menuItem);

        menuItem = new JMenu(langBundle.getString("theme"));
        menuItem.setName("theme");
        for (PainterTheme t: PainterTheme.values()) {
            JMenuItem subItem = new JMenuItem(t.getThemeName());
            subItem.setActionCommand(t.name());
            subItem.addActionListener(event -> themeChanged(event.getActionCommand()));
            menuItem.add(subItem);
        }
        menu.add(menuItem);

        menuBar.add(menu);

        return menuBar;
    }

    private void themeChanged(String name){
        PainterTheme.forEachPainterCollections(item -> item.selectPainter(PainterTheme.valueOf(name)));
        board.repaint();
    }

    private void languageChanged(String lang) {
        class Helper {
            void traverse(Container container) {
                for (Component c : container.getComponents()) {
                    update(c);
                }
            }
            void traverseMenu(JMenu menu) {
                for(MenuElement c : menu.getSubElements())
                if (c instanceof Component)
                    update((Component) c);
            }
            void update(Component c) {
                String name = c.getName();
                if (null != name) try {
                    if (c instanceof AbstractButton)
                        ((AbstractButton)c).setText(langBundle.getString(name));
                    else if (c instanceof JLabel)
                        ((JLabel) c).setText(langBundle.getString(name));
                } catch (MissingResourceException ignored) { }
                if (c instanceof JMenu) {
                    traverseMenu((JMenu) c);
                } else if (c instanceof Container && ((Container) c).getComponentCount()>0)
                    traverse((Container) c);
            }
        }
        if ( !langBundle.setLocale(lang))
            return;
        Helper helper = new Helper();
        helper.traverse(frame);
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

    class Languages {
        private final String[][] locales = {
                {"en","US","english"},
                {"fr","FR","french"},
                {"ru","RU","russian"},
                {"ua","UA","ukrainian"}
        };
        private final String bundlePath = "i18n/i18n";
        private ResourceBundle currentLangBundle;
        private final Vector<String[]> translations = new Vector<>();

        public Languages() {
            Locale locale;
            for(String[] l:locales) {
                locale = new Locale(l[0],l[1]);
                try {
                    ResourceBundle bundle = ResourceBundle.getBundle(bundlePath,locale);
                    if (bundle.getLocale().getLanguage().equals(locale.getLanguage()))
                        translations.add(l);
                    else
                        System.err.println("No locale found"+l[2]);
                } catch (MissingResourceException e) {
                    continue;
                }
            }
            Locale defaultLocale = Locale.getDefault();
            System.err.println(defaultLocale);
            if (!setLocale(defaultLocale.getCountry(),defaultLocale.getLanguage()))
                setLocale(0);
        }

        public boolean setLocale(String country, String language) {
            return setLocale(t -> country.equals(t[1]) && language.equals(t[0]));
        }

        public boolean setLocale(String name) {
            return setLocale(x -> name.equals(x[2]) );
        }

        public boolean setLocale(Predicate<String[]> predicate){
            for (int i=0; i<translations.size();i++) {
                String[] lang = translations.get(i);
                if (predicate.test(lang)) {
                    return setLocale(i);
                }
            }
            return false;
        }

        public boolean setLocale(int index) {
            if (index >= 0 && index < translations.size()) {
                String[] t = translations.get(index);
                Locale locale = new Locale(t[0],t[1]);
                currentLangBundle = ResourceBundle.getBundle(bundlePath,locale);
                return true;
            }
            return false;
        }

        public String[] getLanguages() {
            return translations.stream().map(x -> x[2]).toArray(String[]::new);
        }

        public String getString(String key) {
            return currentLangBundle.getString(key);
        }
    }
}
