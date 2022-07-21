package com.g3ida.withflyingcolours.core.actions

import com.badlogic.gdx.physics.box2d.Body
import com.g3ida.withflyingcolours.utils.CountdownTimer
import com.g3ida.withflyingcolours.utils.extensions.isAlmostZero
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent

class PlayerJumpAction(val physicsBodyComponent: PhysicsBodyComponent): IGameAction {
    private val jumpForce = 760.0f
    private val permissiveness: Float = 0.07f
    private val timeUntilFullJumpIsConsidered = 0.15f

    private val body: Body = physicsBodyComponent.body
    //how much time we permit action since its conditions were met.
    private var mResponsivenessTimer = CountdownTimer(permissiveness, isSet = false)
    //how much do we delay action hoping its conditions to be met.
    private var mPermissivenessTimer = CountdownTimer(permissiveness, isSet = false)
    private var jumpTimer: CountdownTimer = CountdownTimer(timeUntilFullJumpIsConsidered, isSet = false)

    override fun execute() {
        if (canExecute() || mPermissivenessTimer.isRunning()) {
            mPermissivenessTimer.stop()
            mResponsivenessTimer.stop()
            jumpTimer.reset()
            body.run {
                applyLinearImpulse(0f, jumpForce, worldCenter.x, worldCenter.y, true)
            }
        } else {
            mResponsivenessTimer.reset()
        }
    }

    private fun isGrounded(): Boolean = physicsBodyComponent.body.linearVelocity.y.isAlmostZero
    private fun canExecute(): Boolean = isGrounded()

    override fun step(delta: Float) {
        if (canExecute()) {
            mPermissivenessTimer.reset()
            if (mResponsivenessTimer.isRunning()) {
                mResponsivenessTimer.stop()
                mPermissivenessTimer.stop()
                execute()
            }
        } else {
            mResponsivenessTimer.step(delta)
            mPermissivenessTimer.step(delta)
        }
        jumpTimer.step(delta)
    }

    inner class CancelJumpAction: IGameAction {
        override fun execute() {
            if (jumpTimer.isRunning()) {
                jumpTimer.stop()
                val velocity = body.linearVelocity
                if (velocity.y > 0f) { // decrease velocity only if the player is going up !
                    body.setLinearVelocity(velocity.x, velocity.y * 0.5f)
                }
            }
        }
        override fun step(delta: Float) {}
    }
}