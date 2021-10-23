package com.g3ida.withflyingcolours.core.scripts;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.g3ida.withflyingcolours.core.player.controller.PlayerControllerComponent;
import com.g3ida.withflyingcolours.core.player.controller.PlayerControllerSystem;
import com.g3ida.withflyingcolours.core.player.movement.PlayerJumpComponent;
import com.g3ida.withflyingcolours.core.player.movement.PlayerJumpSystem;
import com.g3ida.withflyingcolours.core.player.movement.PlayerRotationComponent;
import com.g3ida.withflyingcolours.core.player.movement.PlayerRotationSystem;
import com.g3ida.withflyingcolours.core.player.movement.PlayerWalkComponent;
import com.g3ida.withflyingcolours.core.player.movement.PlayerWalkSystem;
import com.g3ida.withflyingcolours.utils.RotationDirection;


import games.rednblack.editor.renderer.SceneLoader;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.scripts.BasicScript;
import games.rednblack.editor.renderer.utils.ComponentRetriever;
import games.rednblack.editor.renderer.utils.ItemWrapper;

public class Player extends GameScript {

    private Entity _entity;
    private PlayerControllerComponent _playerController;
    private PlayerRotationComponent _playerRotation;
    private PlayerJumpComponent _playerJump;
    private PlayerWalkComponent _playerWalk;
    TransformComponent _transform;

    private final Vector2 impulse = new Vector2(0, 0);
    private final Vector2 speed = new Vector2(0, 0);

    public Player(PooledEngine engine) {
        super(engine);

        //FIXME: find a better place for initializing systems
        getEngine().addSystem(new PlayerControllerSystem());
        getEngine().addSystem(new PlayerRotationSystem());
        getEngine().addSystem(new PlayerJumpSystem());
        getEngine().addSystem(new PlayerWalkSystem());
    }

    public void initComponents() {

        _transform = ComponentRetriever.get(_entity, TransformComponent.class);

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
    }
}
