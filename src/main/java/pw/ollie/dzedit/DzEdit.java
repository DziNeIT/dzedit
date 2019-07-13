package pw.ollie.dzedit;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Main controller class for DzEdit.
 */
public final class DzEdit {
    /**
     * The DzEdit window (a JFrame).
     */
    private final EditorWindow window;
    /**
     * All currently open tabs.
     */
    private final List<EditorTab> tabs;

    /**
     * Creates a new DzEdit and corresponding {@link EditorWindow}, with one tab - the given file, or an empty tab if
     * the given {@link Path} is {@code null}.
     *
     * @param target the Path to the initial file to open
     */
    public DzEdit(Path target) {
        window = new EditorWindow(this);
        tabs = new ArrayList<>();
        tabs.add(tab(0, target));

        update();
        window.display();
    }

    /**
     * Open a new tab in the editor from the file at the given {@link Path}. If {@code null} is provided an empty tab is
     * created instead.
     *
     * @param path the Path to the file to open
     */
    public void open(Path path) {
        EditorTab tab = tab(tabs.size(), path);
        tab.setText(path == null ? "" : Util.read(path));
        tabs.add(tab);
        window.addTab(tab);
    }

    // internal

    void close(int index) {
        EditorTab tab = tabs.get(index);
        IntStream.range(index + 1, tabs.size()).mapToObj(tabs::get).forEach(EditorTab::decrementIndex);
        tabs.remove(tab);
        window.removeTab(tab);
    }

    void save(int index) {
        if (index >= tabs.size()) {
            return;
        }

        EditorTab tab = tabs.get(index);
        save(tab, null);
    }

    void save(int index, Path target) {
        if (index >= tabs.size()) {
            return;
        }

        EditorTab tab = tabs.get(index);
        save(tab, target);
    }

    private void save(EditorTab tab, Path target) {
        if (target != null) {
            tab.setPath(target);
            tab.setTitle(Util.readableTitle(target));
            window.updateTab(tab);
        }
        if (tab.getPath() != null) {
            Util.writeFile(tab.getPath(), tab.getText());
        }
    }

    private void update() {
        tabs.stream().filter(Util.negate(window::hasTab)).forEach(window::addTab);
    }

    private EditorTab tab(int index, Path path) {
        return new EditorTab(index, path == null ? "new" : Util.readableTitle(path), path);
    }
}
