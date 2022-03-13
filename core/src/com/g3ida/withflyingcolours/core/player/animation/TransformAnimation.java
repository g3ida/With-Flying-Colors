package com.g3ida.withflyingcolours.core.player.animation;

import com.badlogic.gdx.math.Interpolation;

import games.rednblack.editor.renderer.components.TransformComponent;

public class TransformAnimation {

    private final float _animationDuration;
    private final Interpolation _interpolation;
    private final boolean _centerOriginX;
    private final boolean _centerOriginY;
    private float _timer = 0f;

    public TransformAnimation(float duration,
                              Interpolation interpolation,
                              boolean centerOriginX,
                              boolean centerOriginY) {
        this._animationDuration = duration;
        this._interpolation = interpolation;
        this._centerOriginX = centerOriginX;
        this._centerOriginY = centerOriginY;
    }

    public void start() {
        if (this._timer > 0f)
            return;
        this._timer = _animationDuration;

    }

    public void step(TransformComponent transform, float deltaTime) {
        if (_timer > 0f) {
            float normalized = _timer / PlayerAnimationComponent.SQUEEZE_ANIM_DURATION;
            float i = _interpolation.apply(0f, 1f, normalized);
            transform.scaleX = i;
            transform.scaleY = 1f / (i + 0.01f);
            if (!_centerOriginY) {
                transform.originY = 0.5f + (1f - transform.scaleY) * 0.5f;
            }
            if (!_centerOriginX) {
                transform.originX = 0.5f + (1f - transform.scaleX) * 0.5f;
            }
            _timer -= deltaTime;
        } else {
            this.reset(transform);
        }
    }

    public void reset(TransformComponent transform) {
        this._timer = 0f;
        if (transform != null) {
            transform.scaleX = 1f;
            transform.scaleY = 1f;
            transform.originY = 0.5f;
            transform.originX = 0.5f;
        }
    }
}
