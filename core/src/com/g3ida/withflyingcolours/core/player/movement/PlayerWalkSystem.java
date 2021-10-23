package com.g3ida.withflyingcolours.core.player.movement;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.utils.ComponentRetriever;

public class PlayerWalkSystem extends IteratingSystem {

    public PlayerWalkSystem() {
        super(Family.all(PhysicsBodyComponent.class, PlayerWalkComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PhysicsBodyComponent physicsBody = ComponentRetriever.get(entity, PhysicsBodyComponent.class);
        PlayerWalkComponent playerWalk = ComponentRetriever.get(entity, PlayerWalkComponent.class);
        //TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);

        Vector2 velocity = physicsBody.body.getLinearVelocity();
        if (Math.abs(velocity.x) > playerWalk.speed) {
            if (velocity.x * playerWalk.direction < 0) { // not the same direction
                velocity.x += playerWalk.direction * playerWalk.speed;
            }
        }
        else {
            velocity.x = playerWalk.direction * playerWalk.speed;
        }

        physicsBody.body.setLinearVelocity(velocity);
    }
}
