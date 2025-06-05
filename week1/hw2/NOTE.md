# 記号について
🌟： 重要ポイント
✍️： 学び
❓： 疑問点
💡： 解決策（疑問点に対して）
🧠： アイデア、工夫
📌： 参考リンク・資料
🔥： 反省点

# 宿題２
与えられた文字列の全ての文字を使わなくても良いように関数をアップグレードする。
https://github.com/xharaken/step2/tree/master/anagram
入力：small.txt , medium.txt, large.txt
出力：各単語について「最大のスコアを持つアナグラム」を列挙したファイル

## それぞれ
<String word, Integer[][]>
map['a']
dictionaryを全部見て、wordで作れるものを探す
word[i] >= dict.get(i)[j]

## 比較するとき
ArrayList<int[][]> // alpha-のやつでやる
もしくは
ArrayList<HashMap<char,int>>
もしくは
ArrayList<ArrayList<Integer>>

## いつまでやる？
全部見ずに済む方法があれば良い（考える）

## ✍️ 文字列の頻度配列は`char`の引き算で簡潔に書ける
`a.java`参照
```
int[] freq = new int[26];
for (char c : word.toCharArray()) {
    freq['z' - c]++;
}
```

- `'z' - 'z'` = 0, `'z' - 'a'` = 25
というように、char同士で引き算ができる！（ASCII）
- わざわざHashMap作らなくて良いので早い。
---

# 一回提出、larggeの実行時間が10分かかった...
## パフォーマンス改善のために行うこと
- すでにソート済みのwordをsortRandomWordしちゃってた → 消した
```
for(int i=0;i<words.size();i++){
            String word = words.get(i);
            int[] alphabets = makeLetterArray(word);

            // 💡： ここはソート済み！sortRandomWord不要。
            // String sortedWord = sortRandomWord(word);
            alphaDict.put(word, alphabets);
        }
```

- `entrySet()を使った`
- 📌 https://qiita.com/neras_1215/items/e756089312bbd6c353a9
- 便利すぎる。keySetから拾ってくるしかないのかと思ってた。
- このあと結局HashMap自体使うのやめたw

```
// 💡： これは無くして、`(Map.Entry<String, int[]> entry : alphaDict.entrySet()`でループ回せる
        // ArrayList<String> keys = new ArrayList<>(alphaDict.keySet());
        ArrayList<String> anagrams = new ArrayList<>();

        for (Map.Entry<String, int[]> entry : alphaDict.entrySet()){
            ...
        }
```

- 🌟 `WordInfo` クラスを作った
辞書の前処理段階で単語情報をオブジェクトにまとめたので、後続処理を高速化できた！
  - `word`: ソートずみ単語名（aaelp, aegprなどが入る） //nameとかでも良かったかも
  - `freq`: 各アルファベットの出現回数
  - `score`: スコア
  - `originWords`: aelp -> [leap, pale]みたいな感じで元々のワードが

  ```
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
  ```

  - 補助関数 `canMake()`を作った
  ```
  public static boolean canMake(int[] wordFreq, int[] inputFreq) {
    for (int i = 0; i < 26; i++) {
        if (wordFreq[i] > inputFreq[i]) return false;
      }
    return true;
  }
  ```
    - 多分計算量には影響なし？

# 授業中にもらったアドバイス
- 最大順に並び替えたら、スコア最強が出てくるので比較が減る！

# week 2 1 on1 
- 自クラスの比較演算子を自前で作ってしまう