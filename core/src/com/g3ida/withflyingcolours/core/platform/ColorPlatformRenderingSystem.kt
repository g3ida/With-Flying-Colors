package com.g3ida.withflyingcolours.core.platform

import com.artemis.ComponentMapper
import games.rednblack.editor.renderer.components.ViewPortComponent
import com.artemis.systems.IteratingSystem
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import com.g3ida.withflyingcolours.core.player.movement.PlayerJumpComponent
import com.g3ida.withflyingcolours.core.player.movement.PlayerWalkComponent
import com.artemis.PooledComponent
import com.artemis.annotations.All
import com.badlogic.gdx.math.Vector2
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

@All(ColorPlatformRenderingComponent::class, ShaderComponent::class, TransformComponent::class)
class ColorPlatformRenderingSystem : IteratingSystem() {
    protected var mColorPlatformRenderingComponent: ComponentMapper<ColorPlatformRenderingComponent>? = null
    protected var mShaderComponent: ComponentMapper<ShaderComponent>? = null
    protected var mTransformComponent: ComponentMapper<TransformComponent>? = null
    override fun process(entityId: Int) {
        val renderingComponent = mColorPlatformRenderingComponent!![entityId]
        if (renderingComponent.doColorSplash) {
            renderingComponent.doColorSplash = false
            renderingComponent.splashTimer = ColorPlatformRenderingComponent.SPLASH_DURATION
            val shaderComponent = mShaderComponent!![entityId]
            val uniform = ShaderUniformVO()
            uniform.set(world.getSystem(HyperLap2dRenderer::class.java).timeRunning)
            shaderComponent.customUniforms.put("start_time", uniform)
            val camera = GameSettings.mainViewPort!!.camera
            val positionOnViewport = Vector2()
            positionOnViewport.x = GameSettings.mainViewPort!!.worldWidth / 2f + renderingComponent.contactPosition.x - camera.position.x
            positionOnViewport.y = GameSettings.mainViewPort!!.worldHeight / 2f + renderingComponent.contactPosition.y - camera.position.y
            val ratioX = GameSettings.mainViewPort!!.screenWidth / GameSettings.mainViewPort!!.worldWidth
            val ratioY = GameSettings.mainViewPort!!.screenHeight / GameSettings.mainViewPort!!.worldHeight
            val u_contactpos = ShaderUniformVO()
            u_contactpos[positionOnViewport.x * ratioX * 2] = positionOnViewport.y * ratioY * 2
            shaderComponent.customUniforms.put("u_contactpos", u_contactpos)
        }
        if (renderingComponent.splashTimer > 0f) {
            val shaderComponent = ComponentRetriever.get(entityId, ShaderComponent::class.java, world)
            val u_resolution = ShaderUniformVO()
            u_resolution[(GameSettings.mainViewPort!!.screenWidth * 2).toFloat()] = (GameSettings.mainViewPort!!.screenHeight * 2).toFloat()
            shaderComponent.customUniforms.put("u_resolution", u_resolution)
            val u_campos = ShaderUniformVO()
            u_campos[GameSettings.mainViewPort!!.camera.position.x - GameSettings.mainViewPort!!.screenWidth * 0.5f] = GameSettings.mainViewPort!!.camera.position.y - GameSettings.mainViewPort!!.screenHeight * 0.5f
            shaderComponent.customUniforms.put("u_campos", u_campos)
            renderingComponent.splashTimer -= world.delta
        }
    }
}