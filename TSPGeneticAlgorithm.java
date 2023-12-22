import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/*
 * TSPGeneticAlgorithm is an implementation of the GeneticAlgorithm interface for solving the Traveling Salesman Problem (TSP).
 * It provides methods for initializing a population, calculating the tour length, selecting parents, performing crossover and mutation,
 * and replacing the current population with a new population of offspring.
 */
public class TSPGeneticAlgorithm implements GeneticAlgorithm<List<Integer>> {
    private double crossoverProbability;
    private double mutationProbability;
    private List<String> lines;

    /**
     * Constructor to initialize TSPGeneticAlgorithm with crossover and mutation probabilities, and distance matrix lines.
     *
     * @param crossoverProbability The probability of crossover occurring during reproduction.
     * @param mutationProbability The probability of mutation occurring during reproduction.
     * @param lines A List of strings representing the distance matrix between cities.
     */
    public TSPGeneticAlgorithm(double crossoverProbability, double mutationProbability, List<String> lines) {
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        // this.crossoverProbability = 0.8;
        // this.mutationProbability = 0.1;
        this.lines = lines;
    }

    //* */ The initializePopulation method is used to create an initial population of individuals in the genetic algorithm
    @Override
    public List<List<Integer>> initializePopulation(int populationSize, int cities) {
        // Create a new list to store the initial population
        List<List<Integer>> population = new ArrayList<>();

        // Loop to generate individuals for the population
        for (int i = 0; i < populationSize; i++) {
            // Ensure uniqueness of individuals in the population
            while (true) {
                // Generate a random path for an individual
                List<Integer> path = getRandomPath(cities);

                // Check if the population already contains this path
                if (!population.contains(path)) {
                    // Add the unique path to the population
                    population.add(path);
                    break; // Exit the loop for this individual
                }
            }
        }

        // Return the initialized population
        return population;
    }

    //* */ Return the generated random path, which represents a random permutation of integers from 1 to the number of cities
    private List<Integer> getRandomPath(int cities) {
        // Create a new list to store the path
        List<Integer> path = new ArrayList<>();
    
        // Populate the path with integers from 1 to the number of cities
        for (int i = 1; i <= cities; i++) {
            path.add(i);
        }
    
        // Shuffle the elements in the path to create a random permutation
        Collections.shuffle(path);
    
        // Return the random path
        return path;
    }
    
    //* */ Return the calculated total length of the tour. This represents the sum of distances between consecutive cities in the tour
    @Override
    public int tourLength(List<Integer> tour) {
        /*
         * Convert the lines of the distance matrix (read from the input file) into a 2D array (distanceMatrix). Each line represents distances from one city to all other cities.
         */ 
        int[][] distanceMatrix = lines.stream()
                .map(line -> Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray())
                .toArray(int[][]::new);

        /*
         *  For each city in the tour, add the distance from the current city to the next city to the totalLength. The distance is retrieved from the distanceMatrix.
         *  The modulo operation ((i + 1) % tour.size()) is used to handle the circular nature of the tour, ensuring that the last city is connected back to the first city.
         */
        int totalLength = 0;
        for (int i = 0; i < tour.size(); i++) {
            totalLength += distanceMatrix[tour.get(i) - 1][tour.get((i + 1) % tour.size()) - 1];
        }
        return totalLength;
    }


    @Override
    public List<Integer> selectParent(List<List<Integer>> population, List<Integer> fitnessValues) {
        // Calculate the total fitness of the population
        int totalFitness = fitnessValues.stream().mapToInt(Integer::intValue).sum();

        // Initialize a list to store cumulative probabilities for selection
        List<Double> cumulativeProbabilities = new ArrayList<>();
        double cumulativeProbability = 0.0;

        // Calculate cumulative probabilities for selection
        /*
         * Use a loop to calculate cumulative probabilities for each individual in the population.
         * The probability for each individual is its fitness divided by the total fitness
         */
        for (int i = 0; i < fitnessValues.size(); i++) {
            cumulativeProbability += (double) fitnessValues.get(i) / totalFitness;
            cumulativeProbabilities.add(cumulativeProbability);
        }

        // Generate a random value between 0 and 1
        double randomValue = Math.random();

        // Select a parent based on the random value and cumulative probabilities
        /*
         * Use the random value to select a parent based on cumulative probabilities.
         * The loop iterates through the cumulative probabilities until it finds the first one greater than the random value.
         * Print information (for debugging or analysis) about the selected parent's index and the cumulative probability.
         */
        for (int i = 0; i < cumulativeProbabilities.size(); i++) {
            if (randomValue <= cumulativeProbabilities.get(i)) {
                // Print information for debugging or analysis
                System.out.println("Selected parent index: " + i + ", Probability Distribution: " + cumulativeProbabilities.get(i));
                
                // Return the selected parent from the population
                return population.get(i);
            }
        }

        //* */ If no individual is selected based on the random value, return the last individual in the population as a fallback. This ensures that at least one individual is always selected
        return population.get(population.size() - 1);
    }

