package week1.hw2;
 
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

// ❓ score_checker.pyってjavaで実装しないといけない系？？
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HashMap<String, ArrayList<String>> dict = makeSortedDict(sc);
        sc.close();

        solution("abnana", dict);

    }

    public static void solution(String randomWord, HashMap<String, ArrayList<String>> dict) {
        String sortedWord = sortRandomWord(randomWord);
        ArrayList<String> words = new ArrayList<>(dict.keySet());
        HashMap<String, int[]> alphaDict = makeAlphaDict(words);
        ArrayList<String> anagrams = findAnagrams(sortedWord, alphaDict);
        ArrayList<String> origins = findAnagramOrigins(anagrams, dict);

        // スコア最大の単語を1つだけ出力
        String bestWord = null;
        int bestScore = -1;
        for (String s : origins) {
            int score = ScoreChecker.calculateScore(s);
            if (score > bestScore) {
                bestScore = score;
                bestWord = s;
            }
        }
        if (bestWord != null) {
            System.out.println(bestWord);
        } else {
            System.out.println(); // アナグラムがなければ空行
        }
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

    public static ArrayList<String> findAnagrams(String target, HashMap<String, int[]> alphaDict){
        int[] targetLetters = makeLetterArray(target);
        ArrayList<String> keys = new ArrayList<>(alphaDict.keySet());
        ArrayList<String> anagrams = new ArrayList<>();

        for (int i=0;i<alphaDict.size();i++){
            boolean isBroken = false;
            for(int a=0;a<26;a++){
                if(targetLetters[a] < alphaDict.get(keys.get(i))[a]){
                    isBroken = true;
                    break;
                }
            }
            if(isBroken)continue;
            anagrams.add(keys.get(i));
        }

        return anagrams;
    }

    public static ArrayList<String> findAnagramOrigins(ArrayList<String> words, HashMap<String, ArrayList<String>> dictionary) {
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

            String sortedWord = sortRandomWord(word);
            alphaDict.put(sortedWord, alphabets);
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
