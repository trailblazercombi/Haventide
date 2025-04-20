package app.trailblazercombi.haventide.game2.data.turntable

import app.trailblazercombi.haventide.resources.DieType
import com.sun.jdi.AbsentInformationException
import kotlin.random.Random

fun randomDie(): Die {
    return when (Random.nextInt(0, 3)) {
        0 -> Die(DieType.VANGUARD)
        1 -> Die(DieType.SENTINEL)
        2 -> Die(DieType.MEDIC)
        else -> throw AbsentInformationException(
            "It is not possible to create an INVALID die :: RandomDie.new()"
        )
    }
}
