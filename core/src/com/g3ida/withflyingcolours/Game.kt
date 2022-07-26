package com.g3ida.withflyingcolours

import com.badlogic.gdx.Gdx
import com.g3ida.withflyingcolours.core.ecs.components.*
import com.g3ida.withflyingcolours.core.input.keyboard.KeyboardHandler
import com.g3ida.withflyingcolours.screens.GameScreen
import games.rednblack.editor.renderer.utils.ComponentRetriever
import ktx.app.KtxGame
import ktx.app.KtxScreen

class Game : KtxGame<KtxScreen>() {
    val inputManager: KeyboardHandler by lazy { KeyboardHandler() }
    override fun create() {
        addScreen(GameScreen())
        setScreen<GameScreen>()
        Gdx.input.inputProcessor = inputManager
        inputManager.applyDefaultMapping()
        addComponentMappers()
        super.create()
    }

    override fun render() {
        inputManager.update()
        super.render()
    }

    override fun dispose() {
        super.dispose()
    }

    private fun addComponentMappers() {
        ComponentRetriever.addMapper(PlayerAnimationComponent::class.java)
        ComponentRetriever.addMapper(EventListenerComponent::class.java)
        ComponentRetriever.addMapper(FixtureColorComponent::class.java)
        ComponentRetriever.addMapper(ColorPlatformRenderingComponent::class.java)
        ComponentRetriever.addMapper(ColorComponent::class.java)
    }
}