package com.g3ida.withflyingcolours.core.input.commands

import com.g3ida.withflyingcolours.core.common.GameSettings
import com.g3ida.withflyingcolours.core.events.GameEvent
import com.g3ida.withflyingcolours.core.events.EventType

class PlayerJumpCommand: ICommand {
    override fun run() {
        GameSettings.eventHandler.dispatchEvent(GameEvent(EventType.JumpCommand))
    }
}