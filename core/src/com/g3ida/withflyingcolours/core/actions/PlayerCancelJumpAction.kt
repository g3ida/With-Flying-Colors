package com.g3ida.withflyingcolours.core.actions

import com.badlogic.gdx.physics.box2d.Body
import com.g3ida.withflyingcolours.core.actions.IGameAction
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent

class PlayerCancelJumpAction(val physicsBodyComponent: PhysicsBodyComponent): IGameAction {
    private val body: Body = physicsBodyComponent.body

    override fun execute() {
        val velocity = body.linearVelocity
        if (velocity.y > 0f) { // decrease velocity only if the player is going up !
            body.setLinearVelocity(velocity.x, velocity.y * 0.5f)
        }
    }

    override fun step(delta: Float) {}
}