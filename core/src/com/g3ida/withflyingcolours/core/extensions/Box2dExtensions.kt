package com.g3ida.withflyingcolours.core.extensions

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import kotlin.math.abs

fun Body.addDragForce(dragForcePower: Float) {
    val velocity = this.linearVelocity
    val dragForce = Vector2(velocity.x - dragForcePower * abs(velocity.x) * velocity.x, 0.0f)
    this.applyForceToCenter(dragForce, true)
}

fun Body.setLinearVelocityX(velocity: Float) {
    this.setLinearVelocity(velocity, this.linearVelocity.y)
}

fun Body.setLinearVelocityY(velocity: Float) {
    this.setLinearVelocity(this.linearVelocity.y, velocity)
}