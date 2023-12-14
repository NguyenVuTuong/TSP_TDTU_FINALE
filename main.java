import java.io.IOException;
import java.util.List;

public class main {
    public static void main(String[] args) {
        try {
            // Scanner scanner = new Scanner(System.in)
            DataGenerator data = new DataGenerator();
            data.generateTestData("cities.csv");
            // System.out.print("Enter file name: ");
            String fileName = "cities.csv";
            List<String> lines = FileReader.readLines(fileName);

            if (lines.isEmpty()) {
                System.out.println("File is empty!");
            } else {
                int iterations = 100;
                int cities = lines.size();
                int populationSize = lines.size();
                double crossoverProbability = Math.random();
                double mutationProbability = Math.random();

                TSPGeneticAlgorithm tspGeneticAlgorithm = new TSPGeneticAlgorithm(crossoverProbability, mutationProbability, lines);

                TSPSolver tspSolver = new TSPSolver(tspGeneticAlgorithm);
                tspSolver.solve(iterations, populationSize, cities);
            }

            // scanner.close();
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
}
