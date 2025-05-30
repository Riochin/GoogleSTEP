# 記号について
🌟： 重要ポイント
✍️： 学び
❓： 疑問点
💡： 解決策（疑問点に対して）
🧠： アイデア、工夫
📌： 参考リンク・資料

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

## ✍️ アルファベットの配列を作るとき
`a.java`参照
- `'z' - 'z'` = 0
- `'z' - 'a'` = 25
というように、char同士で引き算ができる！（ASCII）
これでわざわざハッシュマップ作って
```
if (word.charAt(i).equals("a")){
    alphas.get("a")++;
} else if(word.charAt(i).equals("b)){
    ...
}
```
みたいなことしなくて良い！（学び）
---