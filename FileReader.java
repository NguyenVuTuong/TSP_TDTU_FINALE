import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

/**
 * FileReader is a utility class that provides a method for reading lines from a file.
 */
class FileReader {
    /**
     * Reads all lines from a file and returns them as a List of Strings.
     *
     * @param fileName The name of the file to read.
     * @return A List of Strings containing the lines read from the file.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    static List<String> readLines(String fileName) throws IOException {
        // Use the Files.readAllLines method to read all lines from the file and return them as a List
        return Files.readAllLines(Paths.get(fileName));
    }
}
