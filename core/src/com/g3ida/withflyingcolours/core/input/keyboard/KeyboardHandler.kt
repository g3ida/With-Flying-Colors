package com.g3ida.withflyingcolours.core.input.keyboard

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.g3ida.withflyingcolours.core.input.commands.ICommand
import ktx.app.KtxInputAdapter
import ktx.collections.gdxArrayOf

class KeyboardHandler: KtxInputAdapter {

    private val keyPressedCommands = gdxArrayOf<ICommand>(initialCapacity = Input.Keys.MAX_KEYCODE)
    private val keyReleasedCommands = gdxArrayOf<ICommand>(initialCapacity = Input.Keys.MAX_KEYCODE)
    private val keyDownCommands = gdxArrayOf<ICommand>(initialCapacity = Input.Keys.MAX_KEYCODE)

    init {
        keyReleasedCommands.insertRange(0, Input.Keys.MAX_KEYCODE)
        keyPressedCommands.insertRange(0, Input.Keys.MAX_KEYCODE)
        keyDownCommands.insertRange(0, Input.Keys.MAX_KEYCODE)
    }

    fun mapCommand(key : KeyboardKey, action: KeyboardAction, command: ICommand) {
        when(action) {
            KeyboardAction.KeyPressed -> keyPressedCommands[key.keycode] = command
            KeyboardAction.KeyReleased -> keyReleasedCommands[key.keycode] = command
            KeyboardAction.KeyDown -> keyDownCommands[key.keycode] = command
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        keyPressedCommands[keycode]?.run()
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        keyReleasedCommands[keycode]?.run()
        return true
    }

    fun update() {
        keyDownCommands
            .withIndex()
            .filter{ Gdx.input.isKeyPressed(it.index) }
            .forEach { it.value?.run() }
    }
}