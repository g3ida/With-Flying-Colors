package com.g3ida.withflyingcolours.core.ecs.components

import com.artemis.PooledComponent
import com.badlogic.gdx.physics.box2d.Fixture
import com.g3ida.withflyingcolours.utils.ColorFixtureInfo
import com.g3ida.withflyingcolours.utils.enums.Direction

class FixtureColorComponent : PooledComponent() {
    var fixtureDirection = mapOf<Fixture, ColorFixtureInfo>()

    fun set(colorFixtures: List<ColorFixtureInfo>) {
        fixtureDirection = colorFixtures.associateBy { it.fixture }
    }

    fun getDirection(fixture: Fixture): Direction? {
        return fixtureDirection.get(fixture)?.direction
    }

    override fun reset() {
        fixtureDirection = mapOf<Fixture, ColorFixtureInfo>()
    }
}