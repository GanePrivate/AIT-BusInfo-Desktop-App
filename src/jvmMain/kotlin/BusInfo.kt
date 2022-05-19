import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class daiyaData(
    val daiya: String?,
    val toDaigaku: Map<Int, List<Int>?>,
    val toYakusa: Map<Int, List<Int>?>,
    val toDaigakuFirst: Int,
    val toYakusaFirst: Int
)

data class NextInfo(
    val minutes: Int,
    val hour: Int,
    val flag: Boolean
)

data class AfterNextInfo(
    val minutes: Int,
    val hour: Int
)

data class ReturnData(
    val todayInfo: daiyaData,
    val toDaigakuInfo: NextInfo,
    val toYakusaInfo: NextInfo,
    val toDaigakuAfterNext: AfterNextInfo,
    val toYakusaAfterNext: AfterNextInfo
)


fun getNearVal(minutes: List<Int>?, mode: Int = 0, isReset: Boolean = false): Int {
    /*
    最も近い値を返す関数
    mode = 0:通常の処理,  1:次の時間の処理,  3:次の次の時間の処理
    :param minutes:     # array:その時間の時刻表(分)の配列データ
    :param mode:        # int：動作モードを指定する値
    :param isReset:     # bool：時間(分)をリセットするか
    :return:            # int：現在時刻から一番近い出発時間(分)
    */


    // 時刻表データ(分)が空なら0を入れておく
    val size: Int = minutes?.size ?: 0

    // オブジェクトのサイズを確認
    if (size < 1 || minutes == null) return -1

    // 今の時間(分)
    var nowMinutes: Int = LocalTime.now().minute

    // 現在時間+1の時刻表データ(分)を返す処理
    if (mode == 1) return minutes[0]

    // リセットフラグを確認する
    if (isReset) nowMinutes = 0

    var isNotThisHour: Boolean = false
    // 次の発車時間が見つかったらその時間を返す
    for (i in 0 until size) {
        if (minutes[i] >= nowMinutes && mode == 0) {
            return minutes[i]
        }

        // 次の次のバスの時刻を返す
        if (minutes[i] >= nowMinutes && mode == 3 && isNotThisHour) {
            return minutes[i]
        }

        if (minutes[i] >= nowMinutes && mode == 3) {
            isNotThisHour = true
        }
    }

    // 次の出発時間が見つからなかったら-1を返す
    return -1
}


