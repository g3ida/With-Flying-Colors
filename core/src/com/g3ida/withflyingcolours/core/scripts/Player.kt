package com.g3ida.withflyingcolours.core.scripts

import com.artemis.ComponentMapper
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import com.artemis.World
import com.badlogic.gdx.physics.box2d.World as Box2dWorld
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.g3ida.withflyingcolours.utils.RotationDirection
import com.g3ida.withflyingcolours.core.player.animation.PlayerAnimationComponent
import com.g3ida.withflyingcolours.core.player.controller.PlayerControllerComponent
import games.rednblack.editor.renderer.utils.ComponentRetriever
import com.g3ida.withflyingcolours.core.camera.CameraSystem
import com.g3ida.withflyingcolours.core.events.EventType
import com.g3ida.withflyingcolours.core.extensions.EPSILON
import com.g3ida.withflyingcolours.core.input.KeyboardHandler
import com.g3ida.withflyingcolours.core.input.KeyboardKey
import com.g3ida.withflyingcolours.core.input.commands.PlayerJumpCommand
import com.g3ida.withflyingcolours.core.input.commands.mapCancellableCommand
import com.g3ida.withflyingcolours.core.player.animation.PlayerSqueezeAnimation
import com.g3ida.withflyingcolours.core.player.movement.*
import games.rednblack.editor.renderer.components.DimensionsComponent
import ktx.collections.GdxArray
import kotlin.math.abs

class Player(engine: World, world: Box2dWorld) : GameScript(engine, world) {
    private var mEntityId = 0
    private var mPlayerController: PlayerControllerComponent? = null
    private var mPlayerRotation: PlayerRotationComponent? = null
    private var mPlayerWalk: PlayerWalkComponent? = null
    private var mPlayerAnim: PlayerAnimationComponent? = null
    var mPlayerControllerComponent: ComponentMapper<PlayerControllerComponent>? = null
    var mPlayerRotationComponent: ComponentMapper<PlayerRotationComponent>? = null
    var mPlayerWalkComponent: ComponentMapper<PlayerWalkComponent>? = null
    var mPlayerAnimationComponent: ComponentMapper<PlayerAnimationComponent>? = null
    lateinit var mPhysicsBodyComponent: PhysicsBodyComponent
    private val mEventActionListenerList = GdxArray<EventActionListener>()

    fun initComponents() {
        //FIXME : find a better place for this
        ComponentRetriever.addMapper(PlayerControllerComponent::class.java)
        ComponentRetriever.addMapper(PlayerRotationComponent::class.java)
        ComponentRetriever.addMapper(PlayerWalkComponent::class.java)
        ComponentRetriever.addMapper(PlayerAnimationComponent::class.java)
        mPlayerController = mPlayerControllerComponent!!.create(mEntityId)
        mPlayerRotation = mPlayerRotationComponent!!.create(mEntityId)
        mPlayerWalk = mPlayerWalkComponent!!.create(mEntityId)
        mPlayerAnim = mPlayerAnimationComponent!!.create(mEntityId)

        // attach player to camera
        val cameraSystem = engine.getSystem(CameraSystem::class.java)
        cameraSystem.setFocus(mEntityId)

        mPhysicsBodyComponent = ComponentRetriever.get(mEntityId, PhysicsBodyComponent::class.java, engine)
        val inputProcessor = Gdx.input.inputProcessor
        (inputProcessor as KeyboardHandler).mapCancellableCommand(KeyboardKey.UP, PlayerJumpCommand(this::isGrounded))

        mEventActionListenerList.run {
            add(PlayerJumpAction(mPhysicsBodyComponent).toActionListener(EventType.JumpCommand))
            add(PlayerCancelJumpAction(mPhysicsBodyComponent).toActionListener(EventType.CancelJump))
        }
    }

    override fun init(item: Int) {
        super.init(item)
        mEntityId = getEntity()
        initComponents()
    }

    override fun dispose() {
        mEventActionListenerList.forEach { it.dispose() }
    }

    override fun act(delta: Float) {

        //TODO: handle left and write controls
        mPlayerWalk!!.direction = mPlayerController!!.moveInput
        if (mPlayerController!!.shouldRotateRight) {
            mPlayerRotation!!.setRotationDirection(RotationDirection.Clockwise)
        }
        if (mPlayerController!!.shouldRotateLeft) {
            mPlayerRotation!!.setRotationDirection(RotationDirection.AntiClockwise)
        }
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