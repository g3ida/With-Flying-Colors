package com.g3ida.withflyingcolours.core.actions

import com.badlogic.gdx.math.MathUtils
import com.g3ida.withflyingcolours.core.events.GameEvent
import com.g3ida.withflyingcolours.utils.extensions.PI2
import com.g3ida.withflyingcolours.utils.CountdownTimer
import com.g3ida.withflyingcolours.utils.RotationDirection
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent

class PlayerRotationAction(val physicsBodyComponent: PhysicsBodyComponent): IGameAction {

    var rotationDuration = 0.12f // duration of the rotation animation.
    var thetaZero = 0f // initial angle, before the rotation is performed.
    var thetaTarget = 0f // target angle, after the rotation is completed.
    var thetaPoint = 0f // the calculated angular velocity.
    val rotationTimer: CountdownTimer = CountdownTimer(rotationDuration, false) // countdown rotation timer. initially set to rotationDuration.
    var canRotate = true // set to false when rotation is in progress.

    override fun step(delta: Float) {
        // update rotation
        if (rotationTimer.isRunning()) {
            rotationTimer.step(delta)
            if (!rotationTimer.isRunning()) {
                // correction for the last frame
                val currentAngle = physicsBodyComponent.body.angle
                thetaPoint = (thetaTarget - currentAngle) / delta
                rotationTimer.stop()
            }
            physicsBodyComponent.body.angularVelocity = thetaPoint
        } else if (!canRotate) {
            thetaPoint = 0f
            physicsBodyComponent.body.angularVelocity = 0f
            rotationTimer.stop()
            canRotate = true
        }
    }

    override fun execute(event: GameEvent) {
        val direction: Int = event.extraData.get("direction", "1").toIntOrNull() ?: 1
        canRotate = false
        thetaZero = physicsBodyComponent.body.angle
        thetaTarget = MathUtils.round((thetaZero + direction * Float.PI2) / Float.PI2) * Float.PI2
        thetaPoint = (thetaTarget - thetaZero) / rotationDuration
        rotationTimer.reset()
    }
}