package com.g3ida.withflyingcolours.core.common.animation

import com.badlogic.gdx.math.Interpolation
import games.rednblack.editor.renderer.components.TransformComponent

interface ITransformAnimation {
    val animationDuration: Float
    val interpolation: Interpolation
    val centerOriginX: Boolean
    val centerOriginY: Boolean

    fun isRunning(): Boolean
    fun isDone(): Boolean
    fun step(transform: TransformComponent, deltaTime: Float)
    fun reset(transform: TransformComponent?)
    fun start()
}