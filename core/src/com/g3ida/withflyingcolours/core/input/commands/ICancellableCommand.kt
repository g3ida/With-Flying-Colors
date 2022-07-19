package com.g3ida.withflyingcolours.core.input.commands

import com.g3ida.withflyingcolours.core.input.KeyboardHandler
import com.g3ida.withflyingcolours.core.input.KeyboardAction
import com.g3ida.withflyingcolours.core.input.KeyboardKey

interface ICancellableCommand: ICommand {
    fun cancel()
}

fun KeyboardHandler.mapCancellableCommand(key : KeyboardKey, command: ICancellableCommand) {
    this.mapCommand(key, KeyboardAction.Down, command)
    this.mapCommand(key, KeyboardAction.Up) { command.cancel() }
}