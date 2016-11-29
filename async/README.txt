# 勉強会

## テーマ
- 非同期処理のパターンと使いどころの整理(議論ベース)
- Springにおける実装方法(コードレビュー)


## 非同期処理のパターンと使いどころの整理(議論ベース)


### 非同期処理とは
非同期とは「あるタスクを実行している間に別のタスクを実行できること」
今回はクライアントに閉じた非同期は対象外とする。


### 非同期にする動機
- 処理負荷が高い
- 処理時間が長い
- 今すぐには実行できない
 (対向システムのデータを待ってからじゃないと駄目とか)


### 基本的な指針
- 処理負荷が高いものはWEBサーバとは別のプロセスやサーバに分けるべき
  WEBシステムは軽い処理を大量にさばくことに向いてるアーキテクチャ。
  リクエスト単位でふりわけることが多いので、処理負荷を均等にするのが難しい。
- 実装する上ではWEBサーバ上で実行したほうが楽なため、
  処理負荷は高くないが時間のかかる処理(他システムAPIのコールなど)はWEBサーバで実行すべき
※ WEBサーバの負荷がそれほど高くない、かつ、限られたユーザが使う
   という条件があるなら、重たい処理をWEB上で実行してもいいかもしれない。
   だが基本は非推奨。


### 処理手段
- WEBサーバで実行
  (別スレッドにするなら「@Async」、「reactive stream」)
- 別プロセスで実行
  - JMS
  - DB契機でバッチ起動(Macchinetta 非同期バッチ、cronで定期バッチ起動)
  - OSのコマンドをコール(地獄のはじまり)


### 処理手段の比較
観点は、「負荷分散、再送制御、配信制御、同時実行数の制御、実装コスト」
[@Async]
  TaskExecutorでThread数や待機数の上限等がカスタマイズ可能。
  ある程度の負荷制御はできるが再送制御や配信制御の仕組みはない。
  実装コストは低い
[JMS]
  やりたいことはなんでも実現できる。実装コストは高い
[DB契機でのバッチ起動]
  サーバ筐体を分けて実行することが多いので負荷分散しやすい。
  再送制御や配信制御や同時実行数の仕組みはない。
[OSのコマンドをコール]
  負荷分散できない。
  再送制御や配信制御や同時実行数の仕組みはない。
  地獄のはじまり


### 通知手段
- 処理が完了するまで待機してリクエストを同期的に返す
 (Callable,DeferredResult,ListenableFuture,CompletableFuture)
- リアルタイムに通知する(<http://www.slideshare.net/mawarimichi/push-37869433>)
   - polling
   - ロングpolling(DeferredResult)
   - SSE(SseEmitter)
   - websocket(Stomp)
- あとで通知する
   - メール
   - タスク確認画面

### 通知手段の比較

#### リクエストを同期的に返す
Tomcatの場合はリクエストごとにworkerスレッドが占有される。
時間のかかるリクエストはTomcatのworkerスレッドを占有するので避けたほうがいい
<http://events.linuxfoundation.org/sites/events/files/slides/Tomcat%20Connectors.pdf>
いずれの手段でも(Callable,DeferredResult,ListenableFuture,CompletableFuture)
手軽にworkerスレッドを重たい処理から解放できる。

#### ポーリング
通常のHTTPリクエストと同様に検索クエリを発行するだけなので実装が楽
 HTTPのタイムアウトを考慮しなくてよいので方式面でも楽

### ロングポーリング
- 非同期APIを習熟してないとスレッドの待ち合わせとかTopicごとの配信が難しい。
- クライアントのリクエスト契機のイベントは楽に実装できる。(スレッドの待ち合わせとかがないので)
- HTTPのタイムアウトを考慮しないといけないので方式面でつらい

### 注意点
TODO:
運用考慮事項が増える。
リアルタイム処理なら客にエラー画面出してリトライさせられる。
いったん受け付けた後の実行エラーはどう返す？
 - ミスったリクエストがキューにたまったときに消せる？消すのは運用対処？
 - 実行ミスったらゴミキューにつっこんどいて後で運用対処?


### Springにおける実装方法(コードレビュー)
処理が完了するまで待機して同期的に返す
(Callable,DeferredResult,ListenableFuture,CompletableFuture)
<http://qiita.com/kazuki43zoo/items/ce88dea403c596249e8a#%E5%8B%95%E4%BD%9C%E7%A2%BA%E8%AA%8D%E7%92%B0%E5%A2%83>

リアルタイムに通知する(<http://www.slideshare.net/mawarimichi/push-37869433>)
  - polling(クライアントでapiを定期的にコールする)
  - ロングpolling(DeferredResult)
  - SSE(SseEmitter)
  - websocket(Stomp)


### javaにおける非同期API
Future,CompletableFuture,Stream ...
- FutureはFuture#getで簡単にブロックされてしまう
- CompletableFutureは非同期完了後に処理をはさみこめるが、
  List<XXX>なデータを返すときに全件揃わないと返せない
- StreamはCollectionなどを変換して生成するもので、
  ホットなデータ(データが生成され続ける？)をStreamに流し続けられるわけではない

ストリーム的に流れる処理の記述が大変(xx秒ごとにコミットして進捗画面を返す、とか)
PUSH型の非同期処理がしたい -> Reactive Stream や java9のFlow APIへ
<http://www.slideshare.net/SpringCentral/imperative-to-reactive-web-applications>


## Tips
- アクティブでないウィンドウではポーリング間隔を調整すると、負荷を減らせる
  page visibility api<https://developer.mozilla.org/ja/docs/Web/Guide/User_experience/Using_the_Page_Visibility_API>

