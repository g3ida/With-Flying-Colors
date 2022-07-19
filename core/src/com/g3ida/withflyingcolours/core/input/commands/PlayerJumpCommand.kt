package com.g3ida.withflyingcolours.core.input.commands

import com.g3ida.withflyingcolours.core.extensions.EPSILON
import com.g3ida.withflyingcolours.utils.CountdownTimer
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import kotlin.math.abs

class PlayerJumpCommand(val physicsBody: PhysicsBodyComponent): PermissiveCommand(), ICancellableCommand {

    private val timeUntilFullJumpIsConsidered = 0.15f
    private var jumpTimer: CountdownTimer = CountdownTimer(timeUntilFullJumpIsConsidered, isSet = false)
    private val jumpForce = 760.0f // the force applied to perform the jump.

    override fun execute() {
        jumpTimer.reset()
        physicsBody.body.applyLinearImpulse(0f, jumpForce, physicsBody.body.worldCenter.x, physicsBody.body.worldCenter.y, true)
    }

    override fun update(delta: Float) {
        super.update(delta)
        jumpTimer.step(delta)
    }

    override fun cancel() {
        if (jumpTimer.isRunning()) {
            //cancel jump
            val velocity = physicsBody.body.linearVelocity
            if (velocity.y > 0f) { // decrease velocity only if the player is going up !
                physicsBody.body.setLinearVelocity(velocity.x, velocity.y * 0.5f)
            }
            jumpTimer.stop()
        }
    }

    override fun canExecute(): Boolean = isGrounded()
    private fun isGrounded(): Boolean = abs(physicsBody.body.linearVelocity.y) < Float.EPSILON
}