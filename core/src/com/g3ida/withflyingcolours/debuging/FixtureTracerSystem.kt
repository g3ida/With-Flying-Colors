package com.g3ida.withflyingcolours.debuging

import com.artemis.ComponentMapper
import com.artemis.World
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack
import com.badlogic.gdx.utils.Pool
import games.rednblack.editor.renderer.components.*
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import games.rednblack.editor.renderer.systems.render.FrameBufferManager
import games.rednblack.editor.renderer.systems.render.logic.DrawableLogic
import games.rednblack.editor.renderer.systems.strategy.RendererSystem
import java.util.*


@All(ViewPortComponent::class)
class FixtureTracerSystem(val batch: Batch, private val hasStencilBuffer: Boolean) :
    IteratingSystem(), RendererSystem {
    lateinit var viewPortMapper: ComponentMapper<ViewPortComponent>
    lateinit var compositeTransformMapper: ComponentMapper<CompositeTransformComponent>
    lateinit var nodeMapper: ComponentMapper<NodeComponent>
    lateinit var parentNodeMapper: ComponentMapper<ParentNodeComponent>
    lateinit var transformMapper: ComponentMapper<TransformComponent>
    lateinit var mainItemComponentMapper: ComponentMapper<MainItemComponent>
    lateinit var dimensionsMapper: ComponentMapper<DimensionsComponent>
    lateinit var layerMapComponentMapper: ComponentMapper<LayerMapComponent>
    lateinit var zIndexComponentMapper: ComponentMapper<ZIndexComponent>
    lateinit var physicsBodyComponentMapper: ComponentMapper<PhysicsBodyComponent>

    private var fixtureDrawLogic = FixtureDrawLogic()

    private val frameBufferManager: FrameBufferManager = FrameBufferManager()
    private val screenCamera: Camera = OrthographicCamera(
        Gdx.graphics.width.toFloat(),
        Gdx.graphics.height.toFloat()
    )
    private val tmpFboCamera: Camera = OrthographicCamera()

    private var invScreenWidth = 0f
    private  var invScreenHeight: Float = 0f

    private var pixelsPerWU = 0
    private val fboM4Stack = Stack<Matrix4>()
    private val fboM4Pool: Pool<Matrix4> = object : Pool<Matrix4>() {
        override fun newObject(): Matrix4 = Matrix4()
    }

    init {
        createCoreFrameBuffers()
        screenCamera.translate(
            screenCamera.viewportWidth * 0.5f,
            screenCamera.viewportHeight * 0.5f,
            0f
        )
        screenCamera.update()
        invScreenWidth = 1f / screenCamera.viewportWidth
        invScreenHeight = 1f / screenCamera.viewportHeight
    }

    fun inject(engine: World) {
        engine.inject(fixtureDrawLogic)
    }

    override fun process(entity: Int) {
        batch.color = Color.WHITE
        val viewPortComponent = viewPortMapper[entity]
        pixelsPerWU = viewPortComponent.pixelsPerWU
        val viewport = viewPortComponent.viewPort
        val camera = viewport.camera
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a)
        frameBufferManager.begin("main")
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
        batch.projectionMatrix = camera.combined
        batch.begin()
        drawRecursively(entity, 1f, camera)
        batch.end()
        frameBufferManager.endCurrent()

        val screenTexture = frameBufferManager.getColorBufferTexture("main")

        batch.projectionMatrix = screenCamera.combined
        batch.begin()
        batch.draw(
            screenTexture,
            viewport.screenX.toFloat(), viewport.screenY.toFloat(), 0f, 0f,
            viewport.screenWidth.toFloat(), viewport.screenHeight.toFloat(), 1f, 1f, 0f,
            0, 0,
            screenTexture.width, screenTexture.height,
            false, true
        )
        batch.end()
    }

    private fun drawRecursively(rootEntity: Int, parentAlpha: Float, camera: Camera) {
        val curCompositeTransformComponent = compositeTransformMapper[rootEntity]
        val transform = transformMapper[rootEntity]
        val dimensions = dimensionsMapper[rootEntity]
        val mainItemComponent = mainItemComponentMapper[rootEntity]
        val fboTag = mainItemComponent.itemIdentifier
        var scissors = false
        if (curCompositeTransformComponent.renderToFBO) {
            //Active composite frame buffer
            batch.end()
            frameBufferManager.createIfNotExists(
                fboTag,
                dimensions.width.toInt() * pixelsPerWU,
                dimensions.height.toInt() * pixelsPerWU,
                false,
                hasStencilBuffer
            )
            tmpFboCamera.viewportWidth = dimensions.width
            tmpFboCamera.viewportHeight = dimensions.height
            tmpFboCamera.position[tmpFboCamera.viewportWidth * 0.5f, tmpFboCamera.viewportHeight * 0.5f] =
                0f
            tmpFboCamera.update()
            batch.projectionMatrix = tmpFboCamera.combined
            fboM4Stack.push(fboM4Pool.obtain().set(tmpFboCamera.combined))
            frameBufferManager.begin(fboTag)
            batch.begin()
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        } else {
            if (transform.shouldTransform()) {
                computeTransform(rootEntity)
                applyTransform(rootEntity, batch)
            }
            if (curCompositeTransformComponent.scissorsEnabled) {
                batch.flush()
                ScissorStack.calculateScissors(
                    camera,
                    transform.oldTransform,
                    curCompositeTransformComponent.clipBounds,
                    curCompositeTransformComponent.scissors
                )
                if (ScissorStack.pushScissors(curCompositeTransformComponent.scissors)) {
                    scissors = true
                }
            }
        }

        drawChildren(rootEntity, batch, curCompositeTransformComponent, parentAlpha, camera)
        if (curCompositeTransformComponent.renderToFBO) {
            //Close FBO and render the result
            batch.end()
            frameBufferManager.endCurrent()
            val fboM4 = fboM4Stack.pop()
            fboM4Pool.free(fboM4)
            val renderingMatrix = if (fboM4Stack.size == 0) camera.combined else fboM4Stack.peek()
            batch.projectionMatrix = renderingMatrix
            batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA)
            batch.begin()
            val bufferTexture = frameBufferManager.getColorBufferTexture(fboTag)
            val scaleX = transform.scaleX * if (transform.flipX) -1 else 1
            val scaleY = transform.scaleY * if (transform.flipY) -1 else 1
            batch.draw(
                bufferTexture,
                transform.x, transform.y,
                transform.originX, transform.originY,
                dimensions.width, dimensions.height,
                scaleX, scaleY,
                transform.rotation,
                0, 0,
                bufferTexture.width, bufferTexture.height,
                false, true
            )
            batch.setBlendFunctionSeparate(
                GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA,
                GL20.GL_ONE_MINUS_DST_ALPHA, GL20.GL_ONE
            )
        } else {
            if (scissors) {
                batch.flush()
                ScissorStack.popScissors()
            }
            if (transform.shouldTransform()) resetTransform(rootEntity, batch)
        }
    }

    private fun drawChildren(
        rootEntity: Int,
        batch: Batch,
        curCompositeTransformComponent: CompositeTransformComponent,
        parentAlpha: Float,
        camera: Camera
    ) {
        val nodeComponent = nodeMapper[rootEntity]
        val children = nodeComponent.children.begin()
        val transform = transformMapper[rootEntity]
        val rootLayers = layerMapComponentMapper[rootEntity]
        var offsetX = transform.x
        var offsetY = transform.y
        if (viewPortMapper.has(rootEntity) || curCompositeTransformComponent.renderToFBO) {
            //Don't offset children in root composite or in FBOs
            offsetX = 0f
            offsetY = 0f
        }
        var i = 0
        val n = nodeComponent.children.size
        while (i < n) {
            val child = children[i]
            val childZIndexComponent = zIndexComponentMapper[child]
            if (!rootLayers.isVisible(childZIndexComponent.layerName)) {
                //Skip if layer is not visible
                i++
                continue
            }
            val childMainItemComponent = mainItemComponentMapper[child]
            if (!childMainItemComponent.visible || childMainItemComponent.culled) {
                //Skip if entity is culled or not visible
                i++
                continue
            }
            val childTransformComponent = transformMapper[child]
            val cx = childTransformComponent.x
            val cy = childTransformComponent.y
            val childNodeComponent = nodeMapper[child]
            if (!transform.shouldTransform() || curCompositeTransformComponent.renderToFBO) {
                // The group doesn't need matrix transformation. Just offset child in screen coordinates.
                childTransformComponent.x = cx + offsetX
                childTransformComponent.y = cy + offsetY
            }
            if (childNodeComponent == null) {
                drawEntity(batch, child, parentAlpha)
            } else {
                //Step into Composite
                drawRecursively(child, parentAlpha, camera)
            }
            if (!transform.shouldTransform() || curCompositeTransformComponent.renderToFBO) {
                //Restore composite relative position.
                childTransformComponent.x = cx
                childTransformComponent.y = cy
            }
            i++
        }
        nodeComponent.children.end()
    }

    private fun drawEntity(batch: Batch, child: Int, parentAlpha: Float) {
        fixtureDrawLogic.draw(batch, child, parentAlpha, DrawableLogic.RenderingType.TEXTURE)
    }

    /**
     * Returns the transform for this group's coordinate system.
     *
     * @param rootEntity
     */
    private fun computeTransform(rootEntity: Int): Matrix4 {
        val parentNodeComponent = parentNodeMapper[rootEntity]
        val curTransform = transformMapper[rootEntity]
        val worldTransform = curTransform.worldTransform
        val originX = curTransform.originX
        val originY = curTransform.originY
        val x = curTransform.x
        val y = curTransform.y
        val rotation = curTransform.rotation
        val scaleX = curTransform.scaleX * if (curTransform.flipX) -1 else 1
        val scaleY = curTransform.scaleY * if (curTransform.flipY) -1 else 1
        worldTransform.setToTrnRotScl(x + originX, y + originY, rotation, scaleX, scaleY)
        if (originX != 0f || originY != 0f) worldTransform.translate(-originX, -originY)

        // Find the parent that transforms.
        var parentEntity = -1
        if (parentNodeComponent != null) {
            parentEntity = parentNodeComponent.parentEntity
        }
        if (parentEntity != -1) {
            val transform = transformMapper[parentEntity]
            if (transform.shouldTransform()) worldTransform.preMul(transform.worldTransform)
        }
        curTransform.computedTransform.set(worldTransform)
        return curTransform.computedTransform
    }

    private  fun applyTransform(rootEntity: Int, batch: Batch) {
        val curTransform = transformMapper[rootEntity]
        curTransform.oldTransform.set(batch.transformMatrix)
        batch.transformMatrix = curTransform.computedTransform
    }

    private fun resetTransform(rootEntity: Int, batch: Batch) {
        val curTransform = transformMapper[rootEntity]
        batch.transformMatrix = curTransform.oldTransform
    }

    public override fun dispose() {
        frameBufferManager.disposeAll()
        fboM4Pool.clear()
        fboM4Stack.clear()
    }

    fun resize(width: Int, height: Int) {
        frameBufferManager.endCurrent()
        frameBufferManager.dispose("main")
        createCoreFrameBuffers()
        screenCamera.viewportWidth = width.toFloat()
        screenCamera.viewportHeight = height.toFloat()
        screenCamera.position[0f, 0f] = 0f
        screenCamera.translate(
            screenCamera.viewportWidth * 0.5f,
            screenCamera.viewportHeight * 0.5f,
            0f
        )
        screenCamera.update()
        invScreenWidth = 1f / screenCamera.viewportWidth
        invScreenHeight = 1f / screenCamera.viewportHeight
    }

    private fun createCoreFrameBuffers() {
        frameBufferManager.createFBO(
            "main", Gdx.graphics.backBufferWidth, Gdx.graphics.backBufferHeight, false,
            hasStencilBuffer, true
        )
    }

    companion object {
        val clearColor = Color(Color.CLEAR)
    }
}