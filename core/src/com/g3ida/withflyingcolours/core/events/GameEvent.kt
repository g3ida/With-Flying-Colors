package com.g3ida.withflyingcolours.core.events

import com.badlogic.gdx.utils.ObjectMap
import ktx.collections.gdxMapOf

class GameEvent(val type: EventType, val extraData: ObjectMap<String, String> = gdxMapOf())