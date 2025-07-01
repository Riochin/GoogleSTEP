package javaSolver;

// 2-opt greedy solver 2025/07/02
// 第１弾！！！！！！
// 3,418.10,	3,832.29,	5,232.96,	9,227.71,	11,511.53,	21,513.95,	48,645.15

import java.io.*;
import java.util.*;

public class Solver2OptGreedy {
    
    /*
     2つの都市間のユークリッド距離を計算
     */
    public static double distance(double[] city1, double[] city2) {
        double dx = city1[0] - city2[0];
        double dy = city1[1] - city2[1];
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /*
     Greedyアルゴリズムで初期解を構築
     */
    public static int[] greedySolve(double[][] cities) {
        int N = cities.length;
        
        // 距離行列を事前計算
        double[][] dist = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = i; j < N; j++) {
                dist[i][j] = dist[j][i] = distance(cities[i], cities[j]);
            }
        }
        
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
                printProgressBar("Greedy: ", tourIndex, N-1, 25);
                lastDisplayedPercent = currentPercent;
            }
        }
        
        // 最終的な100%表示を確実に行う
        if (lastDisplayedPercent != 100) {
            printProgressBar("Greedy: ", N-1, N-1, 25);
        }
        return tour;
    }
    
    /*
     2つのエッジを交換して経路を改善する2-opt操作
     */
    public static int[] twoOptSwap(int[] tour, int i, int j) {
        int[] newTour = new int[tour.length];
        
        // 0からiまでコピー
        for (int k = 0; k <= i; k++) {
            newTour[k] = tour[k];
        }
        
        // i+1からjまでを逆順でコピー
        for (int k = i + 1; k <= j; k++) {
            newTour[k] = tour[j - (k - i - 1)];
        }
        
        // j+1から最後までコピー
        for (int k = j + 1; k < tour.length; k++) {
            newTour[k] = tour[k];
        }
        
        return newTour;
    }
    
    /*
     進行バーを表示
     */
    public static void printProgressBar(String prefix, int current, int total, int barLength) {
        double percentage = (double) current / total * 100;
        int completed = (int) (percentage / 100 * barLength);
        int remaining = barLength - completed;
        
        StringBuilder bar = new StringBuilder();
        bar.append(prefix);
        for (int i = 0; i < completed; i++) {
            bar.append("=");
        }
        for (int i = 0; i < remaining; i++) {
            bar.append("-");
        }
        bar.append(String.format(" %.0f%%", percentage));
        
        System.err.print("\r" + bar.toString());
        if (current == total) {
            System.err.println(); // 完了時に改行
        }
    }

    /*
     経路の総距離を計算（閉路）
     */
    public static double calculateTotalDistance(int[] tour, double[][] cities) {
        double totalDist = 0;
        int N = tour.length;
        for (int i = 0; i < N; i++) {
            int from = tour[i];
            int to = tour[(i + 1) % N];
            totalDist += distance(cities[from], cities[to]);
        }
        return totalDist;
    }
    
    /*
     2-opt改善を適用（局所距離計算版）
     */
    public static int[] improve2Opt(int[] tour, double[][] cities) {
        int N = tour.length;
        
        // 距離行列を事前計算
        double[][] dist = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = i; j < N; j++) {
                dist[i][j] = dist[j][i] = distance(cities[i], cities[j]);
            }
        }
        
        boolean improved = true;
        int[] currentTour = tour.clone();
        int iteration = 0;
        int improvementCount = 0;
        
        System.err.println("Starting 2-opt improvement...");
        double currentDist = calculateTotalDistance(currentTour, cities);
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
                        currentTour = twoOptSwap(currentTour, i, j);
                        
                        // 全体距離を局所的に更新
                        currentDist = currentDist - oldDistance + newDistance;
                        
                        improved = true;
                        improvementCount++;
                        System.err.printf("Iteration %d: Improved! Distance: %.2f (改善数: %d)\n", 
                                        iteration, currentDist, improvementCount);
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
        System.err.printf("2-opt completed! Final distance: %.2f (%.1f%% improvement, %d improvements)\n", 
                         finalDist, (initialDist - finalDist) / initialDist * 100, improvementCount);
        return currentTour;
    }
    
    /*
     メインソルバー：Greedy + 2-opt
     */
    public static int[] solve(double[][] cities) {
        System.err.println("=== TSP Solver: Greedy + 2-opt ===");
        System.err.println("Cities: " + cities.length);
        
        // 全体の進行状況を管理
        int totalPhases = 2;
        int currentPhase = 0;
        
        // 1. Greedyで初期解を構築
        currentPhase = 1;
        printProgressBar("Overall: ", currentPhase, totalPhases, 30);
        System.err.println("Phase 1: Building initial tour with Greedy algorithm...");
        long startTime = System.currentTimeMillis();
        int[] greedyTour = greedySolve(cities);
        long greedyTime = System.currentTimeMillis() - startTime;
        System.err.println("Greedy completed in " + greedyTime + "ms");
        
        // 2. 2-optで改善
        currentPhase = 2;
        printProgressBar("Overall: ", currentPhase, totalPhases, 30);
        System.err.println("Phase 2: Improving tour with 2-opt...");
        startTime = System.currentTimeMillis();
        int[] optimizedTour = improve2Opt(greedyTour, cities);
        long optimizationTime = System.currentTimeMillis() - startTime;
        System.err.println("2-opt completed in " + optimizationTime + "ms");
        
        // 完了
        printProgressBar("Overall: ", totalPhases, totalPhases, 30);
        System.err.println("=== Total processing time: " + (greedyTime + optimizationTime) + "ms ===");
        return optimizedTour;
    }
    
    /*
     CSVファイルから都市座標を読み込み
     */
    public static double[][] readInput(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine(); // ヘッダー行をスキップ
            
            List<double[]> cityList = new ArrayList<>();
            int lineCount = 0;
            
            // まず全体の行数を数える（プログレスバーのため）
            List<String> lines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
            int totalLines = lines.size();
            
            System.err.println("Reading CSV (" + totalLines + " cities):");
            int lastDisplayedPercent = -1;
            
            for (int i = 0; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                cityList.add(new double[]{x, y});
                lineCount++;
                
                // 進行率を計算（0-100%）
                int currentPercent = (int) ((double) lineCount / totalLines * 100);
                
                // 5%刻みまたは完了時に進行バーを表示
                if (currentPercent != lastDisplayedPercent && (currentPercent % 5 == 0 || lineCount == totalLines)) {
                    printProgressBar("Loading: ", lineCount, totalLines, 25);
                    lastDisplayedPercent = currentPercent;
                }
            }
            
            // 最終的な100%表示を確実に行う
            if (lastDisplayedPercent != 100) {
                printProgressBar("Loading: ", totalLines, totalLines, 25);
            }
            System.err.println("Cities loaded successfully!");
            
            return cityList.toArray(new double[cityList.size()][]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /*
     経路を出力
     */
    public static void printTour(int[] tour) {
        System.out.print("index");
        for (int city : tour) {
            System.out.print("\n" + city);
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        System.err.println("DEBUG: Program started");
        if (args.length < 1) {
            System.err.println("Usage: java Solver2OptGreedy <input_file>");
            return;
        }
        System.err.println("DEBUG: Input file: " + args[0]);
        
        double[][] cities = readInput(args[0]);
        if (cities == null) {
            System.err.println("Failed to read input file");
            return;
        }
        
        int[] tour = solve(cities);
        printTour(tour);
    }
} 