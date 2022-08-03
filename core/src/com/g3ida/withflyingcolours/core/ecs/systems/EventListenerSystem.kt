package com.g3ida.withflyingcolours.core.ecs.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.g3ida.withflyingcolours.core.actions.EventActionListener
import com.g3ida.withflyingcolours.core.actions.IInterpolationAction
import com.g3ida.withflyingcolours.core.ecs.components.EventListenerComponent
import games.rednblack.editor.renderer.systems.strategy.InterpolationSystem

@All(EventListenerComponent::class)
class EventListenerSystem: IteratingSystem(), InterpolationSystem {
    private lateinit var mEventListenerCM: ComponentMapper<EventListenerComponent>

    override fun process(entityId: Int) {
        val eventListenerComponent = mEventListenerCM.get(entityId)
        eventListenerComponent.actionListenerList.forEach {
            it.action.step(world.delta)
        }
    }

    override fun interpolate(alpha: Float) {
        val bag = subscription.entities
        for(i in 0 until bag.size()){
            val eventListenerComponent = mEventListenerCM.get(bag[i])
            eventListenerComponent.actionListenerList
                .map(EventActionListener::action)
                .filterIsInstance<IInterpolationAction>()
                .forEach {
                it.interpolate(alpha)
            }
        }
    }

    override fun dispose() {
        super.dispose()
    }
}