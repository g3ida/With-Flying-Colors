package com.g3ida.withflyingcolours.core.player.movement;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;

import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;

@All({PhysicsBodyComponent.class, PlayerRotationComponent.class})
public class PlayerRotationSystem extends IteratingSystem {
    private ComponentMapper<PhysicsBodyComponent> mPhysicsBodyComponent;
    private ComponentMapper<PlayerRotationComponent> mPlayerRotationComponent;

    public PlayerRotationSystem() {
        super();
    }

    @Override
    protected void process(int entityId) {
        PhysicsBodyComponent physicsBody = mPhysicsBodyComponent.get(entityId);
        PlayerRotationComponent playerRotation = mPlayerRotationComponent.get(entityId);

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
            playerRotation.rotationTimer -= world.getDelta();
            if (playerRotation.rotationTimer < 0f)
            {
                // correction for the last frame
                float current_angle = physicsBody.body.getAngle();
                playerRotation.thetaPoint = (playerRotation.thetaTarget - current_angle) / world.getDelta();
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
