package pw.ollie.dzedit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Static utility methods for DzEdit
 */
public final class Utilities {
    /**
     * Reads the contents of a File
     * 
     * @param file
     *            The File to read
     * 
     * @return The contents of the File as a String
     */
    public static String read(final Path path) {
        try {
            return new String(Files.readAllBytes(path));
        } catch (final IOException e) {
            return null;
        }
    }

    /**
     * Writes a String to a File
     * 
     * @param file
     *            The target File to write contents to
     * @param contents
     *            The contents to write to the File
     * 
     * @return Whether we succeeded in writing the contents to the File
     */
    public static boolean writeFile(final Path path, final String contents) {
        try {
            Files.write(path, contents.getBytes());
            return true;
        } catch (final IOException e) {
            return false;
        }
    }

    private Utilities() {
    }
}
