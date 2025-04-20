package app.trailblazercombi.haventide.game2.data.turntable

fun countedDiceMatch(actual: Pair<Int, Int>, required: Pair<Int, Int>): Boolean {
    // 1. Check if we have enough aligned dice, or if we have spare...
    val spareAligned = actual.first - required.first
    if (spareAligned < 0) return false // If this failed, there aren't enough aligned dice...
    return actual.second + spareAligned == required.second
}
