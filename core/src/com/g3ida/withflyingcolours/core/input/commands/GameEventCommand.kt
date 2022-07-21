package com.g3ida.withflyingcolours.core.input.commands

import com.badlogic.gdx.utils.ObjectMap
import com.g3ida.withflyingcolours.core.common.GameSettings
import com.g3ida.withflyingcolours.core.events.EventType
import com.g3ida.withflyingcolours.core.events.GameEvent
import ktx.collections.gdxMapOf

open class GameEventCommand(val event: GameEvent): ICommand {
    override fun run() {
        GameSettings.eventHandler.dispatchEvent(event)
    }
}

fun GameEvent.toCommand(): ICommand {
    return GameEventCommand(this)
}

fun EventType.toGameEventCommand(extraData: ObjectMap<String, String> = gdxMapOf()): ICommand {
    return GameEvent(this, extraData).toCommand()
}
