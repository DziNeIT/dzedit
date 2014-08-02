package dzedit;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static dzedit.Utilities.*;

/**
 * An open source Java text editor, with the aim of being very simple
 * 
 * DzEdit is operable from the command line and by some GUI for loading and
 * saving files. DzEdit is a work in progress.
 * 
 * @author DziNeIT
 * @see {@link https://github.com/DziNeIT/dzedit}
 */
public final class DzEdit {
    /**
     * The Window object, which is a JFrame
     */
    private final Window window;

    // Other
    private File file;
    private String last;

    /**
     * Main constructor for DzEdit. Creates the Window object, which in turn
     * creates all of the components and changes their settings when it is
     * created
     * 
     * After this, the constructor runs listenForCommands(), which repeats until
     * the user closes the program
     */
    private DzEdit() {
        window = new Window(this);

        final Scanner scanner = new Scanner(System.in);
        listenForCommands(scanner);

        // Cleanup afterwards
        scanner.close();
        System.exit(0);
    }

    /**
     * Opens the specified File to the editor
     * 
     * @param file
     *            The File to open
     */
    void open(final File file) {
        window.getTextArea().setText(read(this.file = file));
        System.out.println("Opened file: " + file.getName());
        last = window.getTextArea().getText();
    }

    /**
     * Saves the contents of the current File to the given destination File
     * 
     * @param destination
     *            The File to write the contents to
     */
    void saveAs(final File destination) {
        if (destination == null) {
            return;
        }

        if (!writeFile(destination, window.getTextArea().getText())) {
            System.out.println("ERROR: COULD NOT SAVE FILE");
        } else {
            System.out.println("Saved contents to file: "
                    + destination.getName());
            open(destination);
        }
        last = window.getTextArea().getText();
    }

    /**
     * Saves current file
     */
    void save() {
        saveAs(file);
    }

    /**
     * Listens for commands on a loop
     * 
     * @param scanner
     *            The Scanner to listen to
     */
    private void listenForCommands(final Scanner scanner) {
        final String line = scanner.nextLine();
        if (line.equalsIgnoreCase("close")) {
            return;
        } else if (line.equalsIgnoreCase("save")) {
            save();
        } else if (line.startsWith("saveas")) {
            final String[] split = line.split(" ");
            String first = null;
            try {
                first = split[1];
            } catch (final ArrayIndexOutOfBoundsException e) {
                System.out
                        .println("Must specify filename after command 'saveas'");
            }
            if (first != null) {
                final List<String> list = new ArrayList<>(Arrays.asList(split));
                list.remove(0);
                saveAs(new File(listToString(list, " ")));
            }
        } else if (line.startsWith("open")) {
            final String[] split = line.split(" ");
            String first = null;
            try {
                first = split[1];
            } catch (final ArrayIndexOutOfBoundsException e) {
                System.out
                        .println("Must specify filename after command 'open'");
            }
            if (first != null) {
                final List<String> list = new ArrayList<>(Arrays.asList(split));
                list.remove(0);
                open(new File(listToString(list, " ")));
            }
        }
        listenForCommands(scanner);
    }

    /**
     * Gets the text content at the last time the file was saved
     * 
     * @return Text content of the file the last time we saved
     */
    public String getLast() {
        return last;
    }

    /**
     * Main method for DzEdit. Just calls the constructor
     * 
     * @param args
     *            Command line arguments
     */
    public static void main(String[] args) {
        new DzEdit();
    }
}
