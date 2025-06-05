# 宿題１

- ほぼO(1)で動くハッシュテーブルを自分で実装してみよう
- [サンプルコード](https://github.com/xharaken/step2/blob/master/hash_table.py)あり

# コード
- ☕️ [Java版](https://github.com/Riochin/GoogleSTEP/tree/main/week2/hw1/java)（Python後に追加）
- 🐍 [Python版](https://github.com/Riochin/GoogleSTEP/tree/main/week2/hw1/hash_table.py)

### ヒント
Notionにメモ

# ✅ やったこと
- deleteの実装
- 再ハッシュの実装
  - `rehash()`を定義
  - テーブルサイズ70%以上で2倍
  - テーブルサイズ30%以下で半分
- `calculate_hash()`の += を *=にしたら、衝突が減ったのかびっくりするくらい早くなった！！
  - オーバーフローが怖い...

# ☑️ やること
- 🌟 javaでも同様の実装をする！
  - 📌 https://docs.oracle.com/javase/jp/8/docs/api/java/util/HashMap.html
  - `containsKey()`
  - `isEmpty()`
  - `clear()`
  - `getOrDefault()`
  - `replace`
- テーブルサイズを奇数（素数）にする
- ハッシュの衝突を減らすため、ハッシュ関数を改良する
  - オーバーフロー対策する
- `check_size()`で`rehash`を呼び出す

# 記号について
🌟： 重要ポイント
✍️： 学び
❓： 疑問点
💡： 解決策（疑問点に対して）
🧠： アイデア、工夫
📌： 参考リンク・資料
🔥： 反省点
