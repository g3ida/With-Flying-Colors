package com.g3ida.withflyingcolours.core.input.commands

import com.g3ida.withflyingcolours.core.GameSettings
import com.g3ida.withflyingcolours.core.events.EventType
import com.g3ida.withflyingcolours.core.events.GameEvent
import com.g3ida.withflyingcolours.utils.RotationDirection

class PlayerRotationCommand(direction: RotationDirection): ICommand {
    val eventType by lazy {
        when(direction) {
            RotationDirection.Clockwise -> EventType.RotateRightCommand
            RotationDirection.AntiClockwise -> EventType.RotateLeftCommand
        }
    }

    override fun run() {
        GameSettings.eventHandler.dispatchEvent(GameEvent(eventType))
    }
}