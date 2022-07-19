package com.g3ida.withflyingcolours.core.events

fun interface IEventListener {
    fun onEvent(event: GameEvent)
}