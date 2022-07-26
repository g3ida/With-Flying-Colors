package com.g3ida.withflyingcolours.core.ecs.systems

import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import games.rednblack.editor.renderer.components.TransformComponent
import com.artemis.annotations.All
import com.g3ida.withflyingcolours.core.ecs.components.PlayerAnimationComponent

@All(PlayerAnimationComponent::class, TransformComponent::class)
class PlayerAnimationSystem : IteratingSystem() {
    private lateinit var mPlayerAnimationCM: ComponentMapper<PlayerAnimationComponent>
    private lateinit var mTransformCM: ComponentMapper<TransformComponent>
    override fun process(entityId: Int) {
        val playerAnimation = mPlayerAnimationCM[entityId]
        val transform = mTransformCM[entityId]

        if (playerAnimation.currentAnimation.isDone())
            playerAnimation.reset()
        else if (!playerAnimation.currentAnimation.isRunning())
            playerAnimation.currentAnimation.start()

        playerAnimation.currentAnimation.step(transform, world.delta)
    }
}