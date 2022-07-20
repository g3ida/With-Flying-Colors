package com.g3ida.withflyingcolours.core.ecs.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.g3ida.withflyingcolours.core.ecs.components.EventListenerComponent

@All(EventListenerComponent::class)
class EventListenerSystem: IteratingSystem() {
    private lateinit var mEventListenerCM: ComponentMapper<EventListenerComponent>

    override fun process(entityId: Int) {
        val eventListenerComponent = mEventListenerCM.get(entityId)
        eventListenerComponent.eventActionListenerList.forEach {
            it.action.step(world.delta)
        }
    }
}