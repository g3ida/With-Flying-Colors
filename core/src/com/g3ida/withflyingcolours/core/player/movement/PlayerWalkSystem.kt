package com.g3ida.withflyingcolours.core.player.movement

import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import com.artemis.annotations.All

@All(PlayerWalkComponent::class, PhysicsBodyComponent::class)
class PlayerWalkSystem : IteratingSystem() {
    lateinit var mPhysicsBodyComponent: ComponentMapper<PhysicsBodyComponent>
    lateinit var mPlayerWalkComponent: ComponentMapper<PlayerWalkComponent>
    override fun process(entityId: Int) {
        val physicsBody = mPhysicsBodyComponent[entityId]
        val playerWalk = mPlayerWalkComponent[entityId]
        val velocity = physicsBody.body.linearVelocity
        if (Math.abs(velocity.x) > playerWalk.speed) {
            if (velocity.x * playerWalk.direction < 0) { // not the same direction
                velocity.x += playerWalk.direction * playerWalk.speed
            }
        } else {
            velocity.x = playerWalk.direction * playerWalk.speed
        }
        physicsBody.body.linearVelocity = velocity
    }
}