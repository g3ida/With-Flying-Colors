package com.g3ida.withflyingcolours.core.input.commands

import com.g3ida.withflyingcolours.core.common.GameSettings
import com.g3ida.withflyingcolours.core.events.GameEvent
import com.g3ida.withflyingcolours.core.events.EventType
import com.g3ida.withflyingcolours.utils.CountdownTimer

class PlayerJumpCommand(val isGrounded: () -> Boolean): PermissiveCommand(), ICancellableCommand {

    private val timeUntilFullJumpIsConsidered = 0.15f
    private var jumpTimer: CountdownTimer = CountdownTimer(timeUntilFullJumpIsConsidered, isSet = false)

    override fun execute() {
        jumpTimer.reset()
        GameSettings.eventHandler.dispatchEvent(GameEvent(EventType.JumpCommand))
    }

    override fun update(delta: Float) {
        super.update(delta)
        jumpTimer.step(delta)
    }

    override fun cancel() {
        if (jumpTimer.isRunning()) {
            GameSettings.eventHandler.dispatchEvent(GameEvent(EventType.CancelJump))
            jumpTimer.stop()
        }
    }

    override fun canExecute(): Boolean = isGrounded()
}