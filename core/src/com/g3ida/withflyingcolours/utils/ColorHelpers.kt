package com.g3ida.withflyingcolours.utils

import com.badlogic.gdx.graphics.Color
import java.util.*

fun String.toColor(): Color? {
    return when (this.lowercase(Locale.ROOT)) {
        "blue" -> Color.BLUE
        "green" -> Color.GREEN
        "yellow" -> Color.YELLOW
        "purple" -> Color.PURPLE
        "red" -> Color.RED
        else -> null
    }
}