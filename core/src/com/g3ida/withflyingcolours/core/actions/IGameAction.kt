package com.g3ida.withflyingcolours.core.actions

interface IGameAction {
    fun execute()
    fun step(delta: Float)
}