package com.g3ida.withflyingcolours.core.player.movement;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.utils.ComponentRetriever;

public class PlayerRotationSystem extends IteratingSystem {

    public PlayerRotationSystem() {
        super(Family.all(PhysicsBodyComponent.class, PlayerRotationComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PhysicsBodyComponent physicsBody = ComponentRetriever.get(entity, PhysicsBodyComponent.class);
        PlayerRotationComponent playerRotation = ComponentRetriever.get(entity, PlayerRotationComponent.class);

        //setup rotation
        if(playerRotation.shouldRotate && playerRotation.canRotate) {

            playerRotation.canRotate = false;
            playerRotation.shouldRotate = false;

            playerRotation.thetaZero = physicsBody.body.getAngle();
            float PI_2 = (float)(Math.PI / 2.0);
            playerRotation.thetaTarget = Math.round((playerRotation.thetaZero + playerRotation.rotationDirectionSign * PI_2) / PI_2) * PI_2;

            playerRotation.thetaPoint = (playerRotation.thetaTarget - playerRotation.thetaZero) / playerRotation.rotationDuration;
            playerRotation.rotationTimer = playerRotation.rotationDuration;
        }

        // update rotation
        if (playerRotation.rotationTimer > 0f)
        {
            playerRotation.rotationTimer -= deltaTime;
            if (playerRotation.rotationTimer < 0f)
            {
                // correction for the last frame
                float current_angle = physicsBody.body.getAngle();
                playerRotation.thetaPoint = (playerRotation.thetaTarget - current_angle) / deltaTime;
                playerRotation.rotationTimer = 0;
            }
            physicsBody.body.setAngularVelocity(playerRotation.thetaPoint);
        }
        else if (!playerRotation.canRotate) {
            playerRotation.thetaPoint = 0f;
            physicsBody.body.setAngularVelocity(0f);
            playerRotation.rotationTimer = 0f;
            playerRotation.canRotate = true;
        }
    }
}
