package javaSolver;

/**
 * TSPソルバーの出力・表示関連のヘルパークラス
 * 進行バー表示や結果出力などの機能を提供
 */
public class OutputHelper {
    
    /**
     * 進行バーを表示する
     * @param prefix 進行バーの前に表示するプレフィックス文字列
     * @param current 現在の進行値
     * @param total 全体の値
     * @param barLength 進行バーの文字数
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
    
    /**
     * TSPの経路を標準出力に出力する
     * @param tour 都市の訪問順序を表す配列
     */
    public static void printTour(int[] tour) {
        System.out.print("index");
        for (int city : tour) {
            System.out.print("\n" + city);
        }
        System.out.println();
    }
    
    /**
     * アルゴリズムの実行結果情報を表示する
     * @param algorithmName アルゴリズム名
     * @param distance 経路の総距離
     * @param executionTime 実行時間（ミリ秒）
     */
    public static void printSolutionInfo(String algorithmName, double distance, long executionTime) {
        System.err.printf("%s: Distance=%.2f, Time=%dms\n", algorithmName, distance, executionTime);
    }
    
    /**
     * 改善情報を表示する
     * @param iteration 反復回数
     * @param distance 現在の距離
     * @param improvementCount 改善回数
     */
    public static void printImprovementInfo(int iteration, double distance, int improvementCount) {
        System.err.printf("Iteration %d: Improved! Distance: %.2f (改善数: %d)\n", 
                         iteration, distance, improvementCount);
    }
    
    /**
     * 改善完了情報を表示する
     * @param finalDistance 最終距離
     * @param initialDistance 初期距離
     * @param improvementCount 総改善回数
     */
    public static void printCompletionInfo(double finalDistance, double initialDistance, int improvementCount) {
        double improvementPercentage = (initialDistance - finalDistance) / initialDistance * 100;
        System.err.printf("2-opt completed! Final distance: %.2f (%.1f%% improvement, %d improvements)\n", 
                         finalDistance, improvementPercentage, improvementCount);
    }
} 