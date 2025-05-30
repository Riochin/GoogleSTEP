package week1.hw2;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class Test {
    public static void main(String[] args) throws FileNotFoundException {
        System.setOut(new java.io.PrintStream("output/large_answer.txt")); // 出力をanswer.txtにリダイレクト
        Scanner sc = new Scanner(new File("input/words.txt")); // ファイル名を書き換える
        List<WordInfo> dict = Main.makeDict(sc);
        sc.close();
        
        // 入力ファイル名をコマンドライン引数から取得（なければ input.txt）
        String inputFile = args.length > 0 ? args[0] : "input/large.txt";
        Scanner inputScanner = new Scanner(new File(inputFile));
        while (inputScanner.hasNextLine()) {
            String line = inputScanner.nextLine().trim();
            if (line.isEmpty()) continue;
            // System.out.println("=== Test " + testNum + " ===");
            Main.solution(line, dict);
        }
        inputScanner.close();
    }
}