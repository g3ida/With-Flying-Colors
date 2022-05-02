package com.g3ida.withflyingcolours.core.player.movement;

import com.artemis.Component;
import com.artemis.PooledComponent;
import com.g3ida.withflyingcolours.utils.RotationDirection;

public class PlayerRotationComponent extends PooledComponent {

    public float rotationDuration = 0.12f; // duration of the rotation animation.
    float thetaZero; // initial angle, before the rotation is performed.
    float thetaTarget; // target angle, after the rotation is completed.
    float thetaPoint; // the calculated angular velocity.
    float rotationTimer = 0f; // countdown rotation timer. initially set to rotationDuration.
    byte rotationDirectionSign = 1; // 1 = anti-clockwise, -1 = clockwise.
    boolean canRotate = true; // set to false when rotation is in progress.
    boolean shouldRotate = false; // turn this to true if you want to trigger rotation.

    @Override
    public void reset() {
        canRotate = false;
        shouldRotate = false;
        rotationDirectionSign = 1;
        rotationTimer = 0f;
        rotationDuration = 0f;
        thetaZero = 0f;
        thetaTarget = 0f;
        thetaPoint = 0f;
    }

    public void setRotationDirection(RotationDirection rotationDirection) {
        shouldRotate = true;
        switch (rotationDirection) {
            case clockwise: rotationDirectionSign = -1; break;
            case antiClockwise:  rotationDirectionSign = 1; break;
        }
    }
}
