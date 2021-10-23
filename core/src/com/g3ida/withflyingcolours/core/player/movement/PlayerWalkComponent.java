package com.g3ida.withflyingcolours.core.player.movement;

import games.rednblack.editor.renderer.components.BaseComponent;

public class PlayerWalkComponent implements BaseComponent {

    public int direction = 0;
    public float speed = 5f;

    @Override
    public void reset() {
        direction = 0;
        speed = 3f;
    }
}
