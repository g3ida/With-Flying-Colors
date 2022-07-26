package com.g3ida.withflyingcolours.utils

import kotlin.math.max

class CountdownTimer(val duration: Float, isSet: Boolean = true) {
    var timer = if (isSet) duration else 0f
    fun reset() { timer = duration }
    fun isRunning() = timer > 0f
    fun step(delta: Float) { timer = max(timer - delta, 0f) }
    fun stop() { timer = 0f }
}