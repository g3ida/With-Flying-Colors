package com.g3ida.withflyingcolours.core.input.commands

import com.g3ida.withflyingcolours.core.input.keyboard.KeyboardHandler
import com.g3ida.withflyingcolours.core.input.keyboard.KeyboardAction
import com.g3ida.withflyingcolours.core.input.keyboard.KeyboardKey

interface ICancellableCommand: ICommand {
    fun cancel()
}

fun KeyboardHandler.mapCancellableCommand(key : KeyboardKey, command: ICancellableCommand) {
    this.mapCommand(key, KeyboardAction.KeyPressed, command)
    this.mapCommand(key, KeyboardAction.KeyReleased) { command.cancel() }
}