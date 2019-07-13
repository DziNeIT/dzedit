package pw.ollie.dzedit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * General utility methods for DzEdit including file reading/writing.
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

    /**
     * Produces a readable title from the given {@link Path}. This method will always return the same value for the same
     * Path provided the same value of {@link File#separator}.
     *
     * @param path the Path to get a title for
     * @return readable title for given Path
     */
    public static String readableTitle(Path path) {
        String sep = Pattern.quote(File.separator);
        String[] split = path.toString().split(sep);
        if (split.length == 0) {
            return path.toString();
        }
        return split[split.length - 1];
    }

    /**
     * Returns a {@link Predicate} which tests the opposite of the given Predicate.
     *
     * @param original the Predicate to negate
     * @param <T>      the type of object tested in the Predicate
     * @return an opposite Predicate to the one given
     */
    public static <T> Predicate<T> negate(Predicate<T> original) {
        return (t) -> !original.test(t);
    }

    private Util() {
        throw new UnsupportedOperationException();
    }
}
