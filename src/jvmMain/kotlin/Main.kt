// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Color.black
import java.awt.Color.white
import java.time.LocalTime

val Gray = java.awt.Color(64, 64, 64)
val DarkGray = java.awt.Color(105, 105, 105)
val LightGray = java.awt.Color(245, 245, 245)
val LightBlue = java.awt.Color(240, 255, 255)
val GhostWhite = java.awt.Color(248, 248, 255)

fun java.awt.Color.toCompose(): Color {
    return Color(red, green, blue)
}

fun main() = application {
    Window(
        title = "AIT BUS INFO",
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(width = 500.dp, height = 500.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(color = white.toCompose()).padding(0.dp),  // 背景の色とパディング値を設定
            horizontalAlignment = Alignment.CenterHorizontally  // 中央に配置(水平方向)
        ) {
            Text(
                text = "愛工大バス時刻案内",
                color = black.toCompose(),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 0.dp, end = 0.dp, top = 10.dp, bottom = 16.dp)
            )
            // Spacer(modifier = Modifier.height(40.dp))  // 文字とその下の要素間のスペースを設定

            Column(
                modifier = Modifier.fillMaxSize().background(color = LightGray.toCompose()),  // 背景の色とパディング値を設定
                horizontalAlignment = Alignment.CenterHorizontally  // 中央に配置(水平方向)
            ) {
                TextTabs()
            }
        }
    }
}


@Composable
fun TextTabs() {
    var tabIndex by remember { mutableStateOf(0) }
    val tabData = listOf(
        "八草→大学",
        "大学→八草"
    )
    TabRow(selectedTabIndex = tabIndex) {
        tabData.forEachIndexed { index, text ->
            Tab(selected = tabIndex == index, onClick = {
                tabIndex = index
            }, text = {
                Text(text = text)
            })
        }
    }

    // 選択したタブの時刻表を表示
    viewBusTime(tabIndex)
}


@Composable
fun viewBusTime(tabID: Int) {
    Box(modifier = Modifier.fillMaxWidth().height(35.dp).background(color = DarkGray.toCompose()), Alignment.Center) {
        Text(
            text = "Next",
            color = white.toCompose(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }

    // コルーチンで毎秒ごとに時刻表を更新
    val scope = rememberCoroutineScope()
    var busInfo by remember { mutableStateOf(getAllData()) }

    scope.launch {
        while (true) {
            busInfo = getAllData()  // 運行情報を取得
            delay(1000)
        }
    }

    // コンソールにログを表示する
    println(tabID)

    // 今日バスの運行があるか確認する
    if (busInfo.todayInfo.daiya == null) {
        Text(
            text = "本日、バスの運行はありません",
            color = black.toCompose(),
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 0.dp, end = 0.dp, top = 15.dp, bottom = 15.dp)
        )
        return
    }

    when (tabID) {

        // 八草→大学のタブ選択時
        0 -> {
            if (busInfo.toDaigakuInfo.minutes != -1) {
                // 次のバス出発時刻を表示する
                Text(
                    text = "%02d:%02d".format(busInfo.toDaigakuInfo.hour, busInfo.toDaigakuInfo.minutes),
                    color = black.toCompose(),
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 0.dp, end = 0.dp, top = 15.dp, bottom = 25.dp)
                )
                //Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(color = Gray.toCompose()))

                // 次の次のバス出発時刻を表示する
                Box(
                    modifier = Modifier.fillMaxWidth().height(35.dp).background(color = DarkGray.toCompose()),
                    Alignment.Center
                ) {
                    Text(
                        text = "After the Next",
                        color = white.toCompose(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                val afterNextTime = if (busInfo.toDaigakuAfterNext.minutes != -1) "%02d:%02d".format(busInfo.toDaigakuAfterNext.hour, busInfo.toDaigakuAfterNext.minutes) else "この時間のバスはありません"
                Text(
                    text = afterNextTime,
                    color = black.toCompose(),
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 0.dp, end = 0.dp, top = 15.dp, bottom = 25.dp)
                )

                // 今日の運行ダイヤを表示する
                Box(
                    modifier = Modifier.fillMaxWidth().height(35.dp).background(color = DarkGray.toCompose()),
                    Alignment.Center
                ) {
                    Text(
                        text = "本日の運行ダイヤ",
                        color = white.toCompose(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    text = "今日は${busInfo.todayInfo.daiya}ダイヤです",
                    color = black.toCompose(),
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 0.dp, end = 0.dp, top = 15.dp, bottom = 15.dp)
                )
            } else if (LocalTime.now().hour <= 7) {
                // 0~7時までの間は始発時間を表示
                Text(
                    text = "[始発] 08:%02d".format(busInfo.todayInfo.toDaigakuFirst),
                    color = black.toCompose(),
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 0.dp, end = 0.dp, top = 15.dp, bottom = 15.dp)
                )
            } else {
                Text(
                    text = "本日の運行は終了しました",
                    color = black.toCompose(),
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 0.dp, end = 0.dp, top = 15.dp, bottom = 15.dp)
                )
            }
        }

        // 大学→八草のタブ選択時
        1 -> {
            if (busInfo.toYakusaInfo.minutes != -1) {
                // 次のバス出発時刻を表示する
                Text(
                    text = "%02d:%02d".format(busInfo.toYakusaInfo.hour, busInfo.toYakusaInfo.minutes),
                    color = black.toCompose(),
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 0.dp, end = 0.dp, top = 15.dp, bottom = 25.dp)
                )

                // 次の次のバス出発時刻を表示する
                Box(
                    modifier = Modifier.fillMaxWidth().height(35.dp).background(color = DarkGray.toCompose()),
                    Alignment.Center
                ) {
                    Text(
                        text = "After the Next",
                        color = white.toCompose(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                val afterNextTime = if (busInfo.toYakusaAfterNext.minutes != -1) "%02d:%02d".format(busInfo.toYakusaAfterNext.hour, busInfo.toYakusaAfterNext.minutes) else "この時間のバスはありません"
                Text(
                    text = afterNextTime,
                    color = black.toCompose(),
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 0.dp, end = 0.dp, top = 15.dp, bottom = 25.dp)
                )

                // 今日の運行ダイヤを表示する
                Box(
                    modifier = Modifier.fillMaxWidth().height(35.dp).background(color = DarkGray.toCompose()),
                    Alignment.Center
                ) {
                    Text(
                        text = "本日の運行ダイヤ",
                        color = white.toCompose(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    text = "今日は${busInfo.todayInfo.daiya}ダイヤです",
                    color = black.toCompose(),
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 0.dp, end = 0.dp, top = 15.dp, bottom = 15.dp)
                )
            } else if (LocalTime.now().hour <= 7) {
                // 0~7時までの間は始発時間を表示
                Text(
                    text = "[始発] 08:%02d".format(busInfo.todayInfo.toYakusaFirst),
                    color = black.toCompose(),
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 0.dp, end = 0.dp, top = 15.dp, bottom = 15.dp)
                )
            } else {
                Text(
                    text = "本日の運行は終了しました",
                    color = black.toCompose(),
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 0.dp, end = 0.dp, top = 15.dp, bottom = 15.dp)
                )
            }
        }
    }


}