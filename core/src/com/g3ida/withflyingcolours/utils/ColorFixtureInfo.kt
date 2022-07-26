package com.g3ida.withflyingcolours.utils

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.physics.box2d.Fixture
import com.g3ida.withflyingcolours.utils.enums.Direction

data class ColorFixtureInfo(val fixture: Fixture, val direction: Direction, val color: Color)