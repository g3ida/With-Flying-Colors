package com.g3ida.withflyingcolours.core.input.commands

import com.g3ida.withflyingcolours.core.common.GameSettings
import com.g3ida.withflyingcolours.core.events.EventType
import com.g3ida.withflyingcolours.core.events.GameEvent

class PlayerCancelJumpCommand: ICommand {
    override fun run() {
        GameSettings.eventHandler.dispatchEvent(GameEvent(EventType.CancelJumpCommand))
    }
}