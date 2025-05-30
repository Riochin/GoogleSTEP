package week1.hw2;

public class ScoreChecker {
    // スコア計算用配列（score_checker.pyと同じ）
    private static final int[] SCORES = {1, 3, 2, 2, 1, 3, 3, 1, 1, 4, 4, 2, 2, 1, 1, 3, 4, 1, 1, 1, 2, 3, 3, 4, 3, 4};

    // 単語のスコアを計算
    public static int calculateScore(String word) {
        int score = 0;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if ('a' <= c && c <= 'z') {
                score += SCORES[c - 'a'];
            }
        }
        return score;
    }
}
