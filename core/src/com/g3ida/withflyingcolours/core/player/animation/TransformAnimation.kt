package com.g3ida.withflyingcolours.core.player.animation

import games.rednblack.editor.renderer.components.TransformComponent
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.g3ida.withflyingcolours.core.extensions.*
import kotlin.math.abs

open class TransformAnimation(
    override val animationDuration: Float,
    override val interpolation: Interpolation,
    override val centerOriginX: Boolean = true,
    override val centerOriginY: Boolean = true): ITransformAnimation {

    private var mTimer = 0f
    private var mStarted = false

    override fun isDone() = mTimer <= 0f && mStarted
    override fun isRunning() = mTimer > 0f

    override fun start() {
        if (isRunning()) return
        mTimer = animationDuration
        mStarted = true
    }

    override fun step(transform: TransformComponent, deltaTime: Float) {
        if (mTimer > 0f) {
            // if the player is rotated we should react according to the actual direction
            val sinCosRotation = sincos(transform.rotation * Float.PI / 180.0f)
            val (sinRotation, cosRotation) = sinCosRotation
            val normalized = mTimer / animationDuration
            val mean = 1f
            val i = interpolation.apply(0f, 1f, normalized) - mean

            transform.scaleX = mean + (i * abs(cosRotation) - abs(sinRotation) * i)
            transform.scaleY = mean + (i * abs(sinRotation) - abs(cosRotation) * i)

            if (!centerOriginY) {
                transform.origin = 0.5f + (1f - transform.scale) * 0.5f * sinCosRotation
            }
            if (!centerOriginX) {
                transform.origin = 0.5f + (1f - transform.scale) * 0.5f * sinCosRotation.swapped
            }
            mTimer -= deltaTime
        } else {
            reset(transform)
        }
    }

    override fun reset(transform: TransformComponent?) {
        mTimer = 0f
        mStarted = false
        if (transform != null) {
            transform.scale = Vector2(1f, 1f)
            transform.origin = Vector2(0.5f, 0.5f)
        }
    }
}