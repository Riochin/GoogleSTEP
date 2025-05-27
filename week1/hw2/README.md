# 宿題２
与えられた文字列の全ての文字を使わなくても良いように関数をアップグレードする。
https://github.com/xharaken/step2/tree/master/anagram
入力：small.txt , medium.txt, large.txt
出力：各単語について「最大のスコアを持つアナグラム」を列挙したファイル

1. それぞれ
<String word, Integer[][]>
map['a']
dictionaryを全部見て、wordで作れるものを探す
word[i] >= dict[i][j]

2. 比較するとき
ArrayList<int[][]> // alpha-のやつでやる
もしくは
ArrayList<HashMap<char,int>>
もしくは
ArrayList<ArrayList<Integer>>

3. いつまでやる？
全部見ずに済む方法があれば良い（考える）
