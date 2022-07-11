package com.g3ida.withflyingcolours.core.scripts

import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import com.artemis.World
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.Fixture
import com.g3ida.withflyingcolours.Constants
import com.g3ida.withflyingcolours.Utils
import games.rednblack.editor.renderer.utils.ComponentRetriever
import games.rednblack.editor.renderer.physics.PhysicsContact
import com.g3ida.withflyingcolours.core.platform.ColorPlatformRenderingComponent

class ColorPlatform(engine: World?, world: com.badlogic.gdx.physics.box2d.World?) : GameScript(engine, world), PhysicsContact {
    private var _entityId = 0
    private var _platformRenderingComponent: ColorPlatformRenderingComponent? = null
    fun initComponents() {
        // add ColorPlatformRenderingComponent.
        ComponentRetriever.addMapper(ColorPlatformRenderingComponent::class.java)
        _platformRenderingComponent = engine!!.edit(_entityId).create(ColorPlatformRenderingComponent::class.java)
        _platformRenderingComponent!!.Init(_entityId, engine)
    }

    override fun init(item: Int) {
        super.init(item)
        _entityId = item
        initComponents()
    }

    override fun act(delta: Float) {}
    override fun dispose() {
        _platformRenderingComponent!!.dispose()
    }

    override fun beginContact(contactEntity: Int, contactFixture: Fixture, ownFixture: Fixture, contact: Contact) {
        _platformRenderingComponent!!.doColorSplash = true
        _platformRenderingComponent!!.contactPosition = contactFixture.body.position

        // solve the bug of the player sticking to the walls instead of falling
        val physicsBody = ComponentRetriever.get(contactEntity, PhysicsBodyComponent::class.java, engine)
        if (physicsBody != null && Math.abs(physicsBody.body.linearVelocity.y) > Utils.EPSILON) {
            contact.friction = Constants.Game.PLAYER_WALL_FRICTION
        } else {
            contact.friction = Constants.Game.PLAYER_GROUND_FRICTION
        }
    }

    override fun endContact(contactEntity: Int, contactFixture: Fixture, ownFixture: Fixture, contact: Contact) {}
    override fun preSolve(contactEntity: Int, contactFixture: Fixture, ownFixture: Fixture, contact: Contact) {}
    override fun postSolve(contactEntity: Int, contactFixture: Fixture, ownFixture: Fixture, contact: Contact) {}
}