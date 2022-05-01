package com.g3ida.withflyingcolours.core.scripts;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.g3ida.withflyingcolours.core.platform.ColorPlatformRenderingComponent;
import com.g3ida.withflyingcolours.core.platform.ColorPlatformRenderingSystem;

import games.rednblack.editor.renderer.physics.PhysicsContact;
import games.rednblack.editor.renderer.utils.ComponentRetriever;

public class ColorPlatform extends GameScript implements PhysicsContact {

    private Entity _entity;
    private ColorPlatformRenderingComponent _platformRenderingComponent;

    public ColorPlatform(PooledEngine engine, World world) {
        super(engine, world);
        //FIXME: find a better place for initializing systems
        getEngine().addSystem(new ColorPlatformRenderingSystem());
    }

    public void initComponents() {
        // add ColorPlatformRenderingComponent.
        ComponentRetriever.addMapper(ColorPlatformRenderingComponent.class);
        _entity.add(new ColorPlatformRenderingComponent(_entity));
        _platformRenderingComponent = ComponentRetriever.get(_entity, ColorPlatformRenderingComponent.class);
    }

    @Override
    public void init(Entity item) {
        super.init(item);
        _entity = getEntity();
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
    public void beginContact(Entity contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {
        _platformRenderingComponent.doColorSplash = true;
        _platformRenderingComponent.contactPosition = contactFixture.getBody().getPosition();
    }

    @Override
    public void endContact(Entity contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {

    }

    @Override
    public void preSolve(Entity contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {

    }

    @Override
    public void postSolve(Entity contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {

    }
}
