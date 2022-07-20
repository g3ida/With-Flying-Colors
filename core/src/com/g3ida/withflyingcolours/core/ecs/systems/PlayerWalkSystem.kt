package com.g3ida.withflyingcolours.core.ecs.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.g3ida.withflyingcolours.core.ecs.components.PlayerWalkComponent
import com.g3ida.withflyingcolours.core.extensions.*
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent

const val DRAG_FORCE = 10f

@All(PlayerWalkComponent::class, PhysicsBodyComponent::class)
class PlayerWalkSystem : IteratingSystem() {
    private lateinit var mPhysicsBodyCM: ComponentMapper<PhysicsBodyComponent>
    private lateinit var mPlayerWalkCM: ComponentMapper<PlayerWalkComponent>

    override fun process(entityId: Int) {
        val physicsBody = mPhysicsBodyCM[entityId]
        val playerWalk = mPlayerWalkCM[entityId]
        physicsBody.body.setLinearVelocityX(playerWalk.direction * playerWalk.speed)
        physicsBody.body.addDragForce(DRAG_FORCE) // adds some air resistance
    }
}