package com.g3ida.withflyingcolours.core.player.animation

import games.rednblack.editor.renderer.components.ViewPortComponent
import com.artemis.systems.IteratingSystem
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import com.g3ida.withflyingcolours.core.player.movement.PlayerJumpComponent
import com.g3ida.withflyingcolours.core.player.movement.PlayerWalkComponent
import com.artemis.PooledComponent
import com.badlogic.gdx.math.Interpolation
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

class TransformAnimation(private val _animationDuration: Float,
                         private val _interpolation: Interpolation,
                         private val _centerOriginX: Boolean,
                         private val _centerOriginY: Boolean) {
    private var _timer = 0f
    fun start() {
        if (isRunning) return
        _timer = _animationDuration
    }

    val isRunning: Boolean
        get() = _timer > 0f

    fun step(transform: TransformComponent, deltaTime: Float) {
        if (_timer > 0f) {

            // if the player is rotated we should react according to the actual direction
            val cosRotation = Math.cos(transform.rotation * Utils.PI / 180.0).toFloat()
            val sinRotation = Math.sin(transform.rotation * Utils.PI / 180.0).toFloat()
            val normalized = _timer / _animationDuration
            val mean = 1f
            val i = _interpolation.apply(0f, 1f, normalized) - mean
            transform.scaleX = mean + (i * Math.abs(cosRotation) - Math.abs(sinRotation) * (i + 0.00f))
            transform.scaleY = mean + (i * Math.abs(sinRotation) - Math.abs(cosRotation) * (i + 0.00f))
            if (!_centerOriginY) {
                transform.originY = 0.5f + (1f - transform.scaleY) * 0.5f * cosRotation
                transform.originX = 0.5f + (1f - transform.scaleX) * 0.5f * sinRotation
            }
            if (!_centerOriginX) {
                transform.originX = 0.5f + (1f - transform.scaleX) * 0.5f * cosRotation
                transform.originY = 0.5f + (1f - transform.scaleY) * 0.5f * sinRotation
            }
            _timer -= deltaTime
        } else {
            reset(transform)
        }
    }

    fun reset(transform: TransformComponent?) {
        _timer = 0f
        if (transform != null) {
            transform.scaleX = 1f
            transform.scaleY = 1f
            transform.originY = 0.5f
            transform.originX = 0.5f
        }
    }
}