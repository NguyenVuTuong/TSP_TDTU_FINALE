import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TSPSolver {
    private final TSPGeneticAlgorithm tspGeneticAlgorithm;

    // Constructor to initialize the TSPGeneticAlgorithm instance
    public TSPSolver(TSPGeneticAlgorithm tspGeneticAlgorithm) {
        this.tspGeneticAlgorithm = tspGeneticAlgorithm;
    }

    // Method to solve the TSP using Genetic Algorithm
    public void solve(int iterations, int populationSize, int cities) {
        // Initialize the population with random paths
        List<List<Integer>> population = tspGeneticAlgorithm.initializePopulation(populationSize, cities);
        List<Integer> finalBestTour = new ArrayList<>();
        int finalBestLength = Integer.MAX_VALUE;

        // Record the start time for runtime measurement
        long startTime = System.currentTimeMillis();

        // Perform iterations of the Genetic Algorithm
        for (int iteration = 0; iteration < iterations; iteration++) {
            System.out.println("Iteration " + (iteration + 1) + " - Population:");
            
            // Display details of each individual in the current population
            for (int i = 0; i < population.size(); i++) {
                System.out.println(" Individual " + (i + 1) + ": " + population.get(i) +
                        ", Length: " + tspGeneticAlgorithm.tourLength(population.get(i)));
            }

            // Calculate fitness values for each individual in the population
            List<Integer> fitnessValues = population.stream()
                    .map(tspGeneticAlgorithm::tourLength)
                    .collect(Collectors.toList());

            // Select parents for crossover based on fitness values
            List<List<Integer>> parents = new ArrayList<>();
            for (int j = 0; j < populationSize; j++) {
                parents.add(tspGeneticAlgorithm.selectParent(population, fitnessValues));
            }

            // Generate offspring through crossover and mutation
            List<List<Integer>> offspringPopulation = new ArrayList<>();
            for (int i = 0; i < populationSize - 1; i += 2) {
                List<Integer> parent1 = parents.get(i);
                List<Integer> parent2 = parents.get(i + 1);

                List<List<Integer>> child = tspGeneticAlgorithm.crossover(parent1, parent2);
                List<Integer> child1 = child.get(0);
                List<Integer> child2 = child.get(1);

                child1 = tspGeneticAlgorithm.mutate(child1);
                child2 = tspGeneticAlgorithm.mutate(child2);

                offspringPopulation.addAll(Arrays.asList(child1, child2));
            }

            // Replace the current population with the offspring population
            population = tspGeneticAlgorithm.replacePopulation(population, offspringPopulation);

            // Find the best tour and its length in the current population
            List<Integer> bestTour = Collections.min(population, Comparator.comparingInt(tspGeneticAlgorithm::tourLength));
            int bestLength = tspGeneticAlgorithm.tourLength(bestTour);

            // Display the best tour and its length in the current population
            System.out.println("Best tour: " + bestTour + ", Length: " + bestLength + "\n");

            // Find the final best tour and its length
            if(bestLength < finalBestLength) {
                finalBestLength = bestLength;
                finalBestTour = bestTour;
            }            
        }
        
        // Display the final best tour and its length in the last population
        System.out.println("Final Best Tour: " + finalBestTour + ", Length: " + finalBestLength);

        // Record the end time for runtime measurement
        long endTime = System.currentTimeMillis();
        double executionTime = (endTime - startTime) / 1000.0;
        System.out.println("Runtime: " + executionTime + " seconds");

        // try (BufferedWriter writer = new BufferedWriter(new FileWriter("runtime.csv"))) {
        //     writer.write(String.valueOf(executionTime));
        // } catch (IOException e) {
        //     // Handle IO errors (e.g., file not found, permission issues)
        //     e.printStackTrace();
        // }
    }
}
