package com.g3ida.withflyingcolours.tests

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.g3ida.withflyingcolours.utils.BodyEditorLoader
import org.junit.Test
import java.lang.AssertionError
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BodyEditorLoaderTest : BaseSystemTests() {

    @Test
    @Throws(AssertionError::class)
    fun testBodyFile() {
        // initialize world
        val world = World(Vector2(0f,-9.81f), false)
        val fileName = "assets/test.json"
        val fileHandle = FileHandle(fileName)
        assertTrue(fileHandle.exists(), "The file does not exists")
        val bodyEditorLoader = BodyEditorLoader(fileHandle)

        attachFixtureToBodyAndAssert(world, bodyEditorLoader, "test01", 0, 17)
        attachFixtureToBodyAndAssert(world, bodyEditorLoader, "test02", 0, 11)
        attachFixtureToBodyAndAssert(world, bodyEditorLoader, "test03", 1, 1)
        attachFixtureToBodyAndAssert(world, bodyEditorLoader, "test04", 0, 0)
        attachFixtureToBodyAndAssert(world, bodyEditorLoader, "test05", 0, 0)
    }

    private fun attachFixtureToBodyAndAssert(world: World,
                                             bodyEditorLoader: BodyEditorLoader,
                                             bodyName: String,
                                             expectedCircleFixturesCount: Int,
                                             expectedPolygonFixturesCount: Int) {
        val body = world.createBody(BodyDef())
        bodyEditorLoader.attachFixture(body, bodyName, 1f)
        val expectedTotalFixturesCount = expectedCircleFixturesCount + expectedPolygonFixturesCount

        assertEquals(expectedTotalFixturesCount,
            body.fixtureList.size,
            "total fixtures")

        assertEquals(expectedCircleFixturesCount,
            body.fixtureList.count { it.type == Shape.Type.Circle },
            "circle fixtures")

        assertEquals(expectedPolygonFixturesCount,
            body.fixtureList.count { it.type == Shape.Type.Polygon },
            "polygon fixtures")
    }
}