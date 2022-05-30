package com.g3ida.withflyingcolours.core.player.controller

import com.artemis.ComponentMapper
import games.rednblack.editor.renderer.components.ViewPortComponent
import com.artemis.systems.IteratingSystem
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import com.g3ida.withflyingcolours.core.player.movement.PlayerJumpComponent
import com.g3ida.withflyingcolours.core.player.movement.PlayerWalkComponent
import com.artemis.PooledComponent
import com.artemis.annotations.All
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
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

@All(PlayerControllerComponent::class)
class PlayerControllerSystem : IteratingSystem() {
    var mPlayerControllerComponent: ComponentMapper<PlayerControllerComponent>? = null
    override fun process(entityId: Int) {
        //player movement left/right
        val playerMovement = mPlayerControllerComponent!![entityId]
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerMovement.moveInput = -1
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerMovement.moveInput = 1
        } else {
            playerMovement.moveInput = 0
        }
        //jumping
        playerMovement.shouldJump = Gdx.input.isKeyPressed(Input.Keys.UP)
        //rotating
        playerMovement.shouldRotateLeft = Gdx.input.isKeyJustPressed(Input.Keys.Z)
        playerMovement.shouldRotateRight = Gdx.input.isKeyJustPressed(Input.Keys.C)
    }
}