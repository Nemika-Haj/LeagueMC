package dev.bytestobits.leaguemc

import org.bukkit.Location
import kotlin.math.sqrt

object Util {

    fun calculateDistance(loc1: Location, loc2: Location): Double {
        val dx = loc2.x - loc1.x
        val dy = loc2.y - loc1.y
        val dz = loc2.z - loc1.z

        return sqrt(dx*dx + dy*dy + dz*dz)
    }

}