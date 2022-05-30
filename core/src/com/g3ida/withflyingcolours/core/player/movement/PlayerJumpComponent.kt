package com.g3ida.withflyingcolours.core.player.movement

import com.artemis.PooledComponent
import com.g3ida.withflyingcolours.core.player.PlayerControllerSettings

class PlayerJumpComponent : PooledComponent() {
    var shouldJump = false // set this to true in order to perform jump. turn it to false once the jump key released.

    //internal attributes
    var timeUntilFullJumpIsConsidered = 0.55f // minimum amount of time needed to press the jump button in order to consider it as full jump.
    var jumpTimer = 0f // time while jumping.
    var timeSinceGrounded = 0f // last time since the player hit the ground.
    var jumpForce = 760.0f // the force applied to perform the jump.
    var responsivenessTimer = 0f // time since the player hit the jump button.
    var oldShouldJump = false // previous value of should jump.
    public override fun reset() {
        responsivenessTimer = 0f
        timeUntilFullJumpIsConsidered = 0.15f
        timeSinceGrounded = 0f
        jumpTimer = 0f
        oldShouldJump = false
        shouldJump = true
    }

    companion object {
        val jumpSettings = PlayerControllerSettings(0.08f, 0.07f)
    }
}