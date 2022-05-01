package com.g3ida.withflyingcolours.core.scripts;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.g3ida.withflyingcolours.core.camera.CameraSystem;
import com.g3ida.withflyingcolours.core.player.animation.PlayerAnimationComponent;
import com.g3ida.withflyingcolours.core.player.animation.PlayerAnimationSystem;
import com.g3ida.withflyingcolours.core.player.controller.PlayerControllerComponent;
import com.g3ida.withflyingcolours.core.player.controller.PlayerControllerSystem;
import com.g3ida.withflyingcolours.core.player.movement.PlayerJumpComponent;
import com.g3ida.withflyingcolours.core.player.movement.PlayerJumpSystem;
import com.g3ida.withflyingcolours.core.player.movement.PlayerRotationComponent;
import com.g3ida.withflyingcolours.core.player.movement.PlayerRotationSystem;
import com.g3ida.withflyingcolours.core.player.movement.PlayerWalkComponent;
import com.g3ida.withflyingcolours.core.player.movement.PlayerWalkSystem;
import com.g3ida.withflyingcolours.utils.RotationDirection;

import games.rednblack.editor.renderer.components.DimensionsComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.utils.ComponentRetriever;

public class Player extends GameScript {

    private Entity _entity;
    private PlayerControllerComponent _playerController;
    private PlayerRotationComponent _playerRotation;
    private PlayerJumpComponent _playerJump;
    private PlayerWalkComponent _playerWalk;
    private PlayerAnimationComponent _playerAnim;

    //private ShaderProgram _shader;

    private final Vector2 impulse = new Vector2(0, 0);
    private final Vector2 speed = new Vector2(0, 0);

    public Player(PooledEngine engine, World world) {
        super(engine, world);
        //FIXME: find a better place for initializing systems
        getEngine().addSystem(new PlayerControllerSystem());
        getEngine().addSystem(new PlayerRotationSystem());
        getEngine().addSystem(new PlayerJumpSystem());
        getEngine().addSystem(new PlayerWalkSystem());
        getEngine().addSystem(new PlayerAnimationSystem());
    }

    public void initComponents() {

        // add PlayerMovementComponent to the player entity.
        ComponentRetriever.addMapper(PlayerControllerComponent.class); //FIXME : find a better place for this
        _entity.add(getEngine().createComponent(PlayerControllerComponent.class));
        _playerController = ComponentRetriever.get(_entity, PlayerControllerComponent.class);

        // add PlayerRotationComponent to the player entity.
        ComponentRetriever.addMapper(PlayerRotationComponent.class);
        _entity.add(getEngine().createComponent(PlayerRotationComponent.class));
        _playerRotation = ComponentRetriever.get(_entity, PlayerRotationComponent.class);

        // add PlayerJumpComponent to the player entity.
        ComponentRetriever.addMapper(PlayerJumpComponent.class);
        _entity.add(getEngine().createComponent(PlayerJumpComponent.class));
        _playerJump = ComponentRetriever.get(_entity, PlayerJumpComponent.class);

        // add PlayerWalkComponent to the player entity.
        ComponentRetriever.addMapper(PlayerWalkComponent.class);
        _entity.add(getEngine().createComponent(PlayerWalkComponent.class));
        _playerWalk = ComponentRetriever.get(_entity, PlayerWalkComponent.class);

        // add PlayerWalkComponent to the player entity.
        ComponentRetriever.addMapper(PlayerAnimationComponent.class);
        _entity.add(getEngine().createComponent(PlayerAnimationComponent.class));
        _playerAnim = ComponentRetriever.get(_entity, PlayerAnimationComponent.class);

        // attach player to camera
        CameraSystem cameraSystem = getEngine().getSystem(CameraSystem.class);
        cameraSystem.setFocus(_entity);
    }

    @Override
    public void init(Entity item) {
        super.init(item);
        _entity = getEntity();
        initComponents();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void act(float delta) {

        //TODO: handle left and write controls
        _playerWalk.direction =  _playerController.moveInput;

        _playerJump.shouldJump = _playerController.shouldJump;

        if(_playerController.shouldRotateRight) {
            _playerRotation.setRotationDirection(RotationDirection.clockwise);
        }

        if(_playerController.shouldRotateLeft) {
            _playerRotation.setRotationDirection(RotationDirection.antiClockwise);
        }

        rayCast(delta);
    }

    public void rayCast(float delta) {
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(_entity, DimensionsComponent.class);
        PhysicsBodyComponent physicsBodyComponent = ComponentRetriever.get(_entity, PhysicsBodyComponent.class);
        TransformComponent transformComponent = ComponentRetriever.get(_entity, TransformComponent.class);

        float rayGap = transformComponent.scaleY * dimensionsComponent.height / 2;

        float raySize = -physicsBodyComponent.body.getLinearVelocity().y * delta;

        //float ratioX = GameSettings.mainViewPort.getScreenWidth() / GameSettings.mainViewPort.getWorldWidth();
        //float ratioY = GameSettings.mainViewPort.getScreenHeight() / GameSettings.mainViewPort.getWorldHeight();

        Vector2 rayFrom = new Vector2((transformComponent.x+dimensionsComponent.width/2), (transformComponent.y+rayGap));
        Vector2 rayTo = new Vector2((transformComponent.x+dimensionsComponent.width/2), (transformComponent.y-raySize));

        //cast the ray
        World world = getWorld();

        world.rayCast(new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                //Entity entity = (Entity) fixture.getBody().getUserData();
                //if (entity != null) {
                    PlayerAnimationComponent playerAnimationComponent = ComponentRetriever.get(_entity, PlayerAnimationComponent.class);
                    if (playerAnimationComponent != null) {
                        if (!playerAnimationComponent.squeezeAnimation.isRunning() && !playerAnimationComponent.scaleAnimation.isRunning()) {
                            playerAnimationComponent.doSqueeze = true;
                        }
                    }
                //}
                return 0;
            }
        }, rayFrom, rayTo);
    }
}
