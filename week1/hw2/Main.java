package week1.hw2;
 
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

// ❓ テストがしやすい設計が難しい。makeSortedDictの引数にScannerを渡すのは、テストしにくい。
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HashMap<String, ArrayList<String>> dict = makeSortedDict(sc);
        sc.close();

        solution("abnana", dict);

    }

    public static void solution(String randomWord, HashMap<String, ArrayList<String>> dict) {
        System.out.println("Random Word: " + randomWord);
        String sortedWord = sortRandomWord(randomWord);

        int index = binarySearch(sortedWord, dict);
        if (index == -1) {
            System.out.println("Word not found in dictionary.");
            System.out.println();
            return;
        }
        ArrayList<String> anagrams = findAnagrams(sortedWord, dict);

        System.out.print("Found anagrams: ");
        for(String s : anagrams) {
            System.out.print(s + " ");
        }
        System.out.println();
        System.out.println();
    }

    public static HashMap<String, ArrayList<String>> makeSortedDict(Scanner sc) {
        HashMap<String, ArrayList<String>> dict = new HashMap<>();
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            String sortedWord = sortRandomWord(line);
            dict.computeIfAbsent(sortedWord, k -> new ArrayList<>()).add(line);
        }

        // System.out.println("Dictionary created with " + dict + " unique sorted words.");

        // ソートされた辞書を作成するために、キーをソートする
        ArrayList<String> sortedKeys = new ArrayList<>(dict.keySet());
        sortedKeys.sort(String::compareTo);
        HashMap<String, ArrayList<String>> sortedDict = new LinkedHashMap<>();
        for (String key : sortedKeys) {
            sortedDict.put(key, dict.get(key));
        }

        return sortedDict;

    }


    public static String sortRandomWord(String randomWord){
        char[] chars = randomWord.toCharArray();
        java.util.Arrays.sort(chars);
        return new String(chars);
    }

    public static int binarySearch(String word, HashMap<String,ArrayList<String>> dict) {
        ArrayList<String> keys = new ArrayList<>(dict.keySet());

        int left = 0;
        int right = keys.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            String midWord = keys.get(mid);
            // System.out.println("Checking mid: " + midWord + " at position " + mid);

            if (midWord.equals(word)) {
                return mid;
            } else if (midWord.compareTo(word) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return (-1);

    }

    public static ArrayList<String> findAnagrams(String word, HashMap<String, ArrayList<String>> dictionary) {
        return dictionary.getOrDefault(word, new ArrayList<>());
    }
}
