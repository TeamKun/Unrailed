package net.kunmc.lab.unrailed.game

class LaneState(val lane: LaneInstance) {
    /**
     * このレーンが今所持している鍵の数
     */
    var keys = 0

    /**
     * 今アップグレードの時間かどうか
     */
    var isUpgradeTime = false
}