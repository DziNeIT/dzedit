package pw.ollie.dzedit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Static utility methods for DzEdit
 */
public final class Util {
    /**
     * Reads the contents of the file at the given Path
     *
     * @param path the Path to the file to read
     * @return The contents of the file located at the given Path as a String
     */
    public static String read(final Path path) {
        try {
            return new String(Files.readAllBytes(path));
        } catch (final IOException e) {
            return e.toString();
        }
    }

    /**
     * Writes a String to the file at a given Path
     *
     * @param path     the target path to write contents to the file at
     * @param contents the contents to write to the File
     * @return whether we succeeded in writing the contents to the file at the given Path
     */
    public static boolean writeFile(final Path path, final String contents) {
        try {
            Files.write(path, contents.getBytes());
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Util() {
        throw new UnsupportedOperationException();
    }
}
