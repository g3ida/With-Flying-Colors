package com.g3ida.withflyingcolours.core.player.movement

import com.artemis.ComponentMapper
import games.rednblack.editor.renderer.components.ViewPortComponent
import com.artemis.systems.IteratingSystem
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import com.g3ida.withflyingcolours.core.player.movement.PlayerJumpComponent
import com.g3ida.withflyingcolours.core.player.movement.PlayerWalkComponent
import com.artemis.PooledComponent
import com.artemis.annotations.All
import com.g3ida.withflyingcolours.core.player.PlayerControllerSettings
import com.g3ida.withflyingcolours.core.player.movement.PlayerRotationComponent
import com.g3ida.withflyingcolours.utils.RotationDirection
import com.g3ida.withflyingcolours.core.player.animation.PlayerAnimationComponent
import com.g3ida.withflyingcolours.core.player.animation.TransformAnimation
import com.g3ida.withflyingcolours.core.player.controller.PlayerControllerComponent
import com.g3ida.withflyingcolours.core.scripts.GameScript
import games.rednblack.editor.renderer.utils.ComponentRetriever
import com.g3ida.withflyingcolours.core.camera.CameraSystem
import games.rednblack.editor.renderer.components.DimensionsComponent
import games.rednblack.editor.renderer.scripts.BasicScript
import games.rednblack.editor.renderer.physics.PhysicsContact
import com.g3ida.withflyingcolours.core.platform.ColorPlatformRenderingComponent
import games.rednblack.editor.renderer.components.ShaderComponent
import games.rednblack.editor.renderer.data.ShaderUniformVO
import games.rednblack.editor.renderer.systems.render.HyperLap2dRenderer
import com.g3ida.withflyingcolours.core.GameSettings
import games.rednblack.editor.renderer.data.MainItemVO
import games.rednblack.editor.renderer.utils.ItemWrapper
import games.rednblack.editor.renderer.components.NodeComponent
import games.rednblack.editor.renderer.components.MainItemComponent
import games.rednblack.editor.renderer.SceneLoader
import games.rednblack.editor.renderer.resources.AsyncResourceManager
import games.rednblack.editor.renderer.resources.ResourceManagerLoader.AsyncResourceManagerParam
import games.rednblack.editor.renderer.resources.ResourceManagerLoader

@All(PhysicsBodyComponent::class, PlayerRotationComponent::class)
class PlayerRotationSystem : IteratingSystem() {
    private val mPhysicsBodyComponent: ComponentMapper<PhysicsBodyComponent>? = null
    private val mPlayerRotationComponent: ComponentMapper<PlayerRotationComponent>? = null
    override fun process(entityId: Int) {
        val physicsBody = mPhysicsBodyComponent!![entityId]
        val playerRotation = mPlayerRotationComponent!![entityId]

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