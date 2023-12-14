import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TSPSolver {
    private final TSPGeneticAlgorithm tspGeneticAlgorithm;

    public TSPSolver(TSPGeneticAlgorithm tspGeneticAlgorithm) {
        this.tspGeneticAlgorithm = tspGeneticAlgorithm;
    }

    public void solve(int iterations, int populationSize, int cities) {
        List<List<Integer>> population = tspGeneticAlgorithm.initializePopulation(populationSize, cities);

        long startTime = System.currentTimeMillis();

        for (int iteration = 0; iteration < iterations; iteration++) {
            System.out.println("Iteration " + (iteration + 1) + " - Population:");
            for (int i = 0; i < population.size(); i++) {
                System.out.println(" Individual " + (i + 1) + ": " + population.get(i) +
                        ", Length: " + tspGeneticAlgorithm.tourLength(population.get(i)));
            }

            List<Integer> fitnessValues = population.stream()
                    .map(tspGeneticAlgorithm::tourLength)
                    .collect(Collectors.toList());

            List<List<Integer>> parents = new ArrayList<>();
            for (int j = 0; j < populationSize; j++) {
                parents.add(tspGeneticAlgorithm.selectParent(population, fitnessValues));
            }

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

            population = tspGeneticAlgorithm.replacePopulation(population, offspringPopulation);

            List<Integer> bestTour = Collections.min(population, Comparator.comparingInt(tspGeneticAlgorithm::tourLength));
            int bestLength = tspGeneticAlgorithm.tourLength(bestTour);
            System.out.println("Best tour: " + bestTour + ", Length: " + bestLength + "\n");
        }

        List<Integer> finalBestTour = Collections.min(population, Comparator.comparingInt(tspGeneticAlgorithm::tourLength));
        int finalBestLength = tspGeneticAlgorithm.tourLength(finalBestTour);
        System.out.println("Final Best Tour: " + finalBestTour + ", Length: " + finalBestLength);

        long endTime = System.currentTimeMillis();
        double executionTime = (endTime - startTime) / 1000.0;
        System.out.println("Runtime: " + executionTime + " seconds");
    }
}
