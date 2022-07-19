package com.g3ida.withflyingcolours.core.player.movement

import com.artemis.PooledComponent
import com.g3ida.withflyingcolours.utils.RotationDirection

class PlayerRotationComponent : PooledComponent() {
    var rotationDuration = 0.12f // duration of the rotation animation.
    var thetaZero // initial angle, before the rotation is performed.
            = 0f
    var thetaTarget // target angle, after the rotation is completed.
            = 0f
    var thetaPoint // the calculated angular velocity.
            = 0f
    var rotationTimer = 0f // countdown rotation timer. initially set to rotationDuration.
    var rotationDirectionSign: Byte = 1 // 1 = anti-clockwise, -1 = clockwise.
    var canRotate = true // set to false when rotation is in progress.
    var shouldRotate = false // turn this to true if you want to trigger rotation.
    public override fun reset() {
        canRotate = false
        shouldRotate = false
        rotationDirectionSign = 1
        rotationTimer = 0f
        rotationDuration = 0f
        thetaZero = 0f
        thetaTarget = 0f
        thetaPoint = 0f
    }

    fun setRotationDirection(rotationDirection: RotationDirection?) {
        shouldRotate = true
        when (rotationDirection) {
            RotationDirection.Clockwise -> rotationDirectionSign = -1
            RotationDirection.AntiClockwise -> rotationDirectionSign = 1
            else -> {}
        }
    }
}