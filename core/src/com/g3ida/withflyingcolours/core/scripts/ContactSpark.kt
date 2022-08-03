package com.g3ida.withflyingcolours.core.scripts

import com.artemis.World
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Contact
import com.g3ida.withflyingcolours.core.common.GameSettings
import com.g3ida.withflyingcolours.utils.CountdownTimer
import com.g3ida.withflyingcolours.utils.addShaderComponentToEntity
import com.g3ida.withflyingcolours.utils.extensions.PI
import com.g3ida.withflyingcolours.utils.extensions.calculateAngle
import com.g3ida.withflyingcolours.utils.extensions.minus
import com.g3ida.withflyingcolours.utils.loadShader
import com.g3ida.withflyingcolours.utils.shaders.UniformStore
import games.rednblack.editor.renderer.components.DimensionsComponent
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.ZIndexComponent
import games.rednblack.editor.renderer.utils.ComponentRetriever
import kotlin.math.min
import com.badlogic.gdx.physics.box2d.World as World2d

class ContactSpark(engine: World, world: World2d, val contact: Contact) : GameScript(engine, world) {

    private var mEntityId = 0
    private val shaderName = "contact_spark"
    private var mShaderUniformStore: UniformStore? = null
    private lateinit var mDimensionsComponent: DimensionsComponent
    private lateinit var mTransformComponent: TransformComponent
    private var mAngle = 0f
    private val mDeathTimer = CountdownTimer(0.5f)

    override fun init(item: Int) {
        super.init(item)
        mEntityId = getEntity()
        if (contact.isTouching) {
            val manifold = contact.worldManifold
            if (manifold.numberOfContactPoints >= 2) {
                mTransformComponent = ComponentRetriever.get(mEntityId, TransformComponent::class.java, engine)
                mDimensionsComponent = ComponentRetriever.get(mEntityId, DimensionsComponent::class.java, engine)
                mAngle = calculateAngle(Vector2(-manifold.normal.y, manifold.normal.x), Vector2.X)
                mTransformComponent.rotation = mAngle.rem(Float.PI) * 180f / Float.PI
                val distance = (manifold.points[1] - manifold.points[0]).len()
                mTransformComponent.scaleX = distance / mDimensionsComponent.width
                mTransformComponent.scaleY = 0.05f / mDimensionsComponent.height
                mTransformComponent.originX = 0f
                mTransformComponent.originY = 0f
                mTransformComponent.x = min(manifold.points[0].x, manifold.points[1].x)
                mTransformComponent.y = min(manifold.points[0].y, manifold.points[1].y)
                addShaderComponent()

                val zIndexComponent =  ComponentRetriever.get(mEntityId, ZIndexComponent::class.java, engine)
                zIndexComponent.zIndex = 100
            }
        }
    }

    private fun addShaderComponent() {
        val mShader: ShaderProgram = loadShader(shaderName)
        val shaderComponent = addShaderComponentToEntity(mShader, shaderName, mEntityId, engine)
        mShaderUniformStore = UniformStore(shaderComponent)
        setUniforms(mShaderUniformStore)
    }

    override fun act(delta: Float) {
        mShaderUniformStore?.update()
        mDeathTimer.step(delta)
        if (!mDeathTimer.isRunning()) {
            engine.delete(mEntityId)
        }
    }

    override fun dispose() {
    }

    private fun setUniforms(shaderUniformStore: UniformStore?) {
        if (shaderUniformStore == null)
            return

        val cam = GameSettings.mainViewPort!!.camera
        val view = GameSettings.mainViewPort!!

        with(shaderUniformStore) {
            addUniform2V("u_resolution") {
                UniformStore.U2(view.screenWidth * 2f, view.screenHeight * 2f)
            }

            val bbox = ComponentRetriever.get(mEntityId, DimensionsComponent::class.java, engine)

            addUniform4V("u_bbox") {
                UniformStore.U4(
                    view.worldWidth * .5f + mTransformComponent.x - cam.position.x,
                    view.worldHeight * .5f + mTransformComponent.y - cam.position.y,
                    bbox.width * mTransformComponent.scaleX,
                    bbox.height * mTransformComponent.scaleY,
                )
            }
            addUniform2V("u_world2screen") {
                UniformStore.U2(
                    view.screenWidth / view.worldWidth,
                    view.screenHeight / view.worldHeight)
            }
            addUniform("u_angle", mAngle)
        }
    }
}