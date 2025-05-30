package week1.hw2;

import java.util.List;
import java.util.ArrayList;

public class WordInfo{
    String word;
    int[] freq;
    int score;
    List<String> originalWords = new ArrayList<>();

    WordInfo(String word){
        this.word = word;
        this.freq = Main.makeLetterArray(word);
        this.score = ScoreChecker.calculateScore(word);
    }
}