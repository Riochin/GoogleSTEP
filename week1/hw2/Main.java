package week1.hw2;
 
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

// ❓ score_checker.pyってjavaで実装しないといけない系？？
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<WordInfo> dict = makeDict(sc);
        sc.close();

        solution("abnana", dict);

    }

    public static void solution(String randomWord, List<WordInfo> dict) {
        String bestWord = null;
        int bestScore = -1;

        String sortedWord = sortRandomWord(randomWord);
        // ArrayList<String> words = new ArrayList<>(dict.keySet());
        // HashMap<String, int[]> alphaDict = makeAlphaDict(words);
        ArrayList<WordInfo> anagrams = findAnagrams(sortedWord, dict);

        // スコア最大の単語を1つだけ出力
        for (WordInfo wi : anagrams) {
            if (wi.score > bestScore) {
                bestScore = wi.score;
                bestWord = wi.originalWords.get(0);
            }
        }
        if (bestWord != null) {
            System.out.println(bestWord);
        } else {
            System.out.println(); // アナグラムがなければ空行
        }
    }

    public static List<WordInfo> makeDict(Scanner sc) {
        List<WordInfo> dict = new ArrayList<>();
        Map<String, WordInfo> tempMap = new HashMap<>();

        while(sc.hasNextLine()){
            String word = sc.nextLine();
            String sortedWord = sortRandomWord(word);
            WordInfo wi = tempMap.computeIfAbsent(sortedWord, k -> new WordInfo(sortedWord));
            wi.originalWords.add(word);
        }

        dict.addAll(tempMap.values()); // ★忘れず追加
        return dict;

    }


    public static String sortRandomWord(String randomWord){
        char[] chars = randomWord.toCharArray();
        java.util.Arrays.sort(chars);
        return new String(chars);
    }

    public static ArrayList<WordInfo> findAnagrams(String target, List<WordInfo> dict){
        int[] targetLetters = makeLetterArray(target);
        // 💡： 👇は無くして、`(Map.Entry<String, int[]> entry : alphaDict.entrySet()`でループ回せる
        // ArrayList<String> keys = new ArrayList<>(alphaDict.keySet());
        ArrayList<WordInfo> anagrams = new ArrayList<>();

        for (WordInfo wi : dict) {
            if (canMake(wi.freq, targetLetters)) {
                anagrams.add(wi);
            }
        }

        return anagrams;
    }

    public static boolean canMake(int[] wordFreq, int[] inputFreq) {
        for (int i = 0; i < 26; i++) {
            if (wordFreq[i] > inputFreq[i]) return false;
        }
        return true;
    }
    

    public static ArrayList<String> findAnagramOrigins(List<String> words, HashMap<String, ArrayList<String>> dictionary) {
        ArrayList<String> origins = new ArrayList<>();
        for(String word:words){
            origins.addAll(dictionary.getOrDefault(word, new ArrayList<>()));
        }
        return origins;
    }

    public static HashMap<String, int[]> makeAlphaDict(ArrayList<String> words){
        HashMap<String, int[]> alphaDict = new HashMap<>();

        for(int i=0;i<words.size();i++){
            String word = words.get(i);
            int[] alphabets = makeLetterArray(word);

            // 💡： ここはソート済み！sortRandomWord不要。
            // String sortedWord = sortRandomWord(word);
            alphaDict.put(word, alphabets);
        }

        return alphaDict;
    }

    public static int[] makeLetterArray(String word){
        int[] alphabets = new int[26];

        for(int j=0;j<word.length();j++){
            alphabets['z' - word.charAt(j)] ++ ;
        }

        return alphabets;   
    }
}
