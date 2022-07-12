package com.g3ida.withflyingcolours.core.player.controller

import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.artemis.annotations.All
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

@All(PlayerControllerComponent::class)
class PlayerControllerSystem : IteratingSystem() {
    lateinit var mPlayerControllerComponent: ComponentMapper<PlayerControllerComponent>
    override fun process(entityId: Int) {
        //player movement left/right
        val playerMovement = mPlayerControllerComponent[entityId]
        playerMovement.moveInput = when {
            Gdx.input.isKeyPressed(Input.Keys.LEFT) -> -1
            Gdx.input.isKeyPressed(Input.Keys.RIGHT) -> 1
            else -> 0
        }
        //jumping
        playerMovement.shouldJump = Gdx.input.isKeyPressed(Input.Keys.UP)
        //rotating
        playerMovement.shouldRotateLeft = Gdx.input.isKeyJustPressed(Input.Keys.Z)
        playerMovement.shouldRotateRight = Gdx.input.isKeyJustPressed(Input.Keys.C)
    }
}