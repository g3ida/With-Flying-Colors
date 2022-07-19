package com.g3ida.withflyingcolours.core.input.commands

abstract class ConditionalCommand: ICommand {
    protected open fun canExecute(): Boolean = true
    protected abstract fun execute()
    override fun run() = execute()
}