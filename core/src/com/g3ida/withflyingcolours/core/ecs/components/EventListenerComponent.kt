package com.g3ida.withflyingcolours.core.ecs.components

import com.artemis.PooledComponent
import com.g3ida.withflyingcolours.core.actions.EventActionListener
import ktx.collections.GdxArray

class EventListenerComponent: PooledComponent() {
    val eventActionListenerList = GdxArray<EventActionListener>()

    fun addActionListener(eventActionListener: EventActionListener) {
        eventActionListenerList.add(eventActionListener)
    }

    override fun reset() {
        eventActionListenerList.forEach { it.dispose() }
        eventActionListenerList.clear()
    }
}