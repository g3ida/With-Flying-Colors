package com.g3ida.withflyingcolours.core.scripts

import com.artemis.World
import games.rednblack.editor.renderer.scripts.BasicScript

abstract class GameScript(val engine: World, val world: com.badlogic.gdx.physics.box2d.World) : BasicScript()