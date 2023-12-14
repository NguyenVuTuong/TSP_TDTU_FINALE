import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BruteForce {
    private List<String> lines;
    private List<Integer> bestTour;
    private int bestLength;

    public BruteForce(List<String> lines) {
        this.lines = lines;
        this.bestTour = new ArrayList<>();
        this.bestLength = Integer.MAX_VALUE;
    }

    public List<Integer> solve() {
        long startTime = System.currentTimeMillis();
        int numCities = lines.size();
        List<Integer> tour = new ArrayList<>();
        for (int i = 1; i <= numCities; i++) {
            tour.add(i);
        }

        while (nextPermutation(tour)) {
            int currentLength = calculateTourLength(tour);
            if (currentLength < bestLength) {
                bestTour = new ArrayList<>(tour);
                bestLength = currentLength;
            }
        }

        long endTime = System.currentTimeMillis();
        double executionTime = (endTime - startTime) / 1000.0;
        System.out.println("\nBrute Force Runtime: " + executionTime + " seconds");
        // try (BufferedWriter writer = new BufferedWriter(new FileWriter("runtime.csv", true))) {
        //     writer.write(" "+String.valueOf(executionTime));
        // } catch (IOException e) {
        //     // Handle IO errors (e.g., file not found, permission issues)
        //     e.printStackTrace();
        // }
        return bestTour;
    }

    public void printTourAndLength() {
        System.out.println("Final Best Tour: " + bestTour);
        System.out.println("Length of Best Tour: " + bestLength);
    }

    private boolean nextPermutation(List<Integer> array) {
        int i = array.size() - 1;
        while (i > 0 && array.get(i - 1) >= array.get(i)) {
            i--;
        }

        if (i <= 0) {
            return false;
        }

        int j = array.size() - 1;
        while (array.get(j) <= array.get(i - 1)) {
            j--;
        }

        // Swap the elements at indices i-1 and j
        Collections.swap(array, i - 1, j);

        // Reverse the suffix
        j = array.size() - 1;
        while (i < j) {
            Collections.swap(array, i, j);
            i++;
            j--;
        }

        return true;
    }

    private int calculateTourLength(List<Integer> tour) {
        int[][] distanceMatrix = lines.stream()
                .map(line -> line.split(","))
                .map(arr -> {
                    int[] row = new int[arr.length];
                    for (int i = 0; i < arr.length; i++) {
                        row[i] = Integer.parseInt(arr[i]);
                    }
                    return row;
                })
                .toArray(int[][]::new);

        int totalLength = 0;
        for (int i = 0; i < tour.size(); i++) {
            totalLength += distanceMatrix[tour.get(i) - 1][tour.get((i + 1) % tour.size()) - 1];
        }
        return totalLength;
    }
}
