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
import com.g3ida.withflyingcolours.Utils
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

@All(PhysicsBodyComponent::class, PlayerJumpComponent::class)
class PlayerJumpSystem : IteratingSystem() {
    var mPhysicsBodyComponent: ComponentMapper<PhysicsBodyComponent>? = null
    var mPlayerJumpComponent: ComponentMapper<PlayerJumpComponent>? = null
    override fun process(entityId: Int) {
        val physicsBody = mPhysicsBodyComponent!![entityId]
        val playerJump = mPlayerJumpComponent!![entityId]
        if (isGrounded(physicsBody)) {
            playerJump.timeSinceGrounded = 0f
        }
        var doJump = false //should we jump in this frame ?
        if (isJumpPressed(playerJump)) {
            if (can_jump(playerJump, physicsBody)) {
                doJump = true
            } else { // if the player can't jump right now let's check if he can before the responsiveness time passes.
                playerJump.responsivenessTimer = PlayerJumpComponent.jumpSettings.responsiveness
            }
        }

        //handle responsiveness
        if (playerJump.responsivenessTimer > 0) {
            if (can_jump(playerJump, physicsBody)) {
                playerJump.responsivenessTimer = 0f
                doJump = true
            } else {
                playerJump.responsivenessTimer -= world.delta
            }
        } else {
            playerJump.responsivenessTimer = 0f
        }

        //perform the jump
        if (doJump) {
            run {
                playerJump.jumpTimer = playerJump.timeUntilFullJumpIsConsidered
                physicsBody.body.applyLinearImpulse(0f, playerJump.jumpForce, physicsBody.body.worldCenter.x, physicsBody.body.worldCenter.y, true)
            }
        }

        //jump has been released before full jump reached
        if (!playerJump.shouldJump && playerJump.jumpTimer > Utils.epsilon) {
            //cancel jump
            val velocity = physicsBody.body.linearVelocity
            if (velocity.y > 0) { // decrease velocity only if the player is going up !
                physicsBody.body.setLinearVelocity(velocity.x, velocity.y * 0.5f)
            }
        }
        if (playerJump.shouldJump) {
            playerJump.jumpTimer -= world.delta
        }
        if (!isGrounded(physicsBody)) {
            playerJump.timeSinceGrounded += world.delta
        }
        playerJump.oldShouldJump = playerJump.shouldJump
    }

    fun isJumpPressed(playerJump: PlayerJumpComponent): Boolean {
        return !playerJump.oldShouldJump && playerJump.shouldJump
    }

    fun isJumpReleased(playerJump: PlayerJumpComponent): Boolean {
        return playerJump.oldShouldJump && !playerJump.shouldJump
    }

    fun isJumpDown(playerJump: PlayerJumpComponent): Boolean {
        return playerJump.shouldJump
    }

    fun isGrounded(physicsBody: PhysicsBodyComponent): Boolean {
        // FIXME: this condition is also met on jump peak.
        return Math.abs(physicsBody.body.linearVelocity.y) < Utils.epsilon
    }

    fun can_jump(playerJump: PlayerJumpComponent, physicsBody: PhysicsBodyComponent): Boolean {
        if (isGrounded(physicsBody)) {
            return true
        }
        //handle permissiveness
        val isJumping = physicsBody.body.linearVelocity.y > 0f
        return (playerJump.timeSinceGrounded <= PlayerJumpComponent.jumpSettings.permissiveness
                && !isJumping && playerJump.jumpTimer <= 0f)
    }
}