package com.g3ida.withflyingcolours.tests;

import com.artemis.BaseSystem;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import games.rednblack.editor.renderer.components.MainItemComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.components.shape.PolygonShapeComponent;
import games.rednblack.editor.renderer.systems.PhysicsSystem;

public class BaseSystemTests {

    public class EngineWithEntity {

        public EngineWithEntity(com.artemis.World world, int entityId) {
            this.world = world;
            this.entityId = entityId;
        }

        public com.artemis.World world;
        public int entityId;
    }

    public BaseSystemTests() {

    }

    public void cycleEngineFor(com.artemis.World engine, float seconds) {
        float frameTime = 0.016f; // 60 FPS
        int numFrames = (int)(seconds / frameTime);
        float remaining = seconds - numFrames * frameTime;

        for(int i=0; i < numFrames; i++) {
            engine.setDelta(0.016f);
            engine.process();
        }
        engine.setDelta(remaining);
        engine.process();
    }

    public EngineWithEntity createPhysicsWorldWithDynamicEntity(BaseSystem[] additionalSystems) {
        World world = new World(new Vector2(0, -10), true);
        PhysicsSystem physicsSystem = new PhysicsSystem();
        physicsSystem.setBox2DWorld(world);

        WorldConfigurationBuilder worldConfigurationBuilder = new WorldConfigurationBuilder()
                .with(physicsSystem);
        for (BaseSystem system: additionalSystems) {
            worldConfigurationBuilder = worldConfigurationBuilder.with(additionalSystems);
        }
        WorldConfiguration setup =  worldConfigurationBuilder.build();
        com.artemis.World engine = new com.artemis.World(setup);

        int entityId = engine.create();
        engine.edit(entityId).create(MainItemComponent.class);
        engine.edit(entityId).create(TransformComponent.class);
        //add shape to entity
        engine.edit(entityId).create(PolygonShapeComponent.class).makeRectangle(1, 1);
        // add physics body to entity
        engine.edit(entityId).create(PhysicsBodyComponent.class).bodyType = 2; //dynamic

        return new EngineWithEntity(engine, entityId);
    }
}
