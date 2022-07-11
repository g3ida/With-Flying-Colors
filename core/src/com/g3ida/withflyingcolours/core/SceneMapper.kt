package com.g3ida.withflyingcolours.core

import com.artemis.Entity
import com.artemis.World
import com.g3ida.withflyingcolours.core.scripts.GameScript
import games.rednblack.editor.renderer.utils.ComponentRetriever
import games.rednblack.editor.renderer.utils.ItemWrapper
import games.rednblack.editor.renderer.components.NodeComponent
import games.rednblack.editor.renderer.components.MainItemComponent
import games.rednblack.editor.renderer.SceneLoader
import java.lang.reflect.InvocationTargetException

class SceneMapper {
    private val _packageName: String
    private var _engine: World? = null
    private var _rootWrapper: ItemWrapper? = null
    private var _world: com.badlogic.gdx.physics.box2d.World? = null
    private fun parseSceneGraph(root: Entity) {
        val nodeComponent = ComponentRetriever.get(root.id, NodeComponent::class.java, _engine)
        if (nodeComponent != null) {
            for (child in nodeComponent.children) {
                mapEntity(_engine!!.getEntity(child))
            }
        }
    }

    private fun mapEntity(entity: Entity) {
        val mainItemComponent = ComponentRetriever.get(entity.id, MainItemComponent::class.java, _engine)
        if (mainItemComponent.itemIdentifier == null || mainItemComponent.itemIdentifier.isEmpty()) {
            return
        }
        try {
            val scriptName = mainItemComponent.customVariables.get("script")
            if (scriptName != null) {
                val ScriptClass = "$_packageName.scripts.$scriptName"
                val entityClass = Class.forName(ScriptClass)
                val script = entityClass.getDeclaredConstructor(World::class.java, com.badlogic.gdx.physics.box2d.World::class.java).newInstance(_engine, _world) as GameScript
                _rootWrapper!!.getChild(mainItemComponent.itemIdentifier).addScript(script)
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    fun mapScripts(sceneLoader: SceneLoader) {
        _engine = sceneLoader.engine
        val rootEnityId = sceneLoader.root
        _rootWrapper = ItemWrapper(rootEnityId, _engine)
        _world = sceneLoader.world
        parseSceneGraph(_engine!!.getEntity(rootEnityId))
    }

    init {
        _packageName = this.javaClass.getPackage().name
    }
}