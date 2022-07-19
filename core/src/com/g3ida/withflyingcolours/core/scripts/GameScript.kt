package com.g3ida.withflyingcolours.core.scripts

import com.artemis.World
import com.badlogic.gdx.physics.box2d.World as Box2dWorld
import games.rednblack.editor.renderer.scripts.BasicScript

abstract class GameScript(val engine: World, val world: Box2dWorld) : BasicScript()