package com.g3ida.withflyingcolours.core.platform

import games.rednblack.editor.renderer.components.ViewPortComponent
import com.artemis.systems.IteratingSystem
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import com.g3ida.withflyingcolours.core.player.movement.PlayerJumpComponent
import com.g3ida.withflyingcolours.core.player.movement.PlayerWalkComponent
import com.artemis.PooledComponent
import com.artemis.World
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector2
import com.g3ida.withflyingcolours.Constants
import com.g3ida.withflyingcolours.Utils
import com.g3ida.withflyingcolours.core.player.PlayerControllerSettings
import com.g3ida.withflyingcolours.core.player.movement.PlayerRotationComponent
import com.g3ida.withflyingcolours.utils.RotationDirection
import com.g3ida.withflyingcolours.core.player.animation.PlayerAnimationComponent
import com.g3ida.withflyingcolours.core.player.animation.TransformAnimation
import com.g3ida.withflyingcolours.core.player.controller.PlayerControllerComponent
import com.g3ida.withflyingcolours.core.scripts.GameScript
import games.rednblack.editor.renderer.utils.ComponentRetriever
import com.g3ida.withflyingcolours.core.camera.CameraSystem
import games.rednblack.editor.renderer.components.DimensionsComponent
import games.rednblack.editor.renderer.scripts.BasicScript
import games.rednblack.editor.renderer.physics.PhysicsContact
import com.g3ida.withflyingcolours.core.platform.ColorPlatformRenderingComponent
import games.rednblack.editor.renderer.components.ShaderComponent
import games.rednblack.editor.renderer.data.ShaderUniformVO
import games.rednblack.editor.renderer.systems.render.HyperLap2dRenderer
import com.g3ida.withflyingcolours.core.GameSettings
import games.rednblack.editor.renderer.data.MainItemVO
import games.rednblack.editor.renderer.utils.ItemWrapper
import games.rednblack.editor.renderer.components.NodeComponent
import games.rednblack.editor.renderer.components.MainItemComponent
import games.rednblack.editor.renderer.SceneLoader
import games.rednblack.editor.renderer.resources.AsyncResourceManager
import games.rednblack.editor.renderer.resources.ResourceManagerLoader.AsyncResourceManagerParam
import games.rednblack.editor.renderer.resources.ResourceManagerLoader

class ColorPlatformRenderingComponent : PooledComponent() {
    var doColorSplash = false
    var splashTimer = 0f
    var contactPosition = Vector2()
    private var _shader: ShaderProgram? = null
    fun Init(entityId: Int, engine: World?) {
        _shader = Utils.loadShader(_SHADER_NAME)
        if (!_shader!!.isCompiled()) {
            Gdx.app.log(Constants.LOG_SHADER_PREFIX, _shader!!.getLog())
        }
        val shaderComponent = engine!!.edit(entityId).create(ShaderComponent::class.java)
        shaderComponent.renderingLayer = MainItemVO.RenderingLayer.SCREEN
        shaderComponent.setShader(_SHADER_NAME, _shader)
    }

    public override fun reset() {
        splashTimer = 0f
        contactPosition = Vector2()
    }

    fun dispose() {
        if (_shader != null) {
            _shader!!.dispose()
        }
    }

    companion object {
        const val SPLASH_DURATION = 1f
        private const val _SHADER_NAME = "color_splash"
    }
}