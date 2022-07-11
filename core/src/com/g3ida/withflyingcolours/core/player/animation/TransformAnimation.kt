package com.g3ida.withflyingcolours.core.player.animation

import games.rednblack.editor.renderer.components.TransformComponent
import com.badlogic.gdx.math.Interpolation
import com.g3ida.withflyingcolours.Utils
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class TransformAnimation(private val _animationDuration: Float,
                         private val _interpolation: Interpolation,
                         private val _centerOriginX: Boolean,
                         private val _centerOriginY: Boolean) {
    private var _timer = 0f
    fun start() {
        if (isRunning) return
        _timer = _animationDuration
    }

    val isRunning: Boolean
        get() = _timer > 0f

    fun step(transform: TransformComponent, deltaTime: Float) {
        if (_timer > 0f) {

            // if the player is rotated we should react according to the actual direction
            val cosRotation = cos(transform.rotation * Utils.PI / 180.0).toFloat()
            val sinRotation = sin(transform.rotation * Utils.PI / 180.0).toFloat()
            val normalized = _timer / _animationDuration
            val mean = 1f
            val i = _interpolation.apply(0f, 1f, normalized) - mean
            transform.scaleX = mean + (i * abs(cosRotation) - abs(sinRotation) * (i + 0.00f))
            transform.scaleY = mean + (i * abs(sinRotation) - abs(cosRotation) * (i + 0.00f))
            if (!_centerOriginY) {
                transform.originY = 0.5f + (1f - transform.scaleY) * 0.5f * cosRotation
                transform.originX = 0.5f + (1f - transform.scaleX) * 0.5f * sinRotation
            }
            if (!_centerOriginX) {
                transform.originX = 0.5f + (1f - transform.scaleX) * 0.5f * cosRotation
                transform.originY = 0.5f + (1f - transform.scaleY) * 0.5f * sinRotation
            }
            _timer -= deltaTime
        } else {
            reset(transform)
        }
    }

    fun reset(transform: TransformComponent?) {
        _timer = 0f
        if (transform != null) {
            transform.scaleX = 1f
            transform.scaleY = 1f
            transform.originY = 0.5f
            transform.originX = 0.5f
        }
    }
}