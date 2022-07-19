package com.g3ida.withflyingcolours.core.events

interface IEventProcessor {
    fun subscribe(eventType: EventType, listener: IEventListener)
    fun unsubscribe(listener: IEventListener)
    fun unsubscribe(eventType: EventType, listener: IEventListener)
    fun dispatchEvent(event: GameEvent)
}
