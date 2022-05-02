package com.g3ida.withflyingcolours.tests;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.g3ida.withflyingcolours.Utils;
import com.g3ida.withflyingcolours.core.player.movement.PlayerRotationComponent;
import com.g3ida.withflyingcolours.core.player.movement.PlayerRotationSystem;
import com.g3ida.withflyingcolours.utils.RotationDirection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;

@RunWith(GdxTestRunner.class)
public class RotationSystemTest extends BaseSystemTests {

    @Test
    public void testPlayerRotationSystem() throws AssertionError {

        //prepare engine and entity with physics
        EngineWithEntity engineWithEntity = createPhysicsWorldWithDynamicEntity(new BaseSystem[] {new PlayerRotationSystem() });

        com.artemis.World engine = engineWithEntity.world;
        int entityId = engineWithEntity.entityId;

        PlayerRotationComponent playerRotationComponent = engine.edit(entityId).create(PlayerRotationComponent.class);
        PhysicsBodyComponent physicsBodyComponent = engine.getMapper(PhysicsBodyComponent.class).get(entityId);

        // check that the entity have a valid body and its initial rotation set to 0.
        cycleEngineFor(engine, 1);
        Assert.assertNotNull(physicsBodyComponent.body);
        physicsBodyComponent.body.setFixedRotation(true);
        Assert.assertEquals(0f, physicsBodyComponent.body.getAngle(), 0.01f);

        // rotate the entity to the right by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.clockwise);
        cycleEngineFor(engine, 1);
        Assert.assertEquals(-Utils.PI2, physicsBodyComponent.body.getAngle(), 0.01f);

        // rotate the entity to the right by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.clockwise);
        cycleEngineFor(engine, 1);
        Assert.assertEquals(-Utils.PI, physicsBodyComponent.body.getAngle(), 0.01f);

        // rotate the entity to the right by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.clockwise);
        cycleEngineFor(engine, 1);
        Assert.assertEquals(-(Utils.PI + Utils.PI2), physicsBodyComponent.body.getAngle(), 0.01f);

        // rotate the entity to the right by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.clockwise);
        cycleEngineFor(engine, 1);
        Assert.assertEquals(0f, Utils.modPI2(physicsBodyComponent.body.getAngle()), 0.01f);

        // rotate the entity to the left by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.antiClockwise);
        cycleEngineFor(engine, 1);
        Assert.assertEquals(-(Utils.PI + Utils.PI2), physicsBodyComponent.body.getAngle(), 0.01f);

        // rotate the entity to the left by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.antiClockwise);
        cycleEngineFor(engine, 1);
        Assert.assertEquals(-Utils.PI, physicsBodyComponent.body.getAngle(), 0.01f);

        // rotate the entity to the left by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.antiClockwise);
        cycleEngineFor(engine, 1);
        Assert.assertEquals(-Utils.PI2, physicsBodyComponent.body.getAngle(), 0.01f);

        // rotate the entity to the left by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.antiClockwise);
        cycleEngineFor(engine, 1);
        Assert.assertEquals(0, physicsBodyComponent.body.getAngle(), 0.01f);

        // rotate the entity to the left by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.antiClockwise);
        cycleEngineFor(engine, 1);
        Assert.assertEquals(Utils.PI2, physicsBodyComponent.body.getAngle(), 0.01f);

        //TODO: add test cases with multiple instantaneous rotate request and with variable speed etc...
    }
}