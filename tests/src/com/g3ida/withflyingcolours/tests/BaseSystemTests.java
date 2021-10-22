package com.g3ida.withflyingcolours.tests;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import games.rednblack.editor.renderer.components.MainItemComponent;
import games.rednblack.editor.renderer.components.PolygonComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.systems.PhysicsSystem;

public class BaseSystemTests {

    public BaseSystemTests() {

    }

    public void cycleEngineFor(Engine engine, float seconds) {
        float frameTime = 0.016f; // 60 FPS
        int numFrames = (int)(seconds / frameTime);
        float remaining = seconds - numFrames * frameTime;

        for(int i=0; i < numFrames; i++) {
            engine.update(0.016f);
        }
        engine.update(remaining);
    }

    public Engine createPhysicsWorldWithDynamicEntity() {

        PooledEngine engine = new PooledEngine();
        Entity entity = new Entity();

        entity.add(new MainItemComponent());
        entity.add(new TransformComponent());

        //add shape to entity
        PolygonComponent polygonComponent = new PolygonComponent();
        polygonComponent.makeRectangle(1, 1);
        entity.add(polygonComponent);

        // add physics body to entity
        PhysicsBodyComponent physicsBodyComponent = new PhysicsBodyComponent();
        World world = new World(new Vector2(0, -10), true);
        physicsBodyComponent.bodyType = 2; //dynamic
        entity.add(physicsBodyComponent);

        engine.addSystem(new PhysicsSystem(world));
        engine.addEntity(entity);

        return engine;
    }
}
