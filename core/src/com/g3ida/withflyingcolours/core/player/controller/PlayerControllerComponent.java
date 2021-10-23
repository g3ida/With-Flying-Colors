package com.g3ida.withflyingcolours.core.player.controller;

import games.rednblack.editor.renderer.components.BaseComponent;

public class PlayerControllerComponent implements BaseComponent {

    public int moveInput = 0;
    public boolean shouldRotateLeft = false;
    public boolean shouldRotateRight = false;
    public boolean shouldJump = false;

    @Override
    public void reset() {
        moveInput = 0;
        shouldRotateLeft = false;
        shouldRotateRight = false;
        shouldJump = false;
    }
}
