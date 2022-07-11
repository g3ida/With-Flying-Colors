package com.g3ida.withflyingcolours.core.platform

import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import games.rednblack.editor.renderer.components.TransformComponent
import com.artemis.annotations.All
import com.badlogic.gdx.math.Vector2
import games.rednblack.editor.renderer.utils.ComponentRetriever
import games.rednblack.editor.renderer.components.ShaderComponent
import games.rednblack.editor.renderer.data.ShaderUniformVO
import games.rednblack.editor.renderer.systems.render.HyperLap2dRenderer
import com.g3ida.withflyingcolours.core.GameSettings

@All(ColorPlatformRenderingComponent::class, ShaderComponent::class, TransformComponent::class)
class ColorPlatformRenderingSystem : IteratingSystem() {
    private lateinit var mColorPlatformRenderingComponent: ComponentMapper<ColorPlatformRenderingComponent>
    private lateinit var mShaderComponent: ComponentMapper<ShaderComponent>

    override fun process(entityId: Int) {
        val renderingComponent = mColorPlatformRenderingComponent[entityId]
        if (renderingComponent.doColorSplash) {
            renderingComponent.doColorSplash = false
            renderingComponent.splashTimer = ColorPlatformRenderingComponent.SPLASH_DURATION
            val shaderComponent = mShaderComponent[entityId]
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