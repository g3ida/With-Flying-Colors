package com.g3ida.withflyingcolours.core.scripts

import com.artemis.ComponentMapper
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import com.artemis.World
import com.badlogic.gdx.physics.box2d.World as Box2dWorld
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.Fixture
import com.g3ida.withflyingcolours.core.common.Constants
import com.g3ida.withflyingcolours.core.common.GameSettings
import com.g3ida.withflyingcolours.core.ecs.components.ColorComponent
import com.g3ida.withflyingcolours.utils.extensions.addComponentToEntity
import games.rednblack.editor.renderer.utils.ComponentRetriever
import games.rednblack.editor.renderer.physics.PhysicsContact
import com.g3ida.withflyingcolours.core.ecs.components.ColorPlatformRenderingComponent
import com.g3ida.withflyingcolours.core.ecs.components.FixtureColorComponent
import com.g3ida.withflyingcolours.core.events.EventType
import com.g3ida.withflyingcolours.core.events.GameEvent
import com.g3ida.withflyingcolours.utils.extensions.isAlmostZero

class ColorPlatform(engine: World, world: Box2dWorld) : GameScript(engine, world), PhysicsContact {
    private var mEntityId = 0
    private val mColor by lazy { mColorCM.get(mEntityId).color }
    private lateinit var mPlatformRenderingComponent: ColorPlatformRenderingComponent
    private lateinit var mFixtureColorCM: ComponentMapper<FixtureColorComponent>
    private lateinit var mColorCM: ComponentMapper<ColorComponent>

    fun initComponents() {
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
        // solve the bug of the player sticking to the walls instead of falling
        val physicsBody = ComponentRetriever.get(contactEntity, PhysicsBodyComponent::class.java, engine)
        contact.friction = if (!physicsBody.body.linearVelocity.y.isAlmostZero) {
            Constants.Game.PLAYER_WALL_FRICTION
        } else {
            Constants.Game.PLAYER_GROUND_FRICTION
        }

        val fixtureColorComponent = mFixtureColorCM.get(contactEntity)
        if (fixtureColorComponent != null) { // add check that this is the player (mainItemComponent)
            GameSettings.eventHandler.dispatchEvent(GameEvent(EventType.PlayerLanded))
            val playerColor = fixtureColorComponent.fixtureDirection.get(contactFixture)?.color
            if (playerColor != null && playerColor != mColor) {
                GameSettings.eventHandler.dispatchEvent(GameEvent(EventType.GameOver))
            }
        }
    }

    override fun endContact(contactEntity: Int, contactFixture: Fixture, ownFixture: Fixture, contact: Contact) {}
    override fun preSolve(contactEntity: Int, contactFixture: Fixture, ownFixture: Fixture, contact: Contact) {}
    override fun postSolve(contactEntity: Int, contactFixture: Fixture, ownFixture: Fixture, contact: Contact) {}
}