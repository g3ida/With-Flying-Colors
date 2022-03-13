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
            playerAnimation.squeezeTimer = PlayerAnimationComponent.SQUEEZE_ANIM_DURATION;
            playerAnimation.interpolation = new Interpolation.ElasticOut(1f, 1f, 1, 0.1f);

        }
        else if (playerAnimation.doScale) {
            playerAnimation.doScale = false;
            playerAnimation.squeezeTimer = PlayerAnimationComponent.SCALE_ANIM_DURATION;
            playerAnimation.interpolation = new Interpolation.ElasticIn(1f, 1f, 1, 0.1f);
        }

        TransformComponent transform = ComponentRetriever.get(entity, TransformComponent.class);
        if (playerAnimation.squeezeTimer > 0f) {
            float normalized = playerAnimation.squeezeTimer / PlayerAnimationComponent.SQUEEZE_ANIM_DURATION;
            float i = playerAnimation.interpolation.apply(0, 1, normalized);
            transform.scaleX = i;
            transform.scaleY = 1 / (i + 0.01f);
            playerAnimation.squeezeTimer -= deltaTime;
        }
        else if (playerAnimation.scaleTimer > 0f) {
            float normalized = playerAnimation.scaleTimer / PlayerAnimationComponent.SCALE_ANIM_DURATION;
            float i = playerAnimation.interpolation.apply(0, 1, normalized);
            transform.scaleX = i;
            transform.scaleY = 1 / (i + 0.01f);
            playerAnimation.scaleTimer -= deltaTime;
        }
        else {
            transform.scaleX = 1f;
            transform.scaleY = 1f;
        }
    }
}
