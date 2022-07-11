package com.g3ida.withflyingcolours.tests

import com.g3ida.withflyingcolours.Utils.modPI2
import com.artemis.BaseSystem
import com.g3ida.withflyingcolours.Utils
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import org.junit.runner.RunWith
import com.g3ida.withflyingcolours.core.player.movement.PlayerRotationSystem
import com.g3ida.withflyingcolours.core.player.movement.PlayerRotationComponent
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
        val engineWithEntity = createPhysicsWorldWithDynamicEntity(arrayOf<BaseSystem?>(PlayerRotationSystem()))
        val engine = engineWithEntity.world
        val entityId = engineWithEntity.entityId
        val playerRotationComponent = engine.edit(entityId).create(PlayerRotationComponent::class.java)
        val physicsBodyComponent = engine.getMapper(PhysicsBodyComponent::class.java)[entityId]

        // check that the entity have a valid body and its initial rotation set to 0.
        cycleEngineFor(engine, 1f)
        Assert.assertNotNull(physicsBodyComponent.body)
        physicsBodyComponent.body.isFixedRotation = true
        Assert.assertEquals(0f, physicsBodyComponent.body.angle, 0.01f)

        // rotate the entity to the right by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.clockwise)
        cycleEngineFor(engine, 1f)
        Assert.assertEquals(-Utils.PI2, physicsBodyComponent.body.angle, 0.01f)

        // rotate the entity to the right by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.clockwise)
        cycleEngineFor(engine, 1f)
        Assert.assertEquals(-Utils.PI, physicsBodyComponent.body.angle, 0.01f)

        // rotate the entity to the right by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.clockwise)
        cycleEngineFor(engine, 1f)
        Assert.assertEquals(-(Utils.PI + Utils.PI2), physicsBodyComponent.body.angle, 0.01f)

        // rotate the entity to the right by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.clockwise)
        cycleEngineFor(engine, 1f)
        Assert.assertEquals(0f, physicsBodyComponent.body.angle.modPI2(), 0.01f)

        // rotate the entity to the left by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.antiClockwise)
        cycleEngineFor(engine, 1f)
        Assert.assertEquals(-(Utils.PI + Utils.PI2), physicsBodyComponent.body.angle, 0.01f)

        // rotate the entity to the left by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.antiClockwise)
        cycleEngineFor(engine, 1f)
        Assert.assertEquals(-Utils.PI, physicsBodyComponent.body.angle, 0.01f)

        // rotate the entity to the left by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.antiClockwise)
        cycleEngineFor(engine, 1f)
        Assert.assertEquals(-Utils.PI2, physicsBodyComponent.body.angle, 0.01f)

        // rotate the entity to the left by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.antiClockwise)
        cycleEngineFor(engine, 1f)
        Assert.assertEquals(0f, physicsBodyComponent.body.angle, 0.01f)

        // rotate the entity to the left by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.antiClockwise)
        cycleEngineFor(engine, 1f)
        Assert.assertEquals(Utils.PI2, physicsBodyComponent.body.angle, 0.01f)

        //TODO: add test cases with multiple instantaneous rotate request and with variable speed etc...
    }
}