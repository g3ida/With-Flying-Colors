package com.g3ida.withflyingcolours.core.player.animation

import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import games.rednblack.editor.renderer.components.TransformComponent
import com.artemis.annotations.All

@All(PlayerAnimationComponent::class, TransformComponent::class)
class PlayerAnimationSystem : IteratingSystem() {
    private lateinit var mPlayerAnimationComponent: ComponentMapper<PlayerAnimationComponent>
    private lateinit var mTransformComponent: ComponentMapper<TransformComponent>
    override fun process(entityId: Int) {
        val playerAnimation = mPlayerAnimationComponent[entityId]
        val transform = mTransformComponent[entityId]
        if (playerAnimation.doSqueeze) {
            playerAnimation.doSqueeze = false
            playerAnimation.squeezeAnimation.start()
        } else if (playerAnimation.doScale) {
            playerAnimation.doScale = false
            playerAnimation.scaleAnimation.start()
        }
        playerAnimation.scaleAnimation.step(transform, world.delta)
        playerAnimation.squeezeAnimation.step(transform, world.delta)
    }
}