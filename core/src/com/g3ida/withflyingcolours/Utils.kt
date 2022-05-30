package com.g3ida.withflyingcolours

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram

object Utils {
    var epsilon = 0.001f
    @JvmField
    var PI = 3.14159265359f //use this as Math.PI is double
    @JvmField
    var PI2 = 1.57079632679f
    @JvmStatic
    fun modPI2(angle: Float): Float {
        var ang = angle
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