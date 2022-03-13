package com.g3ida.withflyingcolours.core.player.animation;

import com.badlogic.gdx.math.Interpolation;

import games.rednblack.editor.renderer.components.BaseComponent;

public class PlayerAnimationComponent implements BaseComponent {

    public TransformAnimation scaleAnimation;
    public TransformAnimation squeezeAnimation;

    public boolean doScale;
    public boolean doSqueeze;

    public static final float SQUEEZE_ANIM_DURATION = 0.17f;
    public static final float SCALE_ANIM_DURATION = 0.17f;

    public PlayerAnimationComponent() {
        super();

        this.scaleAnimation = new TransformAnimation(PlayerAnimationComponent.SCALE_ANIM_DURATION,
                new Interpolation.ElasticIn(1f, 1f, 1, 0.1f),
                true,
                true);

        this.squeezeAnimation = new TransformAnimation(PlayerAnimationComponent.SQUEEZE_ANIM_DURATION,
                new Interpolation.ElasticOut(1f, 1f, 1, 0.1f),
                true,
                false);
        reset();
    }

    @Override
    public void reset() {
        this.doScale = false;
        this.doSqueeze = false;
        this.scaleAnimation.reset(null);
        this.squeezeAnimation.reset(null);
    }
}
