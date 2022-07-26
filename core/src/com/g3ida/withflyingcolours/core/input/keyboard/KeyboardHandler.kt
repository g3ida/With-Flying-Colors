package com.g3ida.withflyingcolours.core.input.keyboard

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.g3ida.withflyingcolours.core.events.EventType
import com.g3ida.withflyingcolours.core.input.commands.ICommand
import com.g3ida.withflyingcolours.core.input.commands.toGameEventCommand
import ktx.app.KtxInputAdapter
import ktx.collections.gdxArrayOf
import ktx.collections.gdxMapOf

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

    fun applyDefaultMapping() {
        mapCommand(KeyboardKey.UP,KeyboardAction.KeyPressed, EventType.JumpCommand.toGameEventCommand())
        mapCommand(KeyboardKey.UP,KeyboardAction.KeyReleased, EventType.CancelJumpCommand.toGameEventCommand())
        mapCommand(KeyboardKey.C, KeyboardAction.KeyPressed, EventType.RotateCommand.toGameEventCommand(
            gdxMapOf("direction" to "-1")
        ))
        mapCommand(KeyboardKey.Z, KeyboardAction.KeyPressed, EventType.RotateCommand.toGameEventCommand(
            gdxMapOf("direction" to "1")
        ))
        mapCommand(KeyboardKey.LEFT, KeyboardAction.KeyDown, EventType.MoveCommand.toGameEventCommand(
            gdxMapOf("direction" to "-1")
        ))
        mapCommand(KeyboardKey.RIGHT, KeyboardAction.KeyDown, EventType.MoveCommand.toGameEventCommand(
            gdxMapOf("direction" to "1")
        ))
    }
}