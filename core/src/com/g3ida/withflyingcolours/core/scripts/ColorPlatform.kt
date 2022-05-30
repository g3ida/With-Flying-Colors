package com.g3ida.withflyingcolours.core.scripts

import games.rednblack.editor.renderer.components.ViewPortComponent
import com.artemis.systems.IteratingSystem
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import com.g3ida.withflyingcolours.core.player.movement.PlayerJumpComponent
import com.g3ida.withflyingcolours.core.player.movement.PlayerWalkComponent
import com.artemis.PooledComponent
import com.artemis.World
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.Fixture
import com.g3ida.withflyingcolours.Constants
import com.g3ida.withflyingcolours.Utils
import com.g3ida.withflyingcolours.core.player.PlayerControllerSettings
import com.g3ida.withflyingcolours.core.player.movement.PlayerRotationComponent
import com.g3ida.withflyingcolours.utils.RotationDirection
import com.g3ida.withflyingcolours.core.player.animation.PlayerAnimationComponent
import com.g3ida.withflyingcolours.core.player.animation.TransformAnimation
import com.g3ida.withflyingcolours.core.player.controller.PlayerControllerComponent
import com.g3ida.withflyingcolours.core.scripts.GameScript
import games.rednblack.editor.renderer.utils.ComponentRetriever
import com.g3ida.withflyingcolours.core.camera.CameraSystem
import games.rednblack.editor.renderer.components.DimensionsComponent
import games.rednblack.editor.renderer.scripts.BasicScript
import games.rednblack.editor.renderer.physics.PhysicsContact
import com.g3ida.withflyingcolours.core.platform.ColorPlatformRenderingComponent
import games.rednblack.editor.renderer.components.ShaderComponent
import games.rednblack.editor.renderer.data.ShaderUniformVO
import games.rednblack.editor.renderer.systems.render.HyperLap2dRenderer
import com.g3ida.withflyingcolours.core.GameSettings
import games.rednblack.editor.renderer.data.MainItemVO
import games.rednblack.editor.renderer.utils.ItemWrapper
import games.rednblack.editor.renderer.components.NodeComponent
import games.rednblack.editor.renderer.components.MainItemComponent
import games.rednblack.editor.renderer.SceneLoader
import games.rednblack.editor.renderer.resources.AsyncResourceManager
import games.rednblack.editor.renderer.resources.ResourceManagerLoader.AsyncResourceManagerParam
import games.rednblack.editor.renderer.resources.ResourceManagerLoader

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
        if (physicsBody != null && Math.abs(physicsBody.body.linearVelocity.y) > Utils.epsilon) {
            contact.friction = Constants.Game.PLAYER_WALL_FRICTION
        } else {
            contact.friction = Constants.Game.PLAYER_GROUND_FRICTION
        }
    }

    override fun endContact(contactEntity: Int, contactFixture: Fixture, ownFixture: Fixture, contact: Contact) {}
    override fun preSolve(contactEntity: Int, contactFixture: Fixture, ownFixture: Fixture, contact: Contact) {}
    override fun postSolve(contactEntity: Int, contactFixture: Fixture, ownFixture: Fixture, contact: Contact) {}
}