package com.g3ida.withflyingcolours.core.common

import com.badlogic.gdx.utils.viewport.Viewport
import com.g3ida.withflyingcolours.core.ecs.entities.RuntimeEntitySpawner
import com.g3ida.withflyingcolours.core.events.EventProcessor
import com.g3ida.withflyingcolours.core.events.IEventProcessor

object GameSettings {
    @kotlin.jvm.JvmField
    var mainViewPort: Viewport? = null
    val eventHandler: IEventProcessor by lazy { EventProcessor() }
    lateinit var entitySpawner: RuntimeEntitySpawner
}