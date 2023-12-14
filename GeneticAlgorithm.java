import java.util.List;

public interface GeneticAlgorithm<T> {
    List<T> initializePopulation(int populationSize, int cities);

    int tourLength(List<Integer> tour);

    T selectParent(List<T> population, List<Integer> fitnessValues);

    List<T> crossover(T parent1, T parent2);

    T mutate(T individual);

    List<T> replacePopulation(List<T> currentPopulation, List<T> offspringPopulation);
}