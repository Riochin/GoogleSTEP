package javaSolver;

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
public class Solver2OptGreedy {
    
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
     * 2-opt改善を適用（局所距離計算版）
     * 交差するエッジを交換することで経路を改善
     * @param tour 初期経路
     * @param cities 都市座標の配列
     * @return 改善された経路
     */
    public static int[] improve2Opt(int[] tour, double[][] cities) {
        int N = tour.length;
        
        // 距離行列を事前計算
        double[][] dist = TspUtils.calculateDistanceMatrix(cities);
        
        boolean improved = true;
        int[] currentTour = tour.clone();
        int iteration = 0;
        int improvementCount = 0;
        
        System.err.println("Starting 2-opt improvement...");
        double currentDist = TspUtils.calculateTotalDistance(currentTour, cities);
        double initialDist = currentDist;
        System.err.printf("Initial distance: %.2f\n", initialDist);
        
        while (improved) {
            improved = false;
            iteration++;
            
            for (int i = 0; i < N - 1; i++) {
                for (int j = i + 2; j < N; j++) {
                    // 現在の2つの辺：i→i+1 と j→j+1 (j+1はN-1の場合は0)
                    int a1 = currentTour[i];
                    int a2 = currentTour[i + 1];
                    int b1 = currentTour[j];
                    int b2 = currentTour[(j + 1) % N];
                    
                    // 元々の距離：a1→a2 + b1→b2
                    double oldDistance = dist[a1][a2] + dist[b1][b2];
                    
                    // 交換後の距離：a1→b1 + a2→b2
                    double newDistance = dist[a1][b1] + dist[a2][b2];
                    
                    // 改善があるかチェック（局所計算のみ）
                    if (newDistance < oldDistance) {
                        // 2-opt交換を実行
                        currentTour = TspUtils.twoOptSwap(currentTour, i, j);
                        
                        // 全体距離を局所的に更新
                        currentDist = currentDist - oldDistance + newDistance;
                        
                        improved = true;
                        improvementCount++;
                        OutputHelper.printImprovementInfo(iteration, currentDist, improvementCount);
                        break;
                    }
                }
                if (improved) break;
            }
            if (!improved) {
                System.err.printf("Iteration %d: No improvement found.\n", iteration);
            }
        }
        
        double finalDist = currentDist; // 既に正確に計算済み
        OutputHelper.printCompletionInfo(finalDist, initialDist, improvementCount);
        return currentTour;
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
        
        // 2. 2-optで改善
        currentPhase = 2;
        OutputHelper.printProgressBar("Overall: ", currentPhase, totalPhases, 30);
        System.err.println("Phase 2: Improving tour with 2-opt...");
        startTime = System.currentTimeMillis();
        int[] optimizedTour = improve2Opt(greedyTour, cities);
        long optimizationTime = System.currentTimeMillis() - startTime;
        OutputHelper.printSolutionInfo("2-opt", TspUtils.calculateTotalDistance(optimizedTour, cities), optimizationTime);
        
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