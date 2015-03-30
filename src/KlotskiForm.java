import paint.PainterTheme;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.*;
import java.util.function.Predicate;
import java.util.prefs.Preferences;

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

    final MainMenu mainMenu;
    private final Board board;
    private static JFrame frame;

    static Languages langBundle;
    private final Preferences prefs = Preferences.userNodeForPackage(KlotskiForm.class);

    public KlotskiForm() {
        langBundle = new Languages();
        this.board = new Board();
        this.mainMenu = new MainMenu(this.board.getLevelNames(), langBundle.getLanguages(), PainterTheme.values());
        this.boardPanel.add(this.board);
        this.exitButton.addActionListener(e -> System.exit(0));
        this.resetButton.addActionListener(e -> setLevel(this.board.getCurrentLevelNumber()));
        this.board.setMoveListener(e -> this.movesLabel.setText(formatMovesMsg(e.getActionCommand())));
        this.backButton.addActionListener(e -> undo());
        this.slider1.addChangeListener(event -> changeCellSize());
    }

    private void loadPrefs() {
        themeChanged(this.prefs.get("theme", PainterTheme.values()[0].name()));
        languageChanged(this.prefs.get("language", langBundle.getDefaultLanguage()));
    }

    private void undo() {
        this.board.currentLevel.undo();
        this.movesLabel.setText(formatMovesMsg(Integer.toString(this.board.currentLevel.getMovesCount())));
        frame.repaint();
    }

    private void changeCellSize() {
        this.board.setCellSize(this.slider1.getValue());
        this.board.updateBounds();
        frame.pack();
    }

    private String formatMovesMsg(String count) {
        return MessageFormat.format(langBundle.getString("moves.made.0"),count);
    }

    private void setLevel(int index) {
        this.board.setLevel(index);
        this.mainMenu.selectLevelItem(index);
        this.welcomeLabel.setVisible(false);
        this.levelNameLabel.setVisible(true);
        this.levelNameLabel.setText(this.board.currentLevel.getName());
        this.movesLabel.setText("");
        frame.pack();
        frame.repaint();
    }

    private void themeChanged(String name){
        this.mainMenu.selectThemeItem(name);
        PainterTheme.forEachPainterCollections(item -> item.selectPainter(PainterTheme.valueOf(name)));
        this.prefs.put("theme", name);
        this.board.repaint();
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
        this.prefs.put("language",lang);
        Helper helper = new Helper();
        helper.traverse(frame);
        this.mainMenu.selectLanguageItem(lang);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.LookAndFeelInfo[] f = UIManager.getInstalledLookAndFeels();
            for(UIManager.LookAndFeelInfo i:f) System.err.println(i.getClassName());
            //UIManager.setLookAndFeel("joxy.JoxyLookAndFeel");
            } catch (Exception e) {
                System.err.println("No system specific look&fell. Using default.");
        }
        frame = new JFrame("Klotski");
        KlotskiForm form = new KlotskiForm();
        frame.setContentPane(form.boardPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setJMenuBar(form.mainMenu.getMenuBar());
        frame.pack();
        try {
            URL icon = frame.getClass().getResource("/img/icon.png");
            if (icon == null) throw new IOException();
            frame.setIconImage(ImageIO.read(icon));
        } catch (IOException e) {
            System.err.println("No application icon found");
        }
        form.loadPrefs();
        frame.setVisible(true);
    }

    class MainMenu {
        private final JMenuBar menuBar = new JMenuBar();
        private final MenuGroup levelMenuGroup = new MenuGroup();
        private final MenuGroup themeMenuGroup = new MenuGroup();
        private final MenuGroup languageMenuGroup = new MenuGroup();

        public MainMenu(List<String> levels, String[] languages, PainterTheme[] painterThemes) {
            this.menuBar.add(createGameMenu());
            this.menuBar.add(createLevelMenu(levels));
            this.menuBar.add(createSettingsMenu(languages, painterThemes));
        }

        public void selectLevelItem(int level) {
            this.levelMenuGroup.selectItem(level - 1);
        }

        public void selectThemeItem(String name) {
            this.themeMenuGroup.selectItem(name);
        }

        public void selectLanguageItem(String name) {
            this.languageMenuGroup.selectItem(name);
        }

        public JMenuBar getMenuBar() {
            return this.menuBar;
        }

        private JMenu createGameMenu() {
            JMenu menu = new JMenu(langBundle.getString("game"));
            menu.setName("game");
            menu.setMnemonic('G');

            JMenuItem menuItem = new JMenuItem(langBundle.getString("exit"));
            menuItem.setName("exit");
            menuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
            menuItem.addActionListener(actionEvent -> System.exit(0));
            menu.add(menuItem);
            return menu;
        }

        private JMenu createLevelMenu(List<String> levels) {
            JMenu menu = new JMenu(langBundle.getString("levels"));
            menu.setName("levels");
            menu.setMnemonic('L');

            for (int i = 1; i<levels.size(); i++) {
                JMenuItem menuItem = new JRadioButtonMenuItem(levels.get(i));
                menuItem.addActionListener(event -> setLevel(Integer.parseInt(event.getActionCommand())));
                menuItem.setActionCommand(String.valueOf(i));
                this.levelMenuGroup.add(menuItem);
                menu.add(menuItem);
            }
            return menu;
        }

        private JMenu createSettingsMenu(String[] languages, PainterTheme[] painterThemes) {
            JMenu menu = new JMenu(langBundle.getString("settings"));
            menu.setName("settings");
            menu.setMnemonic('S');
            JMenuItem menuItem = new JMenu(langBundle.getString("language"));
            menuItem.setName("language");
            for (String s : languages) {
                JMenuItem subItem = new JRadioButtonMenuItem(langBundle.getString(s));
                subItem.setName(s);
                subItem.setActionCommand(s);
                subItem.addActionListener(event -> languageChanged(event.getActionCommand()));
                menuItem.add(subItem);
                this.languageMenuGroup.add(subItem);
            }
            menu.add(menuItem);

            menuItem = new JMenu(langBundle.getString("theme"));
            menuItem.setName("theme");

            for (PainterTheme t : painterThemes) {
                JMenuItem subItem = new JRadioButtonMenuItem(t.getThemeName());
                subItem.setName(t.name());
                subItem.setActionCommand(t.name());
                subItem.addActionListener(event -> themeChanged(event.getActionCommand()));
                menuItem.add(subItem);
                this.themeMenuGroup.add(subItem);
            }
            menu.add(menuItem);
            return menu;
        }

        class MenuGroup extends ButtonGroup {

            public void selectItem(int index) {
                this.buttons.stream()
                        .skip(index)
                        .findFirst()
                        .ifPresent(x -> x.setSelected(true));
            }

            public void selectItem(String name){
                this.buttons.stream()
                        .filter(b -> name.equals(b.getName()))
                        .findFirst()
                        .ifPresent(x -> x.setSelected(true));
            }

        }
    }

    enum Locales {
        English("en", "US"),
        French("fr","FR"),
        Russian("ru","RU"),
        Ukrainian("ua","UA");

        private String lang, country;

        Locales(String lang, String country) {
            this.lang = lang;
            this.country = country;
        }

        public String getLang() {
            return this.lang;
        }

        public String getCountry() {
            return this.country;
        }
    }

    class Languages {
        private final String bundlePath = "i18n/i18n";
        private ResourceBundle currentLangBundle;
        private final Vector<Locales> translations = new Vector<>();

        public Languages() {
            for(Locales l: KlotskiForm.Locales.values()) {
                Locale locale = new Locale(l.getLang(),l.getCountry());
                try {
                    ResourceBundle bundle = ResourceBundle.getBundle(this.bundlePath,locale);
                    if (bundle.getLocale().getLanguage().equals(locale.getLanguage()))
                        this.translations.add(l);
                    else
                        System.err.println("No locale found "+l.name());
                    } catch (MissingResourceException ignore) {
                }
            }
            Locale defaultLocale = Locale.getDefault();
            if (!setLocale(defaultLocale.getCountry(),defaultLocale.getLanguage()))
                setLocale(0);
        }

        public boolean setLocale(String country, String language) {
            return setLocale(t -> country.equals(t.getCountry()) && language.equals(t.getLang()));
        }

        public boolean setLocale(String name) {
            return setLocale(x -> name.equals(x.name()) );
        }

        public boolean setLocale(Predicate<Locales> predicate){
            for (int i = 0; i < this.translations.size(); i++) {
                if (predicate.test(this.translations.get(i))) {
                    return setLocale(i);
                }
            }
            return false;
        }

        public boolean setLocale(int index) {
            if (index >= 0 && index < this.translations.size()) {
                Locales t = this.translations.get(index);
                Locale locale = new Locale(t.getLang(),t.getCountry());
                this.currentLangBundle = ResourceBundle.getBundle(this.bundlePath,locale);
                return true;
            }
            return false;
        }

        public String[] getLanguages() {
            return this.translations.stream().map(Enum::name).toArray(String[]::new);
        }

        public String getDefaultLanguage() {
            Locale defaultLocale = Locale.getDefault();
            return this.translations.stream()
                    .filter(t -> defaultLocale.getCountry().equals(t.getCountry())
                            && defaultLocale.getLanguage().equals(t.getLang()))
                    .map(Enum::name)
                    .findFirst().orElse(Locales.English.name());
        }

        public String getString(String key) {
            return this.currentLangBundle.getString(key);
        }
    }
}