fun getTodayTimetable(): daiyaData {
    /*
    今日の時刻表データを返す関数
    :return:    # String?:今日の運行ダイヤ
                # array：toDaigaku  大学行きの今日の時刻表データ,
                # array：toYakusa   八草行きの今日の時刻表データ,
                # int：toDaigakuFirst  大学行きの始発時間(分),
                # int：toYakusaFirst   八草行きの始発時間(分)
    */

    // 今日の日付けを取得
    // val month: Int = LocalDate.now().dayOfMonth

    //　今日の日付をYYYY-MM-DDの形式で取得
    // val today = "2020-6-18"
    val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    // 時刻表データを定義(JSON形式)
    val A1Map = mapOf(0 to null, 1 to null, 2 to null, 3 to null, 4 to null, 5 to null, 6 to null, 7 to null, 8 to listOf(0,5,10,15,20,25,30,35,40,45,50,55), 9 to listOf(0,5,10,15,20,25,30,35,40,50,55), 10 to listOf(0,5,10,15,20,25,30,35,45,55), 11 to listOf(5,15,25,35,45,55), 12 to listOf(5,15,25,35,45,55), 13 to listOf(5,20,35,50), 14 to listOf(5,15,25,35,45,55), 15 to listOf(5,15,30,45), 16 to listOf(0,15,30,45), 17 to listOf(0,10,25,40), 18 to listOf(0,15,45), 19 to listOf(0,15,30,45), 20 to listOf(0,30), 21 to listOf(0), 22 to null, 23 to null)
    val A2Map = mapOf(0 to null, 1 to null, 2 to null, 3 to null, 4 to null, 5 to null, 6 to null, 7 to null, 8 to listOf(20,50), 9 to listOf(20,50), 10 to listOf(20,50), 11 to listOf(0,10,20,30,40,50), 12 to listOf(0,10,20,30,40,50), 13 to listOf(0,15,30,45), 14 to listOf(0,10,20,30,40,45,50,55), 15 to listOf(0,10,20,30,40,50), 16 to listOf(0,5,10,15,25,30,35,40,45,50,55), 17 to listOf(0,10,15,20,25,30,35,40,45,55), 18 to listOf(0,10,20,30,40,50), 19 to listOf(0,15,30,45), 20 to listOf(0,15,30,45), 21 to listOf(0,15,30,45), 22 to null, 23 to null)
    val B1Map = mapOf(0 to null, 1 to null, 2 to null, 3 to null, 4 to null, 5 to null, 6 to null, 7 to null, 8 to listOf(0,10,20,35,45), 9 to listOf(0,5,25,35,50), 10 to listOf(0,10,30,55), 11 to listOf(0,25,50), 12 to listOf(25), 13 to listOf(35), 14 to listOf(5,35), 15 to listOf(5,35), 16 to listOf(5,45), 17 to listOf(10,45), 18 to listOf(5,35), 19 to listOf(35), 20 to listOf(25), 21 to listOf(5), 22 to null, 23 to null)
    val B2Map = mapOf(0 to null, 1 to null, 2 to null, 3 to null, 4 to null, 5 to null, 6 to null, 7 to null, 8 to listOf(50), 9 to listOf(40), 10 to listOf(5,50), 11 to listOf(15,40), 12 to listOf(10), 13 to listOf(20,50), 14 to listOf(20,50), 15 to listOf(20,50), 16 to listOf(20), 17 to listOf(0,30,55), 18 to listOf(20,50), 19 to listOf(20,50), 20 to listOf(40), 21 to listOf(30), 22 to null, 23 to null)
    val C1Map = mapOf(0 to null, 1 to null, 2 to null, 3 to null, 4 to null, 5 to null, 6 to null, 7 to null, 8 to listOf(10,35), 9 to listOf(0,25,50), 10 to listOf(10,55), 11 to listOf(25,50), 12 to listOf(25), 13 to listOf(35), 14 to listOf(5,35), 15 to listOf(5,35), 16 to listOf(5,45), 17 to listOf(10,45), 18 to listOf(5,35), 19 to listOf(35), 20 to listOf(25), 21 to listOf(5), 22 to null, 23 to null)
    val C2Map = mapOf(0 to null, 1 to null, 2 to null, 3 to null, 4 to null, 5 to null, 6 to null, 7 to null, 8 to listOf(50), 9 to listOf(40), 10 to listOf(5,50), 11 to listOf(15,40), 12 to listOf(10), 13 to listOf(20,50), 14 to listOf(20,50), 15 to listOf(20,50), 16 to listOf(20), 17 to listOf(0,30,55), 18 to listOf(20,50), 19 to listOf(20,50), 20 to listOf(40), 21 to listOf(30), 22 to null, 23 to null)
    val daiyaMap = mapOf("2022-04-01" to "C", "2022-04-02" to null, "2022-04-03" to null, "2022-04-04" to "A", "2022-04-05" to "A", "2022-04-06" to "A", "2022-04-07" to "A", "2022-04-08" to "A", "2022-04-09" to null, "2022-04-10" to null, "2022-04-11" to "A", "2022-04-12" to "A", "2022-04-13" to "A", "2022-04-14" to "A", "2022-04-15" to "A", "2022-04-16" to null, "2022-04-17" to null, "2022-04-18" to "A", "2022-04-19" to "A", "2022-04-20" to "A", "2022-04-21" to "A", "2022-04-22" to "A", "2022-04-23" to null, "2022-04-24" to null, "2022-04-25" to "A", "2022-04-26" to "A", "2022-04-27" to "A", "2022-04-28" to "A", "2022-04-29" to null, "2022-04-30" to null, "2022-05-01" to "A", "2022-05-02" to "A", "2022-05-03" to "A", "2022-05-04" to "A", "2022-05-05" to "A", "2022-05-06" to "A", "2022-05-07" to null, "2022-05-08" to null, "2022-05-09" to "A", "2022-05-10" to "A", "2022-05-11" to "A", "2022-05-12" to "A", "2022-05-13" to "A", "2022-05-14" to null, "2022-05-15" to null, "2022-05-16" to "A", "2022-05-17" to "A", "2022-05-18" to "A", "2022-05-19" to "A", "2022-05-20" to "A", "2022-05-21" to null, "2022-05-22" to null, "2022-05-23" to "A", "2022-05-24" to "A", "2022-05-25" to "A", "2022-05-26" to "A", "2022-05-27" to "A", "2022-05-28" to null, "2022-05-29" to null, "2022-05-30" to "A", "2022-05-31" to "A", "2022-06-01" to "A", "2022-06-02" to "A", "2022-06-03" to "A", "2022-06-04" to "C", "2022-06-05" to null, "2022-06-06" to "A", "2022-06-07" to "A", "2022-06-08" to "A", "2022-06-09" to "A", "2022-06-10" to "A", "2022-06-11" to null, "2022-06-12" to null, "2022-06-13" to "A", "2022-06-14" to "A", "2022-06-15" to "A", "2022-06-16" to "A", "2022-06-17" to "A", "2022-06-18" to null, "2022-06-19" to null, "2022-06-20" to "A", "2022-06-21" to "A", "2022-06-22" to "A", "2022-06-23" to "A", "2022-06-24" to "A", "2022-06-25" to null, "2022-06-26" to null, "2022-06-27" to "A", "2022-06-28" to "A", "2022-06-29" to "A", "2022-06-30" to "A", "2022-07-01" to "A", "2022-07-02" to null, "2022-07-03" to null, "2022-07-04" to "A", "2022-07-05" to "A", "2022-07-06" to "A", "2022-07-07" to "A", "2022-07-08" to "A", "2022-07-09" to null, "2022-07-10" to null, "2022-07-11" to "A", "2022-07-12" to "A", "2022-07-13" to "A", "2022-07-14" to "A", "2022-07-15" to "A", "2022-07-16" to null, "2022-07-17" to null, "2022-07-18" to null, "2022-07-19" to "A", "2022-07-20" to "A", "2022-07-21" to "A", "2022-07-22" to "A", "2022-07-23" to null, "2022-07-24" to null, "2022-07-25" to "A", "2022-07-26" to "A", "2022-07-27" to "A", "2022-07-28" to "A", "2022-07-29" to "A", "2022-07-30" to null, "2022-07-31" to null, "2022-08-01" to "A", "2022-08-02" to "A", "2022-08-03" to "A", "2022-08-04" to "A", "2022-08-05" to "C", "2022-08-06" to null, "2022-08-07" to null, "2022-08-08" to "C", "2022-08-09" to "A", "2022-08-10" to "C", "2022-08-11" to null, "2022-08-12" to "C", "2022-08-13" to null, "2022-08-14" to null, "2022-08-15" to null, "2022-08-16" to null, "2022-08-17" to "C", "2022-08-18" to "C", "2022-08-19" to "C", "2022-08-20" to null, "2022-08-21" to null, "2022-08-22" to "C", "2022-08-23" to "C", "2022-08-24" to "C", "2022-08-25" to "C", "2022-08-26" to "C", "2022-08-27" to null, "2022-08-28" to null, "2022-08-29" to "C", "2022-08-30" to "C", "2022-08-31" to "C", "2022-09-01" to "C", "2022-09-02" to "C", "2022-09-03" to null, "2022-09-04" to null, "2022-09-05" to "C", "2022-09-06" to "C", "2022-09-07" to "C", "2022-09-08" to "C", "2022-09-09" to "C", "2022-09-10" to null, "2022-09-11" to null, "2022-09-12" to "C", "2022-09-13" to "C", "2022-09-14" to "C", "2022-09-15" to "C", "2022-09-16" to "C", "2022-09-17" to null, "2022-09-18" to null, "2022-09-19" to null, "2022-09-20" to "A", "2022-09-21" to "B", "2022-09-22" to "A", "2022-09-23" to "A", "2022-09-24" to null, "2022-09-25" to null, "2022-09-26" to "A", "2022-09-27" to "A", "2022-09-28" to "A", "2022-09-29" to "A", "2022-09-30" to "A", "2022-10-01" to null, "2022-10-02" to null, "2022-10-03" to "A", "2022-10-04" to "A", "2022-10-05" to "A", "2022-10-06" to "B", "2022-10-07" to "B", "2022-10-08" to "A", "2022-10-09" to "A", "2022-10-10" to null, "2022-10-11" to "A", "2022-10-12" to "A", "2022-10-13" to "A", "2022-10-14" to "A", "2022-10-15" to null, "2022-10-16" to null, "2022-10-17" to "A", "2022-10-18" to "A", "2022-10-19" to "A", "2022-10-20" to "A", "2022-10-21" to "A", "2022-10-22" to "C", "2022-10-23" to null, "2022-10-24" to "A", "2022-10-25" to "A", "2022-10-26" to "A", "2022-10-27" to "A", "2022-10-28" to "A", "2022-10-29" to "A", "2022-10-30" to "A", "2022-10-31" to "A", "2022-11-01" to "B", "2022-11-02" to "A", "2022-11-03" to null, "2022-11-04" to "A", "2022-11-05" to null, "2022-11-06" to null, "2022-11-07" to "A", "2022-11-08" to "A", "2022-11-09" to "A", "2022-11-10" to "A", "2022-11-11" to "A", "2022-11-12" to "A", "2022-11-13" to null, "2022-11-14" to "A", "2022-11-15" to "A", "2022-11-16" to "A", "2022-11-17" to "A", "2022-11-18" to "A", "2022-11-19" to "A", "2022-11-20" to "A", "2022-11-21" to "A", "2022-11-22" to "A", "2022-11-23" to null, "2022-11-24" to "A", "2022-11-25" to "A", "2022-11-26" to "A", "2022-11-27" to "A", "2022-11-28" to "A", "2022-11-29" to "A", "2022-11-30" to "A", "2022-12-01" to "A", "2022-12-02" to "A", "2022-12-03" to "A", "2022-12-04" to null, "2022-12-05" to "A", "2022-12-06" to "A", "2022-12-07" to "A", "2022-12-08" to "A", "2022-12-09" to "A", "2022-12-10" to null, "2022-12-11" to null, "2022-12-12" to "A", "2022-12-13" to "A", "2022-12-14" to "A", "2022-12-15" to "A", "2022-12-16" to "A", "2022-12-17" to "A", "2022-12-18" to null, "2022-12-19" to "A", "2022-12-20" to "A", "2022-12-21" to "A", "2022-12-22" to "A", "2022-12-23" to "A", "2022-12-24" to null, "2022-12-25" to null, "2022-12-26" to null, "2022-12-27" to null, "2022-12-28" to null, "2022-12-29" to null, "2022-12-30" to null, "2022-12-31" to null, "2023-01-01" to null, "2023-01-02" to null, "2023-01-03" to null, "2023-01-04" to null, "2023-01-05" to null, "2023-01-06" to "C", "2023-01-07" to null, "2023-01-08" to null, "2023-01-09" to null, "2023-01-10" to "C", "2023-01-11" to "C", "2023-01-12" to "A", "2023-01-13" to "A", "2023-01-14" to "A", "2023-01-15" to "A", "2023-01-16" to "A", "2023-01-17" to "A", "2023-01-18" to "A", "2023-01-19" to "A", "2023-01-20" to null, "2023-01-21" to null, "2023-01-22" to null, "2023-01-23" to "A", "2023-01-24" to "A", "2023-01-25" to "A", "2023-01-26" to "C", "2023-01-27" to "A", "2023-01-28" to "A", "2023-01-29" to "A", "2023-01-30" to "A", "2023-01-31" to "A", "2023-02-01" to "A", "2023-02-02" to "A", "2023-02-03" to "A", "2023-02-04" to null, "2023-02-05" to null, "2023-02-06" to "A", "2023-02-07" to "A", "2023-02-08" to "A", "2023-02-09" to "A", "2023-02-10" to "A", "2023-02-11" to null, "2023-02-12" to null, "2023-02-13" to "C", "2023-02-14" to "C", "2023-02-15" to "C", "2023-02-16" to "A", "2023-02-17" to "A", "2023-02-18" to null, "2023-02-19" to null, "2023-02-20" to "C", "2023-02-21" to "A", "2023-02-22" to "A", "2023-02-23" to null, "2023-02-24" to "A", "2023-02-25" to null, "2023-02-26" to null, "2023-02-27" to "C", "2023-02-28" to "C", "2023-03-01" to "C", "2023-03-02" to "C", "2023-03-03" to "C", "2023-03-04" to null, "2023-03-05" to null, "2023-03-06" to "C", "2023-03-07" to "C", "2023-03-08" to "C", "2023-03-09" to "C", "2023-03-10" to "C", "2023-03-11" to null, "2023-03-12" to null, "2023-03-13" to "C", "2023-03-14" to "C", "2023-03-15" to "C", "2023-03-16" to "C", "2023-03-17" to "C", "2023-03-18" to null, "2023-03-19" to null, "2023-03-20" to "C", "2023-03-21" to null, "2023-03-22" to "C", "2023-03-23" to "A", "2023-03-24" to "C", "2023-03-25" to null, "2023-03-26" to null, "2023-03-27" to "C", "2023-03-28" to "C", "2023-03-29" to "C", "2023-03-30" to "C", "2023-03-31" to "C")

    // 今日のダイヤを取り出す
    val daiya: String? = daiyaMap[today]

    when (daiya) {
        "A" -> return daiyaData(daiya, A1Map, A2Map, 0, 20)

        "B" -> return daiyaData(daiya, B1Map, B2Map, 0, 50)

        "C" -> return daiyaData(daiya, C1Map, C2Map, 10, 50)
    }

    // ABCのどのダイヤでもない時!!!
    return daiyaData(null, mapOf(0 to null), mapOf(0 to null), 0, 0)
}


