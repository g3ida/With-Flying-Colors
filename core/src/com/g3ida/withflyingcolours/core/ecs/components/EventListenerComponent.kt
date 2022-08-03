package com.g3ida.withflyingcolours.core.ecs.components

import com.artemis.PooledComponent
import com.g3ida.withflyingcolours.core.actions.EventActionListener
import ktx.collections.GdxArray

class EventListenerComponent: PooledComponent() {
    val actionListenerList = GdxArray<EventActionListener>()

    fun addActionListener(eventActionListener: EventActionListener) {
        actionListenerList.add(eventActionListener)
    }

    override fun reset() {
        actionListenerList.forEach { it.dispose() }
        actionListenerList.clear()
    }
}