package com.g3ida.withflyingcolours.core.player.animation

import games.rednblack.editor.renderer.components.ViewPortComponent
import com.artemis.systems.IteratingSystem
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import com.g3ida.withflyingcolours.core.player.movement.PlayerJumpComponent
import com.g3ida.withflyingcolours.core.player.movement.PlayerWalkComponent
import com.artemis.PooledComponent
import com.badlogic.gdx.math.Interpolation
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

class PlayerAnimationComponent : PooledComponent() {
    var scaleAnimation: TransformAnimation
    var squeezeAnimation: TransformAnimation
    var doScale = false
    var doSqueeze = false
    public override fun reset() {
        doScale = false
        doSqueeze = false
        scaleAnimation.reset(null)
        squeezeAnimation.reset(null)
    }

    companion object {
        const val SQUEEZE_ANIM_DURATION = 0.17f
        const val SCALE_ANIM_DURATION = 0.17f
    }

    init {
        scaleAnimation = TransformAnimation(SCALE_ANIM_DURATION,
                Interpolation.ElasticIn(1f, 1f, 1, 0.1f),
                true,
                true)
        squeezeAnimation = TransformAnimation(SQUEEZE_ANIM_DURATION,
                Interpolation.ElasticOut(1f, 1f, 1, 0.1f),
                true,
                false)
        reset()
    }
}