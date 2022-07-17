package com.g3ida.withflyingcolours

import com.g3ida.withflyingcolours.screens.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class Game : KtxGame<KtxScreen>() {

    override fun create() {
        addScreen(GameScreen())
        setScreen<GameScreen>()
        super.create()
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {
        super.dispose()
    }
}