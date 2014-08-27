# Application Setup

サンプルアプリケーションをセットアップします。

1. サンプルのダウンロード
2. sbt ビルド
3. デバイス割り当て
4. パッケージ作成
5. アプリ実行


## サンプルのダウンロード

以下の手順でサンプル・アプリケーションをダウンロードします。

1. ターミナルを開いて適当なディレクトリを作成する。
2. 以下のコマンドでアプリケーションを取得する。  
```
git clone git@github.com:algas/scala-android-tictactoe.git
```


## sbt ビルド

sbt でアプリケーションをビルドします。  
```
cd scala-android-tictactoe
sbt
```  
ビルドに成功すると sbt インタラクティブシェルが起動します。


## デバイス割り当て

アプリを実行するデバイスを割り当てます。  
sbt インタラクティブシェルで以下のコマンドを実行します。
(> はシェル内部のコマンドであることを示す。 ">" を入力しないこと。)

1. device 一覧表示  
```
> devices
```
2. device 割り当て  
```
> device xxx
```
xxx には devices で表示されたデバイス名を入力する。


## パッケージ作成

アプリケーションパッケージを作成します。  
sbt インタラクティブシェルで以下のコマンドを実行します。
```
> android:package
```

* ビルド時に zipalign 関連でエラーが出たら読む項目  
Android SDK 23 以上に更新した場合には、以下を実行しましょう。  
https://forums.xamarin.com/discussion/19772/zipalign-error-executing-tool-executable-location-invalid-android-sdk-tools-23


## アプリ実行

Android エミュレータでアプリを実行します。  
sbt インタラクティブシェルで以下のコマンドを実行します。
```
> android:run
```
エミュレータ上でアプリが実行すれば、準備完了です。

