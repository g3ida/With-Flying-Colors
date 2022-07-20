package com.g3ida.withflyingcolours.core.ecs.components

import com.artemis.PooledComponent
import com.artemis.World
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector2
import com.g3ida.withflyingcolours.core.common.Constants
import com.g3ida.withflyingcolours.utils.Utils
import com.g3ida.withflyingcolours.utils.extensions.addComponentToEntity
import games.rednblack.editor.renderer.components.ShaderComponent
import games.rednblack.editor.renderer.data.MainItemVO

class ColorPlatformRenderingComponent : PooledComponent() {
    var doColorSplash = false
    var splashTimer = 0f
    var contactPosition = Vector2()
    private lateinit var _shader: ShaderProgram

    fun initialize(entityId: Int, engine: World) {
        _shader = Utils.loadShader(SHADER_NAME)
        if (!_shader.isCompiled) {
            Gdx.app.log(Constants.LOG_SHADER_PREFIX, _shader.getLog())
        }
        val shaderComponent = engine.addComponentToEntity<ShaderComponent>(entityId)
        shaderComponent.renderingLayer = MainItemVO.RenderingLayer.SCREEN
        shaderComponent.setShader(SHADER_NAME, _shader)
    }

    public override fun reset() {
        splashTimer = 0f
        contactPosition = Vector2()
    }

    fun dispose() {
        if (this::_shader.isInitialized) {
            _shader.dispose()
        }
    }

    companion object {
        const val SPLASH_DURATION = 1f
        const val SHADER_NAME = "color_splash"
    }
}