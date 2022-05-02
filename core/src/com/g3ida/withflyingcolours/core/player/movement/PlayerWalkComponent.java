package com.g3ida.withflyingcolours.core.player.movement;

import com.artemis.Component;
import com.artemis.PooledComponent;

public class PlayerWalkComponent extends PooledComponent {

    public int direction = 0;
    public float speed = 5f;

    @Override
    public void reset() {
        direction = 0;
        speed = 3f;
    }
}
