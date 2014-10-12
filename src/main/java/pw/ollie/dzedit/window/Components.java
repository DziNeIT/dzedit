package pw.ollie.dzedit.window;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

import pw.ollie.dzedit.DzEdit;

import static javax.swing.JFileChooser.*;
import static javax.swing.JOptionPane.*;

public class Components {
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

    public Components(final DzEdit main, final Window window) {
        final Container cp = window.getContentPane();

        // Create components
        menuBar = new JMenuBar();

        // File menu
        fileMenu = new JMenu("File");
        newItem = new JMenuItem("New");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        saveAsItem = new JMenuItem("Save As");

        fileMenu.setPreferredSize(new Dimension(50, 25));
        newItem.setPreferredSize(new Dimension(85, 25));
        openItem.setPreferredSize(new Dimension(85, 25));
        saveItem.setPreferredSize(new Dimension(85, 25));
        saveAsItem.setPreferredSize(new Dimension(85, 25));

        // Window menu
        windowMenu = new JMenu("Window");
        newTabItem = new JMenuItem("New Tab");

        windowMenu.setPreferredSize(new Dimension(50, 25));
        newTabItem.setPreferredSize(new Dimension(85, 25));

        // File menu
        menuBar.add(fileMenu);
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);

        // Window menu
        menuBar.add(windowMenu);
        windowMenu.add(newTabItem);

        window.setJMenuBar(menuBar);
        textArea = new JTextArea();
        cp.add(textArea, BorderLayout.CENTER);

        // Create a blank text area when the 'New' button is clicked
        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                textArea.setText("");
                window.setTitle(Window.BASE_WINDOW_NAME);
                main.onNewFile();
                window.nw = true;
            }
        });

        // Opens a GUI for opening files when the 'Open' button is clicked
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser choose = new JFileChooser();
                if (choose.showOpenDialog(window) == APPROVE_OPTION) {
                    String last = main.getLast();
                    if (last != null && textArea.getText() != null
                            && !last.equals(textArea.getText())) {
                        final int n = showOptionDialog(window,
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
                if (choose.showSaveDialog(window) == APPROVE_OPTION) {
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
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public JMenu getFileMenu() {
        return fileMenu;
    }

    public JMenuItem getNewItem() {
        return newItem;
    }

    public JMenuItem getOpenItem() {
        return openItem;
    }

    public JMenuItem getSaveItem() {
        return saveItem;
    }

    public JMenuItem getSaveAsItem() {
        return saveAsItem;
    }

    public JMenu getWindowMenu() {
        return windowMenu;
    }

    public JMenuItem getNewTabItem() {
        return newTabItem;
    }
}
