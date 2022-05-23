# 使用方法

## STEP1: ダウンロード
以下のページから使用しているOSに合うファイルをダウンロードしてください．  
macOSの方はセキュリティの関係でアプリを配布できないため `AIT-busInfo.jar` をダウンロードしてください．

### ダウンロードページ
https://github.com/GanePrivate/AIT-BusInfo-Desktop-App/releases/latest


## STEP2: 起動準備 (macOS or jarファイルを使用する方のみ)
jarファイルを実行するにはJavaの実行環境をインストールする必要があるため，以下のページから `Java SE Development Kit` をダウンロードしてインストールしてください．

### ダウンロードページ
https://www.oracle.com/java/technologies/downloads/

macOSの方は使用している機種のチップが `Intel` なのか `M1` なのかによってインストールするファイルが違うので注意してください！

#### Intelチップの場合
`x64 DMG Installer` の方をダウンロードしてインストールしてください．

#### M1チップの場合
`Arm 64 DMG Installer` の方をダウンロードしてインストールしてください．


## STEP3: インストール・起動

### Windowsの場合
ダウンロードした `AIT-busInfo-Windows.zip` を解凍し，中に入っている `AIT-busInfo.exe` をダブルクリックすると起動できます．


### macOSの場合
ダウンロードした `AIT-busInfo.jar` のファイルを右クリックし，`開く` を押すと起動できます．


### Linuxの場合
ダウンロードしたファイル(`ait-businfo-linux_amd64.deb` or `ait-businfo-linux_amd64.rpm`)をダブルクリックしてインストールしてください．  
ターミナル(端末)を開いてインストールした場所に行き，`./AIT-busInfo` とコマンドを入力すると実行できます．

#### Alma Linuxの場合はデフォルトで以下のパスにインストールされます
`/opt/ait-businfo/bin`