package com.g3ida.withflyingcolours.core.player.animation;

import com.badlogic.gdx.math.Interpolation;
import com.g3ida.withflyingcolours.Utils;

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
        if (isRunning())
            return;
        this._timer = _animationDuration;
    }

    public boolean isRunning() {
        return this._timer > 0f;
    }

    public void step(TransformComponent transform, float deltaTime) {
        if (_timer > 0f) {

            // if the player is rotated we should react according to the actual direction
            float cosRotation = (float)Math.cos(transform.rotation * Utils.PI / 180.);
            float sinRotation = (float)Math.sin(transform.rotation * Utils.PI / 180.);

            float normalized = _timer / _animationDuration;
            float i = _interpolation.apply(0f, 1f, normalized);
            transform.scaleX = i * Math.abs(cosRotation) +  Math.abs(sinRotation) / (i + 0.01f);
            transform.scaleY = i * Math.abs(sinRotation) +  Math.abs(cosRotation) / (i + 0.01f);

            if (!this._centerOriginY) {
                transform.originY = 0.5f + (1f - transform.scaleY) * 0.5f * cosRotation;
                transform.originX = 0.5f + (1f - transform.scaleX) * 0.5f * sinRotation;
            }
            if (!this._centerOriginX) {
                transform.originX = 0.5f + (1f - transform.scaleX) * 0.5f * cosRotation;
                transform.originY = 0.5f + (1f - transform.scaleY) * 0.5f * sinRotation;
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
