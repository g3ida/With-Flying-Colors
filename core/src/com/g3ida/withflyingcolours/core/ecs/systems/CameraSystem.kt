package com.g3ida.withflyingcolours.core.ecs.systems

import com.artemis.ComponentMapper
import games.rednblack.editor.renderer.components.ViewPortComponent
import com.artemis.systems.IteratingSystem
import games.rednblack.editor.renderer.components.TransformComponent
import com.artemis.annotations.All
import games.rednblack.editor.renderer.systems.strategy.RendererSystem
import kotlin.math.max
import kotlin.math.min
import com.g3ida.withflyingcolours.utils.extensions.*

@All(ViewPortComponent::class)
class CameraSystem(private val mXMin: Float, private val mXMax: Float, private val mYMin: Float, private val mYMax: Float) :
    RendererSystem, IteratingSystem() {
    private var mFocusEntityId: Int = -1
    private lateinit var mViewportCM: ComponentMapper<ViewPortComponent>
    private lateinit var mTransformCM: ComponentMapper<TransformComponent>

    fun setFocus(entityId: Int) {
        mFocusEntityId = entityId
    }

    override fun process(entityId: Int) {
        val viewPortComponent = mViewportCM[entityId]
        val camera = viewPortComponent.viewPort.camera
        if (mFocusEntityId != -1) {
            // FIXME: player position should be lower + camera movement should be softer
            val transformComponent = mTransformCM.getOrNull(mFocusEntityId)
            if (transformComponent != null) {
                val x = max(mXMin, min(mXMax, transformComponent.x))
                val y = max(mYMin, min(mYMax, transformComponent.y))
                camera.position[x, y] = 0f
            }
        }
    }
}