package javaSolver;
import java.util.Arrays;

public class solverGreedy {
    public static int distance(int[] city1, int[] city2) {
        return (int) Math.sqrt(Math.pow((city1[0] - city2[0]), 2) 
                + Math.pow((city1[1] - city2[1]), 2));
    }

    public static int[] solve(int[][] cities) {
        int N = cities.length;
        int[][] dist = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = i; j < N; j++) {
                dist[i][j] = dist[j][i] = distance(cities[i], cities[j]);
            }
        }
        int currentCity = 0;
        boolean[] visited = new boolean[N];
        visited[0] = true;
        int[] tour = new int[N];
        tour[0] = currentCity;
        
        for (int tourIndex = 1; tourIndex < N; tourIndex++) {
            int nextCity = -1;
            int minDist = Integer.MAX_VALUE;
            for (int i = 0; i < N; i++) {
                if (!visited[i] && dist[currentCity][i] < minDist) {
                    nextCity = i;
                    minDist = dist[currentCity][i];
                }
            }
            visited[nextCity] = true;
            tour[tourIndex] = nextCity;
            currentCity = nextCity;
        }
        return tour;
    }

    public static int[][] readInput(String filename) {
        return null;
    }

    public static void printTour(int[] tour) {
        System.out.println(Arrays.toString(tour));
    }

    public static void main(String[] args) {
        assert args.length > 1;
        int[] tour = solve(readInput(args[1]));
        printTour(tour);
    }
}
