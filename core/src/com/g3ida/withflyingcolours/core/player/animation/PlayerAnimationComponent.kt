package com.g3ida.withflyingcolours.core.player.animation

import com.artemis.PooledComponent
import com.badlogic.gdx.math.Interpolation

const val SQUEEZE_ANIM_DURATION = 0.17f
const val SCALE_ANIM_DURATION = 0.17f

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