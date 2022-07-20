package com.g3ida.withflyingcolours.core.scripts

import com.artemis.ComponentMapper
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import com.artemis.World
import com.badlogic.gdx.physics.box2d.World as Box2dWorld
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.g3ida.withflyingcolours.core.actions.PlayerCancelJumpAction
import com.g3ida.withflyingcolours.core.actions.PlayerJumpAction
import com.g3ida.withflyingcolours.core.actions.PlayerRotationAction
import com.g3ida.withflyingcolours.utils.RotationDirection
import games.rednblack.editor.renderer.utils.ComponentRetriever
import com.g3ida.withflyingcolours.core.ecs.systems.CameraSystem
import com.g3ida.withflyingcolours.core.events.EventType
import com.g3ida.withflyingcolours.core.extensions.EPSILON
import com.g3ida.withflyingcolours.core.input.keyboard.KeyboardAction
import com.g3ida.withflyingcolours.core.input.keyboard.KeyboardHandler
import com.g3ida.withflyingcolours.core.input.keyboard.KeyboardKey
import com.g3ida.withflyingcolours.core.input.commands.PlayerJumpCommand
import com.g3ida.withflyingcolours.core.input.commands.PlayerRotationCommand
import com.g3ida.withflyingcolours.core.input.commands.mapCancellableCommand
import com.g3ida.withflyingcolours.core.actions.toActionListener
import com.g3ida.withflyingcolours.core.ecs.components.*
import games.rednblack.editor.renderer.components.DimensionsComponent
import kotlin.math.abs

class Player(engine: World, world: Box2dWorld) : GameScript(engine, world) {
    private var mEntityId = 0
    private var mPlayerController: PlayerControllerComponent? = null
    private var mPlayerWalk: PlayerWalkComponent? = null
    private var mPlayerAnim: PlayerAnimationComponent? = null
    lateinit var mPhysicsBodyComponent: PhysicsBodyComponent

    lateinit var mPlayerControllerCM: ComponentMapper<PlayerControllerComponent>
    lateinit var mPlayerWalkCM: ComponentMapper<PlayerWalkComponent>
    lateinit var mPlayerAnimationCM: ComponentMapper<PlayerAnimationComponent>
    lateinit var mEventListenerCM: ComponentMapper<EventListenerComponent>

    fun initComponents() {
        //FIXME : find a better place for this
        ComponentRetriever.addMapper(PlayerControllerComponent::class.java)
        ComponentRetriever.addMapper(PlayerWalkComponent::class.java)
        ComponentRetriever.addMapper(PlayerAnimationComponent::class.java)
        ComponentRetriever.addMapper(EventListenerComponent::class.java)
        mPlayerController = mPlayerControllerCM.create(mEntityId)
        mPlayerWalk = mPlayerWalkCM.create(mEntityId)
        mPlayerAnim = mPlayerAnimationCM.create(mEntityId)

        // attach player to camera
        val cameraSystem = engine.getSystem(CameraSystem::class.java)
        cameraSystem.setFocus(mEntityId)

        mPhysicsBodyComponent = ComponentRetriever.get(mEntityId, PhysicsBodyComponent::class.java, engine)
        val inputProcessor = Gdx.input.inputProcessor as KeyboardHandler

        inputProcessor.mapCancellableCommand(KeyboardKey.UP, PlayerJumpCommand(this::isGrounded))
        inputProcessor.mapCommand(KeyboardKey.C, KeyboardAction.KeyDown, PlayerRotationCommand(RotationDirection.Clockwise))
        inputProcessor.mapCommand(KeyboardKey.Z, KeyboardAction.KeyDown, PlayerRotationCommand(RotationDirection.AntiClockwise))

        val eventListenerComponent = mEventListenerCM.create(mEntityId)
        eventListenerComponent.addActionListener(
            PlayerJumpAction(mPhysicsBodyComponent)
            .toActionListener(EventType.JumpCommand))
        eventListenerComponent.addActionListener(
            PlayerCancelJumpAction(mPhysicsBodyComponent)
            .toActionListener(EventType.CancelJump))
        eventListenerComponent.addActionListener(
            PlayerRotationAction(RotationDirection.AntiClockwise, mPhysicsBodyComponent)
            .toActionListener(EventType.RotateLeftCommand))
        eventListenerComponent.addActionListener(
            PlayerRotationAction(RotationDirection.Clockwise, mPhysicsBodyComponent)
            .toActionListener(EventType.RotateRightCommand))
    }

    override fun init(item: Int) {
        super.init(item)
        mEntityId = getEntity()
        initComponents()
    }

    override fun dispose() {}

    override fun act(delta: Float) {
        mPlayerWalk!!.direction = mPlayerController!!.moveInput
        rayCast(delta)
    }

    fun rayCast(delta: Float) {
        val dimensionsComponent = ComponentRetriever.get(mEntityId, DimensionsComponent::class.java, engine)
        val physicsBodyComponent = ComponentRetriever.get(mEntityId, PhysicsBodyComponent::class.java, engine)
        val transformComponent = ComponentRetriever.get(mEntityId, TransformComponent::class.java, engine)
        val rayGap = transformComponent.scaleY * dimensionsComponent.height / 2f
        val raySize = -physicsBodyComponent.body.linearVelocity.y * delta * 2f
        val rayFrom = Vector2(transformComponent.x + dimensionsComponent.width / 2f, transformComponent.y + rayGap)
        val rayTo = Vector2(transformComponent.x + dimensionsComponent.width / 2f, transformComponent.y + 0.05f - raySize)

        //cast the ray
        val world = world
        world.rayCast({ _, _, _, _ -> //Entity entity = (Entity) fixture.getBody().getUserData();
            //if (entity != null) {
                if (mPlayerAnim?.currentAnimation?.isRunning() == false) {
                    mPlayerAnim!!.currentAnimation = PlayerSqueezeAnimation()
                }
            //}
            0f
        }, rayFrom, rayTo)
    }

    private fun isGrounded(): Boolean = abs(mPhysicsBodyComponent.body.linearVelocity.y) < Float.EPSILON
}