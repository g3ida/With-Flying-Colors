package com.g3ida.withflyingcolours.core.player.movement

import com.badlogic.gdx.utils.Disposable
import com.g3ida.withflyingcolours.core.GameSettings
import com.g3ida.withflyingcolours.core.events.EventType
import com.g3ida.withflyingcolours.core.events.GameEvent
import com.g3ida.withflyingcolours.core.events.IEventListener

class EventActionListener(val eventType: EventType, val action: IGameAction): IEventListener, Disposable {

    init {
        GameSettings.eventHandler.subscribe(eventType, this)
    }

    override fun onEvent(event: GameEvent) {
        action.execute()
    }

    override fun dispose() {
        GameSettings.eventHandler.unsubscribe(eventType,this)
    }
}

fun IGameAction.toActionListener(eventType: EventType): EventActionListener {
    return EventActionListener(eventType, this)
}