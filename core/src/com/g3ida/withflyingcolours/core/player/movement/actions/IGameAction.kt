package com.g3ida.withflyingcolours.core.player.movement.actions

interface IGameAction {
    fun execute()
    fun step(delta: Float)
}