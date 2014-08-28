package pw.ollie.dzedit.window;

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

import pw.ollie.dzedit.DzEdit;

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
    /**
     * The holder of components for the Window
     */
    private final Components components;

    /**
     * Whether there has been a new file created. Used to prevent new files
     * overwriting the previous file when the 'Save' button is clicked
     */
    boolean nw = false;

    public Window(final DzEdit main) {
        super(BASE_WINDOW_NAME);
        DzEdit.curAmount++;
        this.main = main;

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

        components = new Components(main, this);

        // JFrame settings + show JFrame
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        setPreferredSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setResizable(true);
        pack();
        setVisible(true);
    }

    public Components getWindowComponents() {
        return components;
    }
}
