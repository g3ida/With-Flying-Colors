package com.g3ida.withflyingcolours.core.actions

import com.g3ida.withflyingcolours.utils.MoveDirection
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import kotlin.math.abs

class PlayerWalkAction(val direction: MoveDirection, val physicsBodyComponent: PhysicsBodyComponent): IGameAction  {
    val force = 800f
    val speed = 5f
    override fun execute() {
        val velocity = physicsBodyComponent.body.linearVelocity.x
        val maxSpeed = direction.value * speed
        if (direction.value <= 0f && velocity >= maxSpeed || direction.value >= 0f && velocity <= maxSpeed) {
            val coefficient = abs(maxSpeed) / (abs(velocity)+1)
            physicsBodyComponent.body.applyForceToCenter(direction.value * force * coefficient, 0f, true)
        }
    }

    override fun step(delta: Float) {}
}