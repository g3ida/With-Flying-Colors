package com.g3ida.withflyingcolours

import com.badlogic.gdx.Gdx
import com.g3ida.withflyingcolours.core.input.keyboard.KeyboardHandler
import com.g3ida.withflyingcolours.screens.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class Game : KtxGame<KtxScreen>() {
    val inputManager: KeyboardHandler by lazy { KeyboardHandler() }
    override fun create() {
        addScreen(GameScreen())
        setScreen<GameScreen>()
        Gdx.input.inputProcessor = inputManager
        super.create()
    }

    override fun render() {
        inputManager.update(Gdx.graphics.getDeltaTime())
        super.render()
    }

    override fun dispose() {
        super.dispose()
    }
}