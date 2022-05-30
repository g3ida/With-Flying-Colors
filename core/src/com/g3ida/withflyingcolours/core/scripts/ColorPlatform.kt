package com.g3ida.withflyingcolours.core.scripts;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.g3ida.withflyingcolours.Constants;
import com.g3ida.withflyingcolours.Utils;
import com.g3ida.withflyingcolours.core.platform.ColorPlatformRenderingComponent;

import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.physics.PhysicsContact;
import games.rednblack.editor.renderer.utils.ComponentRetriever;

public class ColorPlatform extends GameScript implements PhysicsContact {

    private int _entityId;
    private ColorPlatformRenderingComponent _platformRenderingComponent;

    public ColorPlatform(com.artemis.World engine, World world) {
        super(engine, world);
    }

    public void initComponents() {
        // add ColorPlatformRenderingComponent.
        ComponentRetriever.addMapper(ColorPlatformRenderingComponent.class);
        _platformRenderingComponent = getEngine().edit(_entityId).create(ColorPlatformRenderingComponent.class);
        _platformRenderingComponent.Init(_entityId, getEngine());
    }

    @Override
    public void init(int item) {
        super.init(item);
        _entityId = item;
        initComponents();
    }

    @Override
    public void act(float delta) {

    }

    @Override
    public void dispose() {
        _platformRenderingComponent.dispose();
    }

    @Override
    public void beginContact(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {
        _platformRenderingComponent.doColorSplash = true;
        _platformRenderingComponent.contactPosition = contactFixture.getBody().getPosition();

        // solve the bug of the player sticking to the walls instead of falling
        PhysicsBodyComponent physicsBody = ComponentRetriever.get(contactEntity, PhysicsBodyComponent.class, getEngine());
        if (physicsBody != null && Math.abs(physicsBody.body.getLinearVelocity().y) > Utils.epsilon) {
            contact.setFriction(Constants.Game.PLAYER_WALL_FRICTION);
        } else {
            contact.setFriction(Constants.Game.PLAYER_GROUND_FRICTION);
        }
    }

    @Override
    public void endContact(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {

    }

    @Override
    public void preSolve(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {

    }

    @Override
    public void postSolve(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {

    }
}
