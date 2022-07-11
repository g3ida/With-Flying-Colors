package com.g3ida.withflyingcolours

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram

object Utils {
    const val EPSILON = 0.001f
    const val  PI = 3.14159265359f //use this as Math.PI is double
    const val PI2 = 1.57079632679f

    @JvmName("modPI21")
    fun Float.modPI2(): Float {
        var ang = this
        while (ang < -PI) ang += (2.0 * PI).toFloat()
        while (ang > PI) ang -= (2.0 * PI).toFloat()
        return ang
    }

    fun loadShader(baseFileName: String): ShaderProgram {
        return ShaderProgram(
                Gdx.files.internal(Constants.SHADERS_PATH + baseFileName + ".vert"),
                Gdx.files.internal(Constants.SHADERS_PATH + baseFileName + ".frag"))
    }
}