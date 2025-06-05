# 記号について
🌟： 重要ポイント
✍️： 学び
❓： 疑問点
💡： 解決策（疑問点に対して）
🧠： アイデア、工夫
📌： 参考リンク・資料
🔥： 反省点

# メモ
---

## 動作の整理
`key: k1, value: v1`を put した後、key: k1, value: v2 を putする

1. 最初の put("k1", "v1")
- calculate_hash("k1") % self.bucket_size でバケット番号を計算
- そのバケット（例: buckets[i]）の先頭は None
- 新しい Item("k1", "v1", None) を作成し、buckets[i] にセット
- item_count が1増える

2. 次の put("k1", "v2")
- 同じく calculate_hash("k1") % self.bucket_size でバケット番号を計算（前回と同じ）
- そのバケットの先頭には Item("k1", "v1", None) がいる
- ループで item.key == "k1" なので、item.value を "v2" に上書き
- 既存のノードの値だけが "v2" になる。新しいノードは作られない
- item_count は増えない

まとめ：
- 2回目のputで、既存のノードのvalueがv1からv2に上書きされるだけ
- バケット内のノード数は増えない
- 連結リストの構造も変わらない

# week2 1 on 1
- 10^9 + 7, 10^9 + 9, は素数

# ✍️　6/6追記
- `get_key_array`
- `get_value_array`
- `contains_key`

の３つを追加！結局使わなかったが、実装の勉強になった