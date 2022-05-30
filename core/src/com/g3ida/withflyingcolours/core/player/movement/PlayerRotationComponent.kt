package com.g3ida.withflyingcolours.core.player.movement

import games.rednblack.editor.renderer.components.ViewPortComponent
import com.artemis.systems.IteratingSystem
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import com.g3ida.withflyingcolours.core.player.movement.PlayerJumpComponent
import com.g3ida.withflyingcolours.core.player.movement.PlayerWalkComponent
import com.artemis.PooledComponent
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

class PlayerRotationComponent : PooledComponent() {
    var rotationDuration = 0.12f // duration of the rotation animation.
    var thetaZero // initial angle, before the rotation is performed.
            = 0f
    var thetaTarget // target angle, after the rotation is completed.
            = 0f
    var thetaPoint // the calculated angular velocity.
            = 0f
    var rotationTimer = 0f // countdown rotation timer. initially set to rotationDuration.
    var rotationDirectionSign: Byte = 1 // 1 = anti-clockwise, -1 = clockwise.
    var canRotate = true // set to false when rotation is in progress.
    var shouldRotate = false // turn this to true if you want to trigger rotation.
    public override fun reset() {
        canRotate = false
        shouldRotate = false
        rotationDirectionSign = 1
        rotationTimer = 0f
        rotationDuration = 0f
        thetaZero = 0f
        thetaTarget = 0f
        thetaPoint = 0f
    }

    fun setRotationDirection(rotationDirection: RotationDirection?) {
        shouldRotate = true
        when (rotationDirection) {
            RotationDirection.clockwise -> rotationDirectionSign = -1
            RotationDirection.antiClockwise -> rotationDirectionSign = 1
            else -> {}
        }
    }
}