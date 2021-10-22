package com.g3ida.withflyingcolours.core.player.movement;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.g3ida.withflyingcolours.Utils;

import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.utils.ComponentRetriever;

public class PlayerJumpSystem extends IteratingSystem {

    public PlayerJumpSystem() {
        super(Family.all(PhysicsBodyComponent.class, PlayerJumpComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PhysicsBodyComponent physicsBody = ComponentRetriever.get(entity, PhysicsBodyComponent.class);
        PlayerJumpComponent playerJump = ComponentRetriever.get(entity, PlayerJumpComponent.class);

        playerJump.timeSinceGrounded += deltaTime;
        boolean doJump = false; //should we jump in this frame ?

        if (isJumpPressed(playerJump)) {
            if (can_jump(playerJump, physicsBody)) {
                doJump = true;
            } else { // if the player can't jump right now let's check if he can before the responsiveness time passes.
                playerJump.responsivenessTimer = PlayerJumpComponent.jumpSettigs.responsiveness;
            }
        }

        //handle responsiveness
        if (playerJump.responsivenessTimer > 0) {
            if (can_jump(playerJump, physicsBody)) {
                playerJump.responsivenessTimer = 0;
                doJump = true;
            } else {
                playerJump.responsivenessTimer -= deltaTime;
            }
        } else {
            playerJump.responsivenessTimer = 0;
        }

        //perform the jump
        if (doJump){
            //if (playerJump.shouldJump && !wasJumping)
            {
                playerJump.jumpTimer = playerJump.timeUntilFullJumpIsConsidered;
                //rb.velocity = Vector2.up * jump_force + rb.velocity;
                physicsBody.body.applyForce(new Vector2(0f, playerJump.jumpForce), physicsBody.body.getWorldCenter(), true);
            }
        }

        //jump has been released before full jump reached
        if (!playerJump.shouldJump && playerJump.jumpTimer > Utils.epsilon) {
            //cancel jump
            Vector2 velocity = physicsBody.body.getLinearVelocity();
            if (velocity.y > 0) { // decrease velocity only if the player is going up !
                physicsBody.body.setLinearVelocity(velocity.x, velocity.y * 0.5f);
            }
        }

        if (playerJump.shouldJump) {
            playerJump.jumpTimer -= deltaTime;
        }

        playerJump.oldShouldJump = playerJump.shouldJump;
    }

    boolean isJumpPressed(PlayerJumpComponent playerJump) {
        return !playerJump.oldShouldJump && playerJump.shouldJump;
    }

    boolean isJumpReleased(PlayerJumpComponent playerJump) {
        return playerJump.oldShouldJump && !playerJump.shouldJump;
    }

    boolean isJumpDown(PlayerJumpComponent playerJump) {
        return playerJump.shouldJump;
    }

    boolean isGrounded(PhysicsBodyComponent physicsBody) {
        // FIXME: this condition is also met on jump peak.
        return Math.abs(physicsBody.body.getLinearVelocity().y) < Utils.epsilon;
    }

    boolean can_jump(PlayerJumpComponent playerJump, PhysicsBodyComponent physicsBody) {
        if(isGrounded(physicsBody)) {
            playerJump.timeSinceGrounded = 0;
            return true;
        }
        //handle permissiveness
        boolean isJumping = physicsBody.body.getLinearVelocity().y > 0;
        return playerJump.timeSinceGrounded <= PlayerJumpComponent.jumpSettigs.permissiveness && !isJumping;
    }
}
