package com.g3ida.withflyingcolours.core.player.movement;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;

@All({PlayerWalkComponent.class, PhysicsBodyComponent.class})
public class PlayerWalkSystem extends IteratingSystem {
    ComponentMapper<PhysicsBodyComponent> mPhysicsBodyComponent;
    ComponentMapper<PlayerWalkComponent> mPlayerWalkComponent;

    public PlayerWalkSystem() {
        super();
    }

    @Override
    protected void process(int entityId) {
        PhysicsBodyComponent physicsBody = mPhysicsBodyComponent.get(entityId);
        PlayerWalkComponent playerWalk = mPlayerWalkComponent.get(entityId);

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
