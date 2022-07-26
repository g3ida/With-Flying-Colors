package com.g3ida.withflyingcolours.core

import com.artemis.Entity
import com.artemis.World
import com.badlogic.gdx.physics.box2d.World as Box2dWorld
import com.g3ida.withflyingcolours.core.scripts.GameScript
import com.g3ida.withflyingcolours.utils.EntityBodyLoader
import games.rednblack.editor.renderer.SceneLoader
import games.rednblack.editor.renderer.components.MainItemComponent
import games.rednblack.editor.renderer.components.NodeComponent
import games.rednblack.editor.renderer.utils.ComponentRetriever
import games.rednblack.editor.renderer.utils.ItemWrapper

class SceneMapper(sceneLoader: SceneLoader) {
    private val mPackageName: String = this.javaClass.getPackage().name
    private var mEngine: World
    private var mRootWrapper: ItemWrapper
    private var mWorld: com.badlogic.gdx.physics.box2d.World

    init {
        mEngine = sceneLoader.engine
        val rootEntityId = sceneLoader.root
        mRootWrapper = ItemWrapper(rootEntityId, mEngine)
        mWorld = sceneLoader.world
        parseSceneGraph(mEngine.getEntity(rootEntityId))
    }

    private fun parseSceneGraph(root: Entity) {
        val nodeComponent = ComponentRetriever.get(root.id, NodeComponent::class.java, mEngine)
        if (nodeComponent != null) {
            for (child in nodeComponent.children) {
                mapEntity(mEngine.getEntity(child))
            }
        }
    }

    private fun mapEntity(entity: Entity) {
        val mainItemComponent = ComponentRetriever.get(entity.id, MainItemComponent::class.java, mEngine)
        if (mainItemComponent.itemIdentifier == null || mainItemComponent.itemIdentifier.isEmpty()) {
            return
        }
        try {
            val scriptName = mainItemComponent.customVariables.get("script")
            if (scriptName != null) {
                val scriptClass = "$mPackageName.scripts.$scriptName"
                val entityClass = Class.forName(scriptClass)
                val script = entityClass
                    .getDeclaredConstructor(World::class.java, Box2dWorld::class.java)
                    .newInstance(mEngine, mWorld) as GameScript
                mRootWrapper.getChild(mainItemComponent.itemIdentifier).addScript(script)
            }

            val customBody = mainItemComponent.customVariables.get("body")
            if (customBody != null) {
                val bodyLoader = EntityBodyLoader(entity.id, mEngine, mWorld)
                bodyLoader.load(customBody)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}