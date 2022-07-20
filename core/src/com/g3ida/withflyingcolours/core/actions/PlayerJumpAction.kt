package com.g3ida.withflyingcolours.core.actions

import com.badlogic.gdx.physics.box2d.Body
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent

class PlayerJumpAction(val physicsBodyComponent: PhysicsBodyComponent): IGameAction {
    private val body: Body = physicsBodyComponent.body
    private val jumpForce = 760.0f

    override fun execute() {
        body.run {
            applyLinearImpulse(0f, jumpForce, worldCenter.x, worldCenter.y, true)
        }
    }

    override fun step(delta: Float) { }
}