package javaSolver;

import java.io.*;
import java.util.*;

/**
 * TSP（巡回セールスマン問題）の汎用ユーティリティクラス
 * 複数のアルゴリズムで共通して使用される関数を提供
 */
public class TspUtils {
    
    /**
     * 2つの都市間のユークリッド距離を計算
     * @param city1 都市1の座標 [x, y]
     * @param city2 都市2の座標 [x, y]
     * @return 2都市間の距離
     */
    public static double distance(double[] city1, double[] city2) {
        double dx = city1[0] - city2[0];
        double dy = city1[1] - city2[1];
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * 経路の総距離を計算（閉路として計算）
     * @param tour 都市の訪問順序
     * @param cities 都市座標の配列
     * @return 経路の総距離
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
    
    /**
     * 2つのエッジを交換して経路を改善する2-opt操作
     * 経路の一部を逆順にすることで、交差する経路を解消する
     * @param tour 元の経路
     * @param i 交換開始インデックス
     * @param j 交換終了インデックス
     * @return 2-opt交換後の新しい経路
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
    
    /**
     * CSVファイルから都市座標を読み込み
     * @param filename 入力ファイル名
     * @return 都市座標の2次元配列、読み込み失敗時はnull
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
                    OutputHelper.printProgressBar("Loading: ", lineCount, totalLines, 25);
                    lastDisplayedPercent = currentPercent;
                }
            }
            
            // 最終的な100%表示を確実に行う
            if (lastDisplayedPercent != 100) {
                OutputHelper.printProgressBar("Loading: ", totalLines, totalLines, 25);
            }
            System.err.println("Cities loaded successfully!");
            
            return cityList.toArray(new double[cityList.size()][]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 距離行列を事前計算する
     * @param cities 都市座標の配列
     * @return 都市間距離の2次元配列
     */
    public static double[][] calculateDistanceMatrix(double[][] cities) {
        int N = cities.length;
        double[][] dist = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = i; j < N; j++) {
                dist[i][j] = dist[j][i] = distance(cities[i], cities[j]);
            }
        }
        return dist;
    }
} 