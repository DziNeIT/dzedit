import pw.ollie.dzedit.DzEdit;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main class for DzEdit.
 * <p>
 * If any arguments are provided to {@link #main(String[])} they will be parsed as a file location to open.
 */
public final class Main {
    public static void main(String[] args) {
        Path target = null;
        if (args.length > 0) {
            target = Paths.get(String.join(" ", args));
        }

        new DzEdit(target);
    }
}
