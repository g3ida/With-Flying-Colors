package com.g3ida.withflyingcolours.core.scripts

import com.artemis.ComponentMapper
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import com.g3ida.withflyingcolours.core.player.movement.PlayerJumpComponent
import com.g3ida.withflyingcolours.core.player.movement.PlayerWalkComponent
import com.artemis.World
import com.badlogic.gdx.math.Vector2
import com.g3ida.withflyingcolours.core.player.movement.PlayerRotationComponent
import com.g3ida.withflyingcolours.utils.RotationDirection
import com.g3ida.withflyingcolours.core.player.animation.PlayerAnimationComponent
import com.g3ida.withflyingcolours.core.player.controller.PlayerControllerComponent
import games.rednblack.editor.renderer.utils.ComponentRetriever
import com.g3ida.withflyingcolours.core.camera.CameraSystem
import games.rednblack.editor.renderer.components.DimensionsComponent

class Player(engine: World?, world: com.badlogic.gdx.physics.box2d.World?) : GameScript(engine, world) {
    private var _entityId = 0
    private var _playerController: PlayerControllerComponent? = null
    private var _playerRotation: PlayerRotationComponent? = null
    private var _playerJump: PlayerJumpComponent? = null
    private var _playerWalk: PlayerWalkComponent? = null
    private var _playerAnim: PlayerAnimationComponent? = null
    var mPlayerControllerComponent: ComponentMapper<PlayerControllerComponent>? = null
    var mPlayerRotationComponent: ComponentMapper<PlayerRotationComponent>? = null
    var mPlayerJumpComponent: ComponentMapper<PlayerJumpComponent>? = null
    var mPlayerWalkComponent: ComponentMapper<PlayerWalkComponent>? = null
    var mPlayerAnimationComponent: ComponentMapper<PlayerAnimationComponent>? = null
    fun initComponents() {
        //FIXME : find a better place for this
        ComponentRetriever.addMapper(PlayerControllerComponent::class.java)
        ComponentRetriever.addMapper(PlayerRotationComponent::class.java)
        ComponentRetriever.addMapper(PlayerJumpComponent::class.java)
        ComponentRetriever.addMapper(PlayerWalkComponent::class.java)
        ComponentRetriever.addMapper(PlayerAnimationComponent::class.java)
        _playerController = mPlayerControllerComponent!!.create(_entityId)
        _playerRotation = mPlayerRotationComponent!!.create(_entityId)
        _playerJump = mPlayerJumpComponent!!.create(_entityId)
        _playerWalk = mPlayerWalkComponent!!.create(_entityId)
        _playerAnim = mPlayerAnimationComponent!!.create(_entityId)

        // attach player to camera
        val cameraSystem = engine!!.getSystem(CameraSystem::class.java)
        cameraSystem.setFocus(_entityId)
    }

    override fun init(item: Int) {
        super.init(item)
        _entityId = getEntity()
        initComponents()
    }

    override fun dispose() {}
    override fun act(delta: Float) {

        //TODO: handle left and write controls
        _playerWalk!!.direction = _playerController!!.moveInput
        _playerJump!!.shouldJump = _playerController!!.shouldJump
        if (_playerController!!.shouldRotateRight) {
            _playerRotation!!.setRotationDirection(RotationDirection.Clockwise)
        }
        if (_playerController!!.shouldRotateLeft) {
            _playerRotation!!.setRotationDirection(RotationDirection.AntiClockwise)
        }
        rayCast(delta)
    }

    fun rayCast(delta: Float) {
        val dimensionsComponent = ComponentRetriever.get(_entityId, DimensionsComponent::class.java, engine)
        val physicsBodyComponent = ComponentRetriever.get(_entityId, PhysicsBodyComponent::class.java, engine)
        val transformComponent = ComponentRetriever.get(_entityId, TransformComponent::class.java, engine)
        val rayGap = transformComponent.scaleY * dimensionsComponent.height / 2f
        val raySize = -physicsBodyComponent.body.linearVelocity.y * delta * 2f
        val rayFrom = Vector2(transformComponent.x + dimensionsComponent.width / 2f, transformComponent.y + rayGap)
        val rayTo = Vector2(transformComponent.x + dimensionsComponent.width / 2f, transformComponent.y + 0.05f - raySize)

        //cast the ray
        val world = world
        world!!.rayCast({ _, _, _, _ -> //Entity entity = (Entity) fixture.getBody().getUserData();
            //if (entity != null) {
            if (_playerAnim != null) {
                if (!_playerAnim!!.squeezeAnimation.isRunning && !_playerAnim!!.scaleAnimation.isRunning) {
                    _playerAnim!!.doSqueeze = true
                }
            }
            //}
            0f
        }, rayFrom, rayTo)
    }
}