package com.g3ida.withflyingcolours.core.actions

import com.g3ida.withflyingcolours.core.events.GameEvent
import com.g3ida.withflyingcolours.utils.MoveDirection
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import kotlin.math.abs

class PlayerWalkAction(val physicsBodyComponent: PhysicsBodyComponent): IGameAction  {
    val force = 800f
    val speed = 5f
    override fun execute(event: GameEvent) {
        val direction: Int = event.extraData.get("direction", "1").toIntOrNull() ?: 1
        val velocity = physicsBodyComponent.body.linearVelocity.x
        val maxSpeed = direction * speed
        if (direction <= 0f && velocity >= maxSpeed || direction >= 0f && velocity <= maxSpeed) {
            val coefficient = abs(maxSpeed) / (abs(velocity)+1)
            physicsBodyComponent.body.applyForceToCenter(direction * force * coefficient, 0f, true)
        }
    }

    override fun step(delta: Float) {}
}