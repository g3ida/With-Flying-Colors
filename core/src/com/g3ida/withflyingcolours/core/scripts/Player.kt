package com.g3ida.withflyingcolours.core.scripts

import com.artemis.ComponentMapper
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import com.artemis.World
import com.badlogic.gdx.physics.box2d.World as Box2dWorld
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.g3ida.withflyingcolours.core.actions.*
import games.rednblack.editor.renderer.utils.ComponentRetriever
import com.g3ida.withflyingcolours.core.ecs.systems.CameraSystem
import com.g3ida.withflyingcolours.core.events.EventType
import com.g3ida.withflyingcolours.core.input.keyboard.KeyboardAction
import com.g3ida.withflyingcolours.core.input.keyboard.KeyboardHandler
import com.g3ida.withflyingcolours.core.input.keyboard.KeyboardKey
import com.g3ida.withflyingcolours.core.ecs.components.*
import com.g3ida.withflyingcolours.core.input.commands.*
import com.g3ida.withflyingcolours.utils.extensions.addDragForce
import games.rednblack.editor.renderer.components.DimensionsComponent
import ktx.collections.gdxMapOf

class Player(engine: World, world: Box2dWorld) : GameScript(engine, world) {
    val dragForce = 60f
    private var mEntityId = 0
    private lateinit var mPlayerAnimationCM: ComponentMapper<PlayerAnimationComponent>
    private lateinit var mEventListenerCM: ComponentMapper<EventListenerComponent>
    private lateinit var mPlayerAnim: PlayerAnimationComponent
    private lateinit var mPhysicsBodyComponent: PhysicsBodyComponent

    fun initComponents() {
        //FIXME : find a better place for this
        ComponentRetriever.addMapper(PlayerAnimationComponent::class.java)
        ComponentRetriever.addMapper(EventListenerComponent::class.java)
        mPlayerAnim = mPlayerAnimationCM.create(mEntityId)

        // attach player to camera
        val cameraSystem = engine.getSystem(CameraSystem::class.java)
        cameraSystem.setFocus(mEntityId)

        mPhysicsBodyComponent = ComponentRetriever.get(mEntityId, PhysicsBodyComponent::class.java, engine)
        val inputProcessor = Gdx.input.inputProcessor as KeyboardHandler

        inputProcessor.mapCommand(KeyboardKey.UP,KeyboardAction.KeyPressed, EventType.JumpCommand.toGameEventCommand())
        inputProcessor.mapCommand(KeyboardKey.UP,KeyboardAction.KeyReleased, EventType.CancelJumpCommand.toGameEventCommand())
        inputProcessor.mapCommand(KeyboardKey.C, KeyboardAction.KeyPressed, EventType.RotateCommand.toGameEventCommand(gdxMapOf("direction" to "1")))
        inputProcessor.mapCommand(KeyboardKey.Z, KeyboardAction.KeyPressed, EventType.RotateCommand.toGameEventCommand(gdxMapOf("direction" to "-1")))
        inputProcessor.mapCommand(KeyboardKey.LEFT, KeyboardAction.KeyDown, EventType.MoveCommand.toGameEventCommand(gdxMapOf("direction" to "-1")))
        inputProcessor.mapCommand(KeyboardKey.RIGHT, KeyboardAction.KeyDown, EventType.MoveCommand.toGameEventCommand(gdxMapOf("direction" to "1")))

        val eventListenerComponent = mEventListenerCM.create(mEntityId)
        val playerJumpAction = PlayerJumpAction(mPhysicsBodyComponent)
        eventListenerComponent.run {
            addActionListener(playerJumpAction.toActionListener(EventType.JumpCommand))
            addActionListener(playerJumpAction.toActionListener(EventType.CancelJumpCommand))
            addActionListener(PlayerRotationAction(mPhysicsBodyComponent).toActionListener(EventType.RotateCommand))
            addActionListener(PlayerWalkAction(mPhysicsBodyComponent).toActionListener(EventType.MoveCommand))
        }
    }

    override fun init(item: Int) {
        super.init(item)
        mEntityId = getEntity()
        initComponents()
    }

    override fun dispose() {}

    override fun act(delta: Float) {
        mPhysicsBodyComponent.body.addDragForce(dragForce) // adds some air resistance
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
                if (!mPlayerAnim.currentAnimation.isRunning()) {
                    mPlayerAnim.currentAnimation = PlayerSqueezeAnimation()
                }
            //}
            0f
        }, rayFrom, rayTo)
    }
}