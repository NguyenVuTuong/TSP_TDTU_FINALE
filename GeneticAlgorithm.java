import java.util.List;

/**
 * GeneticAlgorithm is an interface representing the basic operations of a genetic algorithm.
 *
 * @param <T> The type of individuals in the population.
 */
public interface GeneticAlgorithm<T> {
    /**
     * Initializes a population of individuals for the genetic algorithm.
     *
     * @param populationSize The size of the population.
     * @param cities The number of cities in the problem (specific to TSP).
     * @return A List of initialized individuals representing the population.
     */
    List<T> initializePopulation(int populationSize, int cities);

    /**
     * Calculates the length of a tour represented by a list of indices.
     *
     * @param tour The list of indices representing the tour.
     * @return The total length of the tour.
     */
    int tourLength(List<Integer> tour);

    /**
     * Selects a parent from the population based on their fitness values.
     *
     * @param population The current population.
     * @param fitnessValues The fitness values of individuals in the population.
     * @return The selected parent.
     */
    T selectParent(List<T> population, List<Integer> fitnessValues);

    /**
     * Performs crossover operation to create offspring from two parents.
     *
     * @param parent1 The first parent.
     * @param parent2 The second parent.
     * @return A List containing the offspring generated from crossover.
     */
    List<T> crossover(T parent1, T parent2);

    /**
     * Performs mutation operation on an individual.
     *
     * @param individual The individual to be mutated.
     * @return The mutated individual.
     */
    T mutate(T individual);

    /**
     * Replaces the current population with a new population of offspring.
     *
     * @param currentPopulation The current population.
     * @param offspringPopulation The population of offspring generated.
     * @return A List representing the new population.
     */
    List<T> replacePopulation(List<T> currentPopulation, List<T> offspringPopulation);
}
