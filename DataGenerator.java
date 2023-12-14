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
            writer.write("88888,");
            for (int i = 1; i <= numCities; i++) {
                writer.write(String.valueOf(i));
                if (i < numCities) {
                    writer.write(",");
                }
            }
            writer.newLine();

            Random random = new Random();

            // Create a square matrix for distances
            int[][] distances = new int[numCities + 1][numCities + 1];

            for (int i = 1; i <= numCities; i++) {
                for (int j = 1; j <= numCities; j++) {
                    distances[i][j] = -1;
                }
            }
            // Write distances between cities to the file   
            for (int i = 1; i <= numCities; i++) {
                writer.write(String.valueOf(i));
                for (int j = 1; j <= numCities; j++) {
                    // Set distance to a fixed value if the city is the same
                    if (distances[i][j] == -1) {
                        if (i == j) {
                            distances[i][j] = 88888; // Distance between a city and itself is 88888
                        } else {
                            // Use the same distance for both (i, j) and (j, i)
                            distances[i][j] = random.nextInt(100) + 1;
                            distances[j][i] = distances[i][j];
                        }
                    }
                    writer.write("," + distances[i][j]);
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