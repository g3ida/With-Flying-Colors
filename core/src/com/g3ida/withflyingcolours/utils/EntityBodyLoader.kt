package com.g3ida.withflyingcolours.utils

import com.artemis.World
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.physics.box2d.FixtureDef
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import games.rednblack.editor.renderer.physics.PhysicsBodyLoader
import games.rednblack.editor.renderer.utils.ComponentRetriever

class EntityBodyLoader(val entityId: Int, val engine: World) {
    private val defaultBodyName = "main"

    fun load(filename: String) {
        val fileHandle = FileHandle(filename)
        val bodyEditorLoader = BodyEditorLoader(fileHandle)

        val physicsBodyComponent = ComponentRetriever.get(entityId, PhysicsBodyComponent::class.java, engine)
        bodyEditorLoader.attachFixture(physicsBodyComponent.body, defaultBodyName, FixtureDef(), 1f)
        // FIXME: For now we can't mix and match multiple shape types as Hyperlap2d components does not support it
        PhysicsBodyLoader.getInstance().refreshShape(entityId, engine)
    }
}