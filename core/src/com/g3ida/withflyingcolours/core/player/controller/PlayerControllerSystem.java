package com.g3ida.withflyingcolours.core.player.controller;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import games.rednblack.editor.renderer.utils.ComponentRetriever;

public class PlayerControllerSystem extends IteratingSystem {

    public PlayerControllerSystem() {
        super(Family.all(PlayerControllerComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        //player movement left/right
        PlayerControllerComponent playerMovement = ComponentRetriever.get(entity, PlayerControllerComponent.class);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerMovement.moveInput = -1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerMovement.moveInput = 1;
        } else {
            playerMovement.moveInput = 0;
        }

        //jumping
        playerMovement.shouldJump = Gdx.input.isKeyPressed(Input.Keys.UP);

        //rotating
        playerMovement.shouldRotateLeft = Gdx.input.isKeyJustPressed(Input.Keys.Z);

        playerMovement.shouldRotateRight = Gdx.input.isKeyJustPressed(Input.Keys.C);
    }
}
