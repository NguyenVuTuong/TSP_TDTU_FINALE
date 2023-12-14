import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class DataGenerator {
    public void generateTestData(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter number of cities: ");
            int numCities = scanner.nextInt();
            for (int i = 0; i <= numCities; i++) {
                writer.write(String.valueOf(i));
                if (i < numCities) {
                    writer.write(",");
                }
            }
            writer.newLine();

            Random random = new Random();
            for (int i = 1; i <= numCities; i++) {
                writer.write(String.valueOf(i));
                for (int j = 1; j <= numCities; j++) {
                    int distance;
                    if (i == j) {
                        distance = 88888; // Giữa thành phố với chính nó, khoảng cách là 0
                    } else {
                        distance = random.nextInt(100) + 1;
                    }
                    writer.write("," + distance);
                }
                writer.newLine();
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}