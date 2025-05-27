package week1.homework1;
 
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.LinkedHashMap;

// ❓ テストがしやすい設計が難しい。makeSortedDictの引数にScannerを渡すのは、テストしにくい。
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HashMap<String, String> dict = makeSortedDict(sc);
        sc.close();

        solution("abnana", dict);

    }

    public static void solution(String randomWord, HashMap<String, String> dict) {
        System.out.println("Random Word: " + randomWord);
        String sortedWord = sortRandomWord(randomWord);
        String result = binarySearch(sortedWord, dict);

        System.out.println(result);
    }

    public static HashMap<String, String> makeSortedDict(Scanner sc) {
        HashMap<String, String> dict = new HashMap<>();
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            
            dict.put(line, line);
        }
        
        ArrayList<String> originalKeys = new ArrayList<>(dict.keySet());
        for (String word: originalKeys){
            String sortedWord = sortRandomWord(word);
            dict.remove(word);
            dict.put(sortedWord, word);
        }

        Object[] keys = dict.keySet().toArray();
        Arrays.sort(keys);
        
        LinkedHashMap<String, String> sortedDict = new LinkedHashMap<>();
        for (Object keyObj : keys) {
            String key = (String) keyObj;
            sortedDict.put(key, dict.get(key));
        }

        // ❓ なぜソートされた順番でputしているのに、dictがkeyでソートされないのか?
        // 💡 LinkedHashMapを使うと、挿入順序を保持することができる。
        // System.out.println("Sorted Dictionary: " + sortedDict);

        return sortedDict;
    }


    public static String sortRandomWord(String randomWord){
        char[] chars = randomWord.toCharArray();
        java.util.Arrays.sort(chars);
        return new String(chars);
    }

    public static String binarySearch(String word, HashMap<String,String> dict) {
        ArrayList<String> keys = new ArrayList<>(dict.keySet());

        int left = 0;
        int right = keys.size() - 1;

        // ArrayList<String> anagrams = new ArrayList<>();

        while (left <= right) {
            int mid = left + (right - left) / 2;
            String midWord = keys.get(mid);
            // System.out.println("Checking mid: " + midWord + " at position " + mid);

            if (midWord.equals(word)) {
                return ("Found: " + dict.get(midWord));
                // anagrams.add(dict.get(midWord));
                // continue;
            } else if (midWord.compareTo(word) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return ("Not Found: " + word);

    }
}
