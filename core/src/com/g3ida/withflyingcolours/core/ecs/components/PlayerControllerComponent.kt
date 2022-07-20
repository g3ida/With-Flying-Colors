package com.g3ida.withflyingcolours.core.ecs.components

import com.artemis.PooledComponent

class PlayerControllerComponent : PooledComponent() {
    var moveInput = 0
    var shouldRotateLeft = false
    var shouldRotateRight = false
    var shouldJump = false
    public override fun reset() {
        moveInput = 0
        shouldRotateLeft = false
        shouldRotateRight = false
        shouldJump = false
    }
}