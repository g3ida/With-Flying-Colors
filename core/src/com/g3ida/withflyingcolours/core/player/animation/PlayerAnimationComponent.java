package com.g3ida.withflyingcolours.core.player.animation;

import com.badlogic.gdx.math.Interpolation;

import games.rednblack.editor.renderer.components.BaseComponent;

public class PlayerAnimationComponent implements BaseComponent {

    public boolean doScale;
    public boolean doSqueeze;
    public float squeezeTimer;
    public float scaleTimer;
    public static final float SQUEEZE_ANIM_DURATION = 0.17f;
    public static final float SCALE_ANIM_DURATION = 0.17f;
    public Interpolation interpolation;
    public PlayerAnimationComponent() {
        reset();
    }

    @Override
    public void reset() {
        this.doScale = false;
        this.doSqueeze = false;
        this.squeezeTimer = 0;
    }
}
