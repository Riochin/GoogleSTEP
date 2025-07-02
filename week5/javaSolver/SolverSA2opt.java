package javaSolver;

import java.util.Arrays;
import java.util.Random;

// 2-opt greedy solver 2025/07/02
// 第１弾！！！！！！
// 3,418.10,	3,832.29,	5,232.96,	9,227.71,	11,511.53,	21,513.95,	48,645.15

// 第二弾
// 3,418.10,	3,832.29,	4,994.89,	9,195.78,	11,338.42,	21,039.95,	41,818.12

/**
 * Greedy + 2-opt アルゴリズムによるTSPソルバー
 * 1. Greedyアルゴリズムで初期解を構築
 * 2. 2-opt法で局所改善を行う
 */
public class SolverSA2opt {

    public static int[] SASolve(double[][] cities, int[] initTour ){
        double coolRate = 0.00001;
        Random rand = new Random();
        
        int[] currentTour = Arrays.copyOf(initTour, initTour.length);
        int[] bestTour = Arrays.copyOf(initTour, initTour.length);
        double temp = 4; // ‼️ ここ温度初期値！

        int iteration = 0;
        
        double initialDist = TspUtils.calculateTotalDistance(initTour, cities);

        while (temp > 0.2){
            boolean improved = false;
            double r = rand.nextDouble();
            int[] tour = chooseRandomSolve(currentTour, cities); // ‼️todo: 近傍からランダムに選んだかい
            double delta = TspUtils.calculateTotalDistance(tour, cities) - TspUtils.calculateTotalDistance(currentTour, cities);
            if (delta < 0){
                currentTour = Arrays.copyOf(tour, tour.length);
                if (TspUtils.calculateTotalDistance(tour, cities) < TspUtils.calculateTotalDistance(bestTour, cities)){
                    bestTour = Arrays.copyOf(tour, tour.length);
                }
                improved = true;
            } else if ( r <= Math.exp(-delta/temp)){
                currentTour = Arrays.copyOf(tour, tour.length);
                improved = true;
            }
            double currentDist = TspUtils.calculateTotalDistance(currentTour, cities);
            if(improved){
                OutputHelper.printImprovementInfo(iteration, currentDist, 0);
            }

            temp = temp - coolRate;
            iteration ++;

            if (!improved) {
                System.err.printf("Iteration %d: No improvement found.\n", iteration);
            }
        }
        double finalDist = TspUtils.calculateTotalDistance(bestTour, cities);
        OutputHelper.printCompletionInfo(finalDist, initialDist, 0);

        return bestTour;
    }

    public static int[] chooseRandomSolve(int[] tour, double[][] cities){
        Random rand = new Random();
        int i = rand.nextInt(tour.length-1);
        int j = rand.nextInt(tour.length-1);
        
        if(!(i <= j+2)){
            return tour;
        }

        int[] chosenTour = Arrays.copyOf(tour, tour.length);
        chosenTour = TspUtils.twoOptSwap(chosenTour, i, j);

        return chosenTour;
    }
    
    /**
     * Greedyアルゴリズムで初期解を構築
     * 各ステップで現在地から最も近い未訪問都市を選択
     * @param cities 都市座標の配列
     * @return 初期経路
     */
    public static int[] greedySolve(double[][] cities) {
        int N = cities.length;
        
        // 距離行列を事前計算
        double[][] dist = TspUtils.calculateDistanceMatrix(cities);
        
        // Greedy構築
        int currentCity = 0;
        boolean[] visited = new boolean[N];
        visited[0] = true;
        int[] tour = new int[N];
        tour[0] = currentCity;
        
        System.err.println("Building greedy tour:");
        int lastDisplayedPercent = -1;
        
        for (int tourIndex = 1; tourIndex < N; tourIndex++) {
            int nextCity = -1;
            double minDist = Double.MAX_VALUE;
            for (int i = 0; i < N; i++) {
                if (!visited[i] && dist[currentCity][i] < minDist) {
                    nextCity = i;
                    minDist = dist[currentCity][i];
                }
            }
            visited[nextCity] = true;
            tour[tourIndex] = nextCity;
            currentCity = nextCity;
            
            // 進行率を計算（0-100%）
            int currentPercent = (int) ((double) tourIndex / (N-1) * 100);
            
            // 5%刻みまたは完了時に進行バーを表示
            if (currentPercent != lastDisplayedPercent && (currentPercent % 5 == 0 || tourIndex == N-1)) {
                OutputHelper.printProgressBar("Greedy: ", tourIndex, N-1, 25);
                lastDisplayedPercent = currentPercent;
            }
        }
        
        // 最終的な100%表示を確実に行う
        if (lastDisplayedPercent != 100) {
            OutputHelper.printProgressBar("Greedy: ", N-1, N-1, 25);
        }
        return tour;
    }
    
    
    /**
     * メインソルバー：Greedy + 2-opt
     * @param cities 都市座標の配列
     * @return 最適化された経路
     */
    public static int[] solve(double[][] cities) {
        System.err.println("=== TSP Solver: Greedy + 2-opt ===");
        System.err.println("Cities: " + cities.length);
        
        // 全体の進行状況を管理
        int totalPhases = 2;
        int currentPhase = 0;
        
        // 1. Greedyで初期解を構築
        currentPhase = 1;
        OutputHelper.printProgressBar("Overall: ", currentPhase, totalPhases, 30);
        System.err.println("Phase 1: Building initial tour with Greedy algorithm...");
        long startTime = System.currentTimeMillis();
        int[] greedyTour = greedySolve(cities);
        long greedyTime = System.currentTimeMillis() - startTime;
        OutputHelper.printSolutionInfo("Greedy", TspUtils.calculateTotalDistance(greedyTour, cities), greedyTime);
        
        int[] optimizedTour1 = Solver2OptGreedy.improve2Opt(greedyTour, cities);
        
        int[] optimizedTour = SASolve(cities, optimizedTour1);
        long optimizationTime = System.currentTimeMillis() - startTime;

           // 完了
        OutputHelper.printProgressBar("Overall: ", totalPhases, totalPhases, 30);
        System.err.println("=== Total processing time: " + (greedyTime + optimizationTime) + "ms ===");
        return optimizedTour;
    }
    
    /**
     * メイン関数
     * @param args コマンドライン引数（入力ファイル名）
     */
    public static void main(String[] args) {
        System.err.println("DEBUG: Program started");
        if (args.length < 1) {
            System.err.println("Usage: java Solver2OptGreedy <input_file>");
            return;
        }
        System.err.println("DEBUG: Input file: " + args[0]);
        
        double[][] cities = TspUtils.readInput(args[0]);
        if (cities == null) {
            System.err.println("Failed to read input file");
            return;
        }
        
        int[] tour = solve(cities);
        OutputHelper.printTour(tour);
    }
} 