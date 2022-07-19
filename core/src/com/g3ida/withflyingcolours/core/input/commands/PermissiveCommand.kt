package com.g3ida.withflyingcolours.core.input.commands

import com.g3ida.withflyingcolours.core.input.IWithUpdate
import com.g3ida.withflyingcolours.utils.CountdownTimer

abstract class PermissiveCommand(premissiveness: Float = 0.07f): ConditionalCommand(), IWithUpdate {
    //how much time we permit action since its conditions were met.
    private var mResponsivenessTimer = CountdownTimer(premissiveness, isSet = false)
    //how much do we delay action hoping its conditions to be met.
    private var mPermissivenessTimer = CountdownTimer(premissiveness, isSet = false)

    override fun canExecute(): Boolean = true

    override fun run() {
        if (canExecute() || mPermissivenessTimer.isRunning()) {
            mPermissivenessTimer.stop()
            mResponsivenessTimer.stop()
            execute()
        } else {
            mResponsivenessTimer.reset()
        }
    }

    override fun update(delta: Float) {
        if (canExecute()) {
            mPermissivenessTimer.reset()
            if (mResponsivenessTimer.isRunning()) {
                mResponsivenessTimer.stop()
                mPermissivenessTimer.stop()
                execute()
            }
        } else {
            mResponsivenessTimer.step(delta)
            mPermissivenessTimer.step(delta)
        }
    }
}