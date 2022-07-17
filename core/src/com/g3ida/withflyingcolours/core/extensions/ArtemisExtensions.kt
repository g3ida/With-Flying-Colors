package com.g3ida.withflyingcolours.core.extensions

import com.artemis.BaseComponentMapper
import com.artemis.Component
import com.artemis.World
import com.badlogic.gdx.math.Vector2
import games.rednblack.editor.renderer.components.TransformComponent

fun <T : Component?> BaseComponentMapper<T>.getOrNull(entityId: Int): T? {
    return this.getSafe(entityId, null)
}

inline fun <reified T: Component> World.addComponentToEntity(entityId: Int): T {
    return this.edit(entityId).create(T::class.java)
}

var TransformComponent.scale: Vector2
    get() = Vector2(this.scaleX, this.scaleY)
    set(value) {
        this.scaleX = value.x
        this.scaleY = value.y
    }

var TransformComponent.origin: Vector2
    get() = Vector2(this.originX, this.originY)
    set(value) {
        this.originX = value.x
        this.originY = value.y
    }