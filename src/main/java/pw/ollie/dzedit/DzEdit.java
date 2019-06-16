package pw.ollie.dzedit;

import pw.ollie.dzedit.window.Window;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static pw.ollie.dzedit.Util.*;

/**
 * An open source Java text editor, with the aim of being very simple. DzEdit is a work in progress. It currently
 * supports opening and saving files, as well as having multiple open windows
 *
 * @author DziNeIT
 */
public final class DzEdit {
    /**
     * The DzEdit thread pool - each window has its own thread in the thread pool
     */
    public static ExecutorService threads = Executors.newCachedThreadPool();
    /**
     * The amount of windows currently open. Incremented for each construction of a Window object and decremented when a
     * window is closed. If it is 0 after a window has been closed (i.e there are no windows left open), the application
     * terminates
     */
    public static int curAmount = 0;

    /**
     * The Window object, which is a JFrame
     */
    private final Window window;

    /**
     * The path to the file which is currently being edited
     */
    private Path path;
    /**
     * The text content of the file the last time it was saved
     */
    private String last;

    /**
     * Main constructor for DzEdit. Creates the Window object, which in turn creates all of the components and changes
     * their settings when it is created
     * <p>
     * After this, the constructor runs listenForCommands(), which repeats until the user closes the program
     */
    public DzEdit() {
        window = new Window(this);
    }

    /**
     * Opens the specified Path to the editor
     *
     * @param path the Path of the File to open
     */
    public void open(final Path path) {
        window.getWindowComponents().getTextArea().setText(read(this.path = path));
        // Put the path to the file in the title of the window
        window.setTitle(Window.BASE_WINDOW_NAME + " - " + path.toString());
        last = window.getWindowComponents().getTextArea().getText();
    }

    /**
     * Saves the contents of the current File to the given destination
     *
     * @param destination the Path to the File to write the contents to
     */
    public void saveAs(final Path destination) {
        if (destination == null) {
            return;
        }

        if (!writeFile(destination, window.getWindowComponents().getTextArea().getText())) {
            System.err.println("ERROR: COULD NOT SAVE FILE");
        } else {
            open(destination);
        }
        last = window.getWindowComponents().getTextArea().getText();
    }

    /**
     * Saves current file
     */
    public void save() {
        saveAs(path);
    }

    /**
     * Gets the text content at the last time the file was saved
     *
     * @return text content of the file the last time it was saved
     */
    public String getLast() {
        return last;
    }

    /**
     * Called when the user selects the option to create a new, blank file. This stops new files from overwriting the
     * previously saved file when the 'Save' button is clicked
     */
    public void onNewFile() {
        path = null;
        last = null;
    }

    /**
     * Main method for DzEdit. Creates a new DzEdit window, run in a thread pool
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Run DzEdit in a thread in the thread pool
        threads.submit((Runnable) DzEdit::new);
    }
}
