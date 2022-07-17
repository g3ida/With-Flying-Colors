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
import com.g3ida.withflyingcolours.core.extensions.toSceneLoader
import com.g3ida.withflyingcolours.core.extensions.withResourceRetriever
import com.g3ida.withflyingcolours.core.extensions.withSystems

class GameScreen : ScreenAdapter() {
    private val mSceneLoader: SceneLoader
    private val mViewport: Viewport
    private val mCamera: OrthographicCamera
    private val mAssetsLoader: AssetsLoader = AssetsLoader("project.dt")
    private val mEngine: World

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        mViewport.update(width, height)
        if (width != 0 && height != 0) {
            mSceneLoader.resize(width, height)
        }
    }

    override fun dispose() {
        mAssetsLoader.dispose()
        super.dispose()
    }

    fun update() {
        mCamera.update()
    }

    override fun render(delta: Float) {
        super.render(delta)
        update()
        mViewport.apply()
        mEngine.setDelta(delta)
        mEngine.process()
    }

    override fun pause() {}

    init {
        val resourceManager = mAssetsLoader.load()
        mSceneLoader = SceneConfiguration()
            .withSystems(
                CameraSystem(0f, 20f, 0f, 7f),
                PlayerControllerSystem(),
                PlayerRotationSystem(),
                PlayerJumpSystem(),
                PlayerWalkSystem(),
                PlayerAnimationSystem(),
                ColorPlatformRenderingSystem())
            .withResourceRetriever(resourceManager)
            .toSceneLoader()

        mEngine = mSceneLoader.engine
        mCamera = OrthographicCamera()
        mViewport = ExtendViewport(13f, 7f, mCamera)
        GameSettings.mainViewPort = mViewport
        mSceneLoader.loadScene("MainScene", mViewport)
        SceneMapper(mSceneLoader)
    }
}