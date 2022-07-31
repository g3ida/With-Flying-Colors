package com.g3ida.withflyingcolours.utils.shaders

import games.rednblack.editor.renderer.components.ShaderComponent
import games.rednblack.editor.renderer.data.ShaderUniformVO
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import ktx.collections.lastIndex

class UniformStore(val shaderComponent: ShaderComponent) {

    data class U1(val value1: Float)
    data class U2(val value1: Float, val value2: Float)
    data class U3(val value1: Float, val value2: Float, val value3: Float)
    data class U4(val value1: Float, val value2: Float, val value3: Float, val value4: Float)

    private val mUniformList: GdxArray<()->Unit> = gdxArrayOf()

    fun addUniform(name: String, value: Float) {
        val uniform = ShaderUniformVO()
        uniform.set(value)
        shaderComponent.customUniforms.put(name, uniform)
    }

    fun addUniform(name: String, value1: Float, value2: Float) {
        val uniform = ShaderUniformVO()
        uniform.set(value1, value2)
        shaderComponent.customUniforms.put(name, uniform)
    }

    fun addUniform(name: String, value1: Float, value2: Float, value3: Float) {
        val uniform = ShaderUniformVO()
        uniform.set(value1, value2, value3)
        shaderComponent.customUniforms.put(name, uniform)
    }

    fun addUniform(name: String, value1: Float, value2: Float, value3: Float, value4: Float) {
        val uniform = ShaderUniformVO()
        uniform.set(value1, value2, value3, value4)
        shaderComponent.customUniforms.put(name, uniform)
    }

    fun addUniform(name: String, value: ()->U1) {
        mUniformList.add{
            val uniform = ShaderUniformVO()
            uniform.set(value.invoke().value1)
            shaderComponent.customUniforms.put(name, uniform)
        }
        mUniformList[mUniformList.lastIndex].invoke()
    }

    fun addUniform2V(name: String, value: ()->U2) {
        mUniformList.add{
            val uniform = ShaderUniformVO()
            val res = value.invoke()
            uniform.set(res.value1, res.value2)
            shaderComponent.customUniforms.put(name, uniform)
        }
        mUniformList[mUniformList.lastIndex].invoke()
    }

    fun addUniform3V(name: String, value: ()-> U3) {
        mUniformList.add{
            val uniform = ShaderUniformVO()
            val res = value.invoke()
            uniform.set(res.value1, res.value2, res.value3)
            shaderComponent.customUniforms.put(name, uniform)
        }
        mUniformList[mUniformList.lastIndex].invoke()
    }

    fun addUniform4V(name: String, value: ()->U4) {
        mUniformList.add{
            val uniform = ShaderUniformVO()
            val res = value.invoke()
            uniform.set(res.value1, res.value2, res.value3, res.value4)
            shaderComponent.customUniforms.put(name, uniform)
        }
        mUniformList[mUniformList.lastIndex].invoke()
    }

    fun update() {
        mUniformList.forEach { it.invoke() }
    }
}