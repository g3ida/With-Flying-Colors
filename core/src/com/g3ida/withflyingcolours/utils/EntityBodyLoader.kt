package com.g3ida.withflyingcolours.utils

import com.artemis.World
import com.badlogic.gdx.files.FileHandle
import games.rednblack.editor.renderer.components.TransformComponent
import com.badlogic.gdx.physics.box2d.World as Box2dWorld
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import games.rednblack.editor.renderer.components.shape.CircleShapeComponent
import games.rednblack.editor.renderer.components.shape.PolygonShapeComponent
import games.rednblack.editor.renderer.physics.PhysicsBodyLoader
import games.rednblack.editor.renderer.utils.ComponentRetriever

class EntityBodyLoader(val entityId: Int, val engine: World, val box2dWorld: Box2dWorld) {
    private val defaultBodyName = "main"

    fun load(filename: String) {
        val fileHandle = FileHandle(filename)
        val bodyEditorLoader = BodyEditorLoader(fileHandle)

        val physicsBodyComponent = ComponentRetriever.get(entityId, PhysicsBodyComponent::class.java, engine)
        refreshBody(entityId, physicsBodyComponent)
        bodyEditorLoader.attachFixture(physicsBodyComponent.body, defaultBodyName, 1f)
        // FIXME: For now we can't mix and match multiple shape types as Hyperlap2d components does not support it
        PhysicsBodyLoader.getInstance().refreshShape(entityId, engine)
    }

    fun refreshBody(entity: Int, physicsBodyComponent: PhysicsBodyComponent) {
        val polygonShapeComponent = ComponentRetriever.get(entity, PolygonShapeComponent::class.java, engine)
        val circleShapeComponent = ComponentRetriever.get(entity, CircleShapeComponent::class.java, engine)
        if (physicsBodyComponent.body == null && (polygonShapeComponent?.vertices != null || circleShapeComponent != null)) {
            val transformComponent = ComponentRetriever.get(entity, TransformComponent::class.java, engine)
            PhysicsBodyLoader.getInstance()
                .createBody(box2dWorld, entity, physicsBodyComponent, transformComponent, engine)
            physicsBodyComponent.body.setUserData(entity)
        }
        physicsBodyComponent.executeRefresh(entity)
    }
}