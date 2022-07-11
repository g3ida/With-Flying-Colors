package com.g3ida.withflyingcolours

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.g3ida.withflyingcolours.screens.GameScreen

class WithFlyingColours : Game() {
    lateinit var batch: SpriteBatch
    override fun create() {
        batch = SpriteBatch()
        setScreen(GameScreen())
    }

    override fun render() {
        clearScreen()
        super.render()
    }

    override fun dispose() {
        batch.dispose()
    }

    fun clearScreen() {
        val gl = Gdx.gl
        gl.glClearColor(0.1490f, 0.1764f, 0.1960f, 1.0f)
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }
}