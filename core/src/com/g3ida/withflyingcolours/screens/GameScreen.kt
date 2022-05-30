package com.g3ida.withflyingcolours.screens

import com.artemis.World
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import games.rednblack.editor.renderer.SceneLoader
import com.g3ida.withflyingcolours.core.AssetsLoader
import games.rednblack.editor.renderer.SceneConfiguration
import com.g3ida.withflyingcolours.core.camera.CameraSystem
import com.g3ida.withflyingcolours.core.player.controller.PlayerControllerSystem
import com.g3ida.withflyingcolours.core.player.movement.PlayerRotationSystem
import com.g3ida.withflyingcolours.core.player.movement.PlayerJumpSystem
import com.g3ida.withflyingcolours.core.player.movement.PlayerWalkSystem
import com.g3ida.withflyingcolours.core.player.animation.PlayerAnimationSystem
import com.g3ida.withflyingcolours.core.platform.ColorPlatformRenderingSystem
import com.g3ida.withflyingcolours.core.GameSettings
import com.g3ida.withflyingcolours.core.SceneMapper

class GameScreen : ScreenAdapter() {
    private val _sceneLoader: SceneLoader
    private val _viewport: Viewport
    private val _camera: OrthographicCamera
    private val _assetsLoader: AssetsLoader
    private val _engine: World
    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        _viewport.update(width, height)
        if (width != 0 && height != 0) {
            _sceneLoader.resize(width, height)
        }
    }

    override fun dispose() {
        _assetsLoader.dispose()
        super.dispose()
    }

    fun update() {
        _camera.update()
    }

    override fun render(delta: Float) {
        super.render(delta)
        update()
        _viewport.apply()
        _engine.setDelta(delta)
        _engine.process()
    }

    override fun pause() {}

    init {
        _assetsLoader = AssetsLoader("project.dt")
        val _resourceManager = _assetsLoader.load()

        // prepare screen configuration
        val sceneConfiguration = SceneConfiguration()
        // add systems
        val cameraSystem = CameraSystem(0f, 20f, 0f, 7f)
        sceneConfiguration.addSystem(cameraSystem)
        sceneConfiguration.addSystem(PlayerControllerSystem())
        sceneConfiguration.addSystem(PlayerRotationSystem())
        sceneConfiguration.addSystem(PlayerJumpSystem())
        sceneConfiguration.addSystem(PlayerWalkSystem())
        sceneConfiguration.addSystem(PlayerAnimationSystem())
        sceneConfiguration.addSystem(ColorPlatformRenderingSystem())
        sceneConfiguration.setResourceRetriever(_resourceManager)
        _sceneLoader = SceneLoader(sceneConfiguration)
        _engine = _sceneLoader.engine
        _camera = OrthographicCamera()
        _viewport = ExtendViewport(13f, 7f, _camera)
        GameSettings.mainViewPort = _viewport
        _sceneLoader.loadScene("MainScene", _viewport)
        val sceneMapper = SceneMapper()
        sceneMapper.mapScripts(_sceneLoader)
    }
}