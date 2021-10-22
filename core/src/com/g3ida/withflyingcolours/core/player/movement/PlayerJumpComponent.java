package com.g3ida.withflyingcolours.core.player.movement;

import com.g3ida.withflyingcolours.core.player.PlayerControllerSettings;

import games.rednblack.editor.renderer.components.BaseComponent;

public class PlayerJumpComponent implements BaseComponent {

    public static final PlayerControllerSettings jumpSettigs = new PlayerControllerSettings(0.08f, 0.1f);

    public boolean shouldJump = false; // set this to true in order to perform jump. turn it to false once the jump key released.

    //internal attributes
    float timeUntilFullJumpIsConsidered = 0.55f; // minimum amount of time needed to press the jump button in order to consider it as full jump.
    float jumpTimer = 0f; // time while jumping.
    float timeSinceGrounded = 0f; // last time since the player hit the ground.
    float jumpForce = 20000.0f; // the force applied to perform the jump.
    float responsivenessTimer = 0f; // time since the player hit the jump button.
    boolean oldShouldJump = false; // previous value of should jump.

    @Override
    public void reset() {
        responsivenessTimer = 0f;
        timeUntilFullJumpIsConsidered = 0.15f;
        timeSinceGrounded = 0f;
        jumpTimer = 0f;
        oldShouldJump = false;
        shouldJump = true;
    }
}