    //* */ Return the two child paths if crossover occurred, or return the parents if no crossover occurred
    @Override
    public List<List<Integer>> crossover(List<Integer> parent1, List<Integer> parent2) {
        //* */ Use the crossover probability to determine whether a crossover should occur. If the random value is less than or equal to the crossover probability, perform crossover; otherwise, return the parents without crossover
        if (Math.random() <= crossoverProbability) {
            // Randomly select a crossover point
            int crossoverPoint = new Random().nextInt(parent1.size() - 1) + 1;

            // Create two child paths using crossover
            List<Integer> child1 = new ArrayList<>(parent1.subList(0, crossoverPoint));
            List<Integer> child2 = new ArrayList<>(parent2.subList(0, crossoverPoint));

            List<List<Integer>> childRef = Arrays.asList(child1, child2);

            //* */ Identify the remaining cities for each child by filtering out the cities already present in the partial child paths
            List<Integer> remainingCitiesChild1 = parent2.stream()
                    .filter(city -> !childRef.get(0).contains(city))
                    .collect(Collectors.toList());
            List<Integer> remainingCitiesChild2 = parent1.stream()
                    .filter(city -> !childRef.get(1).contains(city))
                    .collect(Collectors.toList());

            //* */ Add the remaining cities to each child's path and use the fixDuplicates method to handle any duplicate cities
            child1.addAll(remainingCitiesChild1);
            child2.addAll(remainingCitiesChild2);

            childRef.set(0, fixDuplicates(child1, parent1));
            childRef.set(1, fixDuplicates(child2, parent2));

            return childRef;
        } else {
            // If no crossover, return the parents
            return Arrays.asList(parent1, parent2);
        }
    }

    //* */ After fixing duplicates, return the child path with the duplicates replaced by unused cities from the parent
    private List<Integer> fixDuplicates(List<Integer> child, List<Integer> parent) {
        // Fix any duplicates in the child path by replacing them with unused cities from the parent
        for (int i = 0; i < child.size(); i++) {
            // Check if the current city in the child path has duplicates
            /* 
             * Use Collections.frequency to check if the current city in the child path has duplicates
             * If duplicates are found, iterate through the parent path to find an unused city
             * If an unused city is found, replace the duplicate in the child path with the unused city
            */ 
            if (Collections.frequency(child, child.get(i)) > 1) {
                // Iterate through the parent path to find an unused city
                for (int city : parent) {
                    // If the city is not present in the child path, replace the duplicate with the unused city
                    if (!child.contains(city)) {
                        child.set(i, city);
                        break;
                    }
                }
            }
        }
        // Return the child path with fixed duplicates
        return child;
    }

    //* */ Return the mutated individual if mutation occurred, or return the original individual if no mutation occurred
    @Override
    public List<Integer> mutate(List<Integer> individual) {
        List<Integer> mutatedIndividual = new ArrayList<>(individual);
        // Check if mutation should occur based on the mutation probability
        if (Math.random() <= mutationProbability) {
            // Randomly select two mutation points
            int mutationPoint1 = new Random().nextInt(individual.size());
            int mutationPoint2;
            
            // Ensure mutationPoint2 is different from mutationPoint1
            do {
                mutationPoint2 = new Random().nextInt(individual.size());
            } while (mutationPoint1 == mutationPoint2);

            // Perform mutation by swapping the values at the two randomly selected mutation points in the individual
            // Collections.swap(individual, mutationPoint1, mutationPoint2);
            
            Collections.swap(mutatedIndividual, mutationPoint1, mutationPoint2);
        }
        // Return the mutated individual
        return mutatedIndividual;
    }

    /*
     * This method replaces the current population with a combination of individuals from both the current population and the offspring population. 
     * The replacement is based on the total tour lengths of the individuals, with the fittest individuals being selected to form the new population
     */
    @Override
    public List<List<Integer>> replacePopulation(List<List<Integer>> currentPopulation, List<List<Integer>> offspringPopulation) {
        //* */ Create a new list (combinedPopulation) by adding all individuals from the current population and offspring population. This step combines the solutions from both populations
        List<List<Integer>> combinedPopulation = new ArrayList<>(currentPopulation);
        combinedPopulation.addAll(offspringPopulation);

        // Sort the combined population based on tour lengths
        /*
         * Use Comparator.comparingInt to sort the combined population based on the total tour lengths of the individuals. The tourLength method is used as the key for comparison
         */
        combinedPopulation.sort(Comparator.comparingInt(this::tourLength));

        //* */ After sorting, select the top individuals from the combined population to replace the current population. The number of individuals selected is equal to the size of the current population
        return combinedPopulation.subList(0, currentPopulation.size());
    }
}
