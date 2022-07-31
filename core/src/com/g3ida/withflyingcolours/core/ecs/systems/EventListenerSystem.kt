package com.g3ida.withflyingcolours.core.ecs.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.utils.Disposable
import com.g3ida.withflyingcolours.core.actions.EventActionListener
import com.g3ida.withflyingcolours.core.ecs.components.EventListenerComponent
import games.rednblack.editor.renderer.systems.strategy.InterpolationSystem

@All(EventListenerComponent::class)
class EventListenerSystem: IteratingSystem(), InterpolationSystem, Disposable {
    private lateinit var mEventListenerCM: ComponentMapper<EventListenerComponent>

    override fun process(entityId: Int) {
        val eventListenerComponent = mEventListenerCM.get(entityId)
        eventListenerComponent.eventActionListenerList.forEach {
            it.action.step(world.delta)
        }
    }

    override fun interpolate(alpha: Float) {
        val bag = subscription.getEntities()
        for(i in 0 until bag.size()){
            val eventListenerComponent = mEventListenerCM.get(i)
            eventListenerComponent?.eventActionListenerList?.forEach {
                it.action.interpolate(alpha)
            }
        }
    }

    override fun dispose() {
        val bag = subscription.getEntities()
        for(i in 0 until bag.size()){
            val eventListenerComponent = mEventListenerCM.get(i)
            eventListenerComponent?.eventActionListenerList
                ?.asSequence()
                ?.map(EventActionListener::action)
                ?.filterIsInstance<Disposable>()
                ?.forEach{ it.dispose() }
        }
        super.dispose()
    }
}