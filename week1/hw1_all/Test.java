package week1.hw1_all;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) throws FileNotFoundException {
        //❓ いちいちここでHashmap作るのは大変
        //💡 ファイルから辞書を作成するようにする
        Scanner sc = new Scanner(new File("words.txt")); // ファイル名を書き換える
        HashMap<String, ArrayList<String>> dict = Main.makeSortedDict(sc);
        sc.close();

        // テストケース
        System.out.println("=== Test 1 ===");
        Main.solution("abnana", dict);

        System.out.println("=== Test 2 ===");
        Main.solution("leap", dict);

        System.out.println("=== Test 3 ===");
        Main.solution("apple", dict);

        System.out.println("=== Test 4 ===");
        Main.solution("aaaaaaaaaaaaaa", dict);

        System.out.println("=== Test 5 ===");
        Main.solution("orange", dict);
    }
}