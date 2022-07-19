package com.g3ida.withflyingcolours.core.input

import com.badlogic.gdx.Input
import com.g3ida.withflyingcolours.core.input.commands.ICommand
import ktx.app.KtxInputAdapter
import ktx.collections.gdxArrayOf

class KeyboardHandler: KtxInputAdapter {

    private val keyDownCommands = gdxArrayOf<ICommand>(initialCapacity = Input.Keys.MAX_KEYCODE)
    private val keyUpCommands = gdxArrayOf<ICommand>(initialCapacity = Input.Keys.MAX_KEYCODE)

    fun mapCommand(key : KeyboardKey,action: KeyboardAction, command: ICommand) {
        when(action) {
            KeyboardAction.Down -> keyDownCommands[key.keycode] = command
            KeyboardAction.Up -> keyUpCommands[key.keycode] = command
        }
    }

    init {
        keyUpCommands.insertRange(0, Input.Keys.MAX_KEYCODE)
        keyDownCommands.insertRange(0, Input.Keys.MAX_KEYCODE)
    }

    override fun keyDown(keycode: Int): Boolean {
        keyDownCommands[keycode]?.run()
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        keyUpCommands[keycode]?.run()
        return true
    }

    fun update(delta: Float) {
        keyUpCommands
            .filter { it is IWithUpdate }
            .forEach { (it as IWithUpdate).update(delta) }

        keyDownCommands
            .filter { it is IWithUpdate }
            .forEach { (it as IWithUpdate).update(delta) }
    }
}