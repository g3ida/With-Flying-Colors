package com.g3ida.withflyingcolours.core.player.movement;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.g3ida.withflyingcolours.Utils;

import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.utils.ComponentRetriever;

@All({PhysicsBodyComponent.class, PlayerJumpComponent.class})
public class PlayerJumpSystem extends IteratingSystem {
    ComponentMapper<PhysicsBodyComponent> mPhysicsBodyComponent;
    ComponentMapper<PlayerJumpComponent> mPlayerJumpComponent;

    public PlayerJumpSystem() {
        super();
    }

    @Override
    protected void process(int entityId) {
        PhysicsBodyComponent physicsBody = mPhysicsBodyComponent.get(entityId);
        PlayerJumpComponent playerJump = mPlayerJumpComponent.get(entityId);

        if(isGrounded(physicsBody)) {
            playerJump.timeSinceGrounded = 0;
        }

        boolean doJump = false; //should we jump in this frame ?

        if (isJumpPressed(playerJump)) {
            if (can_jump(playerJump, physicsBody)) {
                doJump = true;
            } else { // if the player can't jump right now let's check if he can before the responsiveness time passes.
                playerJump.responsivenessTimer = PlayerJumpComponent.jumpSettings.responsiveness;
            }
        }

        //handle responsiveness
        if (playerJump.responsivenessTimer > 0) {
            if (can_jump(playerJump, physicsBody)) {
                playerJump.responsivenessTimer = 0;
                doJump = true;
            } else {
                playerJump.responsivenessTimer -= world.delta;
            }
        } else {
            playerJump.responsivenessTimer = 0;
        }

        //perform the jump
        if (doJump) {
            {
                playerJump.jumpTimer = playerJump.timeUntilFullJumpIsConsidered;
                physicsBody.body.applyLinearImpulse(0f, playerJump.jumpForce, physicsBody.body.getWorldCenter().x, physicsBody.body.getWorldCenter().y, true);
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
            playerJump.jumpTimer -= world.delta;
        }

        if(!isGrounded(physicsBody)) {
            playerJump.timeSinceGrounded += world.delta;
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
            return true;
        }
        //handle permissiveness
        boolean isJumping = physicsBody.body.getLinearVelocity().y > 0f;
        return (playerJump.timeSinceGrounded <= PlayerJumpComponent.jumpSettings.permissiveness)
                && !isJumping && playerJump.jumpTimer <= 0f;
    }
}