fun getNextMin(timeTable: Map<Int, List<Int>?>, hour: Int): NextInfo {
    /*
    次の出発時間を調べる関数
    :param timeTable:   # array:時刻表(分)の配列データ
    :param hour:        # int：現在時間(時)
    :return:            # array：result  大学行きの今日の時刻表データ,
                        # int：hour      現在時間(時) or 現在時間+1(時)
                        # bool：flag     現在時間に+1されたかのフラグ
    */

    var result: Int = getNearVal(timeTable[hour], mode = 0)

    return if (result == -1) {
        // Triple(getNearVal(timeTable[hour + 1], mode = 1), hour+1, true)
        NextInfo(getNearVal(timeTable[hour + 1], mode = 1), hour + 1, true)
    } else {
        // Triple(result, hour, false)
        NextInfo(result, hour, false)
    }
}


fun getAfterNextMin(flag: Boolean, timeTable: Map<Int, List<Int>?>, hour: Int, hour2: Int): AfterNextInfo {
    /*
    次の次の出発時間を調べる関数
    :param flag:        # 現在時間に+1されたかのフラグ
    :param timeTable:   # array:時刻表(分)の配列データ
    :param hour:        # int：現在時間(時)
    :param hour2:       # int：現在時間(時) or 現在時間+1(時)
    :return:            # int：result  次の次の出発時間(分),
                        # int：hour2   現在時間(時) or 現在時間+1(時) or 現在時間+2(時)
    */

    var result: Int
    var retHour: Int = hour2

    if (!flag) {
        result = getNearVal(timeTable[hour], mode = 3)
        if (result == -1) {
            result = getNearVal(timeTable[hour + 1], mode = 1)
            retHour += 1
        }
    } else {
        result = getNearVal(timeTable[hour + 1], mode = 3, isReset = true);
        retHour += 1
        if (result == -1) {
            result = getNearVal(timeTable[hour + 2], mode = 1)
            retHour += 1
        }
    }
    return AfterNextInfo(result, retHour)
}


fun getAllData(): ReturnData {
    val todayInfo = getTodayTimetable()     // 今日の運行時刻表を取得(バスが無い日はnull, null, 0, 0が返ってくる)
    val hour: Int = LocalTime.now().hour-10  // 現在の時間(hour)を取得

    //　大学行きの次のバスの出発時間を調べる(-1が帰ってきた場合はその時間のバスはもう無い)
    val toDaigakuInfo = getNextMin(todayInfo.toDaigaku, hour)

    //　八草行きの次のバスの出発時間を調べる(-1が帰ってきた場合はその時間のバスはもう無い)
    val toYakusaInfo = getNextMin(todayInfo.toYakusa, hour)

    // 次の次のバスの出発時刻を調べる(大学行き)
    val toDaigakuAfterNext = getAfterNextMin(toDaigakuInfo.flag, todayInfo.toDaigaku, hour, hour)

    // 次の次のバスの出発時刻を調べる(八草行き)
    val toYakusaAfterNext = getAfterNextMin(toYakusaInfo.flag, todayInfo.toYakusa, hour, hour)

    return ReturnData(todayInfo, toDaigakuInfo, toYakusaInfo, toDaigakuAfterNext, toYakusaAfterNext)
}