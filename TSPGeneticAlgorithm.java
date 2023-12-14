import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TSPGeneticAlgorithm implements GeneticAlgorithm<List<Integer>> {
    private double crossoverProbability;
    private double mutationProbability;
    private List<String> lines;

    public TSPGeneticAlgorithm(double crossoverProbability, double mutationProbability, List<String> lines) {
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        this.lines = lines;
    }

    @Override
    public List<List<Integer>> initializePopulation(int populationSize, int cities) {
        List<List<Integer>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            while (true) {
                List<Integer> path = getRandomPath(cities);
                if (!population.contains(path)) {
                    population.add(path);
                    break;
                }
            }
        }
        return population;
    }

    private List<Integer> getRandomPath(int cities) {
        List<Integer> path = new ArrayList<>();
        for (int i = 1; i <= cities; i++) {
            path.add(i);
        }
        Collections.shuffle(path);
        return path;
    }

    @Override
    public int tourLength(List<Integer> tour) {
        int[][] distanceMatrix = lines.stream()
                .map(line -> Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray())
                .toArray(int[][]::new);

        int totalLength = 0;
        for (int i = 0; i < tour.size(); i++) {
            totalLength += distanceMatrix[tour.get(i) - 1][tour.get((i + 1) % tour.size()) - 1];
        }
        return totalLength;
    }

    @Override
    public List<Integer> selectParent(List<List<Integer>> population, List<Integer> fitnessValues) {
        int totalFitness = fitnessValues.stream().mapToInt(Integer::intValue).sum();
        List<Double> cumulativeProbabilities = new ArrayList<>();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < fitnessValues.size(); i++) {
            cumulativeProbability += (double) fitnessValues.get(i) / totalFitness;
            cumulativeProbabilities.add(cumulativeProbability);
        }

        double randomValue = Math.random();
        for (int i = 0; i < cumulativeProbabilities.size(); i++) {
            if (randomValue <= cumulativeProbabilities.get(i)) {
                System.out.println("Selected parent index: " + i + ", Probability Distribution: " + cumulativeProbabilities.get(i));
                return population.get(i);
            }
        }
        return population.get(population.size() - 1);
    }

    @Override
    public List<List<Integer>> crossover(List<Integer> parent1, List<Integer> parent2) {
        if (Math.random() <= crossoverProbability) {
            int crossoverPoint = new Random().nextInt(parent1.size() - 1) + 1;
            List<Integer> child1 = new ArrayList<>(parent1.subList(0, crossoverPoint));
            List<Integer> child2 = new ArrayList<>(parent2.subList(0, crossoverPoint));

            List<List<Integer>> childRef = Arrays.asList(child1, child2);

            List<Integer> remainingCitiesChild1 = parent2.stream()
                    .filter(city -> !childRef.get(0).contains(city))
                    .collect(Collectors.toList());
            List<Integer> remainingCitiesChild2 = parent1.stream()
                    .filter(city -> !childRef.get(1).contains(city))
                    .collect(Collectors.toList());

            child1.addAll(remainingCitiesChild1);
            child2.addAll(remainingCitiesChild2);

            childRef.set(0, fixDuplicates(child1, parent1));
            childRef.set(1, fixDuplicates(child2, parent2));

            return childRef;
        } else {
            return Arrays.asList(parent1, parent2);
        }
    }

    private List<Integer> fixDuplicates(List<Integer> child, List<Integer> parent) {
        for (int i = 0; i < child.size(); i++) {
            if (Collections.frequency(child, child.get(i)) > 1) {
                for (int city : parent) {
                    if (!child.contains(city)) {
                        child.set(i, city);
                        break;
                    }
                }
            }
        }
        return child;
    }

    @Override
    public List<Integer> mutate(List<Integer> individual) {
        if (Math.random() <= mutationProbability) {
            int mutationPoint1 = new Random().nextInt(individual.size());
            int mutationPoint2 = new Random().nextInt(individual.size());

            Collections.swap(individual, mutationPoint1, mutationPoint2);
        }
        return individual;
    }

    @Override
    public List<List<Integer>> replacePopulation(List<List<Integer>> currentPopulation, List<List<Integer>> offspringPopulation) {
        List<List<Integer>> combinedPopulation = new ArrayList<>(currentPopulation);
        combinedPopulation.addAll(offspringPopulation);
        combinedPopulation.sort(Comparator.comparingInt(this::tourLength));
        return combinedPopulation.subList(0, currentPopulation.size());
    }
}
