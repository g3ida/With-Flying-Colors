package com.g3ida.withflyingcolours.core.ecs.components

import com.artemis.PooledComponent

class PlayerWalkComponent : PooledComponent() {
    var direction = 0
    var speed = 5f
    public override fun reset() {
        direction = 0
        speed = 3f
    }
}