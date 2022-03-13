package com.g3ida.withflyingcolours.core.player.animation;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Interpolation;

import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.utils.ComponentRetriever;

public class PlayerAnimationSystem extends IteratingSystem {

    public PlayerAnimationSystem() {
        super(Family.all(PlayerAnimationComponent.class, TransformComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerAnimationComponent playerAnimation = ComponentRetriever.get(entity, PlayerAnimationComponent.class);

        if (playerAnimation.doSqueeze) {
            playerAnimation.doSqueeze = false;
            playerAnimation.squeezeAnimation.start();
        }
        else if (playerAnimation.doScale) {
            playerAnimation.doScale = false;
            playerAnimation.scaleAnimation.start();
        }

        TransformComponent transform = ComponentRetriever.get(entity, TransformComponent.class);

        playerAnimation.scaleAnimation.step(transform, deltaTime);
        playerAnimation.squeezeAnimation.step(transform, deltaTime);
    }
}
