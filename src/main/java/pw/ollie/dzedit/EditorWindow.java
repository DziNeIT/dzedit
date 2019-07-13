package pw.ollie.dzedit;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import java.awt.Dimension;

/**
 * The window for the DzEdit editor.
 */
public final class EditorWindow extends JFrame {
    /**
     * The tabbed pane containing each currently open editor tab.
     */
    private final JTabbedPane tabbedPane;

    EditorWindow(final DzEdit editor) {
        super("DzEdit");

        tabbedPane = new JTabbedPane();
        add(tabbedPane);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem saveAsItem = new JMenuItem("Save As");
        JMenuItem closeItem = new JMenuItem("Close Tab");

        newItem.addActionListener(event -> editor.open(null));

        openItem.addActionListener(event -> {
            JFileChooser choose = new JFileChooser();
            if (choose.showOpenDialog(EditorWindow.this) == JFileChooser.APPROVE_OPTION) {
                editor.open(choose.getSelectedFile().toPath());
            }
        });

        saveItem.addActionListener(event -> editor.save(tabbedPane.getSelectedIndex()));

        saveAsItem.addActionListener(event -> {
            JFileChooser choose = new JFileChooser();
            if (choose.showSaveDialog(EditorWindow.this) == JFileChooser.APPROVE_OPTION) {
                editor.save(tabbedPane.getSelectedIndex(), choose.getSelectedFile().toPath());
            }
        });

        closeItem.addActionListener(event -> {
            if (tabbedPane.getTabCount() == 1) {
                return;
            }
            // todo add dialog box asking to save
            editor.close(tabbedPane.getSelectedIndex());
        });

        fileMenu.setPreferredSize(new Dimension(50, 25));
        newItem.setPreferredSize(new Dimension(85, 25));
        openItem.setPreferredSize(new Dimension(85, 25));
        saveItem.setPreferredSize(new Dimension(85, 25));
        saveAsItem.setPreferredSize(new Dimension(85, 25));

        menuBar.add(fileMenu);
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(closeItem);
        setJMenuBar(menuBar);
    }

    // internal

    void display() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        setPreferredSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setResizable(true);
        pack();
        setVisible(true);
    }

    void addTab(EditorTab tab) {
        tabbedPane.add(tab.getTitle(), tab);
    }

    void removeTab(EditorTab tab) {
        tabbedPane.remove(tab);
    }

    void updateTab(EditorTab tab) {
        tabbedPane.setTitleAt(tab.getIndex(), tab.getTitle());
    }

    boolean hasTab(EditorTab tab) {
        return tabbedPane.getTabCount() > tab.getIndex() && tabbedPane.getTabComponentAt(tab.getIndex()) == tab;
    }
}
