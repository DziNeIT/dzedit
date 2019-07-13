package pw.ollie.dzedit;

import javax.swing.JTextArea;
import java.nio.file.Path;

/**
 * Represents a single open tab in DzEdit editor.
 */
public final class EditorTab extends JTextArea {
    /**
     * The tab's index in the tabbed pane.
     */
    private int index;
    /**
     * The short title of the tab.
     */
    private String title;
    /**
     * The Path of the file open in this tab. May be {@code null}.
     */
    private Path path;

    EditorTab(int index, String title, Path path) {
        this.index = index;
        this.title = title;
        this.path = path;
    }

    // internal

    int getIndex() {
        return index;
    }

    String getTitle() {
        return title;
    }

    Path getPath() {
        return path;
    }

    void decrementIndex() {
        index = index - 1;
    }

    void setTitle(String title) {
        this.title = title;
    }

    void setPath(Path path) {
        this.path = path;
    }
}
