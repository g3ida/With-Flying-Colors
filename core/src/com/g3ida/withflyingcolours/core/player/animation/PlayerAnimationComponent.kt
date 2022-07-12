package com.g3ida.withflyingcolours.core.player.animation

import com.artemis.PooledComponent
import com.badlogic.gdx.math.Interpolation

const val SQUEEZE_ANIM_DURATION = 0.17f
const val SCALE_ANIM_DURATION = 0.17f

class PlayerScaleAnimation: TransformAnimation(SCALE_ANIM_DURATION,
    Interpolation.ElasticIn(1f, 1f, 1, 0.1f))
class PlayerSqueezeAnimation: TransformAnimation(SQUEEZE_ANIM_DURATION,
    Interpolation.ElasticOut(1f, 1f, 1, 0.1f), true, false)
class PlayerIdleAnimation: TransformAnimation(0f, Interpolation.linear)

class PlayerAnimationComponent : PooledComponent() {
    var currentAnimation: ITransformAnimation = PlayerIdleAnimation()
    public override fun reset() {
        currentAnimation = PlayerIdleAnimation()
    }
}