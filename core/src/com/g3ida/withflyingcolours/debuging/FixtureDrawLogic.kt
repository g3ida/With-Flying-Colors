package com.g3ida.withflyingcolours.debuging

import com.artemis.ComponentMapper
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.Shape
import games.rednblack.editor.renderer.components.*
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import games.rednblack.editor.renderer.systems.render.logic.DrawableLogic
import games.rednblack.editor.renderer.systems.render.logic.DrawableLogic.RenderingType

class FixtureDrawLogic(val traceColor: Color = Color.GREEN, val lineThickness: Float = 2f): DrawableLogic {

    lateinit var transformMapper: ComponentMapper<TransformComponent>
    lateinit var physicsBodyComponentMapper: ComponentMapper<PhysicsBodyComponent>

    private val batchColor = Color()

    override fun draw(batch: Batch, entity: Int, parentAlpha: Float, renderingType: RenderingType) {
        batchColor.set(batch.color)
        drawShape(batch, entity)
        batch.color = batchColor
    }

    private fun drawShape(batch: Batch, entity: Int) {
        val physicsBodyComponent = physicsBodyComponentMapper[entity]
        val transformComponent = transformMapper[entity]

        val transform = Matrix4()
        transform.set(
            Vector3(transformComponent.originX + transformComponent.x, transformComponent.originY + transformComponent.y, 0f),
            Quaternion(Vector3(0f, 0f, 1f), transformComponent.rotation),
            Vector3(transformComponent.scaleX,  transformComponent.scaleY,  1f)
        )

        val fixtureList = physicsBodyComponent.body.fixtureList
        for (fixture in fixtureList) {
            drawFixture(batch, fixture, transform)
        }
    }

    private fun drawFixture(batch: Batch, fixture: Fixture, transform: Matrix4) {
        val debugRenderer = ShapeRenderer()
        Gdx.gl.glLineWidth(lineThickness)
        debugRenderer.projectionMatrix = batch.projectionMatrix
        debugRenderer.transformMatrix = transform
        debugRenderer.begin(ShapeRenderer.ShapeType.Line)
        debugRenderer.color = traceColor

        fixture.shape.draw(debugRenderer)

        debugRenderer.end()
        Gdx.gl.glLineWidth(1f)
    }

    private fun Shape.draw(shapeRenderer: ShapeRenderer) {
        when(this.type) {
            Shape.Type.Polygon -> drawPolygon(this as PolygonShape, shapeRenderer)
            Shape.Type.Circle -> drawCircle(this as CircleShape, shapeRenderer)
            else -> {}
        }
    }

    private fun drawCircle(circleShape: CircleShape, shapeRenderer: ShapeRenderer) {
        shapeRenderer.circle(circleShape.position.x, circleShape.position.y, circleShape.radius)
    }

    private fun drawPolygon(polygonShape: PolygonShape, shapeRenderer: ShapeRenderer) {
        val verticesCount = polygonShape.vertexCount
        if (verticesCount == 0)
            return

        val start = Vector2()
        val end = Vector2()
        polygonShape.getVertex(0, start)
        for (i in 1 until verticesCount) {
            polygonShape.getVertex(i, end)
            shapeRenderer.line(start, end)
            start.x = end.x
            start.y = end.y
        }
        polygonShape.getVertex(0, start)
        shapeRenderer.line(start, end)
    }
}