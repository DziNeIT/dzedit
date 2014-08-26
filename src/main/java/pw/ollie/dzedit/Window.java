package pw.ollie.dzedit;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import static javax.swing.JFileChooser.*;
import static javax.swing.JOptionPane.*;

/**
 * The Window for DzEdit, which is an extension of JFrame. Deals with all
 * component related stuff
 */
public class Window extends JFrame {
    /**
     * The base name for the window - the name of the file being edited is
     * appended to this, with the two separate by ' - '
     */
    public static final String BASE_WINDOW_NAME = "DzEdit";

    /**
     * The main DzEdit object
     */
    private final DzEdit main;

    // Components
    private final JTextArea textArea;
    private final JMenuBar menuBar;

    // File
    private final JMenu fileMenu;
    private final JMenuItem newItem;
    private final JMenuItem openItem;
    private final JMenuItem saveItem;
    private final JMenuItem saveAsItem;

    // Window
    private final JMenu windowMenu;
    private final JMenuItem newTabItem;

    /**
     * Whether there has been a new file created. Used to prevent new files
     * overwriting the previous file when the 'Save' button is clicked
     */
    private boolean nw = false;

    Window(final DzEdit main) {
        super(BASE_WINDOW_NAME);
        DzEdit.curAmount++;
        this.main = main;

        final Container cp = getContentPane();

        // Create components
        menuBar = new JMenuBar();

        // File menu
        fileMenu = new JMenu("File");
        newItem = new JMenuItem("New");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        saveAsItem = new JMenuItem("Save As");

        fileMenu.setPreferredSize(new Dimension(55, 25));
        newItem.setPreferredSize(new Dimension(58, 25));
        openItem.setPreferredSize(new Dimension(58, 25));
        saveItem.setPreferredSize(new Dimension(58, 25));
        saveAsItem.setPreferredSize(new Dimension(58, 25));

        // Window menu
        windowMenu = new JMenu("Window");
        newTabItem = new JMenuItem("New Tab");

        windowMenu.setPreferredSize(new Dimension(55, 25));
        newTabItem.setPreferredSize(new Dimension(58, 25));

        // File menu
        menuBar.add(fileMenu);
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);

        // Window menu
        menuBar.add(windowMenu);
        windowMenu.add(newTabItem);

        setJMenuBar(menuBar);
        textArea = new JTextArea();
        cp.add(textArea, BorderLayout.CENTER);

        // Exit if there are no more windows open
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                DzEdit.curAmount--;
                if (DzEdit.curAmount == 0) {
                    System.exit(0);
                }
            }
        });

        // Create a blank text area when the 'New' button is clicked
        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                textArea.setText("");
                setTitle(BASE_WINDOW_NAME);
                main.onNewFile();
                nw = true;
            }
        });

        // Opens a GUI for opening files when the 'Open' button is clicked
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser choose = new JFileChooser();
                if (choose.showOpenDialog(Window.this) == APPROVE_OPTION) {
                    String last = main.getLast();
                    if (last != null && textArea.getText() != null
                            && !last.equals(textArea.getText())) {
                        final int n = showOptionDialog(Window.this,
                                "Do you wish to save before\nopening a new file?",
                                "Open", YES_NO_CANCEL_OPTION, QUESTION_MESSAGE, null,
                                new String[] { "Yes", "No", "Cancel" }, "Yes");
                        if (n == 0) {
                            main.save();
                        } else if (n == 2) {
                            return;
                        }
                    }
                    main.open(choose.getSelectedFile().toPath());
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
                if (choose.showSaveDialog(Window.this) == APPROVE_OPTION) {
                    main.saveAs(choose.getSelectedFile().toPath());
                }
            }
        });

        // Create a new window when the 'New Tab' button is clicked
        newTabItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                DzEdit.threads.submit(new Runnable() {
                    @Override
                    public void run() {
                        new DzEdit();
                    }
                });
            }
        });

        // JFrame settings + show JFrame
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        setPreferredSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setResizable(true);
        pack();
        setVisible(true);
    }

    JTextArea getTextArea() {
        return textArea;
    }
}
