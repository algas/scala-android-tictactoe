# ハンズオン勉強会の課題

## 概要

課題用のサンプルアプリは、三目並べゲーム(TicTacToe)の未完成版です。  
このゲームを完成させることが、今回のハンズオン勉強会の課題です。  
課題には基本課題と演習課題の2つがあります。  
もし基本課題が時間内に終わった場合には、演習課題にも挑戦してみてください。


## 実装されている機能

サンプルアプリには以下の機能が実装されています。

* 基本表示  
タイトル文字列と3x3のマス。
* タッチイベント  
タップしたマスに交互にOとXを表示する。
* ターンの表示  
タイトルの下の数値。


## 不足している機能

### 基本課題

* プレイヤー機能  
OとXのどちらのターンかを明示する。
* 重複判定  
同じマスに複数のコマを置けないようにする。
* 勝利判定  
どちらかが3つ揃えたら勝利。すべて埋まったら引き分け。

#### 演習課題

* リセット  
初めからやり直せる機能。
* サイズ調整  
画面サイズに合わせてスケールを自動で変更する。
* CPU  
コンピュータ対戦できるようにAIを実装する。


## ソースコードの構成

今回の課題に必要なのは MainThread.scala のみであり、他のファイルを編集する必要はおそらくありません。  
(src/main/scala/com/geishatokyo/tictactoe/MainThread.scala)

### ビルド設定

* build.sbt: ビルドの設定
* project/build.properties: sbt のバージョン
* project/android.sbt: 依存しているプラグイン

### リソース

* src/main/AndroidManifest.xml: アプリの設定
* src/main/res/layout/main.xml: アプリのレイアウト設定
* src/main/res/values/strings.xml: 文字列リソース

### ソース

* src/main/scala/com/geishatokyo/tictactoe/MainThread.scala: ゲーム内の処理
* src/main/scala/com/geishatokyo/tictactoe/MainActivity.scala: アクティビティ(表示遷移)
* src/main/scala/com/geishatokyo/tictactoe/MainView.scala: ビュー(画面)の制御処理


## 参考資料

* [Scala reference](http://www.scala-lang.org/api/current/)
* [Android reference](http://developer.android.com/reference/packages.html)
* [Scala で書く tetrix](http://eed3si9n.com/tetrix-in-scala/ja/index.html)

