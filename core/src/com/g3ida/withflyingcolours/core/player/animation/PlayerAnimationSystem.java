package com.g3ida.withflyingcolours.core.player.animation;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;

import games.rednblack.editor.renderer.components.TransformComponent;

@All({PlayerAnimationComponent.class, TransformComponent.class})
public class PlayerAnimationSystem extends IteratingSystem {
    private ComponentMapper<PlayerAnimationComponent> mPlayerAnimationComponent;
    private ComponentMapper<TransformComponent> mTransformComponent;

    public PlayerAnimationSystem() {
        super();
    }

    @Override
    protected void process(int entityId) {
        PlayerAnimationComponent playerAnimation = mPlayerAnimationComponent.get(entityId);
        TransformComponent transform = mTransformComponent.get(entityId);

        if (playerAnimation.doSqueeze) {
            playerAnimation.doSqueeze = false;
            playerAnimation.squeezeAnimation.start();
        }
        else if (playerAnimation.doScale) {
            playerAnimation.doScale = false;
            playerAnimation.scaleAnimation.start();
        }

        playerAnimation.scaleAnimation.step(transform, world.delta);
        playerAnimation.squeezeAnimation.step(transform, world.delta);
    }
}
