package com.g3ida.withflyingcolours.tests

import com.g3ida.withflyingcolours.core.common.GameSettings
import com.g3ida.withflyingcolours.core.ecs.components.EventListenerComponent
import com.g3ida.withflyingcolours.core.ecs.systems.EventListenerSystem
import com.g3ida.withflyingcolours.core.events.EventType
import com.g3ida.withflyingcolours.core.events.GameEvent
import com.g3ida.withflyingcolours.utils.extensions.PI
import com.g3ida.withflyingcolours.utils.extensions.PI2
import com.g3ida.withflyingcolours.utils.extensions.modPI2
import com.g3ida.withflyingcolours.core.actions.PlayerRotationAction
import com.g3ida.withflyingcolours.core.actions.EventActionListener
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import org.junit.runner.RunWith
import com.g3ida.withflyingcolours.utils.RotationDirection
import org.junit.Assert
import org.junit.Test
import java.lang.AssertionError

@RunWith(GdxTestRunner::class)
class RotationSystemTest : BaseSystemTests() {
    @Test
    @Throws(AssertionError::class)
    fun testPlayerRotationSystem() {
        //prepare engine and entity with physics
        val engineWithEntity = createPhysicsWorldWithDynamicEntity(EventListenerSystem())
        val engine = engineWithEntity.world
        val entityId = engineWithEntity.entityId
        val playerRotationComponent = engine.edit(entityId).create(EventListenerComponent::class.java)
        val physicsBodyComponent = engine.getMapper(PhysicsBodyComponent::class.java)[entityId]

        // check that the entity have a valid body and its initial rotation set to 0.
        cycleEngineFor(engine, 1f)
        Assert.assertNotNull(physicsBodyComponent.body)
        physicsBodyComponent.body.isFixedRotation = true
        Assert.assertEquals(0f, physicsBodyComponent.body.angle, 0.01f)

        playerRotationComponent.addActionListener(
            EventActionListener(
                EventType.RotateRightCommand,
                PlayerRotationAction(RotationDirection.Clockwise, physicsBodyComponent)
            ))

        playerRotationComponent.addActionListener(
            EventActionListener(
                EventType.RotateLeftCommand,
                PlayerRotationAction(RotationDirection.AntiClockwise, physicsBodyComponent)
            ))

        // rotate the entity to the right by 90deg and assert.
        GameSettings.eventHandler.dispatchEvent(GameEvent(EventType.RotateRightCommand))
        cycleEngineFor(engine, 1f)
        Assert.assertEquals(-Float.PI2, physicsBodyComponent.body.angle, 0.01f)

        // rotate the entity to the right by 90deg and assert.
        GameSettings.eventHandler.dispatchEvent(GameEvent(EventType.RotateRightCommand))
        cycleEngineFor(engine, 1f)
        Assert.assertEquals(-Float.PI, physicsBodyComponent.body.angle, 0.01f)

        // rotate the entity to the right by 90deg and assert.
        GameSettings.eventHandler.dispatchEvent(GameEvent(EventType.RotateRightCommand))
        cycleEngineFor(engine, 1f)
        Assert.assertEquals(-(Float.PI + Float.PI2), physicsBodyComponent.body.angle, 0.01f)

        // rotate the entity to the right by 90deg and assert.
        GameSettings.eventHandler.dispatchEvent(GameEvent(EventType.RotateRightCommand))
        cycleEngineFor(engine, 1f)
        Assert.assertEquals(0f, physicsBodyComponent.body.angle.modPI2(), 0.01f)

        // rotate the entity to the left by 90deg and assert.
        GameSettings.eventHandler.dispatchEvent(GameEvent(EventType.RotateLeftCommand))
        cycleEngineFor(engine, 1f)
        Assert.assertEquals(-(Float.PI + Float.PI2), physicsBodyComponent.body.angle, 0.01f)

        // rotate the entity to the left by 90deg and assert.
        GameSettings.eventHandler.dispatchEvent(GameEvent(EventType.RotateLeftCommand))
        cycleEngineFor(engine, 1f)
        Assert.assertEquals(-Float.PI, physicsBodyComponent.body.angle, 0.01f)

        // rotate the entity to the left by 90deg and assert.
        GameSettings.eventHandler.dispatchEvent(GameEvent(EventType.RotateLeftCommand))
        cycleEngineFor(engine, 1f)
        Assert.assertEquals(-Float.PI2, physicsBodyComponent.body.angle, 0.01f)

        // rotate the entity to the left by 90deg and assert.
        GameSettings.eventHandler.dispatchEvent(GameEvent(EventType.RotateLeftCommand))
        cycleEngineFor(engine, 1f)
        Assert.assertEquals(0f, physicsBodyComponent.body.angle, 0.01f)

        // rotate the entity to the left by 90deg and assert.
        GameSettings.eventHandler.dispatchEvent(GameEvent(EventType.RotateLeftCommand))
        cycleEngineFor(engine, 1f)
        Assert.assertEquals(Float.PI2, physicsBodyComponent.body.angle, 0.01f)

        //TODO: add test cases with multiple instantaneous rotate request and with variable speed etc...
    }
}