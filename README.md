# 囲碁対局結果アプリ

### 要件定義
【利用する人】 
1. お店等でリアル囲碁をしている人
2. 特にリアル囲碁を打った棋譜を残し、あとで振り返りたい人

### ほしい機能
* リアル対戦成績を日別、月別でわかるようにしたい
* 棋譜を[つぶや棋譜](https://gokifu.net/kifutweet2.php)URLを載せることで過去の振り返りを簡単にできるようにする
* 対戦相手はアプリ登録してない人でも登録できるようにする
* 対戦成績一覧は公開設定している全ユーザの成績を観れるようにする

### 使用言語やフレームワーク
フロントエンド：Bootstrap Vue.js  
バックエンド：Java
データベースアクセス：Domaかも

### 使用データベース
MySQL

### 環境構築手順
1. developブランチをクローン
2. src/main/resources/config.propertiesのDB接続情報をローカル用に修正する
3. Tomcat設定して起動すれば動く
