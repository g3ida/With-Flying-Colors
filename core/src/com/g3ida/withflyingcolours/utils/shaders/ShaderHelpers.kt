package com.g3ida.withflyingcolours.utils

import com.artemis.World
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.g3ida.withflyingcolours.core.common.Constants
import com.g3ida.withflyingcolours.utils.exceptions.ComponentAlreadyExistsException
import com.g3ida.withflyingcolours.utils.exceptions.ShaderLoadException
import com.g3ida.withflyingcolours.utils.extensions.addComponentToEntity
import games.rednblack.editor.renderer.components.ShaderComponent
import games.rednblack.editor.renderer.data.MainItemVO
import games.rednblack.editor.renderer.utils.ComponentRetriever

fun loadShader(filename: String): ShaderProgram {
    val shader = Utils.loadShader(filename)
    if (!shader.isCompiled) {
        Gdx.app.log(Constants.LOG_SHADER_PREFIX, shader.getLog())
        throw ShaderLoadException(filename)
    }
    return shader
}

fun addShaderComponentToEntity(shader: ShaderProgram, shaderName: String, entityId: Int, engine: World): ShaderComponent {
    val existingShaderComponent = ComponentRetriever.get(entityId, ShaderComponent::class.java, engine)
    if (existingShaderComponent != null)
        throw ComponentAlreadyExistsException(ShaderComponent::class.toString())
    val shaderComponent = engine.addComponentToEntity<ShaderComponent>(entityId)
    shaderComponent.renderingLayer = MainItemVO.RenderingLayer.SCREEN
    shaderComponent.setShader(shaderName, shader)
    return shaderComponent
}