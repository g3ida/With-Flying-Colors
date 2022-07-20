package com.g3ida.withflyingcolours.core.scripts

import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import com.artemis.World
import com.badlogic.gdx.physics.box2d.World as Box2dWorld
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.Fixture
import com.g3ida.withflyingcolours.core.common.Constants
import com.g3ida.withflyingcolours.core.extensions.EPSILON
import com.g3ida.withflyingcolours.core.extensions.addComponentToEntity
import games.rednblack.editor.renderer.utils.ComponentRetriever
import games.rednblack.editor.renderer.physics.PhysicsContact
import com.g3ida.withflyingcolours.core.ecs.components.ColorPlatformRenderingComponent
import kotlin.math.abs

class ColorPlatform(engine: World, world: Box2dWorld) : GameScript(engine, world), PhysicsContact {
    private var mEntityId = 0
    private lateinit var mPlatformRenderingComponent: ColorPlatformRenderingComponent
    fun initComponents() {
        // add ColorPlatformRenderingComponent.
        ComponentRetriever.addMapper(ColorPlatformRenderingComponent::class.java)
        mPlatformRenderingComponent = engine.addComponentToEntity<ColorPlatformRenderingComponent>(mEntityId)
        mPlatformRenderingComponent.initialize(mEntityId, engine)
    }

    override fun init(item: Int) {
        super.init(item)
        mEntityId = item
        initComponents()
    }

    override fun act(delta: Float) {}
    override fun dispose() {
        if (::mPlatformRenderingComponent.isInitialized) {
            mPlatformRenderingComponent.dispose()
        }
    }

    override fun beginContact(contactEntity: Int, contactFixture: Fixture, ownFixture: Fixture, contact: Contact) {
        mPlatformRenderingComponent.doColorSplash = true
        mPlatformRenderingComponent.contactPosition = contactFixture.body.position

        // solve the bug of the player sticking to the walls instead of falling
        val physicsBody = ComponentRetriever.get(contactEntity, PhysicsBodyComponent::class.java, engine)
        if (physicsBody != null && abs(physicsBody.body.linearVelocity.y) > Float.EPSILON) {
            contact.friction = Constants.Game.PLAYER_WALL_FRICTION
        } else {
            contact.friction = Constants.Game.PLAYER_GROUND_FRICTION
        }
    }

    override fun endContact(contactEntity: Int, contactFixture: Fixture, ownFixture: Fixture, contact: Contact) {}
    override fun preSolve(contactEntity: Int, contactFixture: Fixture, ownFixture: Fixture, contact: Contact) {}
    override fun postSolve(contactEntity: Int, contactFixture: Fixture, ownFixture: Fixture, contact: Contact) {}
}