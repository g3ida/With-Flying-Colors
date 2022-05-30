package com.g3ida.withflyingcolours.core.camera

import com.artemis.ComponentMapper
import games.rednblack.editor.renderer.components.ViewPortComponent
import com.artemis.systems.IteratingSystem
import games.rednblack.editor.renderer.components.TransformComponent
import com.artemis.annotations.All

@All(ViewPortComponent::class)
class CameraSystem(private val _xMin: Float, private val _xMax: Float, private val _yMin: Float, private val _yMax: Float) : IteratingSystem() {
    private var _focusEntityId = -1
    private val _mViewport: ComponentMapper<ViewPortComponent>? = null
    private val _mTransform: ComponentMapper<TransformComponent>? = null
    fun setFocus(entityId: Int) {
        _focusEntityId = entityId
    }

    override fun process(entityId: Int) {
        val viewPortComponent = _mViewport!![entityId]
        val camera = viewPortComponent.viewPort.camera
        if (_focusEntityId != -1) {
            // FIXME: player position should be lower + camera movement should be softer
            val transformComponent = _mTransform!![_focusEntityId]
            if (transformComponent != null) {
                val x = Math.max(_xMin, Math.min(_xMax, transformComponent.x))
                val y = Math.max(_yMin, Math.min(_yMax, transformComponent.y))
                camera.position[x, y] = 0f
            }
        }
    }
}