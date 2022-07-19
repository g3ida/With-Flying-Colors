package com.g3ida.withflyingcolours.core.events

import com.badlogic.gdx.utils.ObjectMap
import ktx.collections.GdxSet
import ktx.collections.gdxSetOf
import ktx.collections.set

class EventProcessor: IEventProcessor {
    private val listeners = ObjectMap<EventType, GdxSet<IEventListener>>()

    override fun subscribe(eventType: EventType, listener: IEventListener) {
        if (listeners.containsKey(eventType)) {
            listeners[eventType].add(listener)
        } else {
            listeners[eventType] = gdxSetOf(listener)
        }
    }

    override fun unsubscribe(listener: IEventListener) {
        listeners.values().forEach { it.remove(listener) }
    }

    override fun unsubscribe(eventType: EventType, listener: IEventListener) {
        val eventListeners = listeners[eventType]
        if (eventListeners != null && listener in eventListeners) {
            eventListeners.remove(listener)
        }
    }

    override fun dispatchEvent(event: GameEvent) {
        listeners.get(event.type, null)?.forEach { it.onEvent(event) }
    }
}