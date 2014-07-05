package dzedit;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * The Window for DzEdit, which is an extension of JFrame. Deals with all
 * component related stuff
 */
public class Window extends JFrame {
    /**
     * The main DzEdit object
     */
    private final DzEdit main;

    // Components
    private final JTextArea textArea;
    private final JMenuBar menuBar;
    private final JMenu fileMenu;
    private final JMenuItem openItem;
    private final JMenuItem saveItem;
    private final JMenuItem saveAsItem;
    private final JMenu optionsMenu;
    private final JMenuItem options;

    Window(final DzEdit main) {
        super("DzEdit");
        this.main = main;

        final Container cp = getContentPane();

        // Create components
        menuBar = new JMenuBar();

        // File menu
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        saveAsItem = new JMenuItem("Save As");

        fileMenu.setPreferredSize(new Dimension(60, 25));
        openItem.setPreferredSize(new Dimension(57, 25));
        saveItem.setPreferredSize(new Dimension(57, 25));
        saveAsItem.setPreferredSize(new Dimension(57, 25));

        // Options menu
        optionsMenu = new JMenu("Options");
        options = new JMenuItem("Options");

        optionsMenu.setPreferredSize(new Dimension(60, 25));
        options.setPreferredSize(new Dimension(57, 25));

        // Other components
        textArea = new JTextArea();

        // Add components

        // File menu
        menuBar.add(fileMenu);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);

        // Options menu
        menuBar.add(optionsMenu);
        optionsMenu.add(options);

        setJMenuBar(menuBar);
        cp.add(textArea, BorderLayout.CENTER);

        // Add listeners

        // Opens a GUI for opening files when the 'Open' button is clicked
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser choose = new JFileChooser();
                if (choose.showOpenDialog(Window.this) == JFileChooser.APPROVE_OPTION) {
                    String last = main.getLast();
                    if (last != null && textArea.getText() != null
                            && !last.equals(textArea.getText())) {
                        final int n = JOptionPane
                                .showOptionDialog(
                                        Window.this,
                                        "Do you wish to save before\nopening a new file?",
                                        "Open",
                                        JOptionPane.YES_NO_CANCEL_OPTION,
                                        JOptionPane.QUESTION_MESSAGE, null,
                                        new String[] { "Yes", "No", "Cancel" },
                                        "Yes");
                        switch (n) {
                        case 0:
                            main.save();
                            break;
                        case 1:
                            break;
                        case 2:
                            return;
                        }
                    }
                    main.open(choose.getSelectedFile());
                }
            }
        });

        // Saves the current file when the 'Save' button is clicked
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (!"disabled".equals(event.getActionCommand())) {
                    main.save();
                }
            }
        });

        // Opens a GUI for saving files when the 'Save As' button is clicked
        saveAsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser choose = new JFileChooser();
                if (choose.showOpenDialog(Window.this) == JFileChooser.APPROVE_OPTION) {
                    main.saveAs(choose.getSelectedFile());
                }
            }
        });

        // JFrame settings + show JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        setPreferredSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setResizable(true);
        pack();
        setVisible(true);
    }

    public JTextArea getTextArea() {
        return textArea;
    }
}
