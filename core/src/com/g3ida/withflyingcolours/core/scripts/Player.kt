package com.g3ida.withflyingcolours.core.scripts

import com.artemis.ComponentMapper
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import com.artemis.World
import com.badlogic.gdx.physics.box2d.World as Box2dWorld
import com.badlogic.gdx.graphics.Color
import com.g3ida.withflyingcolours.core.actions.*
import com.g3ida.withflyingcolours.core.ecs.systems.CameraSystem
import com.g3ida.withflyingcolours.core.events.EventType
import com.g3ida.withflyingcolours.core.ecs.components.*
import com.g3ida.withflyingcolours.utils.ColorFixtureInfo
import com.g3ida.withflyingcolours.utils.enums.Direction
import com.g3ida.withflyingcolours.utils.extensions.addComponentToEntity
import com.g3ida.withflyingcolours.utils.extensions.addDragForce

class Player(engine: World, world: Box2dWorld) : GameScript(engine, world) {
    val dragForce = 60f
    private var mEntityId = 0
    private lateinit var mPlayerAnim: PlayerAnimationComponent
    private lateinit var mPhysicsBodyComponent: PhysicsBodyComponent

    private lateinit var mPlayerAnimationCM: ComponentMapper<PlayerAnimationComponent>
    private lateinit var mEventListenerCM: ComponentMapper<EventListenerComponent>
    private lateinit var mPhysicsBodyCM: ComponentMapper<PhysicsBodyComponent>

    private lateinit var playerDieAction: PlayerDieAction
    fun initComponents() {
        playerDieAction = PlayerDieAction(mEntityId, engine)
        mPlayerAnim = mPlayerAnimationCM.create(mEntityId)
        // attach player to camera
        val cameraSystem = engine.getSystem(CameraSystem::class.java)
        cameraSystem.setFocus(mEntityId)
        mPhysicsBodyComponent = mPhysicsBodyCM.get(mEntityId)
        registerActions()
        initFixtureColor()
    }

    private fun registerActions() {
        val eventListenerComponent = mEventListenerCM.create(mEntityId)
        val playerJumpAction = PlayerJumpAction(mPhysicsBodyComponent)
        eventListenerComponent.run {
            addActionListener(playerJumpAction.toActionListener(EventType.JumpCommand))
            addActionListener(playerJumpAction.toActionListener(EventType.CancelJumpCommand))
            addActionListener(PlayerRotationAction(mPhysicsBodyComponent).toActionListener(EventType.RotateCommand))
            addActionListener(PlayerWalkAction(mPhysicsBodyComponent).toActionListener(EventType.MoveCommand))
            addActionListener(PlayerLandAction(mPlayerAnim).toActionListener(EventType.PlayerLanded))
            addActionListener(playerDieAction.toActionListener(EventType.GameOver))
        }
    }

    private fun defaultFaceColorOf(direction: Direction): Color {
        return when(direction) {
            Direction.Left -> Color.GREEN
            Direction.Right -> Color.RED
            Direction.Top -> Color.PURPLE
            Direction.Bottom -> Color.BLUE
        }
    }

    private fun initFixtureColor() {
        val fixtureColorComponent = engine.addComponentToEntity<FixtureColorComponent>(mEntityId)
        val fixtureColors = mPhysicsBodyComponent.body.fixtureList
            .drop(1) // ignore the base fixture
            .zip(listOf(Direction.Bottom, Direction.Top, Direction.Left, Direction.Right))
            .map { ColorFixtureInfo(it.first, it.second, color = defaultFaceColorOf(it.second)) }
        fixtureColorComponent.set(fixtureColors)
    }

    override fun init(item: Int) {
        super.init(item)
        mEntityId = getEntity()
        initComponents()
    }

    override fun dispose() {
        playerDieAction.dispose()
    }

    override fun act(delta: Float) {
        mPhysicsBodyComponent.body.addDragForce(dragForce) // adds some air resistance
    }
}