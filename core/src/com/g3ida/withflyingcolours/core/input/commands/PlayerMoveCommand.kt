package com.g3ida.withflyingcolours.core.input.commands

import com.g3ida.withflyingcolours.core.common.GameSettings
import com.g3ida.withflyingcolours.core.events.EventType
import com.g3ida.withflyingcolours.core.events.GameEvent
import com.g3ida.withflyingcolours.utils.MoveDirection

class PlayerMoveCommand(val direction: MoveDirection): ICommand {
    val eventType by lazy {
        when(direction) {
            MoveDirection.Left -> EventType.MoveLeftCommand
            MoveDirection.Right -> EventType.MoveRightCommand
        }
    }

    override fun run() {
        GameSettings.eventHandler.dispatchEvent(GameEvent(eventType))
    }
}