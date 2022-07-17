package com.g3ida.withflyingcolours.core.extensions

import com.artemis.BaseSystem
import games.rednblack.editor.renderer.SceneConfiguration
import games.rednblack.editor.renderer.SceneLoader
import games.rednblack.editor.renderer.resources.IResourceRetriever

fun SceneConfiguration.withSystem(system: BaseSystem): SceneConfiguration {
    this.addSystem(system)
    return this
}

fun SceneConfiguration.withSystems(vararg systems: BaseSystem): SceneConfiguration {
    systems.forEach { this.addSystem(it) }
    return this
}

fun SceneConfiguration.withResourceRetriever(resourceRetriever: IResourceRetriever): SceneConfiguration {
    this.setResourceRetriever(resourceRetriever)
    return this
}

fun SceneConfiguration.toSceneLoader(): SceneLoader {
    return SceneLoader(this)
}