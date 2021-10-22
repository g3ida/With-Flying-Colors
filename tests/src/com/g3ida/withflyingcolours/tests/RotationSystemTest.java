package com.g3ida.withflyingcolours.tests;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.g3ida.withflyingcolours.Utils;
import com.g3ida.withflyingcolours.core.player.movement.PlayerRotationComponent;
import com.g3ida.withflyingcolours.core.player.movement.PlayerRotationSystem;
import com.g3ida.withflyingcolours.utils.RotationDirection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.utils.ComponentRetriever;

@RunWith(GdxTestRunner.class)
public class RotationSystemTest extends BaseSystemTests {
    @Test
    public void testPlayerRotationSystem() throws AssertionError {

        //prepare engine and entity with physics
        Engine engine = createPhysicsWorldWithDynamicEntity();
        Entity entity = engine.getEntities().first();

        engine.addSystem(new PlayerRotationSystem());
        ComponentRetriever.addMapper(PlayerRotationComponent.class);
        entity.add(new PlayerRotationComponent());

        PhysicsBodyComponent PhysicsBodyComponent = ComponentRetriever.get(entity, PhysicsBodyComponent.class);
        PlayerRotationComponent playerRotationComponent = ComponentRetriever.get(entity, PlayerRotationComponent.class);

        // check that the entity have a valid body and its initial rotation set to 0.
        cycleEngineFor(engine, 1);
        Assert.assertNotNull(PhysicsBodyComponent.body);
        PhysicsBodyComponent.body.setFixedRotation(true);
        Assert.assertEquals(0f, PhysicsBodyComponent.body.getAngle(), 0.01f);

        // rotate the entity to the right by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.clockwise);
        cycleEngineFor(engine, 1);
        Assert.assertEquals(-Utils.PI2, PhysicsBodyComponent.body.getAngle(), 0.01f);

        // rotate the entity to the right by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.clockwise);
        cycleEngineFor(engine, 1);
        Assert.assertEquals(-Utils.PI, PhysicsBodyComponent.body.getAngle(), 0.01f);

        // rotate the entity to the right by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.clockwise);
        cycleEngineFor(engine, 1);
        Assert.assertEquals(-(Utils.PI + Utils.PI2), PhysicsBodyComponent.body.getAngle(), 0.01f);

        // rotate the entity to the right by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.clockwise);
        cycleEngineFor(engine, 1);
        Assert.assertEquals(0f, Utils.modPI2(PhysicsBodyComponent.body.getAngle()), 0.01f);

        // rotate the entity to the left by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.antiClockwise);
        cycleEngineFor(engine, 1);
        Assert.assertEquals(-(Utils.PI + Utils.PI2), PhysicsBodyComponent.body.getAngle(), 0.01f);

        // rotate the entity to the left by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.antiClockwise);
        cycleEngineFor(engine, 1);
        Assert.assertEquals(-Utils.PI, PhysicsBodyComponent.body.getAngle(), 0.01f);

        // rotate the entity to the left by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.antiClockwise);
        cycleEngineFor(engine, 1);
        Assert.assertEquals(-Utils.PI2, PhysicsBodyComponent.body.getAngle(), 0.01f);

        // rotate the entity to the left by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.antiClockwise);
        cycleEngineFor(engine, 1);
        Assert.assertEquals(0, PhysicsBodyComponent.body.getAngle(), 0.01f);

        // rotate the entity to the left by 90deg and assert.
        playerRotationComponent.setRotationDirection(RotationDirection.antiClockwise);
        cycleEngineFor(engine, 1);
        Assert.assertEquals(Utils.PI2, PhysicsBodyComponent.body.getAngle(), 0.01f);

        //TODO: add test cases with multiple instantaneous rotate request and with variable speed etc...
    }
}