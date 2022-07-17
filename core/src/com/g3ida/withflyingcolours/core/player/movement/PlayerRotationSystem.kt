package com.g3ida.withflyingcolours.core.player.movement

import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import com.artemis.annotations.All

@All(PhysicsBodyComponent::class, PlayerRotationComponent::class)
class PlayerRotationSystem : IteratingSystem() {
    private lateinit var mPhysicsBodyCM: ComponentMapper<PhysicsBodyComponent>
    private lateinit var mPlayerRotationCM: ComponentMapper<PlayerRotationComponent>
    override fun process(entityId: Int) {
        val physicsBody = mPhysicsBodyCM[entityId]
        val playerRotation = mPlayerRotationCM[entityId]

        //setup rotation
        if (playerRotation.shouldRotate && playerRotation.canRotate) {
            playerRotation.canRotate = false
            playerRotation.shouldRotate = false
            playerRotation.thetaZero = physicsBody.body.angle
            val PI_2 = (Math.PI / 2.0).toFloat()
            playerRotation.thetaTarget = Math.round((playerRotation.thetaZero + playerRotation.rotationDirectionSign * PI_2) / PI_2) * PI_2
            playerRotation.thetaPoint = (playerRotation.thetaTarget - playerRotation.thetaZero) / playerRotation.rotationDuration
            playerRotation.rotationTimer = playerRotation.rotationDuration
        }

        // update rotation
        if (playerRotation.rotationTimer > 0f) {
            playerRotation.rotationTimer -= world.getDelta()
            if (playerRotation.rotationTimer < 0f) {
                // correction for the last frame
                val current_angle = physicsBody.body.angle
                playerRotation.thetaPoint = (playerRotation.thetaTarget - current_angle) / world.getDelta()
                playerRotation.rotationTimer = 0f
            }
            physicsBody.body.angularVelocity = playerRotation.thetaPoint
        } else if (!playerRotation.canRotate) {
            playerRotation.thetaPoint = 0f
            physicsBody.body.angularVelocity = 0f
            playerRotation.rotationTimer = 0f
            playerRotation.canRotate = true
        }
    }
}