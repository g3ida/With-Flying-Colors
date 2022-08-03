package com.g3ida.withflyingcolours.core.actions

import com.g3ida.withflyingcolours.core.ecs.components.PlayerAnimationComponent
import com.g3ida.withflyingcolours.core.ecs.components.PlayerSqueezeAnimation
import com.g3ida.withflyingcolours.core.events.GameEvent

class PlayerLandAction(val playerAnimationComponent: PlayerAnimationComponent): IGameAction {
    override fun execute(event: GameEvent) {
        if (!playerAnimationComponent.currentAnimation.isRunning()) {
            playerAnimationComponent.currentAnimation = PlayerSqueezeAnimation()
        }
    }
    override fun step(delta: Float) {}
}