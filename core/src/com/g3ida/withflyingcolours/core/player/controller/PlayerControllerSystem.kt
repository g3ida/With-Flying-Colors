package com.g3ida.withflyingcolours.core.player.controller;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import games.rednblack.editor.renderer.utils.ComponentRetriever;

@All(PlayerControllerComponent.class)
public class PlayerControllerSystem extends IteratingSystem {
    ComponentMapper<PlayerControllerComponent> mPlayerControllerComponent;

    public PlayerControllerSystem() {
        super();
    }

    @Override
    protected void process(int entityId) {
        //player movement left/right
        PlayerControllerComponent playerMovement = mPlayerControllerComponent.get(entityId);
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
