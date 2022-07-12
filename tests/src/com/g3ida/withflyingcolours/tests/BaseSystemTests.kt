package com.g3ida.withflyingcolours.tests

import com.artemis.BaseSystem
import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.badlogic.gdx.math.Vector2
import games.rednblack.editor.renderer.components.MainItemComponent
import games.rednblack.editor.renderer.components.ParentNodeComponent
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import games.rednblack.editor.renderer.components.shape.PolygonShapeComponent
import games.rednblack.editor.renderer.systems.PhysicsSystem
import games.rednblack.editor.renderer.utils.ComponentRetriever

open class BaseSystemTests {
    inner class EngineWithEntity(var world: World, var entityId: Int)

    fun cycleEngineFor(engine: World, seconds: Float) {
        val frameTime = 0.016f // 60 FPS
        val numFrames = (seconds / frameTime).toInt()
        val remaining = seconds - numFrames * frameTime
        for (i in 0 until numFrames) {
            engine.setDelta(0.016f)
            engine.process()
        }
        engine.setDelta(remaining)
        engine.process()
    }

    fun createPhysicsWorldWithDynamicEntity(additionalSystems: Array<BaseSystem>): EngineWithEntity {
        val world = com.badlogic.gdx.physics.box2d.World(Vector2(0f, -10f), true)
        val physicsSystem = PhysicsSystem()
        physicsSystem.setBox2DWorld(world)
        var worldConfigurationBuilder = WorldConfigurationBuilder()
                .with(physicsSystem)
        for (system in additionalSystems) {
            worldConfigurationBuilder = worldConfigurationBuilder.with(*additionalSystems)
        }
        val setup = worldConfigurationBuilder.build()
        val engine = World(setup)
        ComponentRetriever.initialize(engine)

        // create the root entity
        val rootEntityId = engine.create()
        val rootTransform = engine.edit(rootEntityId).create(TransformComponent::class.java)
        rootTransform.originX = 0f
        rootTransform.originY = 0f

        // create the dynamic body entity
        val entityId = engine.create()
        engine.edit(entityId).create(MainItemComponent::class.java)
        val transform = engine.edit(entityId).create(TransformComponent::class.java)
        transform.originX = 0f; transform.originY = 0f
        engine.edit(entityId).create(PolygonShapeComponent::class.java).makeRectangle(1f, 1f)
        engine.edit(entityId).create(PhysicsBodyComponent::class.java).bodyType = 2 //dynamic
        engine.edit(entityId).create(ParentNodeComponent::class.java).parentEntity = rootEntityId

        return EngineWithEntity(engine, entityId)
    }
}