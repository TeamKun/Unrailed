package net.kunmc.lab.unrailed.game

data class GameSetting(
    /**
     * 各駅到着後、いくつ鍵を増やすか
     */
    val keysPerStation: Int = 1,
    /**
     * 各駅到着後、どれぐらいPlayerのPointを増やすか
     */
    val pointPerStation: Int = 1,
    /**
     * 各駅到着後のアップグレードの時間の長さ
     */
    val ticksOfUpgrade: Long = 30 * 20,
    /**
     * ゲーム開始時の最初の準備の時間の長さ
     */
    val ticksOfStarting: Long = 30 * 20
)