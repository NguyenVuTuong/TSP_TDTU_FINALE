import java.io.IOException;
import java.util.List;

public class MainTestKey {
    public static void main(String[] args) {
        try {
            // Generate test data using the DataGenerator
            // DataGenerator data = new DataGenerator();
            // data.generateTestData("cities.csv");
            
            // Read data from the generated file
            String fileName = "cities_testdapan.csv";
            List<String> lines = FileReader.readLines(fileName);

            // Check if the file is empty
            if (lines.isEmpty()) {
                System.out.println("File is empty!");
            } else {
                // Set the number of iterations, cities, and population size
                int iterations = 5;
                int cities = lines.size() -1 ;

                System.out.println(cities);
                // int populationSize = lines.size();
                int populationSize = 5;


                // Generate random probabilities for crossover and mutation
                // double crossoverProbability = Math.random();
                // double mutationProbability = Math.random();
                double crossoverProbability = 0.8;
                double mutationProbability = 0.1;

                // Create an instance of TSPGeneticAlgorithm with the generated parameters
                TSPGeneticAlgorithm tspGeneticAlgorithm = new TSPGeneticAlgorithm(crossoverProbability, mutationProbability, lines);

                // Create an instance of TSPSolver and solve the TSP problem
                TSPSolver tspSolver = new TSPSolver(tspGeneticAlgorithm);
                tspSolver.solve(iterations, populationSize, cities);

                // Uncomment the following lines to compare with BruteForce
                // BruteForce comparation = new BruteForce(lines);
                // comparation.solve();
                // comparation.printTourAndLength();
            }

            // Close the scanner if it was used (commented out for now)
            // scanner.close();
        } catch (IOException e) {
            // Handle file reading errors
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
}
