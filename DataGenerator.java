import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class DataGenerator {
    /**
     * Generates test data for the Traveling Salesman Problem (TSP) and writes it to a file.
     * The generated data includes city indices and distances between cities.
     * The distances are random, and the distance between a city and itself is set to a fixed value (e.g., 88888).
     *
     * @param filename The name of the file to write the generated data.
     */
    public void generateTestData(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            Scanner scanner = new Scanner(System.in);

            // Get the number of cities from user input
            System.out.print("Enter number of cities: ");
            int numCities = scanner.nextInt();

            // Write city indices to the file
            for (int i = 0; i <= numCities; i++) {
                writer.write(String.valueOf(i));
                if (i < numCities) {
                    writer.write(",");
                }
            }
            writer.newLine();

            Random random = new Random();

            // Write distances between cities to the file
            for (int i = 1; i <= numCities; i++) {
                writer.write(String.valueOf(i));
                for (int j = 1; j <= numCities; j++) {
                    int distance;
                    // Set distance to a fixed value if the city is the same
                    if (i == j) {
                        distance = 88888; // Distance between a city and itself is 88888
                    } else {
                        // Generate a random distance between 1 and 100
                        distance = random.nextInt(100) + 1;
                    }
                    writer.write("," + distance);
                }
                writer.newLine();
            }

            scanner.close();
        } catch (IOException e) {
            // Handle IO errors (e.g., file not found, permission issues)
            e.printStackTrace();
        }
    }
}
