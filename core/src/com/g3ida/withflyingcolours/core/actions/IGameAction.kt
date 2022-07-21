package com.g3ida.withflyingcolours.core.actions

import com.g3ida.withflyingcolours.core.events.GameEvent

interface IGameAction {
    fun execute(event: GameEvent)
    fun step(delta: Float)
}