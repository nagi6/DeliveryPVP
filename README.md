# DeliveryPVP
Spigot用納品PVP<br>
<br>
テスト動画 https://youtu.be/BkqyKZcqBTY<br>
[おまけ]銃PVP動画 https://youtu.be/80xmkIG6BV4

## ルール
納品PVPは赤チームと青チームに分かれてお互いのエメラルドを盗みあうPVPです。<br>
どちらかのチームが所定の数のエメラルドを持ち帰るか全滅すると勝利です。<br>
<br>
プレイヤーはそれぞれのチームのリスポーン地点から敵チームのエメラルドブロックに向かいます。<br>
エメラルドを獲得したプレイヤーは自分の陣地の納品ブロックに持ち帰って下さい。(ブロックにタッチ)<br>
味方がエメラルドを持ち帰っている間は新たにエメラルドを取る事は出来ません。<br>
チームが全滅すると負けになるので注意して下さい。<br>

## 使い方
1. /dpvp spawnコマンドで赤チームと青チームのリスポーン地点を決めます
2. /dpvp emeコマンドで赤チームと青チームのエメラルドブロックを決めます(コマンド実行後、エメラルドブロックにしたいブロックをタッチして下さい)
3. /dpvp dlvコマンドで赤チームと青チームの納品ブロックを決めます(コマンド実行後、納品ブロックにしたいブロックをタッチして下さい)
4. その他、ライフ数、勝利に必要なエメラルドの数、試合中のゲームモードを変更出来ます
5. /dpvp addコマンドでプレイヤーをチームに追加して下さい
6. /dpvp startコマンドで5秒後に試合を開始します

## コマンド
試合関連<br>
/dpvp start : 試合を開始します<br>
/dpvp stop : 試合を強制終了します<br>
<br>
チーム関連<br>
/dpvp add <red|blue> <name> : プレイヤーをチームに追加します<br>
/dpvp remove <name> : プレイヤーをチームから除外します<br>
/dpvp reset : チームをリセットします<br>
<br>
設定関連<br>
/dpvp set life <number> : ライフ数を設定します(0にした場合ライフ無限)<br>
/dpvp set point <number> : 勝利に必要なエメラルドの個数を設定します<br>
/dpvp set gamemode <survival|adventure><br>
/dpvp set spawn <red|blue> : 現在地をチームのリスポーン地点に設定します<br>
/dpvp set eme <red|blue> : タッチしたブロックをエメラルドブロックに設定します<br>
/dpvp set dlv <red|blue> : タッチしたブロックを納品ブロックに設定します
