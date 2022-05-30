package com.g3ida.withflyingcolours.core.scripts;

import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.g3ida.withflyingcolours.core.camera.CameraSystem;
import com.g3ida.withflyingcolours.core.player.animation.PlayerAnimationComponent;
import com.g3ida.withflyingcolours.core.player.controller.PlayerControllerComponent;
import com.g3ida.withflyingcolours.core.player.movement.PlayerJumpComponent;
import com.g3ida.withflyingcolours.core.player.movement.PlayerRotationComponent;
import com.g3ida.withflyingcolours.core.player.movement.PlayerWalkComponent;
import com.g3ida.withflyingcolours.utils.RotationDirection;

import games.rednblack.editor.renderer.components.DimensionsComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.utils.ComponentRetriever;

public class Player extends GameScript {

    private int _entityId;
    private PlayerControllerComponent _playerController;
    private PlayerRotationComponent _playerRotation;
    private PlayerJumpComponent _playerJump;
    private PlayerWalkComponent _playerWalk;
    private PlayerAnimationComponent _playerAnim;

    ComponentMapper<PlayerControllerComponent> mPlayerControllerComponent;
    ComponentMapper<PlayerRotationComponent> mPlayerRotationComponent;
    ComponentMapper<PlayerJumpComponent> mPlayerJumpComponent;
    ComponentMapper<PlayerWalkComponent> mPlayerWalkComponent;
    ComponentMapper<PlayerAnimationComponent> mPlayerAnimationComponent;

    public Player(com.artemis.World engine, World world) {
        super(engine, world);
    }

    public void initComponents() {
        //FIXME : find a better place for this
        ComponentRetriever.addMapper(PlayerControllerComponent.class);
        ComponentRetriever.addMapper(PlayerRotationComponent.class);
        ComponentRetriever.addMapper(PlayerJumpComponent.class);
        ComponentRetriever.addMapper(PlayerWalkComponent.class);
        ComponentRetriever.addMapper(PlayerAnimationComponent.class);

        _playerController = mPlayerControllerComponent.create(_entityId);
        _playerRotation = mPlayerRotationComponent.create(_entityId);
        _playerJump = mPlayerJumpComponent.create(_entityId);
        _playerWalk = mPlayerWalkComponent.create(_entityId);
        _playerAnim = mPlayerAnimationComponent.create(_entityId);

        // attach player to camera
        CameraSystem cameraSystem = getEngine().getSystem(CameraSystem.class);
        cameraSystem.setFocus(_entityId);
    }

    @Override
    public void init(int item) {
        super.init(item);
        _entityId = getEntity();
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
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(_entityId, DimensionsComponent.class, getEngine());
        PhysicsBodyComponent physicsBodyComponent = ComponentRetriever.get(_entityId, PhysicsBodyComponent.class, getEngine());
        TransformComponent transformComponent = ComponentRetriever.get(_entityId, TransformComponent.class, getEngine());

        float rayGap = transformComponent.scaleY * dimensionsComponent.height / 2f;

        float raySize = -physicsBodyComponent.body.getLinearVelocity().y * delta * 2f;

        Vector2 rayFrom = new Vector2((transformComponent.x+dimensionsComponent.width/2f), (transformComponent.y+rayGap));
        Vector2 rayTo = new Vector2((transformComponent.x+dimensionsComponent.width/2f), (transformComponent.y+0.05f-raySize));

        //cast the ray
        World world = getWorld();

        world.rayCast(new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                //Entity entity = (Entity) fixture.getBody().getUserData();
                //if (entity != null) {
                    if (_playerAnim != null) {
                        if (!_playerAnim.squeezeAnimation.isRunning() && !_playerAnim.scaleAnimation.isRunning()) {
                            _playerAnim.doSqueeze = true;
                        }
                    }
                //}
                return 0;
            }
        }, rayFrom, rayTo);
    }
}
