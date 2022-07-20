package com.g3ida.withflyingcolours.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.g3ida.withflyingcolours.core.common.Constants

object Utils {
    fun loadShader(baseFileName: String): ShaderProgram {
        return ShaderProgram(
                Gdx.files.internal(Constants.SHADERS_PATH + baseFileName + ".vert"),
                Gdx.files.internal(Constants.SHADERS_PATH + baseFileName + ".frag"))
    }
}