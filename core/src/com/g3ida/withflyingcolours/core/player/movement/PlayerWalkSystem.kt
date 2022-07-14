package com.g3ida.withflyingcolours.core.player.movement

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import kotlin.math.abs

const val DRAG_FORCE = 10f

@All(PlayerWalkComponent::class, PhysicsBodyComponent::class)
class PlayerWalkSystem : IteratingSystem() {
    lateinit var mPhysicsBodyComponent: ComponentMapper<PhysicsBodyComponent>
    lateinit var mPlayerWalkComponent: ComponentMapper<PlayerWalkComponent>

    override fun process(entityId: Int) {
        val physicsBody = mPhysicsBodyComponent[entityId]
        val playerWalk = mPlayerWalkComponent[entityId]

        val velocity = physicsBody.body.linearVelocity
        if (abs(velocity.x) > playerWalk.speed) {
            if (velocity.x * playerWalk.direction < 0) { // not the same direction
                velocity.x += playerWalk.direction * playerWalk.speed
            }
        } else {
            velocity.x = playerWalk.direction * playerWalk.speed
        }
        physicsBody.body.linearVelocity = velocity
        addDragForce(physicsBody.body)
    }

    private fun addDragForce(body: Body) {
        // drag force (creates some air resistance)
        val velocity = body.linearVelocity
        val dragForce = Vector2(velocity.x - DRAG_FORCE * abs(velocity.x) * velocity.x, 0.0f)
        body.applyForceToCenter(dragForce, true)
    }
}