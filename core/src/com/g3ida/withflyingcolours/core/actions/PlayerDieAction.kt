package com.g3ida.withflyingcolours.core.actions

import com.artemis.World
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.Disposable
import com.g3ida.withflyingcolours.core.common.GameSettings
import com.g3ida.withflyingcolours.core.events.GameEvent
import com.g3ida.withflyingcolours.utils.CountdownTimer
import com.g3ida.withflyingcolours.utils.addShaderComponentToEntity
import com.g3ida.withflyingcolours.utils.extensions.PI
import com.g3ida.withflyingcolours.utils.loadShader
import com.g3ida.withflyingcolours.utils.shaders.UniformStore
import games.rednblack.editor.renderer.components.BoundingBoxComponent
import games.rednblack.editor.renderer.components.ShaderComponent
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.data.ShaderUniformVO
import games.rednblack.editor.renderer.systems.render.HyperLap2dRenderer
import games.rednblack.editor.renderer.utils.ComponentRetriever

class PlayerDieAction(val entityId: Int, val engine: World): IInterpolationAction, Disposable {
    private val shaderName = "player_die"
    private val mShader: ShaderProgram = loadShader(shaderName)
    private val mShaderComponent: ShaderComponent
    private val mTimer = CountdownTimer(0.9f, false)
    private val mShaderUniformStore: UniformStore

    init {
        mShaderComponent = addShaderComponentToEntity(mShader, shaderName, entityId, engine)
        mShaderUniformStore = UniformStore(mShaderComponent)
        setUniforms()
    }

    private fun setUniforms() {
        val cam = GameSettings.mainViewPort!!.camera
        val view = GameSettings.mainViewPort!!

        with(mShaderUniformStore) {
            addUniform2V("u_resolution") {
                UniformStore.U2(view.screenWidth * 2f, view.screenHeight * 2f)
            }
            addUniform2V("u_campos") {
                UniformStore.U2(
                    cam.position.x - view.screenWidth * 0.5f,
                    cam.position.y - view.screenHeight * 0.5f
                )
            }
            addUniform2V("u_world") {
                UniformStore.U2(view.worldWidth, view.worldHeight)
            }
            val bbox = ComponentRetriever.get(entityId, BoundingBoxComponent::class.java, engine)
            addUniform4V("u_player_pos") {
                UniformStore.U4(
                    bbox.rectangle.x,
                    bbox.rectangle.y,
                    bbox.rectangle.width,
                    bbox.rectangle.height
                )
            }
            addUniform4V("u_bbox") {
                UniformStore.U4(
                    view.worldWidth * .5f + bbox.rectangle.x - cam.position.x,
                    view.worldHeight * .5f + bbox.rectangle.y - cam.position.y,
                    bbox.rectangle.width,
                    bbox.rectangle.height,
                )
            }
            addUniform2V("u_world2screen") {
                UniformStore.U2(
                    view.screenWidth / view.worldWidth,
                    view.screenHeight / view.worldHeight)
            }
            val transformComponent = ComponentRetriever.get(entityId, TransformComponent::class.java, engine)
            addUniform("u_angle") {
                UniformStore.U1(transformComponent.rotation*Float.PI / 180f)
            }
            addUniform("u_animation_duration", mTimer.duration)

            addUniform("u_animation_timer") {
                UniformStore.U1(mTimer.timer)
            }
        }
    }

    override fun execute(event: GameEvent) {
        mTimer.reset()
        val runningTime = engine.getSystem(HyperLap2dRenderer::class.java).timeRunning
        mShaderUniformStore.addUniform("start_time", runningTime)
    }

    override fun step(delta: Float) {
        mTimer.step(delta)
        mShaderUniformStore.update()

    }

    override fun interpolate(alpha: Float) {
        mShaderUniformStore.update()
    }

    override fun dispose() {
        mShader.dispose()
    }
}