import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

class FileReader {
    static List<String> readLines(String fileName) throws IOException {
        return Files.readAllLines(Paths.get(fileName));
    }
}