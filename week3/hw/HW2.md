# 入力したテストケース

| テスト入力      | 期待される結果 (eval) | 実際の計算結果 | 結果   |
| :-------------- | :-------------------- | :------------- | :----- |
| `1+2`           | `3.0`                 | `3.0`          | PASS!  |
| `1.0+2.1-3`     | `0.1`                 | `0.1`          | PASS!  |
| `2*3`           | `6.0`                 | `6.0`          | PASS!  |
| `6/2`           | `3.0`                 | `3.0`          | PASS!  |
| `1+2*3`         | `7.0`                 | `7.0`          | PASS!  |
| `10-4/2`        | `8.0`                 | `8.0`          | PASS!  |
| `5*2+3`         | `13.0`                | `13.0`         | PASS!  |
| `10/2-1`        | `4.0`                 | `4.0`          | PASS!  |
| `1+2*3-4/2`     | `5.0`                 | `5.0`          | PASS!  |
| `7+5*2-12/3+1`  | `14.0`                | `14.0`         | PASS!  |
| `0/0`           | `Error`               | `Error`        | ERROR!  |
