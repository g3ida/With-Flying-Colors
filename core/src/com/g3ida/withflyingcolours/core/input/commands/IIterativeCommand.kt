package com.g3ida.withflyingcolours.core.input.commands

interface IIterativeCommand: ICommand {
    fun update(delta: Float)
}