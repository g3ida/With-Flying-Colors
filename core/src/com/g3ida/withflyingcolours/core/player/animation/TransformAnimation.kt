package com.g3ida.withflyingcolours.core.player.animation

import games.rednblack.editor.renderer.components.TransformComponent
import com.badlogic.gdx.math.Interpolation
import com.g3ida.withflyingcolours.Utils
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

open class TransformAnimation(
    override val animationDuration: Float,
    override val interpolation: Interpolation,
    override val centerOriginX: Boolean = true,
    override val centerOriginY: Boolean = true): ITransformAnimation {

    private var _timer = 0f
    private var _started = false

    override fun isDone() = _timer <= 0f && _started
    override fun isRunning() = _timer > 0f

    override fun start() {
        if (isRunning()) return
        _timer = animationDuration
        _started = true
    }

    override fun step(transform: TransformComponent, deltaTime: Float) {
        if (_timer > 0f) {

            // if the player is rotated we should react according to the actual direction
            val cosRotation = cos(transform.rotation * Utils.PI / 180.0).toFloat()
            val sinRotation = sin(transform.rotation * Utils.PI / 180.0).toFloat()
            val normalized = _timer / animationDuration
            val mean = 1f
            val i = interpolation.apply(0f, 1f, normalized) - mean
            transform.scaleX = mean + (i * abs(cosRotation) - abs(sinRotation) * (i + 0.00f))
            transform.scaleY = mean + (i * abs(sinRotation) - abs(cosRotation) * (i + 0.00f))
            if (!centerOriginY) {
                transform.originY = 0.5f + (1f - transform.scaleY) * 0.5f * cosRotation
                transform.originX = 0.5f + (1f - transform.scaleX) * 0.5f * sinRotation
            }
            if (!centerOriginX) {
                transform.originX = 0.5f + (1f - transform.scaleX) * 0.5f * cosRotation
                transform.originY = 0.5f + (1f - transform.scaleY) * 0.5f * sinRotation
            }
            _timer -= deltaTime
        } else {
            reset(transform)
        }
    }

    override fun reset(transform: TransformComponent?) {
        _timer = 0f
        _started = false
        if (transform != null) {
            transform.scaleX = 1f
            transform.scaleY = 1f
            transform.originY = 0.5f
            transform.originX = 0.5f
        }
    }
}